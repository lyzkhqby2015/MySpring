package com.zk.example.aop;


import com.zk.library.aop.annotation.After;
import com.zk.library.aop.annotation.Before;

public class MyAspect {

    @Before("com.zk.example.aop.bean.SayHello.say")
    public void before() {
        System.out.println("before");
    }


    @After("com.zk.example.aop.bean.SayHello.say")
    public void after() {
        System.out.println("after");
    }
}
