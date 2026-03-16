package cn.king.controller;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.output.Response;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/langchain4j")
public class HelloController {

    @Resource
    private ChatLanguageModel chatLanguageModel;

    // http://localhost:9001/langchain4j/hello?prompt=你好
    // 方法签名: String generate(String userMessage)
    @GetMapping(value = "/hello")
    public String hello(@RequestParam(value = "prompt", defaultValue = "hello") String prompt) {
        String result = chatLanguageModel.generate(prompt);
        log.info("result: {}", result);
        return result;
    }

    // http://localhost:9001/langchain4j/hello2?prompt=你好
    // 方法签名: Response<AiMessage> generate(ChatMessage... messages)
    @GetMapping(value = "/hello2")
    public String hello2(@RequestParam("prompt") String prompt) {
        Response<AiMessage> aiMessageResponse = chatLanguageModel.generate(UserMessage.from(prompt));
        return aiMessageResponse.content().text();
    }

}

