package cn.king.config;

import cn.king.service.ChatAssistant;
import cn.king.function.InvoiceFunc;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.tool.ToolExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Map;

import static dev.langchain4j.agent.tool.JsonSchemaProperty.description;
import static dev.langchain4j.agent.tool.JsonSchemaProperty.type;

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

    /*
    @Bean
    public ChatAssistant functionAssistant(ChatLanguageModel chatLanguageModel) {
        // 定义工具/函数
        ToolSpecification toolSpecification = ToolSpecification.builder()
                // 函数名: 发票助手
                .name("invoice_assistant")
                // 函数描述
                .description("根据用户提交的开票信息，开具发票")
                // 函数参数
                .addParameter("companyName", type("string"), description("公司名称"))
                .addParameter("dutyNumber", type("string"), description("税号"))
                .addParameter("amount", type("string"), description("金额，保留两位有效数字"))
                .build();

        // 函数的业务逻辑
        ToolExecutor toolExecutor = (toolExecutionRequest, memoryId) -> {
            log.info("调用的函数 id =>>>> {}", toolExecutionRequest.id());
            log.info("调用的函数名 =>>>> {}", toolExecutionRequest.name());
            log.info("调用的函数参数 =>>>> {}", toolExecutionRequest.arguments());
            return "发票开具成功 (函数执行完毕)";
        };

        return AiServices.builder(ChatAssistant.class)
                .chatLanguageModel(chatLanguageModel)
                // 匹配函数定义和函数业务逻辑
                .tools(Map.of(toolSpecification, toolExecutor))
                .build();
    }
    */

    @Bean
    public ChatAssistant functionAssistant(ChatLanguageModel chatLanguageModel) {
        return AiServices.builder(ChatAssistant.class)
                .chatLanguageModel(chatLanguageModel)
                // 开发票的函数
                .tools(new InvoiceFunc())
                .build();
    }

}
