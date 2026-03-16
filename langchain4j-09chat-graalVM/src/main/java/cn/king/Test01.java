package cn.king;

import dev.langchain4j.code.CodeExecutionEngine;
import dev.langchain4j.code.graalvm.GraalVmJavaScriptExecutionEngine;
import dev.langchain4j.code.graalvm.GraalVmPythonExecutionEngine;

public class Test01 {

    /**
     * 斐波那契数列是指这样一个数列：0，1，1，2，3，5，8，13，21，34，55，89……
     * 这个数列从第3项开始 ，每一项都等于前两项之和
     */
    public static void main(String[] args) {
        fibonacciByJavaScript();
        // fibonacciByPython();
    }

    private static void fibonacciByJavaScript() {
        CodeExecutionEngine javaScriptExecutionEngine = new GraalVmJavaScriptExecutionEngine();

        // 斐波那契数列的定义：每一项都是前两项的和，除了最开始的两个指定数值外
        String code =
                """
                function fibonacci(n)
                {
                    if (n <= 1) return n;
                    return fibonacci(n - 1) + fibonacci(n - 2);
                }
            
                fibonacci(7)
                """;

        String result = javaScriptExecutionEngine.execute(code);
        System.out.println(result);
    }

    private static void fibonacciByPython() {
        CodeExecutionEngine pythonExecutionEngine = new GraalVmPythonExecutionEngine();

        String code = """
                def fibonacci_recursive(n):
                    if n <= 1:
                        return n
                    else:
                        return fibonacci_recursive(n-1) + fibonacci_recursive(n-2)
                
                # 打印前6个斐波那契数
                fibonacci_recursive(5)
                """;

        String result = pythonExecutionEngine.execute(code);
        System.out.println(result);
    }

}
