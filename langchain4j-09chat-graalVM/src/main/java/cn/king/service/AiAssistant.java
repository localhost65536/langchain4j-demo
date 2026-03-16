package cn.king.service;

import dev.langchain4j.service.SystemMessage;

public interface AiAssistant {
    // @SystemMessage 最好加上
    @SystemMessage("你是一个能调用 JavaScript 执行工具的助手。遇到计算类问题时，优先通过工具执行 JS 代码，再基于执行结果回答。")
    String chat(String prompt);
}
