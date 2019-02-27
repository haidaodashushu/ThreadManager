package com.wzk.threadmanager;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by 政魁 on 2019/2/27 18:09
 * E-Mail Address：wangzhengkui@yingzi.com
 */
public class QueueTaskUtil {

    public static Queue<Task> taskQueue(Task task) {
        if (task == null) {
            return null;
        }
        //先找到树的跟节点；
        Task root = task.mRoot;
        while (root != null && root.mRoot != null) {
            root = root.mRoot;
        }

        if (root == null) {
            root = task;
        }

        Queue<Task> taskQueue = new LinkedList<>();
        //对树做广度优先遍历
        Queue<Task> tempQueue = new LinkedList<>();
        tempQueue.offer(root);
        while (!tempQueue.isEmpty()) {
            Task temp = tempQueue.poll();
            LinkedList<Task> childList = temp.mChildList;
            if (childList != null && childList.size() > 0) {
                for (Task childTask : childList) {
                    tempQueue.offer(childTask);
                }
            }
            taskQueue.offer(temp);
        }
        return taskQueue;
    }
}
