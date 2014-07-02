package com.axb.android.service;

import java.util.List;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.axb.android.dto.CaseDto;
import com.axb.android.dto.Result;
import com.axb.android.ui.UndoneTaskActivity;

public class UnDoneTask extends BaseAsyncTask {

	public UnDoneTask(Context ctx, String title, String content,
			byte type) {
		super(ctx, title, content, type);
		// TODO Auto-generated constructor stub
	}

	@Override
	void customPreExcuteDoing() {
		// TODO Auto-generated method stub
		Context context = ctx.get();
		if(context != null &&
				context instanceof UndoneTaskActivity){
			UndoneTaskActivity mUndoneTaskActivity = (UndoneTaskActivity)context;
			mUndoneTaskActivity.showRequestUi(content);
		}
		
	}

	@Override
	void errorPositive() {
		// TODO Auto-generated method stub
		Context context = ctx.get();
		if(context != null &&
				context instanceof UndoneTaskActivity){
			UndoneTaskActivity mUndoneTaskActivity = (UndoneTaskActivity)context;
			mUndoneTaskActivity.hideRequestUi();
		}
	}

	@Override
	void afterTask(Result result) {
		// TODO Auto-generated method stub	
		if(result.getSuccess()!=null && result.getSuccess().toLowerCase().equals("true")){
			List<CaseDto> lists = JSON.parseArray(result.getData(),CaseDto.class);
//			Page mPage = JSON.parseObject(result.getPage(), Page.class);
			Context context = ctx.get();
			if(context != null &&
					context instanceof UndoneTaskActivity){
				UndoneTaskActivity mUndoneTaskActivity = (UndoneTaskActivity)context;
				mUndoneTaskActivity.taskSuccessDoing(lists);
			}
//			finish();
		}else{
//			Toast.makeText(ctx.get(), "请求消息失败", Toast.LENGTH_LONG).show();
			showNormalError("学习记录失败", result.getErrmessage());
		}
		
		Context mContext = ctx.get();
		if(mContext != null &&
				mContext instanceof UndoneTaskActivity){
			UndoneTaskActivity mUndoneTaskActivity = (UndoneTaskActivity)mContext;
			mUndoneTaskActivity.hideRequestUi();
		}
	}
	

}
