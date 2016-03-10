#Android学习从零单排之Service
**读了那么多年的书让我明白一个道理。人要稳重，不要想到啥就做啥。做一行越久即使你不会，几年之后慢慢的你也会了，加上一点努力你或许你能成为别人眼中的专家。**
##Service的简介
	服务：运行在后台，就是没有界面的Activity，因为Service和Activity最终都继承自ContextWrapper。
	
##进程的优先级
```
 进程的优先级 分5种  第一种最重要最后被杀死
 	(1) Foreground process 前台进程
    	当Activity执行了onResume方法  用户正在交互
    (2) Visible process 可视进程 Activity一直可以看的见 Activity执行了onPause方法
   	(3) Service process 服务进程 有一个服务被开启了
 	(4) Background process 后台进程 相当于Activity执行了OnStop方法 不可见	(5) Empty process 空进程
    采用startService开启服务 服务只会被开启一次  多次调用startService方法只会走onStartCommand方法 服务会在后台一直运行 直到用户手工停止 服务才会停止 
```
##电话窃听器
步骤：
	1、创建一个类继承Service，并重新onCreate。

```
@Override
    public void onCreate() {
        Log.d(TAG, "电话监听服务已开启");
        super.onCreate();
        telephonyManager = ((TelephonyManager) getSystemService(TELEPHONY_SERVICE));
        //监听来电状态
        telephonyManager.listen(new myPhoneStat(),PhoneStateListener.LISTEN_CALL_STATE);

    }
```

	2、在onCreate方法中通过Context获取系统服务拿到TelephonyManager对象，并监听该对象。
```
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
```
	3、开启服务，并在清单文件配需相应的权限
```
	startService(new Intent(this,PhoneService.class));
	
	<uses-permission android:name="android.permission.READ_PHONE_STATE"/>
    <uses-permission android:name="android.permission.RECORD_AUDIO"/>
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
```









**关于作者**
	- Email：[xiaweifeng@live.cn](https://login.live.com)
	- 项目地址:[https://github.com/swordman20/Hsia03ServiceDemo](https://github.com/swordman20/Hsia03ServiceDemo.git)