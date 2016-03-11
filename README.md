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

##Start开启服务的生命周期（重点）

   * 服务的特点：
 
		服务被创建时调用onCreate、onStartCommand；
	   	服务只能被创建一次，可以开启多次onStartCommand；
	   	服务只能被停止一次； 
		没有onPause、onStop、onResume、onRestart方法，因为service没有界面，长期运行在后台。

* 生命周期的方法：
 
	    onCreate:服务被创建的时候调用这个方法；
		onStartCommand ：开启服务
	    onDestroy：销毁服务

##Bind方式开启服务的生命周期（重点）

    bindService绑定服务、unBindService解除绑定的服务；
	服务是在被绑定的时候被创建，调用oncreate、onbind方法；
    服务只能被绑定一次；
	服务只能被解除一次，接触绑定的时候调用onUnbind、onDestrory方法，如果多次解除绑定会抛出异常；
    

    推荐的方式：

    startService：开启并创建一个服务，服务长期运行在后台；
    bindService:绑定服务，可以调用服务里面的方法；
    unBindService：解除服务，停止服务里面的方法；
    stopService：停止服务，销毁服务对象；
    
##通过Bind方式调用服务里面的方法
	重点：定义一个类继承Service,重写onBind方法，把return null；改为return 自定义对象。

```
	//bind方式开启服务
	Intent intent = new Intent(this, TestService.class);
        conn = new MyServiceConnection();
        //最后一个参数为自动绑定
        bindService(intent, conn,BIND_AUTO_CREATE);
       
       
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
```
##绑定服务抽取成接口（重点）

```
	接口（interface）： 对外开放暴露的功能，但是不会暴露功能实现的细节；
	让中间人实现服务接口的目的：只对外暴露接口里面业务逻辑方法，隐藏中间人里面的其他方法；
```

步骤：
	
1、创建一个服务的接口类，里面包含需要对外暴露的业务逻辑方法：
	
```
	public interface IService {
		public void callMethodInService();
	}
	
```
	
 2、让服务中的中间人实现了服务的接口类：

```
private class MyBinder extends Binder implements IService{
		//(实现服务接口中的方法)使用中间人调用服务里的方法
		public void callMethodInService(){
			methodInService();
		   }
 		}
```
3、在activity中声明接口的成员变量：
	private IService myBinder;

4、强制转换成服务的接口类型
		
```
	private class MyConn implements ServiceConnection {
		//服务连接成功时调用这个方法
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
		//强制转换成服务的接口类型
		myBinder = (IService) service;
		}
```

5、在activity中通过接口的成员变量调用服务的业务逻辑方法：

```
	public void call(View view){
		myBinder.callMethodInService();
	}
```

##利用服务注册广播接收者（动态注册广播）

注：操作频繁的广播事件，如果只是在清单配置文件配置，是不生效的。需要使用代码注册才能生效；
步骤：

```
	//动态注册广播,广播就不需要再清单文件中配置了
        screenBroadCast = new ScreenBroadCast();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        registerReceiver(screenBroadCast,intentFilter);
```
##进程间通信_远程服务aidl的写法（重点）
本地服务：写在自己的应用程序的工程里的服务 ，使用自己应用程序的进程运行这个服务；
远程服务：写在别的应用程序的工程里的服务，使用别的应用程序的进程运行这个服务（安装在同一个手机上的应用程序）；
	IPC： Inter Process Communication（进程间的通讯）；
	aidl: Android Interface definition language 安卓接口定义语言；
	aidl的接口类里面不需要public 、protected、private 等修饰符，默认是公开共享；
步骤：
	1、创建一个服务的接口类，里面包含需要对外暴露的业务逻辑方法：
	2、让服务中的中间人实现了服务的接口类：
	3、修改并拷贝接口文件：
	4、在本地服务的工程中的activity里，绑定服务：
	5、通过接口调用远程服务的方法：

代码如下：

* 服务端代码：
1、创建一个类继承Service，重写onBind方法，返回MyBind。

```
 @Override
    public IBinder onBind(Intent intent) {
        return new MyBind();
    }
```
2、新建一个包路径，并新建一个aidl文件，并添加你想要对我提供的方法。

```
package com.hsia;

interface IMyAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void callShow();
}
```

3、使用Android Studio重构功能，aidl文件会生成一个.java为后缀的文件。

4、让MyBind继承自这个aidl生成的.java类，并实现里面的方法

```
private class MyBind extends IMyAidlInterface.Stub {

        @Override
        public void callShow() throws RemoteException {
        //调用服务里面的方法
            show();
        }
    }
```

* 调用端代码：

1、调用远程服务的初始化
			
```
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
```

2、调用远程服务的点击事件

```
public void click(View view){
        try {
        //调用远程服务的方法。
            iMyAidlInterface.callShow();
            Log.d(TAG,"远程服务");
        } catch (RemoteException e) {
            Log.d(TAG,"远程服务没有找到");
            e.printStackTrace();
        }
    }
```






**关于作者**
	- Email：[xiaweifeng@live.cn](https://login.live.com)
	- 项目地址:[https://github.com/swordman20/Hsia03ServiceDemo](https://github.com/swordman20/Hsia03ServiceDemo.git)