package com.axb.android.service;

import java.util.List;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.axb.android.dto.AnLiJianShang;
import com.axb.android.dto.Result;
import com.axb.android.ui.CaseReferenceActivity;

public class CaseReferenceTask extends BaseAsyncTask {

	public CaseReferenceTask(Context ctx, String title, String content,
			byte type) {
		super(ctx, title, content, type);
	}

	@Override
	void customPreExcuteDoing() {
		Context context = ctx.get();
		if(context != null &&
				context instanceof CaseReferenceActivity){
			CaseReferenceActivity mCaseReferenceActivity = (CaseReferenceActivity)context;
			mCaseReferenceActivity.showRequestUi(content);
		}
	}

	@Override
	void errorPositive() {
		// TODO Auto-generated method stub
		Context context = ctx.get();
		if(context != null &&context instanceof CaseReferenceActivity){
			CaseReferenceActivity mCaseReferenceActivity = (CaseReferenceActivity)context;
			mCaseReferenceActivity.hideRequestUi();
		}
	}

	@Override
	void afterTask(Result result) {
		if(result.getSuccess()!=null && result.getSuccess().toLowerCase().equals("true")){
			List<AnLiJianShang> caseRefList = JSON.parseArray(result.getData(),AnLiJianShang.class);
			Context context = ctx.get();
			if(context != null &&context instanceof CaseReferenceActivity){
				CaseReferenceActivity mCaseReferenceActivity = (CaseReferenceActivity)context;
//				mCaseReferenceActivity.taskSuccessDoing(caseRefList);
			}
		}else{
			showNormalError("Ñ§Ï°¼ÇÂ¼Ê§°Ü", result.getErrmessage());
		}
		
		Context mContext = ctx.get();
		if(mContext != null &&
				mContext instanceof CaseReferenceActivity){
			CaseReferenceActivity mCaseReferenceActivity = (CaseReferenceActivity)mContext;
			mCaseReferenceActivity.hideRequestUi();
		}
	}
	

}
