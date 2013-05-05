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

import com.ta.util.TALogger;

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
 * @date 2013-1-22 下午 9:35
 * @version V1.0
 */
public class NetworkStateReceiver extends BroadcastReceiver
{
	private static Boolean networkAvailable = false;

	@Override
	public void onReceive(Context context, Intent intent)
	{
		TALogger.i(NetworkStateReceiver.this, "网络状态改变.");
		if (!NetWorkUtil.isNetworkAvailable(context))
		{
			TALogger.i(NetworkStateReceiver.this, "网络没有连接.");
			networkAvailable = false;
		} else
		{
			TALogger.i(NetworkStateReceiver.this, "网络连接成功.");
			networkAvailable = true;
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

}