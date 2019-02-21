package com.wzk.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.wzk.threadmanager.Task;
import com.wzk.threadmanager.TaskGroup;
import com.wzk.threadmanager.ThreadManager;
import com.wzk.threadmanager.ThreadRoad;

public class MainActivity extends AppCompatActivity {
    ThreadManager threadManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        threadManager = new ThreadManager();
        final MyTask myTask = new MyTask(0);
        MyTask myTask1 = new MyTask(1);
        MyTask myTask2 = new MyTask(2);
        MyTask myTask3 = new MyTask(3);
        MyTask myTask4 = new MyTask(4);
        final MyTask myTask5 = new MyTask(5);

        final TaskGroup taskGroup = new TaskGroup();

        myTask1.dependOn(myTask4).dependOn(taskGroup);
        taskGroup.addTask(myTask2);
        taskGroup.addTask(myTask3);
        myTask4.dependOn(myTask);
        new Thread() {
            @Override
            public void run() {
                super.run();
                threadManager.execute(ThreadRoad.BACKGROUND_THREAD, myTask5);
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                super.run();
                threadManager.execute(ThreadRoad.BACKGROUND_THREAD, taskGroup);
            }
        }.start();

    }


    class MyTask extends Task {
        int id;

        public MyTask(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            Log.i("MyTask", "run: id:" + id);
        }
    }
}

