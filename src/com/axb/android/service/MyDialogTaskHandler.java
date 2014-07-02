package com.axb.android.service;

import cn.shrek.base.util.rest.DialogTaskHandler;
import cn.shrek.base.util.rest.ZWResult;

import com.axb.android.dto.BaseResult;

public abstract class MyDialogTaskHandler<T extends BaseResult> extends DialogTaskHandler<T> {
//	 private Class<?> parseClazz;
	
	public MyDialogTaskHandler(String title, String content) {
		super(title, content);
		// TODO Auto-generated constructor stub
//		this.parseClazz = parseClazz;
	}

	@Override
	public void postResult(ZWResult<T> arg0) {
		// TODO Auto-generated method stub
		BaseResult mBaseResult = arg0.bodyObj;
		if(mBaseResult == null || !mBaseResult.isSuccess()){
			if(mBaseResult.errmessage.contains("com")
					|| mBaseResult.errmessage.contains("cn")){
				mBaseResult.errmessage = "请求服务器端数据出错啦！";
			}
			
			showNormalError(false, this.title, mBaseResult.errmessage);
			return;
		}
//		arg0.bodyObj.mineData = JSON.parseObject(arg0.bodyObj.data, parseClazz);
		
		minePostResult(arg0.bodyObj);
	}

	public abstract void minePostResult(T arg0);

}
