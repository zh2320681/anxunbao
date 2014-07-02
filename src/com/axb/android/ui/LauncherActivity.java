package com.axb.android.ui;

import android.content.Intent;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.Toast;

import com.axb.android.R;
import com.axb.android.dto.BaseBo;
import com.axb.android.dto.User;
import com.axb.android.service.BaseAsyncTask;
import com.axb.android.service.Command;
import com.axb.android.service.LoginTask;

public class LauncherActivity extends BaseActivity {
	private ImageView logoView;

	@Override
	protected void initialize() {
		// TODO Auto-generated method stub
//		setContentView(R.layout.launcher);
		logoView = (ImageView) findViewById(R.id.laucher_img);

		// 如果上次没有正常退出 下次自动登录
		startAnim();
	}

	private void startAnim() {
		// AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
		// aa.setDuration(3000);
		// logoView.startAnimation(aa);
		// aa.setAnimationListener(new AnimationListener() {
		// @Override
		// public void onAnimationStart(Animation animation) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public void onAnimationRepeat(Animation animation) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public void onAnimationEnd(Animation animation) {
		// // TODO Auto-generated method stub
		// Intent i = new Intent();
		// i.setClass(LaucherActivity.this, LoginActivity.class);
		// startActivity(i);
		// finish();
		// }
		// });

		Handler mHandler = new Handler();
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (mApplication.mSetting.isAutoLogin) {
					login(mApplication.mSetting.account, mApplication.mSetting.password);
				}else{
					Intent i = new Intent();
					i.setClass(LauncherActivity.this, LoginActivity.class);
					startActivity(i);
					finish();
				}
			}
		}, 3000);
	}
	
	
	/**
	 * 登陆
	 */
	private void login(String name,String pw){
		BaseBo mBaseBo = new BaseBo();
		mBaseBo.requestUrl = Command.getAction(Command.COMMAND_LOGIN);
		mBaseBo.maps.put("userName", name);
		mBaseBo.maps.put("password", pw);
		LoginTask mLoginTask = new LoginTask(this, "请求登陆", "正在验证用户信息,请稍等..", BaseAsyncTask.PRE_TASK_NORMAL);
		mLoginTask.execute(mBaseBo);
	}
	
	
	/**
	 * 登陆后任务
	 */
	public void loginTaskSuccessDoing(User mUser){
		if(mApplication.mSetting.isAutoLogin){
			Toast.makeText(this, "自动登录成功,如想切换用户请正常退出!", Toast.LENGTH_LONG).show();
		}else{
			Toast.makeText(this, "登录成功", Toast.LENGTH_LONG).show();
		}
		mApplication.mLoginUser = mUser;
		mApplication.mSetting.isAutoLogin = true;
		mApplication.mSetting.saveSettings(this);
		
		
		Intent i = new Intent();
		i.setClass(this, MainActivity.class);
		startActivity(i);
		finish();
	}
}
