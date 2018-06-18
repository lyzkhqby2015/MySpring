package com.zk.example.aop.bean;

public class SayHelloImpl implements SayHello {

    @Override
    public void say() {
        System.out.println("Hello world");
    }

}
