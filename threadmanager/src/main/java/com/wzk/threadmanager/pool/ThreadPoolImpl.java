package com.wzk.threadmanager.pool;

import android.text.TextUtils;
import android.util.Log;

import com.wzk.threadmanager.ThreadPoolBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * Created by 政魁 on 2019/2/27 17:12
 * E-Mail Address：wangzhengkui@yingzi.com
 */
public class ThreadPoolImpl implements ThreadPool {
    static final String TAG = "ThreadPoolImpl";
    Map<String, ExecutorService> mThreadPoolMaps = new HashMap<>(8);
    ExecutorService mDefaultExecutorService;

    @Override
    public void init(List<ThreadPoolInfo> threadPoolInfos) {
        if (threadPoolInfos == null || threadPoolInfos.size() == 0) {
            return;
        }

        for (ThreadPoolInfo threadPoolInfo : threadPoolInfos) {
            if (!checkThreadInfo(threadPoolInfo)) {
                continue;
            }
            ensureThreadPoolEnable(threadPoolInfo);
            Log.i(TAG, "init: " + threadPoolInfo.toString());
            ThreadPoolExecutor threadPoolExecutor = createThreadPoolExecutor(
                    threadPoolInfo.corePoolSize, threadPoolInfo.maxPoolSize, threadPoolInfo.keepAliveTime,
                    threadPoolInfo.workQueueSize, threadPoolInfo.name);
            threadPoolExecutor.allowCoreThreadTimeOut(true);
            mThreadPoolMaps.put(threadPoolInfo.name, threadPoolExecutor);
        }
    }

    @Override
    public void execute(Runnable runnable) {
        execute(null, runnable);
    }

    @Override
    public void execute(String poolName, Runnable runnable) {
        if (runnable == null) {
            return;
        }
        ExecutorService existsThreadPool = getExistsThreadPool(poolName);
        existsThreadPool.execute(runnable);
    }

    @Override
    public Future submit(String poolName, Runnable runnable) {
        if (runnable == null) {
            return null;
        }
        ExecutorService existsThreadPool = getExistsThreadPool(poolName);
        return existsThreadPool.submit(runnable);
    }

    @Override
    public void destory() {
        Set<Map.Entry<String, ExecutorService>> entrySet = mThreadPoolMaps.entrySet();
        for (Map.Entry<String, ExecutorService> executorServiceEntry : entrySet) {
            executorServiceEntry.getValue().shutdown();
        }
    }

    @Override
    public ThreadPoolExecutor createThreadPoolExecutor(int coreSize, int maxThreadSize, int keepAliveTime, int workQueueSize, String name) {
        BlockingQueue<Runnable> poolWorkQueue =
                new LinkedBlockingQueue<Runnable>(workQueueSize);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                coreSize, maxThreadSize, keepAliveTime, TimeUnit.SECONDS,
                poolWorkQueue, new DefaultThreadFactory(name));
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        return threadPoolExecutor;
    }

    private ThreadPoolExecutor createDefaultThreadPoolExecutor() {
        ThreadPoolExecutor threadPoolExecutor = createThreadPoolExecutor(
                ThreadPoolBuilder.getDefaultCorePoolSize(), ThreadPoolBuilder.getDefaultMaxPoolSize(), ThreadPoolBuilder.getDefaultKeepAliveTime(),
                ThreadPoolBuilder.getDefaultWorkQueueSize(), "wzk_default");
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        return threadPoolExecutor;
    }

    private ExecutorService getThreadPool(String threadPoolName) {
        if (TextUtils.isEmpty(threadPoolName)) {
            throw new IllegalArgumentException("thread pool name is empty");
        }

        return mThreadPoolMaps.get(threadPoolName);
    }

    private ExecutorService getExistsThreadPool(String threadPoolName) {
        if (TextUtils.isEmpty(threadPoolName)) {
            if (mDefaultExecutorService == null) {
                mDefaultExecutorService = createDefaultThreadPoolExecutor();
            }
            return mDefaultExecutorService;
        }

        ExecutorService threadPool = getThreadPool(threadPoolName);
        if (null == threadPool) {
            throw new IllegalArgumentException(String.format("thread pool %s not exists", threadPoolName));
        }

        return threadPool;
    }

    private boolean checkThreadInfo(ThreadPoolInfo threadPoolInfo) {
        if (threadPoolInfo == null) {
            return false;
        }

        if (TextUtils.isEmpty(threadPoolInfo.name)) {
            throw new IllegalArgumentException("thread pool name is empty");
        }
        return true;
    }

    private void ensureThreadPoolEnable(ThreadPoolInfo threadPoolInfo) {
        if (threadPoolInfo.corePoolSize <= 0) {
            threadPoolInfo.setCorePoolSize(ThreadPoolBuilder.getDefaultCorePoolSize());
        }
        if (threadPoolInfo.maxPoolSize <= 0) {
            threadPoolInfo.setMaxPoolSize(ThreadPoolBuilder.getDefaultMaxPoolSize());
        }

        if (threadPoolInfo.keepAliveTime <= 0) {
            threadPoolInfo.setKeepAliveTime(ThreadPoolBuilder.getDefaultKeepAliveTime());
        }

        if (threadPoolInfo.workQueueSize <= 0) {
            threadPoolInfo.setWorkQueueSize(ThreadPoolBuilder.getDefaultWorkQueueSize());
        }
    }
}
