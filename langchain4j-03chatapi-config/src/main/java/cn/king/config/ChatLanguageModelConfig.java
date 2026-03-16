package cn.king.config;

import cn.king.listener.TestChatModelListener;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.List;

@Configuration(proxyBeanMethods = false)
public class ChatLanguageModelConfig {

    @Bean
    public ChatLanguageModel chatLanguageModel() {
        return OpenAiChatModel.builder()
                .apiKey(System.getenv("LANGCHAIN4J_KEY"))
                .modelName("qwen3.5-plus")
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                .logRequests(true) // 请求大模型前打日志 日志界别设置为 debug 才有效
                .logResponses(true) // 大模型响应后打日志 日志级别设置为 debug 才有效
                .listeners(List.of(new TestChatModelListener())) // 监听器
                .maxRetries(2) // 最大重试次数
                .timeout(Duration.ofSeconds(10)) // 超时时间
                .build();
    }

}
