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
package com.ta.util.resoperate;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources.NotFoundException;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;

/**
 * @Title TAResOperateUtils
 * @Package com.ta.util.resoperate
 * @Description TAResOperateUtils是一个操作应用程序Res包资源
 * @author 白猫
 * @date 2013-1-22 上午 11:25
 * @version V1.0
 */
public class TAResOperateUtils
{

	/**
	 * 读取Res中的字符串
	 * 
	 * @param context
	 * @param resourceID
	 *            资源ID
	 * @return 返回相应ID的字符串
	 */
	public static String doReadString(Context context, int resourceID)
	{
		return context.getResources().getString(resourceID);
	}

	/**
	 * 读取Res中的Color值
	 * 
	 * @param context
	 * @param resourceID
	 *            资源ID
	 * @return 返回相应ID的Color值
	 */
	public static int doReadColor(Context context, int resourceID)
			throws NotFoundException
	{
		return context.getResources().getColor(resourceID);
	}

	/**
	 * 读取Res中的ColorStateList值
	 * 
	 * @param context
	 * @param resourceID
	 *            资源ID
	 * @return 返回相应ID的ColorStateList值
	 */
	public static ColorStateList doReadColorStateList(Context context,
			int resourceID) throws NotFoundException
	{
		return context.getResources().getColorStateList(resourceID);
	}

	/**
	 * 读取Res中的Dimension值
	 * 
	 * @param context
	 * @param resourceID
	 *            资源ID
	 * @return 返回相应ID的Dimension值
	 */
	public static float doReadDimension(Context context, int resourceID)
			throws NotFoundException
	{
		return context.getResources().getDimension(resourceID);
	}

	/**
	 * 读取Res中的drawable值
	 * 
	 * @param context
	 * @param resourceID
	 *            资源ID
	 * @return 返回相应ID的drawable值
	 */
	public static Drawable doReadDrawable(Context context, int resourceID)
			throws NotFoundException
	{
		return context.getResources().getDrawable(resourceID);
	}

	/**
	 * 读取Res中的layout值
	 * 
	 * @param context
	 * @param resourceID
	 *            资源ID
	 * @return 返回相应ID的layout值
	 */
	public static XmlResourceParser doReadlayout(Context context, int resourceID)
			throws NotFoundException
	{
		return context.getResources().getLayout(resourceID);
	}

	/**
	 * 读取Res中的String类型Array值
	 * 
	 * @param context
	 * @param resourceID
	 *            资源ID
	 * @return 返回相应ID的String类型Array值
	 */
	public static String[] doReadStringArray(Context context, int resourceID)
			throws NotFoundException
	{
		return context.getResources().getStringArray(resourceID);
	}

	/**
	 * 读取Res中的int类型Array值
	 * 
	 * @param context
	 * @param resourceID
	 *            资源ID
	 * @return 返回相应ID的int类型Array值
	 */
	public static int[] doReadIntArray(Context context, int resourceID)
			throws NotFoundException
	{
		return context.getResources().getIntArray(resourceID);
	}
}
