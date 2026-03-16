package cn.king.config;

import cn.king.service.ChatAssistant;
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
    public ChatAssistant chatAssistant(StreamingChatLanguageModel streamingChatLanguageModel) {
        return AiServices.create(ChatAssistant.class, streamingChatLanguageModel);
    }

}