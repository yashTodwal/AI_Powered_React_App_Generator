package com.project02.Lovable_Clone.dto.chat;

import com.project02.Lovable_Clone.entity.ChatEvent;
import com.project02.Lovable_Clone.entity.ChatSession;
import com.project02.Lovable_Clone.enums.MessageRole;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.List;

public record ChatResponse(
        Long id,
        ChatSession chatSession,
        String content,
        MessageRole messageRole,
        List<ChatEvent>events,
        Integer tokensUsed,
        Instant createdAt
) {
}
