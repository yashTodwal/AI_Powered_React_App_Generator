package com.project02.Lovable_Clone.mapper;

import com.project02.Lovable_Clone.dto.subscription.PlanResponse;
import com.project02.Lovable_Clone.dto.subscription.SubscriptionResponse;
import com.project02.Lovable_Clone.entity.Plan;
import com.project02.Lovable_Clone.entity.Subscription;
import org.mapstruct.Mapper;

@Mapper(componentModel ="spring")
public interface SubscriptionMapper {
    SubscriptionResponse toSubscriptionResponse(Subscription subscription);

    PlanResponse toPlanResponse(Plan plan);
}
