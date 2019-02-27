package com.wzk.threadmanager.pool;

/**
 * Created by 政魁 on 2019/2/26 16:15
 * E-Mail Address：wangzhengkui@yingzi.com
 */
public class ThreadPoolInfo {
    /**
     * 线程名
     */
    String name;
    /**
     * 线程池所允许的最大线程数量
     */
    int maxPoolSize;
    /**
     * 线程池中生存的线程数量
     */
    int corePoolSize;
    /**
     * 线程生存时间
     */
    int keepAliveTime;

    int workQueueSize;

    public ThreadPoolInfo setName(String name) {
        this.name = name;
        return this;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public void setKeepAliveTime(int keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    public void setWorkQueueSize(int workQueueSize) {
        this.workQueueSize = workQueueSize;
    }

    @Override
    public String toString() {
        return "ThreadPoolInfo{" +
                "name='" + name + '\'' +
                ", maxPoolSize=" + maxPoolSize +
                ", corePoolSize=" + corePoolSize +
                ", keepAliveTime=" + keepAliveTime +
                ", workQueueSize=" + workQueueSize +
                '}';
    }
}
