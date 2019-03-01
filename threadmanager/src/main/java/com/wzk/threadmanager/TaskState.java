package com.wzk.threadmanager;

/**
 * Created by 政魁 on 2019/2/28 19:09
 * E-Mail Address：wangzhengkui@yingzi.com
 */
public @interface TaskState {
    int STATE_IDLE = 0;
    int STATE_SUBMIT = 1;
    int STATE_RUNING = 2;
    int STATE_DONE = 3;
}
