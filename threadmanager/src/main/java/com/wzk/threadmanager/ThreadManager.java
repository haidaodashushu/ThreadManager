package com.wzk.threadmanager;

import android.util.Log;

import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

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

    public void execute(final Task task, final Runnable callBack, String threadPollName) {
        WorkerRunnable<Task, Boolean> workerRunnable = new WorkerRunnable<Task, Boolean>() {
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
        FutureTask futureTask = new FutureTask<Boolean>(workerRunnable) {
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
        workerRunnable.taskQueue = QueueTaskUtil.taskQueue(task);
        ThreadPoolManager.getInstance().submit(threadPollName, futureTask);
    }

    public void destroy() {
        mUiTaskHandler = null;
        ThreadPoolManager.getInstance().destroy();
    }

    private static abstract class WorkerRunnable<Task, Result> implements Callable<Result> {
        Queue<Task> taskQueue;
    }

}
