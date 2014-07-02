package com.axb.android.service;

import java.util.List;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.axb.android.dto.CaseDto;
import com.axb.android.dto.Result;
import com.axb.android.ui.StudyRecordActivity;

public class RecordTask extends BaseAsyncTask {

	public RecordTask(Context ctx, String title, String content,
			byte type) {
		super(ctx, title, content, type);
		// TODO Auto-generated constructor stub
	}

	@Override
	void customPreExcuteDoing() {
		// TODO Auto-generated method stub
		Context context = ctx.get();
		if(context != null &&
				context instanceof StudyRecordActivity){
			StudyRecordActivity mStudyRecordActivty = (StudyRecordActivity)context;
			mStudyRecordActivty.showRequestUi(content);
		}
		
	}

	@Override
	void errorPositive() {
		// TODO Auto-generated method stub
		Context context = ctx.get();
		if(context != null &&
				context instanceof StudyRecordActivity){
			StudyRecordActivity mStudyRecordActivty = (StudyRecordActivity)context;
			mStudyRecordActivty.hideRequestUi();
		}
	}

	@Override
	void afterTask(Result result) {
		// TODO Auto-generated method stub
		Context mContext = ctx.get();
		if(mContext != null &&
				mContext instanceof StudyRecordActivity){
			StudyRecordActivity mStudyRecordActivty = (StudyRecordActivity)mContext;
			mStudyRecordActivty.hideRequestUi();
		}
		
		if(result.getSuccess()!=null && result.getSuccess().toLowerCase().equals("true")){
			List<CaseDto> lists = JSON.parseArray(result.getData(),CaseDto.class);
//			Page mPage = JSON.parseObject(result.getPage(), Page.class);
			Context context = ctx.get();
			if(context != null &&
					context instanceof StudyRecordActivity){
				StudyRecordActivity mStudyRecordActivty = (StudyRecordActivity)mContext;
				mStudyRecordActivty.taskSuccessDoing(lists,result.getPage());
			}
			
//			finish();
		}else{
//			Toast.makeText(ctx.get(), "请求消息失败", Toast.LENGTH_LONG).show();
			showNormalError("学习记录失败", result.getErrmessage());
		}
	}
	

}
