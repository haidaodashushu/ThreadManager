package com.wzk.threadmanager;

import com.wzk.threadmanager.pool.ThreadPoolInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 政魁 on 2019/2/26 16:23
 * E-Mail Address：wangzhengkui@yingzi.com
 */
public class ThreadPoolBuilder {
    //这里参考AsyncTask中的值
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    // We want at least 2 threads and at most 4 threads in the core pool,
    // preferring to have 1 less than the CPU count to avoid saturating
    // the CPU with background work
    private static final int CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4));
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final int KEEP_ALIVE_SECONDS = 30;

    private static final int WORK_QUEUE_SIZE = 128;

    List<ThreadPoolInfo> mThreadPoolInfoList = new ArrayList<>();

    public ThreadPoolManager build() {
        ThreadPoolManager threadPoolManager = ThreadPoolManager.getInstance();
        threadPoolManager.init(mThreadPoolInfoList);
        return threadPoolManager;
    }

    public ThreadPoolBuilder addThreadPool(ThreadPoolInfo threadPoolInfo) {
        mThreadPoolInfoList.add(threadPoolInfo);
        return this;
    }

    public ThreadPoolBuilder createThreadPool() {
        ThreadPoolInfo threadPoolInfo = new ThreadPoolInfo();
        addThreadPool(threadPoolInfo);
        return this;
    }

    private ThreadPoolInfo getLastThreadPollInfo() {
        int size = mThreadPoolInfoList.size();
        return mThreadPoolInfoList.get(size - 1);
    }

    public ThreadPoolBuilder setName(String name) {
        getLastThreadPollInfo().setName(name);
        return this;
    }

    public ThreadPoolBuilder setMaxPoolSize(int maxPoolSize) {
        getLastThreadPollInfo().setMaxPoolSize(maxPoolSize);
        return this;
    }

    public ThreadPoolBuilder setCorePoolSize(int corePoolSize) {
        getLastThreadPollInfo().setCorePoolSize(corePoolSize);
        return this;
    }

    public ThreadPoolBuilder setKeepAliveTime(int keepAliveTime) {
        getLastThreadPollInfo().setKeepAliveTime(keepAliveTime);
        return this;
    }

    public ThreadPoolBuilder setWorkQueueSize(int workQueueSize) {
        getLastThreadPollInfo().setWorkQueueSize(workQueueSize);
        return this;
    }

    public ThreadPoolBuilder setMultiThread(boolean multiThread) {
        getLastThreadPollInfo().setMultiThread(multiThread);
        return this;
    }

    public static int getDefaultCorePoolSize() {
        return CORE_POOL_SIZE;
    }

    public static int getDefaultMaxPoolSize() {
        return MAXIMUM_POOL_SIZE;
    }

    public static int getDefaultKeepAliveTime() {
        return KEEP_ALIVE_SECONDS;
    }

    public static int getDefaultWorkQueueSize() {
        return WORK_QUEUE_SIZE;
    }
}
