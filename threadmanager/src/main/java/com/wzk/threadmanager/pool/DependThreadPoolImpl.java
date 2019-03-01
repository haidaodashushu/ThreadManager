package com.wzk.threadmanager.pool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by 政魁 on 2019/2/28 20:07
 * E-Mail Address：wangzhengkui@yingzi.com
 */
public class DependThreadPoolImpl extends ThreadPoolImpl {

    @Override
    public ThreadPoolExecutor createThreadPoolExecutor(int coreSize, int maxThreadSize, int keepAliveTime, int workQueueSize, String name) {
        BlockingQueue<Runnable> workQueue = new LinkedBlockingDeque<Runnable>(workQueueSize);
        return new DependThreadPoolExecutor(coreSize, maxThreadSize, keepAliveTime, workQueue, name);
    }
}
