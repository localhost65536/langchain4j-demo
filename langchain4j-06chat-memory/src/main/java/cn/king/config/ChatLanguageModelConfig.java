package cn.king.config;

import cn.king.service.ChatMemoryAssistant;
import cn.king.service.ChatWithOutMemoryAssistant;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration(proxyBeanMethods = false)
public class ChatLanguageModelConfig {
    @Bean
    public StreamingChatLanguageModel streamingChatLanguageModel() {
        return OpenAiStreamingChatModel.builder()
                .apiKey(System.getenv("LANGCHAIN4J_KEY"))
                .modelName("qwen3.5-plus")
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                .timeout(Duration.ofSeconds(10))
                .build();
    }

    @Bean
    public ChatWithOutMemoryAssistant chatWithOutMemoryAssistant(StreamingChatLanguageModel streamingChatLanguageModel) {
        return AiServices.builder(ChatWithOutMemoryAssistant.class)
                .streamingChatLanguageModel(streamingChatLanguageModel)
                .build();
    }

    @Bean
    public ChatMemoryAssistant chatMemoryAssistant(StreamingChatLanguageModel streamingChatLanguageModel) {
        return AiServices.builder(ChatMemoryAssistant.class)
                .streamingChatLanguageModel(streamingChatLanguageModel)
                // 记忆功能/对话历史对话功能 的提供者
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(100))
                .build();
    }

}