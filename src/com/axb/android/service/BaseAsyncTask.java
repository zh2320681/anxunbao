package com.axb.android.service;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

import com.axb.android.R;
import com.axb.android.dto.BaseBo;
import com.axb.android.dto.Result;
import com.axb.android.exception.CallJSONException;
import com.axb.android.exception.CallServiceException;
import com.axb.android.exception.NetworkAvailableException;

public abstract class BaseAsyncTask extends AsyncTask<BaseBo, Integer, Result> {
	private static final String TAG = "BaseAsyncTask";

	public static final byte PRE_TASK_NORMAL = 0x01;
	public static final byte PRE_TASK_CUSTOM = 0x02;
	public static final byte PRE_TASK_DONOTHING = 0x03;

	public WeakReference<Context> ctx;
	private ProgressDialog progressDialog;
	public boolean isTaskSuccess = true;
	private String title;
	public String content;
	private byte preType;
	
	private BaseBo mBaseBo; //请求的数据 可以自带

	
	public BaseAsyncTask(Context ctx, String title, String content, byte type) {
		this.ctx = new WeakReference<Context>(ctx);
		this.title = title;
		this.content = content;
		this.preType = type;
		
//		if(ctx instanceof BaseActivity){
//			((BaseActivity)ctx).addTask(this);
//		}
		
	}

	@Override
	protected void onPreExecute() {
		//任务执行前
		super.onPreExecute();
		if(judgeTaskValid()){
			switch(preType){
			case PRE_TASK_NORMAL:
				Context mContext = ctx.get();
				if (mContext != null && title!=null
					&& content!=null) {
				progressDialog = new ProgressDialog(mContext);
				progressDialog.setTitle(this.title);
				progressDialog.setMessage(this.content);
				progressDialog.setCancelable(false);
				if (judgeTaskValid() && progressDialog!=null && !progressDialog.isShowing())
					progressDialog.show();
				}
				break;
			case PRE_TASK_CUSTOM:
				customPreExcuteDoing();
				break;
			case PRE_TASK_DONOTHING:
	
				break;
			}
		}
		
	}

	@Override
	protected Result doInBackground(BaseBo... params) {
		// TODO Auto-generated method stub
		Result r = new Result();
		try {
			r = excuteTask(params);
		} catch (NetworkAvailableException e) {
			// 移动数据连接被禁用
			 if (judgeTaskValid() && progressDialog != null && progressDialog.isShowing()) {
				 progressDialog.dismiss();
				 progressDialog.cancel();
			 }
			r.setSuccess("false");
			r.setRequestError("网络数据连接异常");
			r.setErrmessage("网络数据连接异常,请检测网络~~");
			isTaskSuccess = false;
		} catch (CallJSONException e) {
			// 获取json数据出错
			 if (judgeTaskValid() && progressDialog != null && progressDialog.isShowing()) {
				 progressDialog.dismiss();
				 progressDialog.cancel();
			 }
			// Log.e(TAG, e.getMessage());
			r.setSuccess("false");
			r.setRequestError("获取数据出错");
			r.setErrmessage("请求服务器端数据出错啦！");
			isTaskSuccess = false;
		} catch (CallServiceException e) {
			// TODO: handle exception
			// 获取json数据出错
			 if (judgeTaskValid() && progressDialog != null && progressDialog.isShowing()) {
				 progressDialog.dismiss();
				 progressDialog.cancel();
			 }
			r.setSuccess("false");
			r.setRequestError("获取数据出错");
			r.setErrmessage("服务器无响应!");
			isTaskSuccess = false;
		}
		return r;
	}

	@Override
	protected void onPostExecute(Result result) {
		// 任务执行后
		super.onPostExecute(result);
		 if (judgeTaskValid() && progressDialog != null) {
			 progressDialog.cancel();
		 }
		if (!isTaskSuccess) {
			showNormalError(result.getRequestError(), result.getErrmessage());
		} else {
			afterTask(result);
		}

	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	private final boolean judgeTaskValid() {
		Context mContext = ctx.get();
		return mContext != null && !((Activity) mContext).isFinishing() && !isCancelled();
	}
	
	

	public void showNormalError(String errorTitle, String errorContent) {
		Context mContext = ctx.get();
		if (mContext != null && errorTitle != null && errorContent != null) {
			AlertDialog.Builder build = new AlertDialog.Builder(mContext);
			build.setTitle(errorTitle)
					.setMessage(errorContent)
					.setIcon(R.drawable.alert3)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
									errorPositive();
								}
							});
			if (judgeTaskValid()) {
				build.show();
			}
		}

	}
	
	
	
	public void setmBaseBo(BaseBo mBaseBo) {
		this.mBaseBo = mBaseBo;
	}

	/**
	 * 自定义 任务前动作
	 */
	abstract void customPreExcuteDoing();
	
	/**
	 * 出现异常后 点击确定后的动作
	 */
	abstract void errorPositive();

	
	/**
	 * 实现类 做的动作
	 * 
	 * @param params
	 * @return
	 * @throws NetworkAvailableException
	 * @throws CallJSONException
	 */
	 final Result excuteTask(BaseBo... params)
			throws NetworkAvailableException, CallJSONException,CallServiceException{
		 	BaseBo bo = null;
		 	if(mBaseBo != null){
		 		bo = mBaseBo;
		 	}else{
		 		bo = (BaseBo) params[0];
		 	}
		 	Context mContext = ctx.get();
		 	if(mContext == null){
		 		return null;
		 	}
		 	BaseService mBaseService = new BaseService(mContext);
		 	Result mResult = mBaseService.callJSON(bo.maps, bo.requestUrl);

			return mResult;
	}

	abstract void afterTask(Result result);

}
