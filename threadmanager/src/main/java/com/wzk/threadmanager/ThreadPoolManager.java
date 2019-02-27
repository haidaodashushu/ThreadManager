package com.wzk.threadmanager;

import com.wzk.threadmanager.pool.ThreadPool;
import com.wzk.threadmanager.pool.ThreadPoolImpl;
import com.wzk.threadmanager.pool.ThreadPoolInfo;

import java.util.List;
import java.util.concurrent.FutureTask;

/**
 * Created by 政魁 on 2019/2/26 16:23
 * E-Mail Address：wangzhengkui@yingzi.com
 */
class ThreadPoolManager {
    public static final String TAG = "ThreadPoolManager";
    ThreadPool mThreadPool = new ThreadPoolImpl();

    private ThreadPoolManager() {
    }

    void submit(String threadPollName, FutureTask futureTask) {
        mThreadPool.submit(threadPollName, futureTask);
    }

    private static class SingleTon {
        private static final ThreadPoolManager sInstance = new ThreadPoolManager();
    }

    public static ThreadPoolManager getInstance() {
        return SingleTon.sInstance;
    }

    void init(List<ThreadPoolInfo> threadPoolInfos) {
        mThreadPool.init(threadPoolInfos);
    }

    public void destroy() {
        mThreadPool.destory();
    }

}
