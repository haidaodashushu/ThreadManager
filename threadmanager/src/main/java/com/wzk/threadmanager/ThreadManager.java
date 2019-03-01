package com.wzk.threadmanager;

import android.util.Log;

import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by 政魁 on 2019/2/19 14:38
 * E-Mail Address：wangzhengkui@yingzi.com
 */
public class ThreadManager {
    public static final String TAG = "ThreadManager";

    UITaskHandler mUiTaskHandler;

    private ThreadManager() {
        mUiTaskHandler = new UITaskHandler();
    }

    private static class SingleTon {
        private static final ThreadManager sInstance = new ThreadManager();
    }

    public static ThreadManager getInstance() {
        return SingleTon.sInstance;
    }

    public void execute(Task task) {
        execute(task, null);
    }

    public void execute(Task task, Task callback) {
        execute(task, callback, null);
    }

    /**
     *
     * @param task 任务树，该task
     * @param callBack
     * @param threadPollName
     */
    public void execute(Task task, final Runnable callBack, String threadPollName) {
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
        ThreadPoolManager.getInstance().execute(threadPollName, futureTask);
    }

    /**
     *
     * @param task
     * @param callBack
     * @param threadPollName
     * @param multiThread 是否使用多个线程
     */
    public void execute(Task task, final Runnable callBack, String threadPollName, boolean multiThread) {
        if (!multiThread) {
            execute(task, callBack, threadPollName);
            return;
        }

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
        ThreadPoolManager.getInstance().dependExecute(root, resultListener);

    }

    public void destroy() {
        mUiTaskHandler = null;
        ThreadPoolManager.getInstance().destroy();
    }

    private static abstract class WorkerRunnableQueue<Task, Result> implements Callable<Result> {
        Queue<Task> taskQueue;
    }

    private static abstract class WorkerRunnable<Task, Result> implements Callable<Result> {
        Task task;
    }
}
