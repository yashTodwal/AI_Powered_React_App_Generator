package com.project02.Lovable_Clone.service;

import com.project02.Lovable_Clone.dto.chat.StreamResponse;
import reactor.core.publisher.Flux;

public interface AiGenerationService {

    Flux<StreamResponse> streamResponse(String message, Long projectId);
}
