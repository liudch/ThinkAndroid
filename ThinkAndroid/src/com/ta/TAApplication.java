/*
 * Copyright (C) 2013  WhiteCat ��è (www.thinkandroid.cn)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ta;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.HashMap;
import java.util.Stack;
import com.ta.exception.TAAppException;
import com.ta.exception.TANoSuchCommandException;
import com.ta.mvc.command.TACommandExecutor;
import com.ta.mvc.command.TAICommand;
import com.ta.mvc.common.TAIResponseListener;
import com.ta.mvc.common.TARequest;
import com.ta.mvc.common.TAResponse;
import com.ta.mvc.controller.ActivityStackInfo;
import com.ta.mvc.controller.NavigationDirection;
import com.ta.util.TAInjector;
import com.ta.util.TALogger;
import com.ta.util.cache.TAFileCache;
import com.ta.util.config.TAIConfig;
import com.ta.util.config.TAPreferenceConfig;
import com.ta.util.config.TAPropertiesConfig;
import com.ta.util.db.TASQLiteDatabasePool;
import com.ta.util.layoutloader.TAILayoutLoader;
import com.ta.util.layoutloader.TALayoutLoader;

import android.app.Application;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

public class TAApplication extends Application implements TAIResponseListener
{
	/** ������ ΪPreference */
	public final static int PREFERENCECONFIG = 0;
	/** ������ ΪPROPERTIESCONFIG */
	public final static int PROPERTIESCONFIG = 1;
	/** ������ */
	private TAIConfig mCurrentConfig;
	/** ��ȡ�����ļ�ID������ */
	private TAILayoutLoader mLayoutLoader;
	/** ������ע���� */
	private TAInjector mInjector;
	/** App�쳣���������� */
	private UncaughtExceptionHandler uncaughtExceptionHandler;
	private static TAApplication application;
	private TACommandExecutor mCommandExecutor;
	private TAActivity currentActivity;
	private final HashMap<String, Class<? extends TAActivity>> registeredActivities = new HashMap<String, Class<? extends TAActivity>>();
	private Stack<ActivityStackInfo> activityStack = new Stack<ActivityStackInfo>();
	private NavigationDirection currentNavigationDirection;
	/** ThinkAndroid �ļ����� */
	private TAFileCache mFileCache;
	/** ThinkAndroid���ݿ����ӳ� */
	private TASQLiteDatabasePool mSQLiteDatabasePool;
	/** ThinkAndroid Ӧ�ó�������Activity������ */
	private TAAppManager mAppManager;
	private Boolean networkAvailable = false;

	@Override
	public void onCreate()
	{
		// TODO Auto-generated method stub
		onPreCreateApplication();
		super.onCreate();
		doOncreate();
		onAfterCreateApplication();
		getAppManager();
	}

	private void doOncreate()
	{
		// TODO Auto-generated method stub
		this.application = this;
		// ע��App�쳣����������
		Thread.setDefaultUncaughtExceptionHandler(getUncaughtExceptionHandler());
		mCommandExecutor = TACommandExecutor.getInstance();
	}

	/**
	 * ��ȡApplication
	 * 
	 * @return
	 */
	public static TAApplication getApplication()
	{
		return application;
	}

	protected void onAfterCreateApplication()
	{
		// TODO Auto-generated method stub

	}

	protected void onPreCreateApplication()
	{
		// TODO Auto-generated method stub

	}

	public TAIConfig getPreferenceConfig()
	{
		return getConfig(PREFERENCECONFIG);
	}

	public TAIConfig getPropertiesConfig()
	{
		return getConfig(PROPERTIESCONFIG);
	}

	public TAIConfig getConfig(int confingType)
	{
		if (confingType == PREFERENCECONFIG)
		{
			mCurrentConfig = TAPreferenceConfig.getPreferenceConfig(this);

		} else if (confingType == PROPERTIESCONFIG)
		{
			mCurrentConfig = TAPropertiesConfig.getPropertiesConfig(this);
		} else
		{
			mCurrentConfig = TAPropertiesConfig.getPropertiesConfig(this);
		}
		if (!mCurrentConfig.isLoadConfig())
		{
			mCurrentConfig.loadConfig();
		}
		return mCurrentConfig;
	}

	public TAIConfig getCurrentConfig()
	{
		if (mCurrentConfig == null)
		{
		mCurrentConfig=getPreferenceConfig();
		}
		return mCurrentConfig;
	}

	public TAILayoutLoader getLayoutLoader()
	{
		if (mLayoutLoader == null)
		{
		mLayoutLoader=TALayoutLoader.getInstance(this);
		}
		return mLayoutLoader;
	}

	public void setLayoutLoader(TAILayoutLoader layoutLoader)
	{
		this.mLayoutLoader = layoutLoader;
	}

	/**
	 * ���� App�쳣����������
	 * 
	 * @param uncaughtExceptionHandler
	 */
	public void setUncaughtExceptionHandler(
			UncaughtExceptionHandler uncaughtExceptionHandler)
	{
		this.uncaughtExceptionHandler = uncaughtExceptionHandler;
	}

	private UncaughtExceptionHandler getUncaughtExceptionHandler()
	{
		if (uncaughtExceptionHandler == null)
		{
			uncaughtExceptionHandler = TAAppException.getInstance(this);
		}
		return uncaughtExceptionHandler;
	}

	public TAInjector getInjector()
	{
		if (mInjector == null)
		{
			mInjector = TAInjector.getInstance();
		}
		return mInjector;
	}

	public void setInjector(TAInjector injector)
	{
		this.mInjector = injector;
	}

	public void onActivityCreating(TAActivity activity)
	{
		if (activityStack.size() > 0)
		{
			ActivityStackInfo info = activityStack.peek();
			if (info != null)
			{
				TAResponse response = info.getResponse();
				activity.preProcessData(response);
			}
		}
	}

	public void onActivityCreated(TAActivity activity)
	{
		if (currentActivity != null)
		{
			currentActivity.finish();
		}
		currentActivity = activity;

		int size = activityStack.size();

		if (size > 0)
		{
			ActivityStackInfo info = activityStack.peek();
			if (info != null)
			{
				TAResponse response = info.getResponse();
				activity.processData(response);

				if (size >= 2 && !info.isRecord())
				{
					activityStack.pop();
				}
			}
		}
	}

	public void doCommand(String commandKey, TARequest request,
			TAIResponseListener listener, boolean record, boolean resetStack)
	{
		TALogger.i(TAApplication.this, "go with cmdid=" + commandKey
				+ ", record: " + record + ",rs: " + resetStack + ", request: "
				+ request);
		if (resetStack)
		{
			activityStack.clear();
		}

		currentNavigationDirection = NavigationDirection.Forward;

		ActivityStackInfo info = new ActivityStackInfo(commandKey, request,
				record, resetStack);
		activityStack.add(info);

		Object[] newTag =
		{ request.getTag(), listener };
		request.setTag(newTag);

		TALogger.i(TAApplication.this, "Enqueue-ing command");
		try
		{
			if (listener == null)
			{
				TACommandExecutor.getInstance().enqueueCommand(commandKey,
						request, this);
			} else
			{
				TACommandExecutor.getInstance().enqueueCommand(commandKey,
						request, listener);
			}

		} catch (TANoSuchCommandException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TALogger.i(TAApplication.this, "Enqueued command");
	}

	public void back()
	{
		TALogger.i(TAApplication.this,
				"ActivityStack Size: " + activityStack.size());
		if (activityStack != null && activityStack.size() != 0)
		{
			if (activityStack.size() >= 2)
			{
				activityStack.pop();
			}

			currentNavigationDirection = NavigationDirection.Backward;
			ActivityStackInfo info = activityStack.peek();
			try
			{
				TACommandExecutor.getInstance().enqueueCommand(
						info.getCommandKey(), info.getRequest(), this);
			} catch (TANoSuchCommandException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void processResponse(Message msg)
	{
		TAResponse response = (TAResponse) msg.obj;
		ActivityStackInfo top = activityStack.peek();
		top.setResponse(response);
		if (response != null)
		{
			int targetActivityKeyResID = response.getActivityKeyResID();
			String targetActivityKey = "";
			if (targetActivityKeyResID != 0)
			{
				targetActivityKey = getString(targetActivityKeyResID);
			}
			if (targetActivityKey != null
					&& targetActivityKey.equalsIgnoreCase(""))
			{
				targetActivityKey = response.getActivityKey();
			}
			Object[] newTag = (Object[]) response.getTag();
			Object tag = newTag[0];
			response.setTag(tag);
			Class<? extends TAActivity> cls = registeredActivities
					.get(targetActivityKey);
			TALogger.i(TAApplication.this,
					"Launching new activity // else, current Direction: "
							+ currentNavigationDirection);

			int asize = activityStack.size();
			TALogger.i(TAApplication.this,
					"Current Stack Size (before processing): " + asize);

			switch (currentNavigationDirection)
			{
			case Forward:
				if (asize >= 2)
				{
					if (!top.isRecord())
					{
						activityStack.pop();
					}
				}
				break;
			case Backward:
				// Popping of the last command from the stack would have
				// happened in (back)
				// Just reset the navigation direction
				currentNavigationDirection = NavigationDirection.Forward;
				break;
			}
			TALogger.i(
					TAApplication.this,
					"Current Stack Size (after processing): "
							+ activityStack.size());

			if (cls != null)
			{
				Intent launcherIntent = new Intent(currentActivity, cls);
				currentActivity.startActivity(launcherIntent);
				currentActivity.finish();
				top.setActivityClass(cls);
			}

		}

	}

	public void registerActivity(int resID, Class<? extends TAActivity> clz)
	{
		String activityKey = getString(resID);
		registeredActivities.put(activityKey, clz);
	}

	public void registerActivity(String activityKey,
			Class<? extends TAActivity> clz)
	{
		registeredActivities.put(activityKey, clz);
	}

	public void unregisterActivity(int resID)
	{
		String activityKey = getString(resID);
		unregisterActivity(activityKey);
	}

	public void unregisterActivity(String activityKey)
	{
		registeredActivities.remove(activityKey);
	}

	public void registerCommand(int resID, Class<? extends TAICommand> command)
	{

		String commandKey = getString(resID);
		registerCommand(commandKey, command);

	}

	public void registerCommand(String commandKey,
			Class<? extends TAICommand> command)
	{
		if (command != null)
		{
			mCommandExecutor.registerCommand(commandKey, command);
		}
	}

	public void unregisterCommand(int resID)
	{
		String commandKey = getString(resID);
		unregisterCommand(commandKey);
	}

	public void unregisterCommand(String commandKey)
	{

		mCommandExecutor.unregisterCommand(commandKey);
	}

	public TAFileCache getFileCache()
	{
		return mFileCache;
	}

	public void setFileCache(TAFileCache fileCache)
	{
		this.mFileCache = fileCache;
	}

	private Handler handler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			processResponse(msg);
		}
	};

	private void handleResponse(TAResponse response)
	{
		Message msg = new Message();
		msg.what = 0;
		msg.obj = response;
		handler.sendMessage(msg);
	}

	@Override
	public void onStart(TAResponse response)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccess(TAResponse response)
	{
		// TODO Auto-generated method stub
		handleResponse(response);
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
		handleResponse(response);
	}

	public TASQLiteDatabasePool getSQLiteDatabasePool()
	{
		if (mSQLiteDatabasePool == null)
		{
			mSQLiteDatabasePool = TASQLiteDatabasePool.getInstance(this);
			mSQLiteDatabasePool.createPool();
		}
		return mSQLiteDatabasePool;
	}

	public void setSQLiteDatabasePool(TASQLiteDatabasePool sqliteDatabasePool)
	{
		this.mSQLiteDatabasePool = sqliteDatabasePool;
	}

	public TAAppManager getAppManager()
	{
		if (mAppManager == null)
		{
			mAppManager = TAAppManager.getAppManager();
		}
		return mAppManager;
	}

	/**
	 * �˳�Ӧ�ó���
	 * 
	 * @param isBackground
	 *            �Ƿ񿪿�����̨����,����Ϊtrue��Ϊ��̨����
	 */
	public void exitApp(Boolean isBackground)
	{
		mAppManager.AppExit(this, isBackground);
	}

	/**
	 * ��ȡ��ǰ����״̬��trueΪ�������ӳɹ���������������ʧ��
	 * 
	 * @return
	 */
	public Boolean isNetworkAvailable()
	{
		return networkAvailable;
	}
}
