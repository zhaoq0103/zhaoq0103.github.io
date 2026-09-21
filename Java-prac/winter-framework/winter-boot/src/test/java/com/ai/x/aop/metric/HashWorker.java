package com.ai.x.aop.metric;


import com.ai.x.annotation.Component;

@Component
public class HashWorker extends BaseWorker {

    /*
    * 一个方法被声明为 final 时，它将不能被子类重写或覆盖,所以不能动态生成代理方法
    * 这里是无法代理的
    * */
    @Metric("SHA-1")
    public final String sha1(String input) {
        return hash("SHA-1", input);
    }

    @Metric("SHA-256")
    public String sha256(String input) {
        return hash("SHA-256", input);
    }
}
