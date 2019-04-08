package com.wzk.test.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;

import com.wzk.test.Node;
import com.wzk.threadmanager.Task;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Created by 政魁 on 2019/4/2 10:10
 * E-Mail Address：wangzhengkui@yingzi.com
 */
public class NodeTreeView extends View {
    public static final int NODE_RADIUS = 30;
    public static final int HORZIONTIAL_DIS = 30;
    public static final int VERTICAL_DIS = 100;
    private Paint mNodePaint;
    private int mHorzontialDis = HORZIONTIAL_DIS;
    private int mVerticalDis = VERTICAL_DIS;

    //    SparseArray<Node> mLevelNodes;
    SparseArray<LinkedList<Node>> mLevelNodes;

    private int mNodeRadius;
    private int mNodeTextSize = 13;
    private int mNodeStrokeWidth;
    private int mNodeLength;
    private Node mRootNode;

    private Rect mRect = new Rect();

    public NodeTreeView(Context context) {
        this(context, null);
    }

    public NodeTreeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NodeTreeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mLevelNodes = new SparseArray<>();

        mNodePaint = new Paint();
        mNodePaint.setColor(Color.BLACK);
        mNodePaint.setStyle(Paint.Style.STROKE);

        setNodeRadius(NODE_RADIUS);
        setNodeStrokeWidth(3);
        mNodePaint.setAntiAlias(true);
        mNodePaint.setTextSize(dip2px(mNodeTextSize));
    }

    public void setNodeRadius(int radius) {
        this.mNodeRadius = radius;
        mNodeLength = (mNodeRadius + mNodeStrokeWidth) * 2;
    }

    public void setNodeStrokeWidth(int strokeWidth) {
        this.mNodeStrokeWidth = strokeWidth;
        mNodeLength = (mNodeRadius + mNodeStrokeWidth) * 2;
        mNodePaint.setStrokeWidth(strokeWidth);
    }

    public void setRootTask(Task task) {
        mRootNode = Node.parseToDrawNode(task);
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mRect.set(left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mRootNode == null) {
            return;
        }
        calculateNodePosition();
        drawNodeTree(canvas);
    }

    private void calculateNodePosition() {

        int x = getWidth() / 2;
        int y = (int) (mNodeRadius + mNodePaint.getStrokeWidth());
        setNodePosition(mRootNode, x, y);
        updateChildNode(mRootNode);
    }

    private void updateChildNode(Node node) {
        //广度遍历
        Queue<Node> tempQueue = new LinkedList<>();

        tempQueue.offer(node);
        while (!tempQueue.isEmpty()) {
            Node temp = tempQueue.poll();
            int x = temp.getX();
            int y = temp.getY();
            saveNodeLevel(temp);
            List<Node> childList = temp.getChildList();
            if (childList == null || childList.size() == 0) {
                continue;
            }
            int childWidth = (mNodeLength + mHorzontialDis) * (childList.size() - 1);

            for (int i = 0; i < childList.size(); i++) {
                Node childNode = childList.get(i);
                tempQueue.offer(childNode);
                int childX = (x - childWidth / 2) + (mNodeLength + mHorzontialDis) * i;
                int childY = y + mVerticalDis;
                setNodePosition(childNode, childX, childY);
                int delta = checkNodePositionIn(childNode);
                if (delta > 0) {
                    calculateNodePosition();
                    updateNodePosition(temp, delta);
                    break;
                }

            }


        }

    }

    private int checkNodePositionIn(Node node) {
        LinkedList<Node> childNodeList = mLevelNodes.get(node.getLevel());
        if (childNodeList == null || childNodeList.size() == 0) {
            return -1;
        }

        return childNodeList.getLast().getX() - node.getX();
    }

    private void updateNodePosition(Node node, int distance) {
        Node parentNode = node.getParentNode();
        int delta = checkNodePositionIn(node);

        while (true) {
            if (delta > 0) {
                distance += delta;
                parentNode = parentNode.getParentNode();
            } else {
                break;
            }
        }
        if (parentNode != null) {
            parentNode.setX(parentNode.getX() + distance);
            updateChildNode(parentNode);
        }

    }

    private void saveNodeLevel(Node node) {
        LinkedList<Node> nodeList = mLevelNodes.get(node.getLevel());
        if (nodeList == null) {
            nodeList = new LinkedList<>();
            mLevelNodes.put(node.getLevel(), nodeList);
        }

        nodeList.add(node);
    }

    private void setNodePosition(Node node, int x, int y) {
        node.setX(x).setY(y);
    }

    private void drawNodeTree(Canvas canvas) {
        drawNodeItSelf(canvas, mRootNode);
        drawChild(canvas, mRootNode);
    }

    private void drawNodeItSelf(Canvas canvas, Node node) {
        mNodePaint.setStyle(Paint.Style.STROKE);
        float x = node.getX();
        float y = node.getY();
        canvas.drawCircle(x, y, mNodeRadius, mNodePaint);
        mNodePaint.setStyle(Paint.Style.FILL);

        int textWidth = (int) mNodePaint.measureText(node.getName());
        float textYDelta = (mNodePaint.getFontMetrics().ascent / 2 + mNodePaint.getFontMetrics().descent / 2);
        canvas.drawText(node.getName(), x - textWidth / 2, y - textYDelta, mNodePaint);
    }

    private void drawChild(Canvas canvas, Node node) {
        List<Node> childList = node.getChildList();
        if (childList == null) {
            return;
        }

        float x = node.getX();
        float y = node.getY();
        for (int i = 0; i < childList.size(); i++) {
            Node childNode = childList.get(i);
            int childX = childNode.getX();
            int childY = childNode.getY();
            drawNodeItSelf(canvas, childNode);
            canvas.drawLine(x, y, childX, childY, mNodePaint);
            drawChild(canvas, childNode);
        }
    }


    private int dip2px(int dis) {
        return (int) (dis * getResources().getDisplayMetrics().density + 0.5f);
    }

}

