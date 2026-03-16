package cn.king.config;

import cn.king.service.LawAssistant;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

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
    public LawAssistant lawAssistant(ChatLanguageModel chatLanguageModel) {
        return AiServices.create(LawAssistant.class, chatLanguageModel);
    }

}