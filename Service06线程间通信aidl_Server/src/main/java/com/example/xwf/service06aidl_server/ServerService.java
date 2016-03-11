package com.example.xwf.service06aidl_server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.hsia.IMyAidlInterface;

/**
 * Created:         Hsia on 16/3/12.
 * Email:           xiaweifeng@live.cn
 * Description:     {TODO}(用一句话描述该文件做什么)
 */
public class ServerService extends Service {
    private static final String TAG = "Hsia" ;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //老是忘记了
        System.out.println("远程服务的onBind");
        return new MyBind();
    }

    private void show(){
        Toast.makeText(getApplicationContext(), "我是远程服务里面的方法", Toast.LENGTH_SHORT).show();
        Log.d(TAG,"我是远程服务里面的方法");
    }
    private class MyBind extends IMyAidlInterface.Stub {

        @Override
        public void callShow() throws RemoteException {
            show();
        }
    }
}
