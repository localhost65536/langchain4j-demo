package cn.king.config;

import cn.king.service.ChatAssistant;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration(proxyBeanMethods = false)
public class ChatLanguageModelConfig {

    @Bean
    public ChatLanguageModel chatLanguageModelQwen() {
        return OpenAiChatModel.builder()
                .apiKey(System.getenv("QWEN_API_KEY"))
                .modelName("qwen3.5-plus")
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                .build();
    }

    @Bean
    @Primary
    public ChatLanguageModel chatLanguageModelDeepseek() {
        return OpenAiChatModel.builder()
                .apiKey(System.getenv("DEEPSEEK_API_KEY"))
                .modelName("deepseek-chat") // 对应 DeepSeek-V3.2 的非思考模式
                // .modelName("deepseek-reasoner") // 对应 DeepSeek-V3.2 的思考模式
                .baseUrl("https://api.deepseek.com")
                .build();
    }

    // 使用 @Primary 或 @Qualifier 指定首选 bean
    // public ChatAssistant chatAssistant(@Qualifier("chatLanguageModelDeepseek") ChatLanguageModel chatLanguageModel) {
    @Bean
    public ChatAssistant chatAssistant(ChatLanguageModel chatLanguageModel) {
        return AiServices.create(ChatAssistant.class, chatLanguageModel);
    }

}
