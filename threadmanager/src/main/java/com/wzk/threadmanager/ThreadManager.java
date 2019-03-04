package com.wzk.threadmanager;

/**
 * Created by 政魁 on 2019/2/19 14:38
 * E-Mail Address：wangzhengkui@yingzi.com
 */
public class ThreadManager {

    private ThreadManager() {
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

    public void execute(Task task, final Runnable callBack, String threadPollName) {
        ThreadPoolManager.getInstance().execute(task, callBack, threadPollName);

    }

    public void destroy() {
        ThreadPoolManager.getInstance().destroy();
    }

}
