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
	
	private BaseBo mBaseBo; //��������� �����Դ�

	
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
		//����ִ��ǰ
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
			// �ƶ��������ӱ�����
			 if (judgeTaskValid() && progressDialog != null && progressDialog.isShowing()) {
				 progressDialog.dismiss();
				 progressDialog.cancel();
			 }
			r.setSuccess("false");
			r.setRequestError("�������������쳣");
			r.setErrmessage("�������������쳣,��������~~");
			isTaskSuccess = false;
		} catch (CallJSONException e) {
			// ��ȡjson���ݳ���
			 if (judgeTaskValid() && progressDialog != null && progressDialog.isShowing()) {
				 progressDialog.dismiss();
				 progressDialog.cancel();
			 }
			// Log.e(TAG, e.getMessage());
			r.setSuccess("false");
			r.setRequestError("��ȡ���ݳ���");
			r.setErrmessage("��������������ݳ�������");
			isTaskSuccess = false;
		} catch (CallServiceException e) {
			// TODO: handle exception
			// ��ȡjson���ݳ���
			 if (judgeTaskValid() && progressDialog != null && progressDialog.isShowing()) {
				 progressDialog.dismiss();
				 progressDialog.cancel();
			 }
			r.setSuccess("false");
			r.setRequestError("��ȡ���ݳ���");
			r.setErrmessage("����������Ӧ!");
			isTaskSuccess = false;
		}
		return r;
	}

	@Override
	protected void onPostExecute(Result result) {
		// ����ִ�к�
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
					.setPositiveButton("ȷ��",
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
	 * �Զ��� ����ǰ����
	 */
	abstract void customPreExcuteDoing();
	
	/**
	 * �����쳣�� ���ȷ����Ķ���
	 */
	abstract void errorPositive();

	
	/**
	 * ʵ���� ���Ķ���
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
