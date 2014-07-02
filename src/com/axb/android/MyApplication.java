package com.axb.android;

import org.apache.http.params.CoreConnectionPNames;
import org.springframework.http.HttpMethod;

import cn.shrek.base.ZWApplication;
import cn.shrek.base.util.rest.ZWRequestConfig;

import com.axb.android.dto.User;
import com.axb.android.service.Command;
import com.axb.android.util.Setting;
import com.axb.android.util.db.DBOperator;
import com.axb.android.util.load.MyImageLoader;

public class MyApplication extends ZWApplication {
	
	public User mLoginUser; //登陆用户信息
	public static int faceSize; //头像的大小
	public int unReadMsg;
	public int unReadTask;
	public MyImageLoader mImageLoader; 
	
	public Setting mSetting;
	public DBOperator mDatabaseAdapter;
	
	@Override
	public void onAfterCreateApplication() {
		// TODO Auto-generated method stub
//		super.onPreCreateApplication();
		
		faceSize = screenWidth/9;
		
		mSetting = Setting.newIntance(this);
		mDatabaseAdapter = new DBOperator(this);
		
		mImageLoader = MyImageLoader.newInstance(this,mSetting.cacheDir);
		
		/**
		 * 配置默认请求
		 */
		ZWRequestConfig config = new ZWRequestConfig(HttpMethod.POST);
		config.putHeaderValue(CoreConnectionPNames.CONNECTION_TIMEOUT, Command.CONN_TIME_OUT+"");
		config.putHeaderValue(CoreConnectionPNames.SO_TIMEOUT, Command.READ_TIME_OUT+"");
		config.putHeaderValue("Connection", "Close");

		ZWRequestConfig.setDefault(config);
	}

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
	}
	
	
	public void clearData(){
		mLoginUser = null;
		mSetting.isAutoLogin = false;
		mSetting.account = null;
		mSetting.password = null;
		mSetting.saveSettings(this);
//		mImageLoader.clear();
//		mImageLoader = null;
		mActivityManager.popAllActivity();
	}
	
}
