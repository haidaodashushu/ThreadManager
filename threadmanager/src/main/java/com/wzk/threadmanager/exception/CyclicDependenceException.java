package com.wzk.threadmanager.exception;

import com.wzk.threadmanager.Task;

/**
 * Created by 政魁 on 2019/2/28 09:43
 * E-Mail Address：wangzhengkui@yingzi.com
 */
public class CyclicDependenceException extends ThreadManagerException {

    public CyclicDependenceException(Task task) {
        super(task);
    }

    @Override
    public String toString() {
        return String.format("the name %s of task have a cyclic dependence, please check!", mTask.getName());
    }
}
