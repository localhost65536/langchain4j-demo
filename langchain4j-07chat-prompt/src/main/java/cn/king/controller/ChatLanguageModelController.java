package cn.king.controller;

import cn.king.entity.LawPrompt;
import cn.king.service.LawAssistant;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ChatLanguageModelController {
    @Resource
    private LawAssistant lawAssistant;

    // 案例 1
    // http://localhost:9007/chatprompt/test1
    @GetMapping(value = "/chatprompt/test1")
    public void test1() {
        String chat = lawAssistant.chat("什么是知识产权？");
        System.out.println(chat);

        String chat2 = lawAssistant.chat("什么是java？");
        System.out.println(chat2);

        String chat3 = lawAssistant.chat("介绍下西瓜和芒果");
        System.out.println(chat3);

        String chat4 = lawAssistant.chat("飞机发动机原理");
        System.out.println(chat4);
    }

    // 案例 2
    @GetMapping(value = "/chatprompt/test2")
    public void test2() {
        LawPrompt prompt = new LawPrompt();
        prompt.setLegal("知识产权");
        prompt.setQuestion("什么是知识产权");

        String chat = lawAssistant.chat(prompt);
        System.out.println(chat);
    }

    @GetMapping(value = "/chatprompt/test3")
    public void test3() {
        // 默认 from 构造使用 it 属性作为默认占位符
        PromptTemplate template = PromptTemplate.from("请解释中国法律中的'{{it}}'概念。");
        Prompt prompt = template.apply("知识产权");
        System.out.println(prompt.text()); // 输出: 请解释中国法律中的'知识产权'概念。

        // apply 方法接受 Map 作为参数
        PromptTemplate template2 = PromptTemplate.from("请解释中国法律中的'{{legal1}}'概念v2。");
        Prompt prompt2 = template2.apply(Map.of("legal1", "知识产权"));
        System.out.println(prompt2.text()); // 输出: 请解释中国法律中的'知识产权'概念v2。
    }

}