package com.wzk.threadmanager;

import com.wzk.threadmanager.pool.DependThreadPoolImpl;
import com.wzk.threadmanager.pool.ThreadPool;
import com.wzk.threadmanager.pool.ThreadPoolImpl;
import com.wzk.threadmanager.pool.ThreadPoolInfo;

import java.util.List;

/**
 * Created by 政魁 on 2019/2/26 16:23
 * E-Mail Address：wangzhengkui@yingzi.com
 */
class ThreadPoolManager {
    private ThreadPool mThreadPool = new ThreadPoolImpl();
    private ThreadPool mDependThreadPool = new DependThreadPoolImpl();
    private ThreadPoolManager() {
    }

    void execute(String threadPollName, Runnable runnable) {
        mThreadPool.execute(threadPollName, runnable);
    }

    /**
     * 依赖任务只有一个线程池
     * @param runnable
     */
    void dependExecute(Runnable runnable, OnResultListener resultListener ) {
        mDependThreadPool.execute(runnable);
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

    static interface OnResultListener<V> {
        void onResult(V v);
    }


}
