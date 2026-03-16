package cn.king.function;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import lombok.extern.slf4j.Slf4j;

/**
 * 开发票的函数
 */
@Slf4j
public class InvoiceFunc {

    @Tool("根据用户提交的开票信息进行开票")
    public String handle(@P("公司名称") String companyName,
                         @P("税号") String dutyNumber,
                         @P("金额保留两位有效数字") String amount) throws Exception {
        log.info("调用函数的参数: companyName => {}, dutyNumber => {}, amount => {}", companyName, dutyNumber, amount);
        return "开票成功 (函数调用完毕)";
    }

}
