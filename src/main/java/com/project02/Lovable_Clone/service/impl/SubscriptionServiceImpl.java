package com.project02.Lovable_Clone.service.impl;

import com.project02.Lovable_Clone.dto.subscription.SubscriptionResponse;
import com.project02.Lovable_Clone.entity.Plan;
import com.project02.Lovable_Clone.entity.Subscription;
import com.project02.Lovable_Clone.entity.User;
import com.project02.Lovable_Clone.enums.SubscriptionStatus;
import com.project02.Lovable_Clone.error.ResourceNotFoundException;
import com.project02.Lovable_Clone.mapper.SubscriptionMapper;
import com.project02.Lovable_Clone.repository.PlanRepository;
import com.project02.Lovable_Clone.repository.ProjectMemberRepository;
import com.project02.Lovable_Clone.repository.SubscriptionRepository;
import com.project02.Lovable_Clone.repository.UserRepository;
import com.project02.Lovable_Clone.security.AuthUtil;
import com.project02.Lovable_Clone.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionServiceImpl implements SubscriptionService {
    private final ProjectMemberRepository projectMemberRepository;

    private final AuthUtil authUtil;
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final PlanRepository planRepository;
    private final UserRepository userRepository;
    @Override
    public SubscriptionResponse getCurrentSubscription() {
        Long userId = authUtil.getCurrentUserId();
        var currentSubscription = subscriptionRepository.findByUserIdAndStatusIn(userId, Set.of(SubscriptionStatus.ACTIVE, SubscriptionStatus.PAST_DUE, SubscriptionStatus.TRIALING)).orElse(new Subscription());
        return subscriptionMapper.toSubscriptionResponse(currentSubscription);
    }

    @Override
    public void activateSubscription(Long userId, Long planId, String subscriptionId, String customerId) {
        boolean exists = subscriptionRepository.existsByStripeSubscriptionId(subscriptionId);
        if(exists) return;
        User user = getUser(userId);
        Plan plan = getPlan(planId);
        Subscription subscription = Subscription.builder()
                .user(user)
                .plan(plan)
                .stripeSubscriptionId(subscriptionId)
                .status(SubscriptionStatus.INCOMPLETE)
                .build();
        subscriptionRepository.save(subscription);
    }

    @Override
    @Transactional
    public void updateSubscription(String gatewaySubscriptonId, SubscriptionStatus status, Instant periodStart, Instant periodEnd, Boolean cancelAtPeriodEnd, Long planId) {
        Subscription subscription = getSubscription(gatewaySubscriptonId);
        boolean hasSubscriptionUpdated = false;
        if(status != null && status!=subscription.getStatus()){
            subscription.setStatus(status);
            hasSubscriptionUpdated = true;
        }
        if(periodStart != null && !periodStart.equals(subscription.getCurrentPeriodStart())){
            subscription.setCurrentPeriodStart(periodStart);
            hasSubscriptionUpdated = true;
        }
        if(periodEnd != null && !periodEnd.equals(subscription.getCurrentPeriodEnd())){
            subscription.setCurrentPeriodEnd(periodEnd);
            hasSubscriptionUpdated = true;
        }
        if(cancelAtPeriodEnd != null && cancelAtPeriodEnd != subscription.getCancelAtPeriodEnd()){
            subscription.setCancelAtPeriodEnd(cancelAtPeriodEnd);
            hasSubscriptionUpdated = true;
        }
        if(planId != null && !planId.equals(subscription.getPlan().getId())){
            Plan plan = getPlan(planId);
            subscription.setPlan(plan);
            hasSubscriptionUpdated = true;
        }
        if(hasSubscriptionUpdated){
            log.debug("Subscription has been updated: {}",gatewaySubscriptonId);
            subscriptionRepository.save(subscription);
        }
    }

    @Override
    public void cancelSubscription(String gatewaySubscriptonId) {
        Subscription subscription = getSubscription(gatewaySubscriptonId);
        subscription.setStatus(SubscriptionStatus.CANCELLED);
        subscriptionRepository.save(subscription);
    }

    @Override
    public void renewSubscriptionPeriod(String gatewaySubscriptonId, Instant periodStart, Instant periodEnd) {
        Subscription subscription = getSubscription(gatewaySubscriptonId);
        Instant newStart = periodStart!=null?periodStart:subscription.getCurrentPeriodEnd();
        subscription.setCurrentPeriodStart(newStart);
        subscription.setCurrentPeriodEnd(periodEnd);
        if(subscription.getStatus() == SubscriptionStatus.PAST_DUE || subscription.getStatus() == SubscriptionStatus.INCOMPLETE){
            subscription.setStatus(SubscriptionStatus.ACTIVE);
        }
        subscriptionRepository.save(subscription);
    }

    @Override
    public void markSubscriptionPastDue(String gatewaySubscriptonId) {
        Subscription subscription = getSubscription(gatewaySubscriptonId);
        if(subscription.getStatus() == SubscriptionStatus.PAST_DUE){
            log.debug("Subscription is already due, gatewaySubscriptonId: ",gatewaySubscriptonId);
            return;
        }
        subscription.setStatus(SubscriptionStatus.PAST_DUE);
        subscriptionRepository.save(subscription);
    }
    private final Integer FREE_TIER_PROJECTS_ALLOWED = 100;
    @Override
    public boolean canCreateNewProject() {
        Long userId = authUtil.getCurrentUserId();
        SubscriptionResponse currentSubscription = getCurrentSubscription();
        int countOfOwnedProjects = projectMemberRepository.countProjectOwnedByUser(userId);
        if(currentSubscription.plan() == null){
            return countOfOwnedProjects<FREE_TIER_PROJECTS_ALLOWED;
        }
        return countOfOwnedProjects<currentSubscription.plan().maxProjects();
    }

    private User getUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User: ", userId.toString()));
        return user;
    }
    private Plan getPlan(Long planId) {
        Plan plan = planRepository.findById(planId).orElseThrow(() -> new ResourceNotFoundException("Plan: ", planId.toString()));
        return plan;
    }
    private Subscription getSubscription(String gatewaySubscriptionId) {
        Subscription subscription = subscriptionRepository.findByStripeSubscriptionId(gatewaySubscriptionId).orElseThrow(() -> new ResourceNotFoundException("Subscription: ",gatewaySubscriptionId));
        return subscription;
    }

}
