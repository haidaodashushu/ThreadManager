package com.wzk.threadmanager;


import java.util.LinkedList;

/**
 * Created by 政魁 on 2019/2/19 14:54
 * E-Mail Address：wangzhengkui@yingzi.com
 */
public abstract class Task<V> implements Runnable{
    public Task mRoot;
    public LinkedList<Task> mChildList;

    public Task dependOn(Task task) {
        if (task == this) {
            return this;
        }
        this.mRoot = task;
        if (task.mChildList == null) {
            task.mChildList = new LinkedList<>();
        }
        task.mChildList.add(this);
        return task;
    }
}
