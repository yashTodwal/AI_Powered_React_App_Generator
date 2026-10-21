package com.project02.Lovable_Clone.service;

import com.project02.Lovable_Clone.dto.subscription.PlanLimitResponse;
import com.project02.Lovable_Clone.dto.subscription.UsageTodayResponse;
import org.jspecify.annotations.Nullable;

public interface UsageService {
    void recordTokenUsage(Long userId, int actualTokens);
    void checkDailyTokensUsage();
}
