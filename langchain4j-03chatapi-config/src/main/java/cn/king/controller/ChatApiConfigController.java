package cn.king.controller;

import dev.langchain4j.model.chat.ChatLanguageModel;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: wjl@king.cn
 * @time: 2026/3/15
 * @version: 1.0.0
 * @description:
 */
@RestController
public class ChatApiConfigController {

    @Resource
    private ChatLanguageModel chatLanguageModel;

    // http://localhost:9003/chat?prompt=你好
    @GetMapping(value = "/chat")
    public String chat(@RequestParam("prompt") String prompt) {
        return chatLanguageModel.generate(prompt);
    }

}
