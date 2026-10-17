package com.project02.Lovable_Clone.service.impl;

import com.project02.Lovable_Clone.dto.chat.ChatResponse;
import com.project02.Lovable_Clone.entity.ChatMessage;
import com.project02.Lovable_Clone.entity.ChatSession;
import com.project02.Lovable_Clone.entity.ChatSessionId;
import com.project02.Lovable_Clone.mapper.ChatMapper;
import com.project02.Lovable_Clone.repository.ChatMessageRepository;
import com.project02.Lovable_Clone.repository.ChatSessionRepository;
import com.project02.Lovable_Clone.security.AuthUtil;
import com.project02.Lovable_Clone.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatServiceImpl implements ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatSessionRepository chatSessionRepository;
    private final AuthUtil authUtil;
    private final ChatMapper chatMapper;

    @Override
    public List<ChatResponse> getProjectChatHistory(Long projectId) {
        Long userId = authUtil.getCurrentUserId();

        ChatSession chatSession = chatSessionRepository.getReferenceById(
                new ChatSessionId(projectId, userId)
        );

        List<ChatMessage> chatMessageList = chatMessageRepository.findByChatSession(chatSession);
        return chatMapper.fromListOfChatMessage(chatMessageList);
    }
}
