package com.example.xwf.service03bindservice;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ServiceConnection conn;
    private TestService.MyIBinder myIBinder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void startSer(View view){
    startService(new Intent(this, TestService.class));
    }
    public void stopSer(View view){
    stopService(new Intent(this, TestService.class));
    }

    public void bindStart(View view){
        Intent intent = new Intent(this, TestService.class);
        conn = new MyServiceConnection();
        //最后一个参数为自动绑定
        bindService(intent, conn,BIND_AUTO_CREATE);
    }



    public void bindStop(View view){
        unbindService(conn);
    }
    public void bindMe(View view){
        myIBinder.showToast();
    }

    //定义一个类实现ServiceConnection，当服务连接成功是调用（前提是onBind返回不是null）
    private class MyServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TestService.TAG,"bind服务onServiceConnected");
            myIBinder = (TestService.MyIBinder)service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TestService.TAG,"bind服务onServiceDisconnected");
        }
    }
}
