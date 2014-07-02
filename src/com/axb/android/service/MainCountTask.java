package com.axb.android.service;

import android.content.Context;

import com.axb.android.dto.Result;
import com.axb.android.ui.MainActivity;

public class MainCountTask extends BaseAsyncTask {

	public MainCountTask(Context ctx, String title, String content,
			byte type) {
		super(ctx, title, content, type);
		// TODO Auto-generated constructor stub
	}

	@Override
	void customPreExcuteDoing() {
		// TODO Auto-generated method stub
		Context context = ctx.get();
		if(context != null &&
				context instanceof MainActivity){
			MainActivity mMainActivity = (MainActivity)context;
			mMainActivity.showRequestUi(content);
		}
		
	}

	@Override
	void errorPositive() {
		// TODO Auto-generated method stub
		Context context = ctx.get();
		if(context != null &&
				context instanceof MainActivity){
			MainActivity mMainActivity = (MainActivity)context;
			mMainActivity.hideRequestUi();
		}
	}

	@Override
	void afterTask(Result result) {
		// TODO Auto-generated method stub	
		if(result.getSuccess()!=null && result.getSuccess().toLowerCase().equals("true")){
			Context context = ctx.get();
			if(context != null &&
					context instanceof MainActivity){
				MainActivity mMainActivity = (MainActivity)context;
				mMainActivity.taskSuccessDoing(result.getUnReadTask(),result.getUnReadMsg(),result.getUnReadALJJ());
			}
//			finish();
		}else{
//			Toast.makeText(ctx.get(), "请求消息失败", Toast.LENGTH_LONG).show();
			showNormalError("学习记录失败", result.getErrmessage());
		}
		
		Context mContext = ctx.get();
		if(mContext != null &&
				mContext instanceof MainActivity){
			MainActivity mMainActivity = (MainActivity)mContext;
			mMainActivity.hideRequestUi();
		}
	}
	

}
