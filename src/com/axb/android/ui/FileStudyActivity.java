package com.axb.android.ui;

import java.util.Date;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;

import com.axb.android.R;
import com.axb.android.dto.BaseBo;
import com.axb.android.dto.CaseDto;
import com.axb.android.service.BaseAsyncTask;
import com.axb.android.service.Command;
import com.axb.android.service.FileTask;
import com.axb.android.service.GetCaseContentTask;
import com.axb.android.util.CommonUtil;

public class FileStudyActivity extends BaseActivity {
	private Button backBtn;
	private CaseDto mCaseDto; 
	private WebView mWebView;
	
//	private boolean isSelf;
	private int answerFlag;//跳转标识
	
	@Override
	protected void initialize(){
		// TODO Auto-generated method stub
//		setContentView(R.layout.filestudy);
		
		mCaseDto = (CaseDto)getIntent().getSerializableExtra("TaskDto");
		answerFlag = getIntent().getIntExtra("answerFlag", SelfListActivity.SELF_FLAG);
		init();
	}
	
	private void init(){
		backBtn = (Button)findViewById(R.id.fs_back);
		mWebView = (WebView)findViewById(R.id.fs_content);
		
		backBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onBackDoing();
			}
		});
		
		String content = mCaseDto.getContent(); 
		if(content != null){
			CommonUtil.webViewCommon(this, mWebView, content);
		}else{
			GetCaseContentTask mGetCaseContentTask = new GetCaseContentTask(FileStudyActivity.this, "获取内容", "正在获取文件内容,请稍等..", BaseAsyncTask.PRE_TASK_NORMAL);
			BaseBo mBaseBo = new BaseBo();
			mBaseBo.requestUrl = Command.getAction(Command.COMMAND_CASE_CONTENT);
			mBaseBo.maps.put("userName", mApplication.mSetting.account);
			mBaseBo.maps.put("password", mApplication.mSetting.password);
			mBaseBo.maps.put("caseGuid", mCaseDto.guid);
			mGetCaseContentTask.execute(mBaseBo);
		}
	}
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		try {
			mWebView.stopLoading();
		} catch (Exception e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
	}
	
	private void onBackDoing(){
//		if(CommonUtil.judgeIntAvild(mCaseDto.isFinish)){
//			finish();
//			return;
//		}
//		}
		
		AlertDialog.Builder builder = new AlertDialog.Builder(FileStudyActivity.this);
		builder.setTitle("任务完成");
		builder.setMessage("本文件学习是否完毕?");
		builder.setPositiveButton("完成", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				FileTask mFileTask = new FileTask(FileStudyActivity.this, "文件学习", "正在提交文件学习完成,请稍等..", BaseAsyncTask.PRE_TASK_CUSTOM);
				try {
					BaseBo mBaseBo = getTaskBaseBo();
					mFileTask.execute(mBaseBo);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		builder.setNegativeButton("未完成", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		builder.create().show();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			onBackDoing();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public void onTaskAfterDoing(){	
		if (answerFlag == SelfListActivity.SELF_FLAG) {
			// 自学
			SelfListActivity.isUpdateData = true;
			SelfMainActivity.isUpdateCount = true;
		} else if(answerFlag == SelfListActivity.NOTOK_FLAG){
			// 通知 界面更新
			MainActivity.isUpdateCount = true;
			// 通知未完任务更新
			UndoneTaskActivity.isUpdateData = true;
		} else if(answerFlag == SelfListActivity.DAILY_FLAG){
			//将每日一题设置已经学习
			mApplication.mLoginUser.noStudyDays = 0;
			mApplication.mLoginUser.lastStudyTime = new Date();
			mApplication.mLoginUser.dailyStudyCount++;
		}
		
		finish();
	}
	
	
	public void afterContentTaskDoing(String content){
		mCaseDto.setContent(content);
		CommonUtil.webViewCommon(this, mWebView, content);
	}
	
	/**
	 * 获取BaseBo
	 * @return
	 * @throws Exception 
	 */
	private BaseBo getTaskBaseBo() throws Exception {
		BaseBo mBaseBo = new BaseBo();
		mBaseBo.maps.put("userName",mApplication.mSetting.account);
		mBaseBo.maps.put("password",mApplication.mSetting.password);
		mBaseBo.maps.put("caseGuid",mCaseDto.guid);
		mBaseBo.maps.put("taskGuid",mCaseDto.taskGuid);
		switch (answerFlag) {
		
			case SelfListActivity.NOTOK_FLAG:
				//未完成任务
				mBaseBo.requestUrl = Command.getAction(Command.COMMAND_FILE);
				break;
			case SelfListActivity.SELF_FLAG:
				//自学
				mBaseBo.requestUrl = Command.getAction(Command.COMMAND_SELF_FILE);
				break;
			case SelfListActivity.DAILY_FLAG:
				//每日一题
				mBaseBo.requestUrl = Command.getAction(Command.COMMAND_DAILY_COMMIT);
				break;
			case SelfListActivity.ALJJ_FLAG:
				//案例借鉴
				mBaseBo.requestUrl = Command.getAction(Command.COMMAND_ALJJ_COMMIT);
				break;
			}
		return mBaseBo;
	}
}
