package com.springboot.async.proxy.main;

import com.springboot.async.proxy.api.AsyncProxy;
import com.springboot.async.proxy.business.DemoService;
import com.springboot.async.proxy.business.impl.DemoServiceImpl;
import com.springboot.async.proxy.core.DynamicProxy;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println(String.format("Main Thread:%s", Thread.currentThread().getName()));
        DemoService demoService = new DemoServiceImpl();

        AsyncProxy asyncProxy = new DynamicProxy(demoService);
        DemoService target = (DemoService) asyncProxy.proxy();
        target.perform();
    }
}
