package com.axb.android.ui;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.axb.android.R;
import com.axb.android.dto.BaseBo;
import com.axb.android.dto.MessageDto;
import com.axb.android.service.BaseAsyncTask;
import com.axb.android.service.Command;
import com.axb.android.service.GetMsgContentTask;
import com.axb.android.service.SetMsgReadTask;
import com.axb.android.util.CommonUtil;

public class MessageInfoActivity extends BaseActivity {
	private MessageDto msg;
	private TextView titleView;
	public Button backBtn;
	private WebView contentWebView;
	private View requestLayout;
	private TextView requestInfo;
	
	@Override
	protected void initialize() {
		// TODO Auto-generated method stub
//		setContentView(R.layout.message_info);
		
		setResult(Activity.RESULT_CANCELED);
		
		msg = (MessageDto)getIntent().getSerializableExtra("MessageDto");
		
		titleView = (TextView)findViewById(R.id.mi_title);
//		titleView.setText(msg.title.length()>5?msg.title.subSequence(0, 4)+"...":msg.title);
		titleView.setText(msg.title);
		contentWebView = (WebView)findViewById(R.id.mi_content);
		
		requestLayout = findViewById(R.id.load_requestLayout);
		requestInfo = (TextView)findViewById(R.id.load_requestInfo);
		requestLayout.setVisibility(View.INVISIBLE);
		
		backBtn = (Button)findViewById(R.id.ml_back);
		
		
		addHandler();
		
		if(msg.content == null
				|| msg.content.equals("")){
			GetMsgContentTask mGetMsgContentTask = new GetMsgContentTask(this, "获取内容", "正在获取消息的内容,请稍等...", BaseAsyncTask.PRE_TASK_CUSTOM);
			BaseBo mBaseBo = new BaseBo();
			mBaseBo.requestUrl = Command.getAction(Command.COMMAND_MSG_CONTENT);
			mBaseBo.maps.put("userName", mApplication.mSetting.account);
			mBaseBo.maps.put("password", mApplication.mSetting.password); 
			mBaseBo.maps.put("msgGuid", msg.guid);
			mGetMsgContentTask.execute(mBaseBo);
		}else{
			CommonUtil.webViewCommon(this, contentWebView, msg.content);
			task();
		}
		
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		try {
			contentWebView.stopLoading();
		} catch (Exception e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
	}
	
	private void addHandler(){
		backBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	
	/**
	 * 显示请求时候 ui
	 * 
	 * @param info
	 */
	public void showRequestUi(String info) {
		requestInfo.setText(info);
		if (!requestLayout.isShown()) {
			requestLayout.setVisibility(View.VISIBLE);
			// requestView.setAnimation(AnimationUtils.loadAnimation(this,
			// R.anim.slide_right_in));
		}
	}

	/**
	 * 隐藏请求时候 ui
	 * 
	 * @param info
	 */
	public void hideRequestUi() {
		requestLayout.setVisibility(View.INVISIBLE);
	}
	
	/**
	 * 如果消息是未读  设置为已读
	 */
	private void task(){
		if(msg != null && msg.lookTime == 0){
			SetMsgReadTask mSetMsgReadTask = new SetMsgReadTask(this, "设置消息已读", "正在设置消息为已读,请稍等...", BaseAsyncTask.PRE_TASK_CUSTOM);
			BaseBo mBaseBo = new BaseBo();
			mBaseBo.requestUrl = Command.getAction(Command.COMMAND_MSG_READ);
			mBaseBo.maps.put("userName", mApplication.mSetting.account);
			mBaseBo.maps.put("password", mApplication.mSetting.password); 
			mBaseBo.maps.put("msgGuid", msg.guid);
			mSetMsgReadTask.execute(mBaseBo);
		}
	}
	
	public void afterContentTaskDoing(String content){
		msg.content = content;
		CommonUtil.webViewCommon(this, contentWebView, msg.content);
		task();
	}
	
}
