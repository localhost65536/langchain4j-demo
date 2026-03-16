package cn.king.controller;

import cn.king.service.ChatMemoryAssistant;
import cn.king.service.ChatWithOutMemoryAssistant;
import jakarta.annotation.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatLanguageModelController {

    @Resource
    private ChatWithOutMemoryAssistant chatWithOutMemoryAssistant;
    @Resource
    private ChatMemoryAssistant chatMemoryAssistant;

    // http://localhost:9006/chatmemory/test1
    @GetMapping(value = "/chatmemory/test1", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public void testWithOutChatMemory() {
        chatWithOutMemoryAssistant.chat("你好，我的名字叫张三").subscribe(System.out::println);
        chatWithOutMemoryAssistant.chat("我的名字是什么").subscribe(System.out::println);
    }

    @GetMapping(value = "/chatmemory/test2", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public void testWithChatMemory() {
        chatMemoryAssistant.chat(1L, "你好！我的名字是张三").subscribe(System.out::println);
        chatMemoryAssistant.chat(3L, "你好！我的名字是李四").subscribe(System.out::println);

        chatMemoryAssistant.chat(1L, "我的名字是什么").subscribe(System.out::println);
        chatMemoryAssistant.chat(3L, "我的名字是什么").subscribe(System.out::println);
    }

}