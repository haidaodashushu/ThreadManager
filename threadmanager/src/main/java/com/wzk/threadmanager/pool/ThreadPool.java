package com.wzk.threadmanager.pool;

import java.util.List;

/**
 * Created by 政魁 on 2019/2/26 15:48
 * E-Mail Address：wangzhengkui@yingzi.com
 */
public interface ThreadPool {

    void submit(String poolName, Runnable runnable);

    void init(List<ThreadPoolInfo> threadPoolInfos);
    void destory();
}
