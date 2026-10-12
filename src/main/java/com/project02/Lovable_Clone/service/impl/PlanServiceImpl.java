package com.project02.Lovable_Clone.service.impl;

import com.project02.Lovable_Clone.dto.subscription.PlanResponse;
import com.project02.Lovable_Clone.service.PlanService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanServiceImpl implements PlanService {

    @Override
    public List<PlanResponse> getAllActivePlans() {
        return List.of();
    }
}
