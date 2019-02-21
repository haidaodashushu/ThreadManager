package com.wzk.threadmanager;

import android.support.annotation.IntDef;

/**
 * Created by 政魁 on 2019/2/19 15:44
 * E-Mail Address：wangzhengkui@yingzi.com
 */

@IntDef({ThreadRoad.MAIN_THREAD,
        ThreadRoad.BACKGROUND_THREAD,
        ThreadRoad.WORK_THREAD})
public @interface ThreadRoad {
    public static final int MAIN_THREAD = 1;
    public static final int BACKGROUND_THREAD = 2;
    public static final int WORK_THREAD = 3;
}
