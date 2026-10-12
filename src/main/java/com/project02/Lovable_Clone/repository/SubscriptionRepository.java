package com.project02.Lovable_Clone.repository;

import com.project02.Lovable_Clone.entity.Subscription;
import com.project02.Lovable_Clone.enums.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.Set;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Optional<Subscription> findByUserIdAndStatusIn(Long userId, Set<SubscriptionStatus> active);

    boolean existsByStripeSubscriptionId(String subscriptionId);

    Optional<Subscription> findByStripeSubscriptionId(String gatewaySubscriptionId);
}
