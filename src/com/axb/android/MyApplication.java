package com.axb.android;

import org.apache.http.params.CoreConnectionPNames;
import org.springframework.http.HttpMethod;

import cn.tt100.base.ZWApplication;
import cn.tt100.base.util.rest.ZWRequestConfig;

import com.axb.android.dto.User;
import com.axb.android.service.Command;
import com.axb.android.util.Setting;
import com.axb.android.util.db.DBOperator;
import com.axb.android.util.load.MyImageLoader;

public class MyApplication extends ZWApplication {
	
	public User mLoginUser; //��½�û���Ϣ
	public static int faceSize; //ͷ��Ĵ�С
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
		 * ����Ĭ������
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
