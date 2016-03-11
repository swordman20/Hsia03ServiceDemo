package com.example.xwf.service07aidl_client;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.hsia.IMyAidlInterface;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Hsia";
    private IMyAidlInterface iMyAidlInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent();
        //安卓5.0不允许通过隐式意图去开启服务，否则回报这个错
        //Service Intent must be explicit:
//        Android 5.0程序运行报Service Intent must be explicit错误，原因是5.0的service必须显式调用
//        intent.setAction("com.hsia");
        //第一个参数，包名，第二个参数包名+类名
        intent.setComponent(new ComponentName("com.example.xwf.service06aidl_server","com.example.xwf.service06aidl_server.ServerService"));
        ServiceConnection conn = new MyServiceConnection();
        bindService(intent,conn,BIND_AUTO_CREATE);
    }
    public void click(View view){
        try {
            iMyAidlInterface.callShow();
            Log.d(TAG,"远程服务");
        } catch (RemoteException e) {
            Log.d(TAG,"远程服务没有找到");
            e.printStackTrace();
        }
    }

    private class MyServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //这个写法有些特别和，bindservice不太一样
            iMyAidlInterface = IMyAidlInterface.Stub.asInterface(service);
            Log.d(TAG,"服务连接成功");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }
}
