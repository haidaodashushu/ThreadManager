package com.wzk.threadmanager;

import java.util.LinkedList;

/**
 * Created by 政魁 on 2019/2/19 16:11
 * E-Mail Address：wangzhengkui@yingzi.com
 */
public class TaskGroup extends Task {
    LinkedList<Task> mTaskList;
    public TaskGroup(String name) {
        super(name);
        mTaskList = new LinkedList<>();
    }

    public void addTask(Task task) {
        mTaskList.add(task);
    }

    @Override
    public void run() {
        if (mTaskList.size() == 0) {
            return;
        }
        for (Task task : mTaskList) {
            task.run();
        }
    }


}
