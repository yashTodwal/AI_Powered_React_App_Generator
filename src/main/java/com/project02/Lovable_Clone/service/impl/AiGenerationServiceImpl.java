package com.project02.Lovable_Clone.service.impl;

import com.project02.Lovable_Clone.entity.*;
import com.project02.Lovable_Clone.enums.ChatEventType;
import com.project02.Lovable_Clone.enums.MessageRole;
import com.project02.Lovable_Clone.error.ResourceNotFoundException;
import com.project02.Lovable_Clone.llm.LlmResponseParser;
import com.project02.Lovable_Clone.llm.PromptUtils;
import com.project02.Lovable_Clone.llm.advisors.FileTreeContextAdvisor;
import com.project02.Lovable_Clone.llm.tools.CodeGenerationTools;
import com.project02.Lovable_Clone.repository.*;
import com.project02.Lovable_Clone.security.AuthUtil;
import com.project02.Lovable_Clone.service.AiGenerationService;
import com.project02.Lovable_Clone.service.ProjectFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiGenerationServiceImpl implements AiGenerationService{

    private final ChatClient chatClient;
    private final AuthUtil authUtil;
    private final ProjectFileService projectFileService;
    private final FileTreeContextAdvisor fileTreeContextAdvisor;
    private final ChatSessionRepository chatSessionRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final LlmResponseParser llmResponseParser;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatEventRepository chatEventRepository;

    private static final Pattern FILE_TAG_PATTERN = Pattern.compile("<file path=\"([^\"]+)\">(.*?)</file>", Pattern.DOTALL);
    
    @Override
    @PreAuthorize("@security.canEditProject(#projectId)")
    public Flux<String> streamResponse(String userMessage, Long projectId) {
        Long userId = authUtil.getCurrentUserId();
        ChatSession chatSession = createChatSessionIfNotExists(projectId,userId);

        Map<String, Object> advisorParams = Map.of(
                "userId", userId,
                "projectId", projectId
        );

        StringBuilder fullResponseBuffer = new StringBuilder();
        CodeGenerationTools codeGenerationTools = new CodeGenerationTools(projectFileService, projectId);
        AtomicReference<Long> startTime = new AtomicReference<>(System.currentTimeMillis());
        AtomicReference<Long> endTime = new AtomicReference<>(0L);

        return chatClient.prompt()
                .system(PromptUtils.CODE_GENERATION_SYSTEM_PROMPT)
                .user(userMessage)
                .tools(codeGenerationTools)
                .advisors(advisorSpec -> {
                    advisorSpec.params(advisorParams);
                    advisorSpec.advisors(fileTreeContextAdvisor);
                    }
                )
                .stream()
                .chatResponse()
                .doOnNext(chatResponse -> {
                    String content = chatResponse.getResult().getOutput().getText();
                    if(content != null && !content.isEmpty() && endTime.get() == 0) { // first non-empty chunk received
                        endTime.set(System.currentTimeMillis());
                    }
                    fullResponseBuffer.append(content);
                })
                .doOnComplete(() -> {
                    Schedulers.boundedElastic().schedule(()->{
                        long duration = (endTime.get() - startTime.get()) /  1000;
                        finalizeChats(userMessage,chatSession,fullResponseBuffer.toString(),duration);
                    });
                })
                .doOnError(error -> log.error("Error during streaming of project: " + error))
                .map(response -> Objects.requireNonNull(response.getResult().getOutput().getText()));
    }

    private void finalizeChats(String userMessage, ChatSession chatSession, String fullText, Long duration){
        Long projectId = chatSession.getProject().getId();
        chatMessageRepository.save(
                ChatMessage.builder()
                        .chatSession(chatSession)
                        .messageRole(MessageRole.USER)
                        .content(userMessage)
                        .build()
        );
        ChatMessage assistantChatMessage = ChatMessage.builder()
                .messageRole(MessageRole.ASSISTANT)
                .content("ASSISTANT")
                .chatSession(chatSession)
                .build();

        assistantChatMessage = chatMessageRepository.save(assistantChatMessage);
        List<ChatEvent> chatEventList = llmResponseParser.parseChatEvents(fullText,assistantChatMessage);
        chatEventList.addFirst(ChatEvent.builder()
                .type(ChatEventType.THOUGHT)
                .chatMessage(assistantChatMessage)
                .content("Thought for "+duration+"s")
                .sequenceOrder(0)
                .build());
        chatEventList.stream()
                .filter(e->e.getType() == ChatEventType.FILE_EDIT)
                .forEach(e->projectFileService.saveFile(projectId,e.getFilePath(),e.getContent()));

        chatEventRepository.saveAll(chatEventList);


    }

    private ChatSession createChatSessionIfNotExists(Long projectId, Long userId) {
        ChatSessionId chatSessionId = new ChatSessionId(projectId,userId);
        ChatSession chatSession = chatSessionRepository.findById(chatSessionId).orElse(null);
        if(chatSession == null){
            Project project = projectRepository.findById(projectId).orElseThrow(()->new ResourceNotFoundException("Project: ",projectId.toString()));
            User user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User: ",userId.toString()));
            chatSession = ChatSession.builder()
                    .id(chatSessionId)
                    .project(project)
                    .user(user)
                    .build();
            chatSession = chatSessionRepository.save(chatSession);
        }
        return chatSession;
    }
}
