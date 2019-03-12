package com.wzk.threadmanager;

import com.wzk.threadmanager.exception.DependenceException;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by 政魁 on 2019/2/19 14:54
 * E-Mail Address：wangzhengkui@yingzi.com
 */
public abstract class Task<V> implements Runnable {
    Task<V> mRoot;
    List<Task<V>> mChildList;
    private String mName;
    private TaskWrap<V> mTaskWrap;
    private int mState = TaskState.STATE_IDLE;

    public Task(String name) {
        this.mName = name;
        mTaskWrap = new TaskWrap<>(this);
    }

    public abstract V runTask();

    @Override
    public final void run() {
        mTaskWrap.run();
    }

    public Task dependOn(Task task) {
        if (task == this) {
            return this;
        }
        if (mRoot != null) {
            throw new DependenceException(this);
        }
        //检查循环依赖
        QueueTaskUtil.checkCyclicDepend(this, task);
        this.mRoot = task;
        if (task.mChildList == null) {
            task.mChildList = new LinkedList<>();
        }
        task.mChildList.add(this);
        return task;
    }

    public List<Task<V>> getChildList() {
        return mChildList;
    }

    public String getName() {
        return mName;
    }

    public V get() {
        return mTaskWrap.get();
    }
    @Override
    public String toString() {
        return "Task{" +
                "mName='" + mName + '\'' +
                '}';
    }

    void setOnResultListener(ThreadPoolManager.OnResultListener<V> listener) {
        mTaskWrap.setOnResultListener(listener);
    }

    public void setState(@TaskState int state) {
        this.mState = state;
    }

    public boolean isRunning() {
        return mState == TaskState.STATE_RUNING;
    }

    public boolean isIdle() {
        return mState == TaskState.STATE_IDLE;
    }

    public boolean isDone() {
        return mState == TaskState.STATE_DONE;
    }

}
