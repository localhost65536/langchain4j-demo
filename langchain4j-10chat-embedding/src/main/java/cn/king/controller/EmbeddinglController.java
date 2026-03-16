package cn.king.controller;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingStore;
import io.qdrant.client.QdrantClient;
import jakarta.annotation.Resource;
import dev.langchain4j.model.embedding.EmbeddingModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import io.qdrant.client.grpc.Collections;
import dev.langchain4j.data.embedding.Embedding;

import static dev.langchain4j.store.embedding.filter.MetadataFilterBuilder.metadataKey;

/**
 * @author: wjl@king.cn
 * @time: 2026/3/16
 * @version: 1.0.0
 * @description:
 */
@RestController
public class EmbeddinglController {

    @Resource
    private EmbeddingModel embeddingModel; // 文本向量化模型
    @Resource
    private QdrantClient qdrantClient;// 向量数据库访问的连接客户端
    @Resource
    private EmbeddingStore<TextSegment> embeddingStore;// 对向量数据库CRUD的操作类

    /**
     * 文本向量化测试，看看形成向量后的文本
     * http://localhost:9010/embedding/embed
     */
    @GetMapping(value = "/embedding/embed")
    public String embed() {
        String prompt = """
                   咏鸡
                鸡鸣破晓光，
                红冠映朝阳。
                金羽披霞彩，
                昂首步高岗。
                """;
        Response<Embedding> embeddingResponse = embeddingModel.embed(prompt);
        // System.out.println(embeddingResponse);
        return embeddingResponse.content().toString();
    }

    /**
     * 新建向量数据库实例和创建索引：test-qdrant
     * 类似mysql create database test-qdrant
     * http://localhost:9010/embedding/createCollection
     */
    @GetMapping(value = "/embedding/createCollection")
    public void createCollection() {
        var vectorParams = Collections.VectorParams.newBuilder()
                .setDistance(Collections.Distance.Cosine)
                .setSize(1024)
                .build();
        qdrantClient.createCollectionAsync("test-qdrant", vectorParams);
    }

    /**
     * 往向量数据库新增文本记录
     * http://localhost:9010/embedding/add
     */
    @GetMapping(value = "/embedding/add")
    public String add() {
        String prompt = """
                咏鸡
                鸡鸣破晓光，
                红冠映朝阳。
                金羽披霞彩，
                昂首步高岗。
                """;
        TextSegment segment1 = TextSegment.from(prompt);
        segment1.metadata().put("author", "wang");
        segment1.metadata().put("UserId", "001");
        Embedding embedding1 = embeddingModel.embed(segment1).content();
        String result = embeddingStore.add(embedding1, segment1);
        System.out.println(result);
        return result;
    }

    /**
     * 查询向量数据库
     * http://localhost:9010/embedding/query1
     */
    @GetMapping(value = "/embedding/query1")
    public void query1() {
        // 把 "咏鸡现代诗" 嵌入
        Embedding queryEmbedding = embeddingModel.embed("咏鸡现代诗").content();
        // 使用嵌入后的内容查询
        EmbeddingSearchRequest embeddingSearchRequest = EmbeddingSearchRequest.builder()
                .queryEmbedding(queryEmbedding)
                // 最多只要一条数据
                .maxResults(1)
                .build();
        EmbeddingSearchResult<TextSegment> searchResult = embeddingStore.search(embeddingSearchRequest);
        System.out.println(searchResult.matches().get(0).embedded().text());
    }

    /**
     * 查询向量数据库
     * http://localhost:9010/embedding/query2
     */
    @GetMapping(value = "/embedding/query2")
    public void query2() {
        Embedding queryEmbedding = embeddingModel.embed("咏鸡").content();
        EmbeddingSearchRequest embeddingSearchRequest = EmbeddingSearchRequest.builder()
                .queryEmbedding(queryEmbedding)
                // 只查询作者是 wang 的内容
                .filter(metadataKey("author").isEqualTo("wang"))
                .maxResults(1)
                .build();
        EmbeddingSearchResult<TextSegment> searchResult = embeddingStore.search(embeddingSearchRequest);
        System.out.println(searchResult.matches().get(0).embedded().text());
    }

}
