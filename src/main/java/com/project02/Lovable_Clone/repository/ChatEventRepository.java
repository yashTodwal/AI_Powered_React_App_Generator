package com.project02.Lovable_Clone.repository;

import com.project02.Lovable_Clone.entity.ChatEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatEventRepository extends JpaRepository<ChatEvent, Long> {
}
