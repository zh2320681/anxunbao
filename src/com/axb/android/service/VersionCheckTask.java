package com.axb.android.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.axb.android.R;
import com.axb.android.dto.Result;
import com.axb.android.dto.Version;
import com.axb.android.util.CommonUtil;
import com.axb.android.util.Setting;
//import com.wei.util.common.CommonUtils;


public class VersionCheckTask extends BaseAsyncTask {
	boolean isShowNoUpdateInfo; // 是否在没有更新的清空下 弹出说明
	ProgressDialog progressDialog;
	Handler mHandler;

	public VersionCheckTask(Context ctx,String title,String info, boolean isShowNoUpdateInfo
			,Handler mHandler) {
		// TODO Auto-generated constructor stub
		super(ctx, title, info,PRE_TASK_DONOTHING);
		this.isShowNoUpdateInfo = isShowNoUpdateInfo;
		this.mHandler = mHandler;
	}

	@Override
	void errorPositive() {
		// TODO Auto-generated method stub

	}

	@Override
	void afterTask(Result result) {
		// TODO Auto-generated method stub
		Context context = ctx.get();
		if (context!=null && result.getSuccess() != null
				&& result.getSuccess().toLowerCase().equals("true")) {
			final Version v = JSON.parseObject(result.getData(), Version.class);
			int nowVersionId = CommonUtil.getVersionId(context);
			if (v != null) {
				if (v.version_id > nowVersionId) {
					AlertDialog.Builder build = new AlertDialog.Builder(context);
					build.setTitle("版本管理")
							.setMessage("发现新的版本号,是否更新？\n\n\t更新内容:\n\t"+v.remark+"\n")
							.setIcon(R.drawable.alert3)
							.setPositiveButton("更新",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											String downPath = Command
													.getDownLoadPath(v.fileName);
//											sendTaskToService(6, 2, downPath,
//													Command.getSavePath(), true,
//													true);
											//使用传统方法下载
											downFile(downPath,v.fileName);
										}
									});
					build.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

								}

							});
					build.show();
				} else {
					if (isShowNoUpdateInfo) {
						AlertDialog.Builder build = new AlertDialog.Builder(context);
						build.setTitle("版本管理")
								.setMessage("程序为最新版!")
								.setIcon(R.drawable.alert3)
								.setPositiveButton("确定",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {

											}
										});
						build.show();
					}
				}
			}
		} else {
			// showError(null, null);
		}
	}

//	private void sendTaskToService(int maxPoolSize, int taskThreadSize,
//			String downloadPath, String savePath, boolean isShowNotify,
//			boolean isAutoOpen) {
//		Intent intent = new Intent(ctx, DownloadService.class);
//		intent.putExtra(DownloadConstant.INTENT_SERVICE, DownloadConstant.INTENT_START_DOWNLOAD);
//		intent.putExtra(DownloadConstant.INTENT_MAXPOOLSIZE, maxPoolSize);
//		intent.putExtra(DownloadConstant.INTENT_TASKTHREADSIZE, taskThreadSize);
//		intent.putExtra(DownloadConstant.INTENT_DOWNLOADPATH, downloadPath);
//		intent.putExtra(DownloadConstant.INTENT_SAVEPATH, savePath);
//		intent.putExtra(DownloadConstant.INTENT_SHOWNOTIFY, isShowNotify);
//		intent.putExtra(DownloadConstant.INTENT_ISAUTOOPEN, isAutoOpen);
//		ctx.startService(intent);
//	}
	
	
	private void downFile(final String url,final String fileName) {
		final Context context;
		if(ctx!=null && (context = ctx.get())!=null){
			//检测sd卡
			if(!CommonUtil.isSdCardExist()){
				Toast.makeText(context, "检测到sd卡不存在，下载线程停止!", Toast.LENGTH_LONG).show();
				return;
			}
//			boolean isUserShutDonw = false;
			final Thread downThread = new Thread() {
				
				public void run() {
					HttpParams httpParams = new BasicHttpParams();
					HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
					HttpConnectionParams.setSoTimeout(httpParams, 10000);
					HttpClient client = new DefaultHttpClient(httpParams);
					HttpGet get = new HttpGet(url);
					HttpResponse response;
					try {
						response = client.execute(get);
						if (response.getStatusLine().getStatusCode() == 200) {

							HttpEntity entity = response.getEntity();
							final long length = entity.getContentLength();
							mHandler.post(new Runnable() {
								public void run() {
									progressDialog.setMax(Integer.valueOf(String.valueOf(length)));
								}
							});
						    Log.i("length:", length+"");
							InputStream is = entity.getContent();
							FileOutputStream fileOutputStream = null;
							if (is != null) {
								File file = new File(Setting.cacheDir,fileName);
								if (file.exists()) {
									file.delete();
									file.createNewFile();
								}
								fileOutputStream = new FileOutputStream(file);
								byte[] buf = new byte[5*1024];
								int ch = -1;
								int count = 0;
								while ((ch = is.read(buf)) != -1) {
									progressDialog.setProgress(count);
									try {
										Thread.sleep(15);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
									fileOutputStream.write(buf, 0, ch);
									count += ch;

								}
								fileOutputStream.flush();
								if (fileOutputStream != null) {
									fileOutputStream.close();
								}
								progressDialog.cancel();
								down(file);
							}
							

						}else{
							mHandler.post(new Runnable() {
								public void run() {
									progressDialog.cancel();
									Toast.makeText(context, "下载出现异常,请稍候再试!", Toast.LENGTH_LONG).show();
								}
							});
						}

					} catch (ClientProtocolException e) {
						e.printStackTrace();
						mHandler.post(new Runnable() {
							public void run() {
								progressDialog.cancel();
								Toast.makeText(context, "下载出现异常,请稍候再试!", Toast.LENGTH_LONG).show();
							}
						});
					} catch (Exception e) {
						e.printStackTrace();
						mHandler.post(new Runnable() {
							public void run() {
								progressDialog.cancel();
								Toast.makeText(context, "下载出现异常,请稍候再试!", Toast.LENGTH_LONG).show();
							}
						});
					}
				}

			};
			
			progressDialog = new ProgressDialog(context);
			progressDialog.setTitle("版本升级");
			progressDialog.setMessage("下载安装文件,请稍等...");
			progressDialog.setCancelable(true);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progressDialog.show();
			downThread.start();
		}
		
	}
	
	private void down(final File file) {
		mHandler.post(new Runnable() {
			public void run() {
				progressDialog.cancel();
				update(file);
			}
		});
	}

	private void update(File file) {	
		Context context = null;
		if(ctx!=null && (context = ctx.get())!=null){
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(
					Uri.fromFile(file),
					"application/vnd.android.package-archive");
			context.startActivity(intent);
		}
	}

	@Override
	void customPreExcuteDoing() {
		// TODO Auto-generated method stub
		
	}
}
