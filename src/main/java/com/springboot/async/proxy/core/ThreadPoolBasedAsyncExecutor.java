package com.springboot.async.proxy.core;

import com.springboot.async.proxy.api.AsyncExecutor;
import com.springboot.async.proxy.api.AsyncResult;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.*;

public class ThreadPoolBasedAsyncExecutor extends ThreadPoolExecutor implements AsyncExecutor {
    private static volatile boolean isInit = false;
    private static volatile boolean isDestroy = false;
    private static ExecutorService executorService = null;

    public ThreadPoolBasedAsyncExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public ThreadPoolBasedAsyncExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    public ThreadPoolBasedAsyncExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    public ThreadPoolBasedAsyncExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }


    public static <T> AsyncResult<T> submit(Object target, Method method, Object[] objects) {
        //初始化的判断
        if (!isInit) {
            init();
        }
        //通过线程池提交
        Future future = executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    method.invoke(target, objects);
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
        FutureBasedAsyncResult<T> asyncResult = new FutureBasedAsyncResult<>();
        asyncResult.setFuture(future);
        return asyncResult;
    }

    private static synchronized void init() {
        try {
            if (isInit) {
                return;
            }
            executorService = Executors.newFixedThreadPool(10);
            updateExecutorStatus(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static synchronized void destory() {
        if (isDestroy) {
            return;
        }
    }

    private static void updateExecutorStatus(final boolean initStatus) {
        isInit = initStatus;
        isDestroy = !isInit;
    }

}
