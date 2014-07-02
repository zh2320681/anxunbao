package com.axb.android.service;

import android.content.Context;
import android.widget.Toast;

import com.axb.android.dto.Result;
import com.axb.android.ui.FileStudyActivity;

public class FileTask extends BaseAsyncTask {

	public FileTask(Context ctx, String title, String content,
			byte type) {
		super(ctx, title, content, type);
		// TODO Auto-generated constructor stub
	}

	@Override
	void customPreExcuteDoing() {
		// TODO Auto-generated method stub
		Context context = ctx.get();
	
		
	}

	@Override
	void errorPositive() {
		// TODO Auto-generated method stub
		Context context = ctx.get();
		
	}

	@Override
	void afterTask(Result result) {
		// TODO Auto-generated method stub
		Context mContext = ctx.get();
		
		if(result.getSuccess()!=null && result.getSuccess().toLowerCase().equals("true")){
			if(mContext != null
					&& mContext instanceof FileStudyActivity){
				FileStudyActivity mFileStudyActivity = (FileStudyActivity)mContext;
				Toast.makeText(mContext, "文件学习设置完成！", Toast.LENGTH_SHORT).show();
				mFileStudyActivity.onTaskAfterDoing();
			} 
			
			
//			finish();
		}else{
//			Toast.makeText(ctx.get(), "请求消息失败", Toast.LENGTH_LONG).show();
			showNormalError("保存答案", result.getErrmessage());
		}
	}
	

}
