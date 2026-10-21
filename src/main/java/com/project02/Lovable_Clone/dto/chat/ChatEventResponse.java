package com.project02.Lovable_Clone.dto.chat;

import com.project02.Lovable_Clone.entity.ChatMessage;
import com.project02.Lovable_Clone.enums.ChatEventType;
import jakarta.persistence.*;

public record ChatEventResponse(
        Long id,
        ChatEventType type,
        Integer sequenceOrder,
        String content,
        String filePath,
        String metadata
) {
}
