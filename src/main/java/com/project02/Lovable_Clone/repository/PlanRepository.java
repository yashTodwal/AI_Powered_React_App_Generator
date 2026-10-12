package com.project02.Lovable_Clone.repository;

import aj.org.objectweb.asm.commons.Remapper;
import com.project02.Lovable_Clone.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PlanRepository extends JpaRepository<Plan, Long> {

    Optional<Plan> findByStripePriceId(String id);
}
