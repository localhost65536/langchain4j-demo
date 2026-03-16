package cn.king.config;

import cn.king.service.ChatAssistant;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import dev.langchain4j.data.segment.TextSegment;

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

    /**
     * 需要预处理文档并将其存储在专门的嵌入存储（也称为矢量数据库/向量数据库）中。当用户提出问题时，这对于快速找到相关信息是必要的。
     * 我们可以使用我们支持的 15 多个嵌入存储中的任何一个，但为了简单起见，我们将使用内存中的嵌入存储：
     */
    @Bean
    public InMemoryEmbeddingStore<TextSegment> embeddingStore() {
        // langchain4j 自带的向量数据库. 运行在内存中
        return new InMemoryEmbeddingStore<>();
    }

    @Bean
    public ChatAssistant assistant(ChatLanguageModel chatLanguageModel, EmbeddingStore<TextSegment> embeddingStore) {
        return AiServices.builder(ChatAssistant.class)
                .chatLanguageModel(chatLanguageModel)
                // 记忆功能,之前案例有讲解
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                // 存储到 embeddingStore() 这个向量数据库中
                .contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore))
                .build();
    }

}