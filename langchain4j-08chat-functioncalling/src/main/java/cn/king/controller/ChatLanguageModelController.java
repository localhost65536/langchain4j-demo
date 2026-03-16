package cn.king.controller;

import cn.king.service.ChatAssistant;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ChatLanguageModelController {
    @Resource
    private ChatAssistant functionAssistant;

    // http://localhost:9008/chatfunction/test1
    @GetMapping(value = "/chatfunction/test1")
    public void test1() {
        String chat = functionAssistant.chat("开张发票,公司: 老王科技有限公司 税号: wang123 金额: 648.12");
        log.info("模型响应: {}", chat);
    }

}