package cn.king.service;

import cn.king.entity.LawPrompt;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

/**
 * @author: wjl@king.cn
 * @time: 2026/3/16
 * @version: 1.0.0
 * @description: 法律助手
 */
public interface LawAssistant {

    // 案例 1
    @SystemMessage("你是一位专业的中国法律顾问，只回答与中国法律相关的问题。" +
            "输出限制：对于其他领域的问题禁止回答，" +
            "直接返回'抱歉，我只能回答中国法律相关的问题。'")
    @UserMessage("请回答以下法律问题：{{question}}")
    String chat(@V("question") String question);

    // 案例 2
    @SystemMessage("你是一位专业的中国法律顾问，只回答与中国法律相关的问题。" +
            "输出限制：对于其他领域的问题禁止回答，直接返回'抱歉，我只能回答中国法律相关的问题。'")
    String chat(LawPrompt lawPrompt);

}

