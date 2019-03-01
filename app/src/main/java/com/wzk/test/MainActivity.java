package com.wzk.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.wzk.threadmanager.Task;
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
        ThreadManager threadManager = ThreadManager.getInstance();
        int i = 10;
        final MyTask myTask10 = new MyTask(i++);
        MyTask task11 = new MyTask(i++);
        MyTask task12 = new MyTask(i++);
        MyTask task13 = new MyTask(i++);
        MyTask task14 = new MyTask(i++);
        MyTask task15 = new MyTask(i++);
        MyTask task16 = new MyTask(i++);
        MyTask task17 = new MyTask(i++);
        MyTask task18 = new MyTask(i++);
        MyTask task19 = new MyTask(i++);
        task19.dependOn(task13);
        task13.dependOn(task11);
        task14.dependOn(task11);
        task15.dependOn(task11);

        task16.dependOn(task12);
        task17.dependOn(task12);
        task18.dependOn(task12);

        task11.dependOn(myTask10);
        task12.dependOn(myTask10);

        threadManager.execute(task16, new Runnable() {
            @Override
            public void run() {
                Log.i("MyTask", "run: done:" + myTask10.get());
            }
        }, Constants.ThreadPoolName.BACK_GROUND, true);
    }


    class MyTask extends Task {
        int id;
        @Override
        public Object runTask() {
            if (id == 6) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Log.i("MyTask", "run: id:" + id +"，thread:"+ Thread.currentThread().getName());
            return id;
        }

        public MyTask(int id) {
            super(String.valueOf(id));
            this.id = id;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ThreadManager.getInstance().destroy();
    }
}

