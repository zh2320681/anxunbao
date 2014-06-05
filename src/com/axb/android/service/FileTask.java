package com.axb.android.service;

import android.content.Context;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.axb.android.dto.CaseQuestion;
import com.axb.android.dto.Result;
import com.axb.android.ui.AnguiStudyActivity;
import com.axb.android.ui.CaseStudyActivity;
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
				Toast.makeText(mContext, "�ļ�ѧϰ������ɣ�", Toast.LENGTH_SHORT).show();
				mFileStudyActivity.onTaskAfterDoing();
			} 
			
			
//			finish();
		}else{
//			Toast.makeText(ctx.get(), "������Ϣʧ��", Toast.LENGTH_LONG).show();
			showNormalError("�����", result.getErrmessage());
		}
	}
	

}
