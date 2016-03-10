package com.example.xwf.service02;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.IOException;

/**
 * Created:         Hsia on 16/3/10.
 * Email:           xiaweifeng@live.cn
 * Description:     {TODO}(用一句话描述该文件做什么)
 */
public class PhoneService extends Service{
    private static final String TAG = "Hsia";
    private TelephonyManager telephonyManager;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "电话监听服务已开启");
        super.onCreate();
        telephonyManager = ((TelephonyManager) getSystemService(TELEPHONY_SERVICE));
        //监听来电状态
        telephonyManager.listen(new myPhoneStat(),PhoneStateListener.LISTEN_CALL_STATE);

    }

    class myPhoneStat extends PhoneStateListener{
        MediaRecorder recorder;
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state){
                //来电状态
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.d(TAG, "有电话来了,我就准备一个录音机");
//                    初始化MediaRecorder对象
                    recorder = new MediaRecorder();
                    //设置音频的来源
                    recorder.setAudioSource(MediaRecorder.AudioSource.MIC);    //三星   zet中兴   华为
                    //设置饮音频的输出格式  3gp
                    recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    //设置音频的编码格式
                    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    //设置音频文件保存的路径
                    String dir = Environment.getExternalStorageDirectory() + "/download/ll.mp3";
                    recorder.setOutputFile(dir);
                    //准备录音
                    try {
                        recorder.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;
                //接通电话
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.d(TAG, "电话接通了，我就开始录音");
                    recorder.start();
                    break;
                //闲置状态
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.d(TAG,"录音结束，我就保存文件");
                    if (recorder != null) {
                    Log.d(TAG,"test");
                        recorder.stop();
                        recorder.reset();   // You can reuse the object by going back to setAudioSource() step
                        recorder.release();
                        recorder=null;
                    }
                    break;
                default:
                    break;
            }
        }
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
