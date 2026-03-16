package cn.king.service;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;
import reactor.core.publisher.Flux;

/**
 * @author: wjl@king.cn
 * @time: 2026/3/16
 * @version: 1.0.0
 * @description:
 */
public interface ChatMemoryAssistant {

    /**
     * @param: userId 用户 id,区分用户账号
     * @param: prompt 用户提示词
     */
    Flux<String> chat(@MemoryId Long userId, @UserMessage String prompt);

}

