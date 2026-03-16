package cn.king.service;

import reactor.core.publisher.Flux;

/**
 * @author: wjl@king.cn
 * @time: 2026/3/16
 * @version: 1.0.0
 * @description:
 */
public interface ChatWithOutMemoryAssistant {
    Flux<String> chat(String prompt);
}

