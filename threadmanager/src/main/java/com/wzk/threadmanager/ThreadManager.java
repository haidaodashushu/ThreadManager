package com.wzk.threadmanager;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Process;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by 政魁 on 2019/2/19 14:38
 * E-Mail Address：wangzhengkui@yingzi.com
 */
public class ThreadManager {
    public static final String TAG = "ThreadManager";
    private final Object mMainLock = new Object();
    private final Object mBackGroundLock = new Object();
    private final Object mWorkLock = new Object();
    private Handler mMainHandler;
    private Handler mBackGroundHandler;
    private Handler mWorkHandler;

    public ThreadManager() {
    }
    public void execute(@ThreadRoad int road, Task task) {
        switch (road) {
            case ThreadRoad.MAIN_THREAD:
                executeOnMain(task);
            case ThreadRoad.BACKGROUND_THREAD:
                executeOnBackground(task);
            case ThreadRoad.WORK_THREAD:
                executeOnWork(task);
        }
    }

    private void executeOnMain(Task task) {
        synchronized (mMainLock) {
            if (mMainHandler == null) {
                synchronized (mMainLock) {
                    mMainHandler = new Handler(Looper.getMainLooper());
                }
            }
        }
        handleTask(mMainHandler, task);
    }

    private void executeOnBackground(final Task task) {
        synchronized (mBackGroundLock) {
            if (mBackGroundHandler == null) {
                synchronized (mBackGroundLock) {
                    mBackGroundHandler = createHandler("BackgroundThread");
                }
            }
        }
        handleTask(mBackGroundHandler, task);

    }

    private void executeOnWork(Task task) {
        synchronized (mWorkLock) {
            if (mWorkHandler == null) {
                synchronized (mWorkLock) {
                    mWorkHandler = createHandler("WorkHandler");
                }
            }
        }
        handleTask(mWorkHandler, task);
    }

    private void handleTask(Handler handler, Task task) {
        if (task == null) {
            return;
        }
        //先找到树的跟节点；
        Task root = task.mRoot;
        while (root != null && root.mRoot != null) {
            root = root.mRoot;
        }

        if (root == null) {
            root = task;
        }

        Queue<Task> taskQueue = new LinkedList<>();
        //对树做广度优先遍历
        Queue<Task> tempQueue = new LinkedList<>();
        tempQueue.offer(root);
        while (!tempQueue.isEmpty()) {
            Task temp = tempQueue.poll();
            LinkedList<Task> childList = temp.mChildList;
            if (childList != null && childList.size() > 0) {
                for (Task childTask : childList) {
                    tempQueue.offer(childTask);
                }
            }
            taskQueue.offer(temp);
        }

        while (!taskQueue.isEmpty()) {
            handler.post(taskQueue.poll());
        }
    }

    private Handler createHandler(String name) {
        HandlerThread handlerThread = new HandlerThread(name);

        handlerThread.start();
        return new Handler(handlerThread.getLooper());
    }

}
