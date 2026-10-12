package com.project02.Lovable_Clone.service;

import com.project02.Lovable_Clone.dto.subscription.PlanResponse;

import java.util.List;

public interface PlanService {
    List<PlanResponse> getAllActivePlans();
}
