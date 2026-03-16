package cn.king.controller;

import cn.king.service.ChatAssistant;
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
public class ChatApiController {

    @Resource
    private ChatAssistant chatAssistant;

    // http://localhost:9003/highApiTest?prompt=你好
    @GetMapping("/highApiTest")
    public String highApiTest(@RequestParam("prompt") String prompt) {
        return chatAssistant.chat(prompt);
    }

}
