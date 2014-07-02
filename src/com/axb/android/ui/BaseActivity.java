package com.axb.android.ui;

import android.os.Bundle;
import android.view.Window;
import cn.shrek.base.ui.ZWActivity;

import com.axb.android.MyApplication;

public class BaseActivity extends ZWActivity {
	public MyApplication mApplication;

	@Override
	protected void onBaseCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onBaseCreate(savedInstanceState);
		mApplication = (MyApplication)getApplication();
	}
	
	@Override
	protected void addListener() {
		// TODO Auto-generated method stub
	}



	@Override
	protected void initialize() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void notifyObserver(Object arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}
}
