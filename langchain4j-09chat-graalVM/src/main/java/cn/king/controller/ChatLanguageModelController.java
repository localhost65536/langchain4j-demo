package cn.king.controller;

import cn.king.service.AiAssistant;
import dev.langchain4j.code.CodeExecutionEngine;
import dev.langchain4j.model.chat.ChatLanguageModel;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class ChatLanguageModelController {

    private static final Pattern CODE_BLOCK = Pattern.compile("```(?:javascript|js)?\\s*(.*?)```", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);

    @Resource
    private AiAssistant aiAssistant;

    @Resource
    private ChatLanguageModel chatLanguageModel;

    @Resource(name = "javaScriptExecutionEngine")
    private CodeExecutionEngine javaScriptExecutionEngine;

    // 1) Tool calling 模式：模型自动调用 GraalVmJavaScriptExecutionTool
    // http://localhost:9009/chatgraalvm/tool?prompt=请用js计算斐波那契数列第8项
    @GetMapping("/chatgraalvm/tool")
    public String tool(@RequestParam("prompt") String prompt) {
        return aiAssistant.chat(prompt);
    }

    // 2) Manual 模式：模型生成代码 -> 本地执行 -> 执行结果再返回模型
    // http://localhost:9009/chatgraalvm/fibonacci?n=8
    @GetMapping("/chatgraalvm/fibonacci")
    public Map<String, String> fibonacci(@RequestParam("n") int n) {

        String codePrompt = """
                你是 JavaScript 代码生成器。
                请只输出一段可直接执行的 JavaScript 代码，不要任何解释。
                要求定义 fibonacci(n) 并返回 fibonacci(%d) 的值。
                """.formatted(n);

        String modelCodeRaw = chatLanguageModel.generate(codePrompt);
        String jsCode = extractCode(modelCodeRaw);
        String executionResult = javaScriptExecutionEngine.execute(jsCode);

        String finalAnswerPrompt = """
                用户问题：斐波那契数列第 %d 项是多少？
                JavaScript 执行结果：%s
                请基于执行结果给出最终回答，简洁即可。
                """.formatted(n, executionResult);

        String finalAnswer = chatLanguageModel.generate(finalAnswerPrompt);

        Map<String, String> response = new LinkedHashMap<>();
        response.put("generatedCode", jsCode);
        response.put("executionResult", executionResult);
        response.put("finalAnswer", finalAnswer);
        return response;
    }

    private String extractCode(String raw) {
        Matcher matcher = CODE_BLOCK.matcher(raw);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return raw.trim();
    }

}
