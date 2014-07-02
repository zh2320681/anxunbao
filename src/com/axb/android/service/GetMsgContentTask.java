package com.axb.android.service;

import android.content.Context;

import com.axb.android.dto.Result;
import com.axb.android.ui.MessageInfoActivity;
import com.axb.android.util.CommonUtil;

public class GetMsgContentTask extends BaseAsyncTask {

	public GetMsgContentTask(Context ctx, String title, String content,
			byte type) {
		super(ctx, title, content, type);
		// TODO Auto-generated constructor stub
	}

	@Override
	void customPreExcuteDoing() {
		// TODO Auto-generated method stub
		Context context = ctx.get();
		if(context != null &&
				context instanceof MessageInfoActivity){
			MessageInfoActivity mMessageInfoActivity = (MessageInfoActivity)context;
			mMessageInfoActivity.showRequestUi(content);
		}
//		
//		if(context != null &&
//				context instanceof AnguiStudyActivity){
//			AnguiStudyActivity mAnguiStudyActivity = (AnguiStudyActivity)context;
//			mAnguiStudyActivity.showRequestUi(content);
//		}
		
//		if(context != null &&
//				context instanceof FileStudyActivity){
//			FileStudyActivity mFileStudyActivity = (FileStudyActivity)context;
//			mFileStudyActivity.showRequestUi(content);
//		}
	}

	@Override
	void errorPositive() {
		// TODO Auto-generated method stub
		Context context = ctx.get();
		if(context != null &&
				context instanceof MessageInfoActivity){
			MessageInfoActivity mMessageInfoActivity = (MessageInfoActivity)context;
			mMessageInfoActivity.hideRequestUi();
		}
//		
//		if(context != null &&
//				context instanceof AnguiStudyActivity){
//			AnguiStudyActivity mAnguiStudyActivity = (AnguiStudyActivity)context;
//			mAnguiStudyActivity.hideRequestUi();
//		}
	}

	@Override
	void afterTask(Result result) {
		// TODO Auto-generated method stub
		Context mContext = ctx.get();
		if(mContext != null &&
				mContext instanceof MessageInfoActivity){
			MessageInfoActivity mMessageInfoActivity = (MessageInfoActivity)mContext;
			mMessageInfoActivity.hideRequestUi();
		}
//		
//		if(mContext != null &&
//				mContext instanceof AnguiStudyActivity){
//			AnguiStudyActivity mAnguiStudyActivity = (AnguiStudyActivity)mContext;
//			mAnguiStudyActivity.hideRequestUi();
//		}
		
		if(result.getSuccess()!=null && result.getSuccess().toLowerCase().equals("true")){
			if(mContext != null &&
					mContext instanceof MessageInfoActivity){
				MessageInfoActivity mMessageInfoActivity = (MessageInfoActivity)mContext;
				String content = CommonUtil.replaceImgPath(result.getData(), Command.SERVICE_URL);
				mMessageInfoActivity.afterContentTaskDoing(content);
			}
			
			
//			finish();
		}else{
//			Toast.makeText(ctx.get(), "请求消息失败", Toast.LENGTH_LONG).show();
			showNormalError("设置消息已读", result.getErrmessage());
		}
	}
	

}
