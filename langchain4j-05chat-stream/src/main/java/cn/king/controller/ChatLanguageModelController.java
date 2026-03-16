package cn.king.controller;

import cn.king.service.ChatAssistant;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.StreamingResponseHandler;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.output.Response;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

@RestController
@Slf4j
public class ChatLanguageModelController {

    @Resource
    private StreamingChatLanguageModel streamingChatLanguageModel;

    @Resource
    private ChatAssistant chatAssistant;

    // http://localhost:9005/chatStream?prompt=介绍一下鲁菜
    // 此接口为演示 demo, 能用的接口为 chatStream1 chatStream2
    @GetMapping(value = "/chatStream")
    public void chatStream(@RequestParam("prompt") String prompt,
                           HttpServletResponse response) {
        // 设置响应的字符编码
        response.setCharacterEncoding("UTF-8");

        // 调用 chatFlux 方法返回值的 subscribe (订阅) 方法
        // Reactor 里一旦 subscribe()，就会触发整条数据流开始执行；
        chatAssistant.chatFlux(prompt).subscribe(
                // 流式输出有新内容时执行 (打印流式输出内容)
                System.out::println,
                // 代码报错时执行
                Throwable::printStackTrace,
                // 流式输出完成时执行
                () -> System.out.println("onComplete")
        );

    }

    @GetMapping(value = "/chatStream1", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatStream1(@RequestParam("prompt") String prompt,
                                    HttpServletResponse response) {
        // 设置响应的字符编码
        response.setCharacterEncoding("UTF-8");
        // 让 Spring 去订阅并写回响应
        // 在典型的响应式应用里，通常不需要自己显式调用 subscribe()，
        // 而是把 Flux 交给 Spring 这样的框架去处理订阅和输出。Project Reactor 官方文档就是这么建议的。
        return chatAssistant.chatFlux(prompt);
    }

    // 用低级 api 演示流式输出用法
    @GetMapping(value = "/chatStream2", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter chatStream2(@RequestParam("prompt") String prompt,
                                  HttpServletResponse response) {
        // 设置响应字符编码
        response.setCharacterEncoding("UTF-8");
        // 0L 表示不超时；也可以改成 60_000L
        SseEmitter emitter = new SseEmitter(0L);

        streamingChatLanguageModel.generate(prompt, new StreamingResponseHandler<AiMessage>() {

            @Override
            public void onNext(String token) {
                try {
                    // 把每个 token 推给前端
                    emitter.send(SseEmitter.event().name("token").data(token));
                } catch (Exception e) {
                    log.error("发送 SSE 失败", e);
                    emitter.completeWithError(e);
                }
            }

            @Override
            public void onComplete(Response<AiMessage> resp) {
                try {
                    // 告诉前端流结束了
                    emitter.send(SseEmitter.event().name("done").data("[DONE]"));
                } catch (Exception e) {
                    log.error("发送完成事件失败", e);
                } finally {
                    emitter.complete();
                }
            }

            @Override
            public void onError(Throwable error) {
                log.error("大模型调用失败", error);
                emitter.completeWithError(error);
            }
        });

        return emitter;
    }

}

