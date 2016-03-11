package com.example.xwf.service04bindinterface;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private Iservice is;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //通过bindService方式开启服务
        ServiceConnection conn = new MyServiceConnection();
        bindService(new Intent(this,TestService.class),conn,BIND_AUTO_CREATE);
    }
    public void start(View view){
        //调用接口提供的方法
        is.showToast();
    }

    private class MyServiceConnection implements ServiceConnection {
        //当服务连接成功时调用次方法
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            is = (Iservice)service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }
}
