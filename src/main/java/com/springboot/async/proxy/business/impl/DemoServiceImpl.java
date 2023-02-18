package com.springboot.async.proxy.business.impl;

import com.springboot.async.proxy.business.DemoService;

public class DemoServiceImpl implements DemoService {
    @Override
    public void perform() throws InterruptedException {
        System.out.println((String.format("Request Thread:%s", Thread.currentThread().getName())));
        Thread.sleep(5000);
    }
}
