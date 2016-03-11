package com.example.xwf.screenbroadcast05;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created:         Hsia on 16/3/11.
 * Email:           xiaweifeng@live.cn
 * Description:     {TODO}(用一句话描述该文件做什么)
 */
public class ScreenBroadCast extends BroadcastReceiver {
    private static final String TAG = "Hsia";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"onReceive");
        //获取广播的事件类型
        String action = intent.getAction();
        if ("android.intent.action.SCREEN_OFF".equals(action)) {
            Log.d(TAG,"屏幕关闭了");

        }else if("android.intent.action.SCREEN_ON".equals(action)){
            Log.d(TAG,"屏幕亮了");
        }
    }
}
