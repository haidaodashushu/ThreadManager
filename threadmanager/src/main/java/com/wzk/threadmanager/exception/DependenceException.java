package com.wzk.threadmanager.exception;

import com.wzk.threadmanager.Task;

/**
 * Created by 政魁 on 2019/2/28 09:52
 * E-Mail Address：wangzhengkui@yingzi.com
 */
public class DependenceException extends ThreadManagerException {
    public DependenceException(Task task) {
        super(task);
    }

    @Override
    public String toString() {
        return String.format("the name %s of task has already a dependent task, please check!", mTask.getName());
    }
}
