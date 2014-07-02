package com.axb.android.ui;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.shrek.base.util.rest.ZWAsyncTask;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.axb.android.R;
import com.axb.android.dto.BaseResult;
import com.axb.android.dto.User;
import com.axb.android.service.Command;
import com.axb.android.service.MyDialogTaskHandler;

public class LoginActivity extends BaseActivity {
	private EditText accountView, passwordView;
	private Button loginBtn;

	@Override
	protected void initialize() {
		// TODO Auto-generated method stub
		accountView = (EditText) findViewById(R.id.login_account);
		passwordView = (EditText) findViewById(R.id.login_pw);

		loginBtn = (Button) findViewById(R.id.login_btn);

		loginBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 登录
				String account = accountView.getText().toString();
				String pwStr = passwordView.getText().toString();
				// 用户登录
				if (account == null || account.length() <= 0) {
					Toast.makeText(LoginActivity.this, "账号不能为空!",
							Toast.LENGTH_LONG).show();
					return;
				}
				if (pwStr == null || pwStr.length() <= 0) {
					Toast.makeText(LoginActivity.this, "密码不能为空!",
							Toast.LENGTH_LONG).show();
					return;
				}

				mApplication.mSetting.account = account;
				mApplication.mSetting.password = pwStr;
				login(account, pwStr);

			}
		});
	}

	/**
	 * 登陆
	 */
	private void login(String name, String pw) {

		ZWAsyncTask.excuteTaskWithParas(LoginActivity.this,
				Command.getRestActionUrl(Command.COMMAND_LOGIN),
				 new TypeReference<BaseResult>(){}, new MyDialogTaskHandler<BaseResult>("请求登陆", "正在验证用户信息,请稍等..") {

					@Override
					public void minePostResult(BaseResult arg0) {
						// TODO Auto-generated method stub
						User mUser = JSON.parseObject(arg0.data, User.class);
						mApplication.mLoginUser = mUser;
						// mApplication.unReadMsg = unReadMsg;
						// mApplication.unReadTask = unReadTask;
						mApplication.mSetting.isAutoLogin = true;
						mApplication.mSetting.saveSettings(LoginActivity.this);

						Intent i = new Intent();
						i.setClass(LoginActivity.this, MainActivity.class);
						startActivity(i);
						finish();
					}

					
				},name,pw);
	}

	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			Intent i = new Intent();
			i.setClass(LoginActivity.this, SettingActivity.class);
			startActivity(i);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
