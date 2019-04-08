package com.wzk.threadmanager;

import com.wzk.threadmanager.exception.CyclicDependenceException;

import java.util.LinkedList;
import java.util.List;
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
        Task root = getRootTask(task);
        Queue<Task> taskQueue = new LinkedList<>();
        //对树做广度优先遍历
        Queue<Task> tempQueue = new LinkedList<>();
        tempQueue.offer(root);
        while (!tempQueue.isEmpty()) {
            Task temp = tempQueue.poll();
            List<Task> childList = temp.getChildList();
            if (childList != null && childList.size() > 0) {
                for (Task childTask : childList) {
                    tempQueue.offer(childTask);
                }
            }
            taskQueue.offer(temp);
        }
        return taskQueue;
    }

    public static Task getRootTask(Task task) {
        //先找到树的跟节点；
        Task root = task.mParent;
        while (root != null && root.mParent != null) {
            root = root.mParent;
            //检测循环依赖
            if (root == task) {
                throw new CyclicDependenceException(task);
            }
        }

        if (root == null) {
            root = task;
        }
        return root;
    }

    static void checkCyclicDepend(Task child, Task parent) {
        if (child.mChildList == null || child.mChildList.size() == 0) {
            return;
        }
        List<Task> childList = child.getChildList();
        for (Task task : childList) {
            if (task == parent) {
                throw new CyclicDependenceException(task);
            } else {
                checkCyclicDepend(task, parent);
            }
        }
    }
}
