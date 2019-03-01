package com.wzk.threadmanager;

import java.util.LinkedList;

/**
 * Created by 政魁 on 2019/2/19 16:11
 * E-Mail Address：wangzhengkui@yingzi.com
 */
public class TaskGroup<E> extends Task<E> {
    LinkedList<Task> mTaskList;
    public TaskGroup(String name) {
        super(name);
        mTaskList = new LinkedList<>();
    }

    @Override
    public E runTask() {
        if (mTaskList.size() == 0) {
            return null;
        }
        for (Task task : mTaskList) {
            task.run();
        }
        return null;
    }


    public void addTask(Task task) {
        mTaskList.add(task);
    }
}
