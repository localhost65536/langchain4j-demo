package cn.king.config;

import cn.king.service.AiAssistant;
import dev.langchain4j.agent.tool.graalvm.GraalVmJavaScriptExecutionTool;
import dev.langchain4j.code.CodeExecutionEngine;
import dev.langchain4j.code.graalvm.GraalVmJavaScriptExecutionEngine;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Slf4j
@Configuration(proxyBeanMethods = false)
public class ChatLanguageModelConfig {

    @Bean
    public ChatLanguageModel chatLanguageModel() {
        return OpenAiChatModel.builder()
                .apiKey(System.getenv("LANGCHAIN4J_KEY"))
                .modelName("qwen3.5-plus")
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                .timeout(Duration.ofSeconds(60))
                .build();
    }

    @Bean
    public AiAssistant aiAssistant(ChatLanguageModel chatLanguageModel){
        return AiServices.builder(AiAssistant.class)
                .chatLanguageModel(chatLanguageModel)
                // 工具调用模式：让模型在需要时调用 JS 执行工具
                .tools(new GraalVmJavaScriptExecutionTool())
                .build();
    }

    // 手动执行 js 代码使用
    @Bean("javaScriptExecutionEngine")
    public CodeExecutionEngine javaScriptExecutionEngine() {
        return new GraalVmJavaScriptExecutionEngine();
    }

}
