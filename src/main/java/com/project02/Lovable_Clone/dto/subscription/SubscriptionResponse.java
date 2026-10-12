package com.project02.Lovable_Clone.dto.subscription;

import java.time.Instant;

public record SubscriptionResponse(
        PlanResponse plan,
        String status,
        Instant currentPeriodEnd,
        Long tokenUsedThisCycle
) {
}
