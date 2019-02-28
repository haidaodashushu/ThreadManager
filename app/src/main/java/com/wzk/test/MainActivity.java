package com.wzk.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.wzk.threadmanager.Task;
import com.wzk.threadmanager.TaskGroup;
import com.wzk.threadmanager.ThreadManager;
import com.wzk.threadmanager.ThreadPoolBuilder;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.textId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test();
            }
        });

        ThreadPoolBuilder builder = new ThreadPoolBuilder().createThreadPool().setName(Constants.ThreadPoolName.BACK_GROUND)
                .createThreadPool().setName(Constants.ThreadPoolName.DEFAULT);
        builder.build();
    }

    private void test() {
        final MyTask myTask = new MyTask(0);
        MyTask myTask1 = new MyTask(1);
        MyTask myTask2 = new MyTask(2);
        MyTask myTask3 = new MyTask(3);
        MyTask myTask4 = new MyTask(4);
        final MyTask myTask5 = new MyTask(5);
        final MyTask myTask6 = new MyTask(6);

        final TaskGroup taskGroup = new TaskGroup("7");

        myTask1.dependOn(myTask4).dependOn(taskGroup);
        taskGroup.dependOn(myTask1);
        taskGroup.addTask(myTask2);
        taskGroup.addTask(myTask3);

        ThreadManager threadManager = ThreadManager.getInstance();
        threadManager.execute(taskGroup, new Runnable() {
            @Override
            public void run() {
                Log.i("MyTask", "run: done taskGroup");
            }
        }, Constants.ThreadPoolName.DEFAULT);

        threadManager.execute(myTask5, new Runnable() {
            @Override
            public void run() {
                Log.i("MyTask", "run: done " + myTask5.id);
            }
        }, Constants.ThreadPoolName.BACK_GROUND);

        threadManager.execute(myTask6);
    }


    class MyTask extends Task {
        int id;

        public MyTask(String name) {
            super(name);
        }

        public MyTask(int id) {
            super(String.valueOf(id));
            this.id = id;
        }

        @Override
        public void run() {
//            if (id == 3) {
//                try {
//                    Thread.sleep(10);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
            Log.i("MyTask", "run: id:" + id);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ThreadManager.getInstance().destroy();
    }
}

