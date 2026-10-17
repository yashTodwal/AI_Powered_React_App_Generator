package com.project02.Lovable_Clone.repository;

import com.project02.Lovable_Clone.entity.ChatSession;
import com.project02.Lovable_Clone.entity.ChatSessionId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatSessionRepository extends JpaRepository<ChatSession, ChatSessionId>{
}
