package com.zk.example.aop;

import com.zk.example.aop.bean.SayHello;
import com.zk.example.aop.bean.SayHelloImpl;
import com.zk.library.aop.AspectManager;

import java.util.HashMap;
import java.util.Map;

public class AopMain {

    public static void main(String[] args) {
        Map<String, Object> beanMap = new HashMap<>();
        SayHelloImpl sayHello = new SayHelloImpl();
        beanMap.put("SayHello", sayHello);
        AspectManager aspectManager = new AspectManager(beanMap);
        try {
            Class clazz = Class.forName("com.zk.example.aop.MyAspect");
            aspectManager.parse(clazz);
            ((SayHello)beanMap.get("SayHello")).say();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
