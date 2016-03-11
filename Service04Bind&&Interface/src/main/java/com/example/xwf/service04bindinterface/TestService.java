package com.example.xwf.service04bindinterface;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created:         Hsia on 16/3/11.
 * Email:           xiaweifeng@live.cn
 * Description:     {TODO}(这是一个服务的测试类)
 */
public class TestService extends Service{
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBind();
    }
    //定义一个一个类继承Binder
    private class MyBind extends Binder implements Iservice{
        @Override
        public void showToast() {
          show();
        }
    }

    public void show(){
        Toast.makeText(getApplicationContext(),"我是接口里面的方法",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
