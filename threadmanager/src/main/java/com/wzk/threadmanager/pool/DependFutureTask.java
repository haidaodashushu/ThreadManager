package com.wzk.threadmanager.pool;

import android.support.annotation.NonNull;

import com.wzk.threadmanager.Task;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * Created by 政魁 on 2019/2/28 16:42
 * E-Mail Address：wangzhengkui@yingzi.com
 */
class DependFutureTask<V> extends FutureTask<V> {
    Task task;

    DependFutureTask(@NonNull Callable<V> callable) {
        super(callable);
    }

    DependFutureTask(@NonNull Runnable runnable, V result) {
        super(runnable, result);
    }

    DependFutureTask(Task task) {
        super((Callable<V>) Executors.callable(task));
        this.task = task;
    }

    @Override
    public void run() {
        super.run();
    }
}
