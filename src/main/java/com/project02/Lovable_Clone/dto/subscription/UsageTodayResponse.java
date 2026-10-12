package com.project02.Lovable_Clone.dto.subscription;

public record UsageTodayResponse(
        Integer tokensUsed,
        Integer tokensLimit,
        Integer previewsRunning,
        Integer previewsList
) {
}
