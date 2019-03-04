package com.wzk.threadmanager;

import android.text.TextUtils;
import android.util.Log;

import com.wzk.threadmanager.pool.ThreadPool;
import com.wzk.threadmanager.pool.ThreadPoolImpl;
import com.wzk.threadmanager.pool.ThreadPoolInfo;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by 政魁 on 2019/2/26 16:23
 * E-Mail Address：wangzhengkui@yingzi.com
 */
class ThreadPoolManager {
    public static final String TAG = "ThreadPoolManager";
    private ThreadPool mThreadPool = new ThreadPoolImpl();
    List<ThreadPoolInfo> mThreadPoolInfos;
    UITaskHandler mUiTaskHandler;

    private ThreadPoolManager() {
        mUiTaskHandler = new UITaskHandler();
    }

    void execute(Task task, Runnable callback, String threadPollName) {
        ThreadPoolInfo threadPoolInfo = getThreadPoolInfo(threadPollName);
        if (threadPoolInfo != null && threadPoolInfo.isMultiThread()) {
            executeMultiThread(task, callback, threadPollName);
        } else {
            executeOneThread(task, callback, threadPollName);
        }
    }

    /**
     * @param task           任务树，该task
     * @param callBack
     * @param threadPollName
     */
    private void executeOneThread(Task task, final Runnable callBack, String threadPollName) {
        WorkerRunnableQueue<Task, Boolean> workerRunnableQueue = new WorkerRunnableQueue<Task, Boolean>() {
            @Override
            public Boolean call() throws Exception {
                while (taskQueue != null && !taskQueue.isEmpty()) {
                    Task task = taskQueue.poll();
                    if (task != null) {
                        task.run();
                    }
                }
                return true;
            }
        };
        FutureTask futureTask = new FutureTask<Boolean>(workerRunnableQueue) {
            @Override
            protected void done() {
                super.done();
                //执行完毕；
                Log.i(TAG, "done: " + Thread.currentThread().getName());
                if (callBack != null) {
                    mUiTaskHandler.handleTask(callBack);
                }
            }
        };
        workerRunnableQueue.taskQueue = QueueTaskUtil.taskQueue(task);
        mThreadPool.execute(threadPollName, futureTask);
    }

    /**
     * @param task           要执行的任务
     * @param callBack       回调任务执行完成
     * @param threadPollName 线程池名字
     */
    private void executeMultiThread(Task task, final Runnable callBack, String threadPollName) {
        final AtomicInteger atomicInteger = new AtomicInteger(0);
        ThreadPoolManager.OnResultListener resultListener = new ThreadPoolManager.OnResultListener() {
            @Override
            public void onResult(Object object) {
                int decrementAndGet = atomicInteger.decrementAndGet();
                if (decrementAndGet == 0) {
                    mUiTaskHandler.handleTask(callBack);
                }
            }
        };
        Queue<Task> taskQueue = QueueTaskUtil.taskQueue(task);
        atomicInteger.set(taskQueue.size());
        while (!taskQueue.isEmpty()) {
            taskQueue.poll().setOnResultListener(resultListener);
        }
        Task root = QueueTaskUtil.getRootTask(task);
        mThreadPool.execute(threadPollName, root);
    }

    private static class SingleTon {
        private static final ThreadPoolManager sInstance = new ThreadPoolManager();
    }

    public static ThreadPoolManager getInstance() {
        return SingleTon.sInstance;
    }

    void init(List<ThreadPoolInfo> threadPoolInfos) {
        mThreadPoolInfos = threadPoolInfos;
        mThreadPool.init(threadPoolInfos);
    }

    public ThreadPoolInfo getThreadPoolInfo(String threadPoolName) {
        if (mThreadPoolInfos == null || mThreadPoolInfos.size() == 0) {
            return null;
        }
        if (TextUtils.isEmpty(threadPoolName)) {
            return null;
        }
        for (ThreadPoolInfo threadPoolInfo : mThreadPoolInfos) {
            if (threadPoolName.equals(threadPoolInfo.getName())) {
                return threadPoolInfo;
            }
        }
        return null;
    }

    public void destroy() {
        mUiTaskHandler = null;
        mThreadPool.destroy();
    }

    static interface OnResultListener<V> {
        void onResult(V v);
    }

    private static abstract class WorkerRunnableQueue<Task, Result> implements Callable<Result> {
        Queue<Task> taskQueue;
    }

}
