package com.example.mymusicbox;
import android.os.Bundle;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener
{
    // 获取界面中显示歌曲标题、作者文本框
    TextView title, author;
    // 播放/暂停、停止按钮
    ImageButton play, stop;
    //上一首，下一首
    ImageButton pre, next;
    ImageView cover;

    ActivityReceiver activityReceiver;

    public static final String CTL_ACTION =
            "org.crazyit.action.CTL_ACTION";
    public static final String UPDATE_ACTION =
            "org.crazyit.action.UPDATE_ACTION";
    // 定义音乐的播放状态，1代表没有播放；2代表正在播放；3代表暂停
    int status = 1;
    String[] titleStrs = new String[] { "无羁", "莲花坞", "荒城渡" };
    String[] authorStrs = new String[] { "王一博", "林海", "周深" };
    Integer[] covers = new Integer[] { R.drawable.unrestrained, R.drawable.lotus,
            R.drawable.barrencity};

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 获取程序界面界面中的两个按钮
        play = (ImageButton) this.findViewById(R.id.play);
        stop = (ImageButton) this.findViewById(R.id.stop);
        title = (TextView) findViewById(R.id.title);
        author = (TextView) findViewById(R.id.author);
        cover = findViewById(R.id.cover);
        pre = this.findViewById(R.id.pre);
        next = this.findViewById(R.id.next);

        // 为两个按钮的单击事件添加监听器
        play.setOnClickListener(this);
        stop.setOnClickListener(this);
        // 为上一首、下一首的单击事件添加监听器
        pre.setOnClickListener(this);
        next.setOnClickListener(this);

        activityReceiver = new ActivityReceiver();
        // 创建IntentFilter
        IntentFilter filter = new IntentFilter();
        // 指定BroadcastReceiver监听的Action
        filter.addAction(UPDATE_ACTION);
        // 注册BroadcastReceiver
        registerReceiver(activityReceiver, filter);

        Intent intent = new Intent(this, MusicService.class);
        // 启动后台Service
        startService(intent);
    }
    // 自定义的BroadcastReceiver，负责监听从Service传回来的广播
    public class ActivityReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            // 获取Intent中的update消息，update代表播放状态
            int update = intent.getIntExtra("update", -1);
            // 获取Intent中的current消息，current代表当前正在播放的歌曲
            int current = intent.getIntExtra("current", -1);
            if (current >= 0)
            {
                title.setText(titleStrs[current]);
                author.setText(authorStrs[current]);
                cover.setImageResource(covers[current]);
            }
            switch (update)
            {
                case 1:
                    play.setImageResource(R.drawable.play);
                    status = 1;
                    break;
                // 控制系统进入播放状态
                case 2:
                    // 播放状态下设置使用暂停图标
                    play.setImageResource(R.drawable.pause);
                    // 设置当前状态
                    status = 2;
                    break;
                // 控制系统进入暂停状态
                case 3:
                    // 暂停状态下设置使用播放图标
                    play.setImageResource(R.drawable.play);
                    // 设置当前状态
                    status = 3;
                    break;
            }
        }
    }
    @Override
    public void onClick(View source)
    {
        // 创建Intent
        Intent intent = new Intent("org.crazyit.action.CTL_ACTION");
        switch (source.getId())
        {
            // 按下播放/暂停按钮
            case R.id.play:
                intent.putExtra("control", 1);
                break;
            // 按下停止按钮
            case R.id.stop:
                intent.putExtra("control", 2);
                break;
            case R.id.pre:
                intent.putExtra("control",3);
            case R.id.next:
                intent.putExtra("control",4);
        }
        // 发送广播，将被Service组件中的BroadcastReceiver接收到
        sendBroadcast(intent);
    }
}

