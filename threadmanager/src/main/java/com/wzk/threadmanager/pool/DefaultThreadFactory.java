package com.wzk.threadmanager.pool;

import android.support.annotation.NonNull;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by 政魁 on 2019/2/26 17:16
 * E-Mail Address：wangzhengkui@yingzi.com
 */
public class DefaultThreadFactory implements ThreadFactory {
    private String mThreadNamePrefix;
    private AtomicInteger mCount = new AtomicInteger(1);
    public DefaultThreadFactory(String threadNamePrefix) {
        this.mThreadNamePrefix = threadNamePrefix;
    }

    @Override
    public Thread newThread(@NonNull Runnable r) {
        Thread thread = new Thread(r);
        thread.setName(mThreadNamePrefix + "-" + mCount.getAndIncrement());
        thread.setPriority(Thread.NORM_PRIORITY);
        return thread;
    }
}
