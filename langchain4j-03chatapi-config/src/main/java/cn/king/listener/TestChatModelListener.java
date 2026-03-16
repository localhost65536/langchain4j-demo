package cn.king.listener;

import cn.hutool.core.util.IdUtil;
import dev.langchain4j.model.chat.listener.ChatModelErrorContext;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.chat.listener.ChatModelRequestContext;
import dev.langchain4j.model.chat.listener.ChatModelResponseContext;
import lombok.extern.slf4j.Slf4j;

/**
 * 监听器就是请求大模型前 请求后 出异常 时执行对应方法
 */
@Slf4j
public class TestChatModelListener implements ChatModelListener {

    @Override
    public void onRequest(ChatModelRequestContext requestContext) {
        // onRequest配置的k:v键值对，在onResponse阶段可以获得，上下文传递参数好用
        String uuidValue = IdUtil.simpleUUID();
        requestContext.attributes().put("TraceID-0316", uuidValue);
        log.info("*****************>请求参数 requestContext:{}", requestContext + "\t" + uuidValue);
    }

    @Override
    public void onResponse(ChatModelResponseContext responseContext) {
        // 能跨方法传参
        Object object = responseContext.attributes().get("TraceID-0316");
        log.info("*****************>返回结果 responseContext:{}", object);
    }

    @Override
    public void onError(ChatModelErrorContext errorContext) {
        log.error("请求异常 ChatModelErrorContext:{}", errorContext);
    }

}