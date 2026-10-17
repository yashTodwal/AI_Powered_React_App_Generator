package com.project02.Lovable_Clone.mapper;

import com.project02.Lovable_Clone.dto.chat.ChatResponse;
import com.project02.Lovable_Clone.entity.ChatMessage;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel ="spring")
public interface ChatMapper {
    List<ChatResponse> fromListOfChatMessage(List<ChatMessage> chatMessageList);
}
