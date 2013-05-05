/*
 * Copyright (C) 2013  WhiteCat 白猫 (www.thinkandroid.cn)
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
package com.ta.util.netstate;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.ta.TAApplication;
import com.ta.util.TALogger;
import com.ta.util.netstate.NetWorkUtil.netType;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @Title NetworkStateReceiver
 * @Package com.ta.util.netstate
 * @Description 是一个检测网络状态改变的，需要配置 <receiver
 *              android:name="com.ta.core.util.extend.net.NetworkStateReceiver"
 *              > <intent-filter> <action
 *              android:name="android.net.conn.CONNECTIVITY_CHANGE" /> <action
 *              android:name="android.cat.conn.CONNECTIVITY_CHANGE" />
 *              </intent-filter> </receiver>
 * @author 白猫
 * @date 2013-5-5 下午 22:47
 * @version V1.2
 */
public class NetworkStateReceiver extends BroadcastReceiver
{
	private static Boolean networkAvailable = false;
	private netType netType;
	private static HashMap<String, TANetChangeObserver> netChangeObserverHashMap = new HashMap<String, TANetChangeObserver>();

	@Override
	public void onReceive(Context context, Intent intent)
	{
		TALogger.i(NetworkStateReceiver.this, "网络状态改变.");
		Iterator<Entry<String, TANetChangeObserver>> iter = netChangeObserverHashMap
				.entrySet().iterator();
		if (!NetWorkUtil.isNetworkAvailable(context))
		{
			TALogger.i(NetworkStateReceiver.this, "没有网络连接.");

			networkAvailable = false;
		} else
		{
			TALogger.i(NetworkStateReceiver.this, "网络连接成功.");
			netType = NetWorkUtil.getAPNType(context);
			networkAvailable = true;
		}
		while (iter.hasNext())
		{
			Map.Entry<String, TANetChangeObserver> entry = iter.next();
			TANetChangeObserver observer = entry.getValue();
			if (observer != null)
			{
				notifyObserver(observer);
			}
		}
	}

	/**
	 * 获取当前网络状态，true为网络连接成功，否则网络连接失败
	 * 
	 * @return
	 */
	public static Boolean isNetworkAvailable()
	{
		return networkAvailable;
	}

	public netType getAPNType()
	{
		return netType;
	}

	private void notifyObserver(TANetChangeObserver observer)
	{

		if (isNetworkAvailable())
		{
			observer.connect(netType);
		} else
		{
			observer.disConnect();
		}
	}

	/**
	 * 注册网络连接观察者
	 * 
	 * @param observerKey
	 *            observerKey
	 * @param observer
	 *            网络变化观察者
	 */
	public static void registerObserver(String observerKey,
			TANetChangeObserver observer)
	{
		if (netChangeObserverHashMap == null)
		{
			netChangeObserverHashMap = new HashMap<String, TANetChangeObserver>();
		}
		netChangeObserverHashMap.put(observerKey, observer);
	}

	/**
	 * 注册网络连接观察者
	 * 
	 * @param resID
	 *            observerKey的 资源id
	 * @param observer
	 *            网络变化观察者
	 */
	public static void registerObserver(int resID, TANetChangeObserver observer)
	{
		String observerKey = TAApplication.getApplication().getString(resID);
		registerObserver(observerKey, observer);
	}

	/**
	 * 注销网络连接观察者
	 * 
	 * @param resID
	 *            observerKey
	 */
	public static void unRegisterObserver(String observerKey)
	{
		if (netChangeObserverHashMap != null)
		{
			netChangeObserverHashMap.remove(observerKey);
		}
	}

	/**
	 * 注销网络连接观察者
	 * 
	 * @param resID
	 *            observerKey
	 */
	public static void unRegisterObserver(int resID)
	{
		String observerKey = TAApplication.getApplication().getString(resID);
		unRegisterObserver(observerKey);
	}

}