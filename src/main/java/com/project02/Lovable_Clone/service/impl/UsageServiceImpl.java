package com.project02.Lovable_Clone.service.impl;

import com.project02.Lovable_Clone.dto.subscription.PlanLimitResponse;
import com.project02.Lovable_Clone.dto.subscription.UsageTodayResponse;
import com.project02.Lovable_Clone.service.UsageService;
import org.springframework.stereotype.Service;

@Service
public class UsageServiceImpl implements UsageService {
    @Override
    public UsageTodayResponse getTodayUsageOfUSer(Long userId) {
        return null;
    }

    @Override
    public PlanLimitResponse getCurrentSubscriptionLimitsOfUSer(Long userId) {
        return null;
    }
}
