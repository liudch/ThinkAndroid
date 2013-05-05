#ThinkAndroid 交流平台
* QQ群：169415162
* 网址：[http://www.thinkandroid.cn](http://www.thinkandroid.cn)
----
#  ThinkAndroid简介 
* ThinkAndroid是包含Android mvc和简易sqlite orm以及ioc模块，它封装了Android httpclitent中的http模块,
* 具有快速构建文件缓存功能，无需考虑什么格式的文件，都可以非常轻松的实现缓存，它实现了图片缓存，在android中
* 加载的图片的时候oom的问题和快速滑动的时候图片加载位置错位等问题都可以轻易的解决掉。他还包括了一个手机开发中
* 经常应用的实用工具类，如日志管理，配置文件管理，android下载器模块，网络切换检测等等工具。
* ThinkAndroid的开发宗旨是简洁，快速


##目前ThinkAndroid主要有以下模块：

* MVC模块：实现视图与模型的分离。

* ioc模块：android中的ioc模块，完全注解方式就可以进行UI绑定、res中的资源的读取、以及对象的初始化。

* 数据库模块：android中的orm框架，使用了线程池对sqlite进行操作。
* 
* http模块：通过httpclient进行封装http数据请求，支持异步及同步方式加载。

* 缓存模块：通过简单的配置及设计可以很好的实现缓存，对缓存可以随意的配置

* 图片缓存模块：imageview加载图片的时候无需考虑图片加载过程中出现的oom和android容器快速滑动时候出现的图片错位等现象。

* 配置器模块：可以对简易的实现配对配置的操作，目前配置文件可以支持Preference、Properties对配置进行存取。

* 日志打印模块：可以较快的轻易的是实现日志打印，支持日志打印的扩展，目前支持对sdcard写入本地打印、以及控制台打印

* 下载器模块:可以简单的实现多线程下载、后台下载、断点续传、对下载进行控制、如开始、暂停、删除等等。

* 网络状态检测模块：当网络状态改变时，对其进行检测。


---
## 使用ThinkAndroid快速开发框架需要有以下权限：

```xml
<!-- 访问互联网权限 根据需要添加-->
<uses-permission android:name="android.permission.INTERNET" />
<!-- SDCARD读写权限 根据需要添加 -->
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<!-- 网络状态检测权限  根据需要添加-->
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

 <application
        android:name="com.ta.TAApplication" >
      application必须配置以上格式
```
Activity必须继承TAActivity
----
##ThinkAndroid使用方法：
关于ThinkAndroid的更多介绍，请点击[这里](http://www.thinkandroid.cn/forum.php?mod=forumdisplay&fid=36)
##MVC模块
* MVC模块：实现视图与模型的分离。
```java
  getTAApplication().registerCommand(R.string.comand,
				TAIdentityCommand.class);
		getTAApplication().registerCommand(R.string.comand,
				TAIdentityCommand.class);
		TALogger.addLogger(new TAPrintToFileLogger());
		TARequest request = new TARequest();
		doCommand(R.string.comand, request, new TAIResponseListener()
		{

			@Override
			public void onStart(TAResponse response)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(TAResponse response)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void onRuning(TAResponse response)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void onFailure(TAResponse response)
			{
				// TODO Auto-generated method stub

			}

		}, false, true, true);
    
    
    
  
package com.ta.mvc.command;

import com.ta.mvc.common.TAIResponseListener;
import com.ta.mvc.common.TARequest;
import com.ta.mvc.common.TAResponse;

public class TAIdentityCommand extends TACommand
{
  @Override
	protected void executeCommand()
	{
		// TODO Auto-generated method stub
		TARequest request = getRequest();
		TAResponse response = new TAResponse();
		response.setTag(request.getTag());
		response.setData(request.getData());
		response.setActivityKey((String) request.getActivityKey());
		response.setActivityKeyResID(request.getActivityKeyResID());
		setResponse(response);
		notifyListener(true);
	}

	protected void notifyListener(boolean success)
	{
		TAIResponseListener responseListener = getResponseListener();
		if (responseListener != null)
		{
			onComandUpdate(command_success);
		}
	}
}


----

##ioc模块使用方法：
* 完全注解方式就可以进行UI绑定、res中的资源的读取、以及对象的初始化。

```java
public class ThinkAndroidDemoActivity extends TAActivity {

     
	@TAInject 
	Entity entity; //目前只能对无参构造函数进行初始化
	@TAInjectResource(R.string.app_name)
	String appNameString;
	@TAInjectResource(R.attr.test)
	int[] test; 
	@TAInjectView(R.id.add);
	Button addButton;
}
```

##数据库模块
* android中的orm框架，使用了线程池对sqlite进行操作。

```java
public class ThinkAndroidDemoActivity extends TAActivity {

   TASQLiteDatabasePool sqlitePool = getTAApplication()
				.getSQLiteDatabasePool();
		TASQLiteDatabase sqliteDatabase=sqlitePool.getSQLiteDatabase();
		//使用中
		sqliteDatabase.insert(entity);
		sqlitePool.returnSQLiteDatabase(sqliteDatabase); 
 
}
```

##Http模块使用方法：
###异步get方法
```java
  AsyncHttpClient client = new AsyncHttpClient();
		client.get("http://www.thinkandroid.cn/", new AsyncHttpResponseHandler()
		{
			@Override
			public void onSuccess(String content)
			{
				// TODO Auto-generated method stub
				super.onSuccess(content);
				TALogger.d(LoginActivity.this, content);
			}
			
			@Override
			public void onStart()
			{
				// TODO Auto-generated method stub
				super.onStart();
			}
			
			@Override
			public void onFailure(Throwable error)
			{
				// TODO Auto-generated method stub
				super.onFailure(error);
			}
			
			@Override
			public void onFinish()
			{
				// TODO Auto-generated method stub
				super.onFinish();
			}

		});
```
##Http模块使用方法：
###同步get方法

```java
  TASyncHttpClient client = new TASyncHttpClient();
  	client.get("http://www.thinkandroid.cn/", new AsyncHttpResponseHandler()
		{
			@Override
			public void onSuccess(String content)
			{
				// TODO Auto-generated method stub
				super.onSuccess(content);
				TALogger.d(LoginActivity.this, content);
			}
			
			@Override
			public void onStart()
			{
				// TODO Auto-generated method stub
				super.onStart();
			}
			
			@Override
			public void onFailure(Throwable error)
			{
				// TODO Auto-generated method stub
				super.onFailure(error);
			}
			
			@Override
			public void onFinish()
			{
				// TODO Auto-generated method stub
				super.onFinish();
			}

		});
```
### 使用http模块上传文件 或者 提交数据 到服务器（post方法）
文件上传到服务器，服务器如何接收，请查看[这里](http://www.oschina.net/question/105836_85825)

```java
RequestParams params = new RequestParams();
  	  params.put("username", "white_cat");
		  params.put("password", "123456");
		  params.put("email", "2640017581@qq.com");
		  params.put("profile_picture", new File("/mnt/sdcard/testpic.jpg")); // 上传文件
		  params.put("profile_picture2", inputStream); // 上传数据流
		  params.put("profile_picture3", new ByteArrayInputStream(bytes)); // 提交字节流
		client.post("http://www.thinkandroid.cn/", new AsyncHttpResponseHandler()
		{
			@Override
			public void onSuccess(String content)
			{
				// TODO Auto-generated method stub
				super.onSuccess(content);
				TALogger.d(LoginActivity.this, content);
			}
			
			@Override
			public void onStart()
			{
				// TODO Auto-generated method stub
				super.onStart();
			}
			
			@Override
			public void onFailure(Throwable error)
			{
				// TODO Auto-generated method stub
				super.onFailure(error);
			}
			
			@Override
			public void onFinish()
			{
				// TODO Auto-generated method stub
				super.onFinish();
			}

		});
	}
```


----

###使用http下载文件：
* 支持断点续传，随时停止下载任务 或者 开始任务

```java
    AsyncHttpClient syncHttpClient = new AsyncHttpClient();
  	   FileHttpResponseHandler fHandler = new FileHttpResponseHandler(
					TAExternalOverFroyoUtils.getDiskCacheDir(TestActivity.this,
					 "sdfsdfsdf").getAbsolutePath())
		   {
			         
					
					 @Override 
					 public void onProgress(String speed, String progress) {
						 //TODO Auto-generated method stub 
						 super.onProgress(speed, progress);
					 TALogger.v(TestActivity.this, progress + "--------" + speed); }
					 
					 @Override 
					 public void onFailure(Throwable error) { 
						 // TODOAuto-generated method stub 
						 super.onFailure(error); }
				 
				 @Override
				 public void onSuccess(byte[] binaryData) { 
					 // TODOAuto-generated method stub 
					 super.onSuccess(binaryData);
				 TALogger.d(TestActivity.this, "kaishi8了"); } };
		 syncHttpClient .download(
				"http://static.qiyi.com/ext/common/iQIYI/QIYImedia_4_01.exe",
				 fHandler);
         //停止
        fHandler.setInterrupt(interrupt);
        
```


##图片模块 方法 
* imageview加载图片的时候无需考虑图片加载过程中出现的oom和android容器快速滑动时候出现的图片错位等现象。

```java
package com.test;
import com.ta.TAApplication;
import com.ta.util.bitmap.TABitmapCacheWork;
import com.ta.util.bitmap.TADownloadBitmapHandler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class Adapter extends BaseAdapter
{
  TABitmapCacheWork imageFetcher;
	Context mContext;

	public Adapter(Context context, TAApplication application)
	{
		TADownloadBitmapHandler downloadBitmapFetcher = new TADownloadBitmapHandler(
				context, 100);
		imageFetcher = new TABitmapCacheWork(context);
		imageFetcher.setProcessDataHandler(downloadBitmapFetcher);
		imageFetcher.setFileCache(application.getFileCache());
		this.mContext = context;
	}

	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return Images.imageThumbUrls.length;
	}

	@Override
	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		return Images.imageThumbUrls[position];
	}

	@Override
	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View baseView = inflater.inflate(R.layout.login_adapter, null);
		final ImageView imageView = (ImageView) baseView
				.findViewById(R.id.imageView);
     //加载图片
		imageFetcher.loadFormCache(getItem(position), imageView);
		return baseView;
	}
}



使用 
  @TAInjectView(id = R.id.gridView)
	GridView  gridView;
	Adapter adapter = new Adapter(this, getTAApplication());
	gridView.setAdapter(adapter);
```

##缓存模块 方法 
* 很简单的实现缓存，以一个简单的文件缓存为例
* 下载处理类

```java
package com.test.file;

import com.ta.util.cache.TAProcessDataHandler;
//下载处理类
public class TAProcessStringHandler extends TAProcessDataHandler
{
  @Override
	public byte[] processData(Object data)
	{
		// TODO Auto-generated method stub
		String mynameString="white_cat";
		//这里对数据进行处理，如下载东西等等，转换为byte[]以供缓存存储使用
		return mynameString.getBytes();
	}
}
```

* 缓存结果返回操作类

```java
package com.test.file;

import android.widget.TextView;

import com.ta.util.cache.TACallBackHandler;

public class TAStringCallBackHandler extends TACallBackHandler<TextView>
{
  @Override
	public void onStart(TextView t, Object data)
	{
		// TODO Auto-generated method stub
		super.onStart(t, data);
	}

	@Override
	public void onSuccess(TextView t, Object data, byte[] buffer)
	{
		// TODO Auto-generated method stub
		super.onSuccess(t, data, buffer);
	}

	@Override
	public void onFailure(TextView t, Object data)
	{
		// TODO Auto-generated method stub
		super.onFailure(t, data);
	}
}
```
* 程序调用

```java
TAFileCacheWork<TextView> taFileCacheWork=new TAFileCacheWork<TextView>();
  	taFileCacheWork.setFileCache(getTAApplication().getFileCache());
		taFileCacheWork.setCallBackHandler(new TAStringCallBackHandler());
		taFileCacheWork.setProcessDataHandler(new TAProcessStringHandler());
		taFileCacheWork.loadFormCache("http://www.baidu.com", textView);
```

##打印模块使用方法
可以较快的轻易的是实现日志打印，支持日志打印的扩展，目前支持对sdcard写入本地打印、以及控制台打印
添加打印器
TALogger.addLogger(new TAPrintToFileLogger());
调用打印
TALogger.addLogger(new TAPrintToFileLogger());
TALogger.d(TestActivity.this, "test");


##配置器模块


#关于作者
* ThinkAndroid交流网站：[http://www.thinkandroid.cn](http://www.thinkandroid.cn)
* ThinkAndroid交流QQ群 ： 169415162


