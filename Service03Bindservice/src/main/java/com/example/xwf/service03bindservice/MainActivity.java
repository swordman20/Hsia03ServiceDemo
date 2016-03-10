package com.example.xwf.service03bindservice;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private MainActivity.myServiceConnection myServiceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myServiceConnection = new myServiceConnection();
    }
    public void start(View view){
        startService(new Intent(this,TestService.class));
    }

    public void bindStart(View view){
        bindService(new Intent(this,TestService.class),myServiceConnection,BIND_AUTO_CREATE);
    }
    public void bindStop(View view){
        unbindService(myServiceConnection);
    }
    class myServiceConnection implements ServiceConnection{

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG,"onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG,"onServiceDisconnected");
        }
    }
}
