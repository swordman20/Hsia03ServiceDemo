package com.example.xwf.service03bindservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created:         Hsia on 16/3/11.
 * Email:           xiaweifeng@live.cn
 * Description:     {TODO}(用一句话描述该文件做什么)
 */
public class TestService extends Service {
    public static final String TAG = "Hsia";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "服务里面的onBind");
        return new MyIBinder();
    }
    public class MyIBinder extends Binder{
        public void showToast(){
            Log.d(TAG,"服务里面的匿名类MyBind的showToaSt方法");
            show();
        }
    }
    public void show(){
        Toast.makeText(getApplicationContext(),"我是服务里面的方法",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "服务里面的onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "服务里面的onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"服务里面的onDestroy");
        super.onDestroy();
    }

}
