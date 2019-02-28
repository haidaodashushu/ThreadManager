package com.wzk.threadmanager.exception;

import com.wzk.threadmanager.Task;

/**
 * Created by 政魁 on 2019/2/28 09:49
 * E-Mail Address：wangzhengkui@yingzi.com
 */
class ThreadManagerException extends RuntimeException {
    Task mTask;

    public ThreadManagerException() {
    }

    public ThreadManagerException(Task task) {
        this.mTask = task;
    }
}
