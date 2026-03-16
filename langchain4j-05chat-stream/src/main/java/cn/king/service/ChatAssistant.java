package cn.king.service;

import reactor.core.publisher.Flux;

public interface ChatAssistant {
    Flux<String> chatFlux(String prompt);
}

