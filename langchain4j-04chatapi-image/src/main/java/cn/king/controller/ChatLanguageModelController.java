package cn.king.controller;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.output.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.Base64;

@Slf4j
@RestController
public class ChatLanguageModelController {

    @Autowired
    private ChatLanguageModel chatLanguageModel;

    @Value("classpath:static/img/1.jpg")
    private Resource resource;

    // http://localhost:9004/image/call
    @GetMapping(value = "/image/call")
    public String readImageContent() throws IOException {
        // 获取字节数组
        byte[] byteArray = resource.getContentAsByteArray();
        // 把字节数组以 base64 编码的形式变成字符串
        String base64Data = Base64.getEncoder().encodeToString(byteArray);
        // log.info("base64Data: {}", base64Data);

        // 提示词, 包括问题和图片
        UserMessage userMessage = UserMessage.from(
                TextContent.from("这张图片描述的是什么"),
                ImageContent.from(base64Data, "image/jpeg")
        );

        Response<AiMessage> response = chatLanguageModel.generate(userMessage);
        return response.content().text();
    }

}
