/*
 * Copyright (C) 2013  WhiteCat ∞◊√® (www.thinkandroid.cn)
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
package com.ta.util.bitmap;

import com.ta.util.cache.TAFileCache.TACacheParams;
import com.ta.util.cache.TAFileCache;
import com.ta.util.cache.TAFileCacheWork;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.ImageView;

public class TABitmapCacheWork extends TAFileCacheWork<ImageView>
{

	protected Resources mResources;
	private TACacheParams mCacheParams;
	private static final String TAG = "TABitmapCacheWork";
	private Context mContext;

	public TABitmapCacheWork(Context context)
	{
		mResources = context.getResources();
		this.mContext = context;
	}

	@Override
	public void loadFormCache(Object data, ImageView responseObject)
	{
		// TODO Auto-generated method stub
		if (getCallBackHandler() == null)
		{
			TABitmapCallBackHanlder callBackHanlder = new TABitmapCallBackHanlder();
			setCallBackHandler(callBackHanlder);
		}
		if (getProcessDataHandler() == null)
		{
			TADownloadBitmapHandler downloadBitmapFetcher = new TADownloadBitmapHandler(
					mContext, 100);
			setProcessDataHandler(downloadBitmapFetcher);
		}
		super.loadFormCache(data, responseObject);
	}

	/**
	 * …Ë÷√Õº∆¨ª∫¥Ê
	 * 
	 * @param cacheParams
	 *            œÏ”¶≤Œ ˝
	 */
	public void setBitmapCache(TACacheParams cacheParams)
	{
		mCacheParams = cacheParams;
		setFileCache(new TAFileCache(cacheParams));
	}

	public void setBitmapCache(FragmentManager fragmentManager,
			TACacheParams cacheParams)
	{
		mCacheParams = cacheParams;
		setFileCache(findOrCreateCache(fragmentManager, mCacheParams));
	}

	public static TAFileCache findOrCreateCache(
			FragmentManager fragmentManager, TACacheParams cacheParams)
	{

		// Search for, or create an instance of the non-UI RetainFragment
		final RetainFragment mRetainFragment = findOrCreateRetainFragment(fragmentManager);

		// See if we already have an ImageCache stored in RetainFragment
		TAFileCache bitmapCache = (TAFileCache) mRetainFragment.getObject();

		// No existing ImageCache, create one and store it in RetainFragment
		if (bitmapCache == null)
		{
			bitmapCache = new TAFileCache(cacheParams);
			mRetainFragment.setObject(bitmapCache);
		}

		return bitmapCache;
	}

	/**
	 * Locate an existing instance of this Fragment or if not found, create and
	 * add it using FragmentManager.
	 * 
	 * @param fm
	 *            The FragmentManager manager to use.
	 * @return The existing instance of the Fragment or the new instance if just
	 *         created.
	 */
	public static RetainFragment findOrCreateRetainFragment(FragmentManager fm)
	{

		// Check to see if we have retained the worker fragment.
		RetainFragment mRetainFragment = (RetainFragment) fm
				.findFragmentByTag(TAG);

		// If not retained (or first time running), we need to create and add
		// it.
		if (mRetainFragment == null)
		{
			mRetainFragment = new RetainFragment();
			fm.beginTransaction().add(mRetainFragment, TAG)
					.commitAllowingStateLoss();
		}

		return mRetainFragment;
	}

	/**
	 * A simple non-UI Fragment that stores a single Object and is retained over
	 * configuration changes. It will be used to retain the ImageCache object.
	 */
	public static class RetainFragment extends Fragment
	{
		private Object mObject;

		/**
		 * Empty constructor as per the Fragment documentation
		 */
		public RetainFragment()
		{
		}

		@Override
		public void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);

			// Make sure this Fragment is retained over a configuration change
			setRetainInstance(true);
		}

		/**
		 * Store a single object in this Fragment.
		 * 
		 * @param object
		 *            The object to store
		 */
		public void setObject(Object object)
		{
			mObject = object;
		}

		/**
		 * Get the stored object.
		 * 
		 * @return The stored object
		 */
		public Object getObject()
		{
			return mObject;
		}
	}

	@Override
	protected void initDiskCacheInternal()
	{
		// TODO Auto-generated method stub
		TADownloadBitmapHandler downloadBitmapFetcher = (TADownloadBitmapHandler) getProcessDataHandler();
		super.initDiskCacheInternal();
		if (downloadBitmapFetcher != null)
		{
			downloadBitmapFetcher.initDiskCacheInternal();
		}
	}

	protected void clearCacheInternal()
	{
		super.clearCacheInternal();
		TADownloadBitmapHandler downloadBitmapFetcher = (TADownloadBitmapHandler) getProcessDataHandler();
		if (downloadBitmapFetcher != null)
		{
			downloadBitmapFetcher.clearCacheInternal();
		}
	}

	protected void flushCacheInternal()
	{
		super.flushCacheInternal();
		TADownloadBitmapHandler downloadBitmapFetcher = (TADownloadBitmapHandler) getProcessDataHandler();
		if (downloadBitmapFetcher != null)
		{
			downloadBitmapFetcher.flushCacheInternal();
		}
	}

	protected void closeCacheInternal()
	{
		super.closeCacheInternal();
		TADownloadBitmapHandler downloadBitmapFetcher = (TADownloadBitmapHandler) getProcessDataHandler();
		if (downloadBitmapFetcher != null)
		{
			downloadBitmapFetcher.closeCacheInternal();
		}
	}
}
