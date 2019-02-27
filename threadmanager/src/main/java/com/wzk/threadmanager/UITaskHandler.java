package com.wzk.threadmanager;

import android.os.Handler;
import android.os.Looper;

import java.util.Queue;

/**
 * Created by 政魁 on 2019/2/27 17:47
 * E-Mail Address：wangzhengkui@yingzi.com
 */
class UITaskHandler {
    private final Object mMainLock = new Object();
    private Handler mMainHandler;

    UITaskHandler() {

    }

    void handleTask(Task task) {
        synchronized (mMainLock) {
            if (mMainHandler == null) {
                synchronized (mMainLock) {
                    mMainHandler = new Handler(Looper.getMainLooper());
                }
            }
        }
        handleMainTask(mMainHandler, task);
    }

    private void handleMainTask(Handler handler, Task task) {

        Queue<Task> taskQueue = QueueTaskUtil.taskQueue(task);
        while (taskQueue != null && !taskQueue.isEmpty()) {
            handler.post(taskQueue.poll());
        }
    }
}
