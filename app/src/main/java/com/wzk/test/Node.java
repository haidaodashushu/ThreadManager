package com.wzk.test;

import com.wzk.threadmanager.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 政魁 on 2019/4/2 16:46
 * E-Mail Address：wangzhengkui@yingzi.com
 */
public class Node {
    private Node mParentNode;
    private List<Node> mChildList;
    private int x, y, level;
    String text;
    private Task task;

    private Node(Task task) {
        this.task = task;
    }

    private void addChild(Node node) {
        if (mChildList == null) {
            mChildList = new ArrayList<>();
        }
        if (node == null) {
            return;
        }
        node.setLevel(getLevel() + 1);
        mChildList.add(node);
    }

    public static Node parseToDrawNode(Task rootTask) {
        if (rootTask == null) {
            return null;
        }
        Node node = new Node(rootTask);
        node.setLevel(0);
        parseChildNode(node, rootTask);
        //因为taskQueue里保证的是根节点在第一个，所以对于nodeQueue来说，第一个也是根节点。
        return node;
    }

    private static void parseChildNode(Node parentNode, Task task) {
        List<Task> childList = task.getChildList();
        if (childList == null) {
            return;
        }
        for (Task childTask : childList) {
            Node childNode = new Node(childTask);
            childNode.mParentNode = parentNode;
            parentNode.addChild(childNode);
            parseChildNode(childNode, childTask);
        }
    }

    public String getName() {
        return task.getName();
    }

    public int getX() {
        return x;
    }

    public Node setX(int x) {
        this.x = x;
        return this;
    }

    public int getY() {
        return y;
    }

    public Node setY(int y) {
        this.y = y;
        return this;
    }

    public Node getParentNode() {
        return mParentNode;
    }

    public List<Node> getChildList() {
        return mChildList;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
