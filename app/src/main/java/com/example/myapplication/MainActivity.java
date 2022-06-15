package com.example.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener {
    private final ImageView[] images = new ImageView[21];
    private final int[] ids = {R.id.img12, R.id.img13, R.id.img14,
            R.id.img21, R.id.img22, R.id.img23, R.id.img24, R.id.img25,
            R.id.img31, R.id.img32, R.id.img33, R.id.img34, R.id.img35,
            R.id.img41, R.id.img42, R.id.img43, R.id.img44, R.id.img45,
            R.id.img52, R.id.img53, R.id.img54};
    boolean flag = false;
    int count = 0;
    int oldID = 0, newID = 0;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            images[oldID].setBackgroundResource(R.mipmap.crystalno1);
            newID = (int) (Math.random() * 21);
            images[newID].setBackgroundResource(R.mipmap.crystal1);
            oldID = newID;
        }
    };
    Runnable runnable = () -> {
        while (flag) {
            try {
                Thread.sleep(1000);
                Message message = Message.obtain();
                handler.sendMessage(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };
    private TextView tvCount, tvRemainTime;
    private Button btnReplay, btnPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);
        initView();
        btnPlay.setOnClickListener(this);
        btnReplay.setOnClickListener(this);
        for (ImageView image : images) {
            image.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnPlay:
                new MyCount(60 * 1000, 1000).start();
                Thread thread = new Thread(runnable);
                thread.start();
                flag = true;
                break;
            case R.id.btnReplay:
                flag = true;
                new MyCount(60 * 1000, 1000).start();
                thread = new Thread(runnable);
                thread.start();
                break;
            default:
                if (images[oldID].getId() == view.getId() && flag) {
                    count++;
                    tvCount.setText("击中数：" + count);
                }
        }
    }

    void initView() {
        tvCount = this.findViewById(R.id.tvCount);
        tvRemainTime = this.findViewById(R.id.tvRemainTime);
        btnPlay = this.findViewById(R.id.btnPlay);
        btnReplay = this.findViewById(R.id.btnReplay);
        for (int i = 0; i < images.length; i++) {
            images[i] = this.findViewById(ids[i]);
        }
    }

    private class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            long hour = l / 1000 / 3600;
            long minute = l / 1000 % 3600 / 60;
            long second = l / 1000 % 3600 % 60;
            tvRemainTime.setText("倒计时：" + hour + ":" + minute + ":" + second);
        }

        @Override
        public void onFinish() {
            tvRemainTime.setText("游戏时间到！");
            flag = false;
            images[oldID].setBackgroundResource(R.mipmap.crystalno1);
        }
    }
}

