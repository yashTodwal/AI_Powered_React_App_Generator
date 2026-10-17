package com.project02.Lovable_Clone.service;


import com.project02.Lovable_Clone.dto.chat.ChatResponse;

import java.util.List;

public interface ChatService {
    List<ChatResponse> getProjectChatHistory(Long projectId);
}
