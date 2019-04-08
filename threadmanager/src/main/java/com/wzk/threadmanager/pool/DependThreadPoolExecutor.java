package com.wzk.threadmanager.pool;

import com.wzk.threadmanager.Task;
import com.wzk.threadmanager.TaskState;

import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by 政魁 on 2019/2/28 16:08
 * E-Mail Address：wangzhengkui@yingzi.com
 */
public class DependThreadPoolExecutor extends ThreadPoolExecutor {

    DependThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, BlockingQueue workQueue, String name) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue, new DefaultThreadFactory(name));
    }

    @Override
    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
        return new DependFutureTask<>(callable);
    }

    @Override
    protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value) {
        if (runnable instanceof Task) {
            return new DependFutureTask<>(((Task) runnable));
        }
        return new DependFutureTask<>(runnable, value);
    }

    @Override
    public void execute(Runnable command) {
        if (command instanceof Task) {
            DependFutureTask<Object> futureTask = new DependFutureTask<>(((Task) command));
            super.execute(futureTask);
        } else {
            super.execute(command);
        }
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        //执行自己之后，再去执行子任务的。
        Task task = null;
        if (r instanceof DependFutureTask) {
            DependFutureTask futureTask = (DependFutureTask) r;
            task = futureTask.task;
        } else if (r instanceof Task) {
            task = ((Task) r);
        } else {
//            throw new IllegalArgumentException("this ThreadPoolExecutor can only submit DependFutureTask or Task");
            return;
        }
        if (task == null || task.getChildList() == null) {
            return;
        }
        Iterator<Task> iterator = task.getChildList().iterator();
        while (iterator.hasNext()) {
            Task runTask = iterator.next();
            if (runTask != null && runTask != task && runTask.isIdle()) {
                runTask.setState(TaskState.STATE_SUBMIT);
                submit(runTask);
            }
        }
    }

}
