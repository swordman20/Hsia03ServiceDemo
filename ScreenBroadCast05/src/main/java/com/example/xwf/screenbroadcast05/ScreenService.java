package com.example.xwf.screenbroadcast05;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created:         Hsia on 16/3/12.
 * Email:           xiaweifeng@live.cn
 * Description:     {TODO}(用一句话描述该文件做什么)
 */
public class ScreenService extends Service {

    private ScreenBroadCast screenBroadCast;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        //动态注册广播,广播就不需要再清单文件中配置了
        screenBroadCast = new ScreenBroadCast();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        registerReceiver(screenBroadCast,intentFilter);
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(screenBroadCast);
        super.onDestroy();
    }
}
