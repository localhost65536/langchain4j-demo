package cn.king.controller;

import cn.king.service.ChatAssistant;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import jakarta.annotation.Resource;
import dev.langchain4j.data.segment.TextSegment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RAGController {

    @Resource
    InMemoryEmbeddingStore<TextSegment> embeddingStore;

    @Resource
    ChatAssistant chatAssistant;

    // http://localhost:9011/rag/add
    @GetMapping(value = "/rag/add")
    public String testAdd() {
        // 索引
        Document document = FileSystemDocumentLoader.loadDocument("/doc/老王科技有限公司规章制度.pdf");
        EmbeddingStoreIngestor.ingest(document, embeddingStore);
        // 检索
        return chatAssistant.chat("如何报销发票");
    }

}