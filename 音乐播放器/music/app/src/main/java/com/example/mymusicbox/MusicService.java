package com.example.mymusicbox;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.IBinder;

import java.io.IOException;

public class MusicService extends Service
{
    MyReceiver serviceReceiver;
    AssetManager am;
    String[] musics = new String[] { "unrestrained.mp3" +
            "", "lotus.mp3",
            "barrencity.mp3"};
    MediaPlayer mPlayer;
    // 当前的状态，1代表没有播放；2代表正在播放；3代表暂停
    int status = 1;
    // 记录当前正在播放的音乐
    int current = 0;
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
    @Override
    public void onCreate()
    {
        super.onCreate();
        am = getAssets();
        // 创建BroadcastReceiver
        serviceReceiver = new MyReceiver();
        // 创建IntentFilter
        IntentFilter filter = new IntentFilter();
        filter.addAction(MainActivity.CTL_ACTION);
        registerReceiver(serviceReceiver, filter);
        // 创建MediaPlayer
        mPlayer = new MediaPlayer();
        // 为MediaPlayer播放完成事件绑定监听器
        mPlayer.setOnCompletionListener(new OnCompletionListener() // ①
        {
            @Override
            public void onCompletion(MediaPlayer mp)
            {
                current++;
                if (current >= 3)
                {
                    current = 0;
                }
                //发送广播通知Activity更改文本框
                Intent sendIntent = new Intent(MainActivity.UPDATE_ACTION);
                sendIntent.putExtra("current", current);
                // 发送广播，将被Activity组件中的BroadcastReceiver接收到
                sendBroadcast(sendIntent);
                // 准备并播放音乐
                prepareAndPlay(musics[current]);
            }
        });
    }
    public class MyReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(final Context context, Intent intent)
        {
            int control = intent.getIntExtra("control", -1);
            switch (control)
            {
                // 播放或暂停
                case 1:
                    // 原来处于没有播放状态
                    if (status == 1)
                    {
                        // 准备并播放音乐
                        prepareAndPlay(musics[current]);
                        status = 2;
                    }
                    // 原来处于播放状态
                    else if (status == 2)
                    {
                        // 暂停
                        mPlayer.pause();
                        // 改变为暂停状态
                        status = 3;
                    }
                    // 原来处于暂停状态
                    else if (status == 3)
                    {
                        // 播放
                        mPlayer.start();
                        // 改变状态
                        status = 2;
                    }
                    break;
                // 停止声音
                case 2:
                    // 如果原来正在播放或暂停
                    if (status == 2 || status == 3)
                    {
                        // 停止播放
                        mPlayer.stop();
                        status = 1;
                    }
                case 3:
                    current--;
                    if(current < 0)
                        current = 0;
                    prepareAndPlay(musics[current]);
                case 4:
                    current++;
                    if(current > musics.length-1)
                        current = 0;
                    prepareAndPlay(musics[current]);
            }
            // 广播通知Activity更改图标、文本框
            Intent sendIntent = new Intent(MainActivity.UPDATE_ACTION);
            sendIntent.putExtra("update", status);
            sendIntent.putExtra("current", current);
            // 发送广播，将被Activity组件中的BroadcastReceiver接收到
            sendBroadcast(sendIntent);
        }
    }
    private void prepareAndPlay(String music)
    {
        try
        {
            // 打开指定音乐文件
            AssetFileDescriptor afd = am.openFd(music);
            mPlayer.reset();
            // 使用MediaPlayer加载指定的声音文件。
            mPlayer.setDataSource(afd.getFileDescriptor(),
                    afd.getStartOffset(), afd.getLength());
            // 准备声音
            mPlayer.prepare();
            // 播放
            mPlayer.start();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
