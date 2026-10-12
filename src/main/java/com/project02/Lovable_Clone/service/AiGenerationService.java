package com.project02.Lovable_Clone.service;

import reactor.core.publisher.Flux;

public interface AiGenerationService {

    Flux<String> streamResponse(String message, Long projectId);
}
