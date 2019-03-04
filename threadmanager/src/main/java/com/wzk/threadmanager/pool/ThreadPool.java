package com.wzk.threadmanager.pool;

import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by 政魁 on 2019/2/26 15:48
 * E-Mail Address：wangzhengkui@yingzi.com
 */
public interface ThreadPool {

    void execute(Runnable runnable);
    void execute(String poolName, Runnable runnable);
    Future submit(String poolName, Runnable runnable);
    void init(List<ThreadPoolInfo> threadPoolInfos);
    ThreadPoolExecutor createThreadPoolExecutor(int coreSize, int maxThreadSize, int keepAliveTime, int workQueueSize, String name);
    ThreadPoolExecutor getThreadPoolExecutor(String poolName);
    void destroy();
}
