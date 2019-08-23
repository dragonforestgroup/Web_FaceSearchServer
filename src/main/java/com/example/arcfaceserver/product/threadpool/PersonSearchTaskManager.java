package com.example.arcfaceserver.product.threadpool;

import com.example.arcfaceserver.common.CompareResult;
import com.example.arcfaceserver.product.beans.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池管理
 */

public class PersonSearchTaskManager {

    private static PersonSearchTaskManager instance;

    public static PersonSearchTaskManager getInstance() {
        if (instance == null)
            instance = new PersonSearchTaskManager();
        return instance;
    }

    /**
     * 线程池
     */
    private ThreadPoolExecutor threadPoolExecutor;

    private PersonSearchTaskManager() {
        /* 创建线程池：
         * 核心线程数       10
         * 非核心线程数   20
         * 超时时间           10s
         * 队列长度           5
         */
        //threadPoolExecutor=new ThreadPoolExecutor(5, 20, 10, TimeUnit.SECONDS,new LinkedBlockingQueue<>(5),rejectedExecutionHandler);
//        threadPoolExecutor=(ThreadPoolExecutor) Executors.newCachedThreadPool();
        threadPoolExecutor = (ThreadPoolExecutor) Executors.newScheduledThreadPool(10);

    }


    /**
     * 执行任务队列
     *
     * @param taskList
     * @return
     */
    public List<Future<Result>> excute(List<PersonSearchTask> taskList) {
        List<Future<Result>> futureList = new ArrayList<>();
        for (PersonSearchTask task : taskList) {
            Future<Result> future = threadPoolExecutor.submit(task);
            futureList.add(future);
        }
        return futureList;
    }

    /**
     * 停止
     */
    public void stop() {
        threadPoolExecutor.shutdown();
    }


}