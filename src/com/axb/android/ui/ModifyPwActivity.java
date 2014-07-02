package com.axb.android.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.axb.android.R;
import com.axb.android.dto.BaseBo;
import com.axb.android.service.BaseAsyncTask;
import com.axb.android.service.Command;
import com.axb.android.service.ModifyPasswordTask;

public class ModifyPwActivity extends BaseActivity {
	private EditText oldView,newView,new1View;
	private Button backBtn,saveBtn,czBtn;
	private String requestPwStr;
	
	private OnClickListener mClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v == backBtn){
				finish();
			}else if(v == saveBtn){
				String oldStr = oldView.getText().toString();
				if(oldStr == null || oldStr.equals("")){
					Toast.makeText(ModifyPwActivity.this, "请输入旧密码!", Toast.LENGTH_SHORT).show();
					return;
				}
				
				String newStr = newView.getText().toString();
				if(newStr == null || newStr.equals("")){
					Toast.makeText(ModifyPwActivity.this, "请输入新密码!", Toast.LENGTH_SHORT).show();
					return;
				}
				
				String newStr1 = new1View.getText().toString();
				if(newStr1 == null || newStr1.equals("")){
					Toast.makeText(ModifyPwActivity.this, "请再一次输入新密码!", Toast.LENGTH_SHORT).show();
					return;
				}
				
				if(!newStr.equals(newStr1)){
					Toast.makeText(ModifyPwActivity.this, "两次输入的新密码不一致!", Toast.LENGTH_SHORT).show();
					newView.setText(null);
					new1View.setText(null);
					return;
				}
				
				if(!oldStr.equals(mApplication.mLoginUser.password)){
					Toast.makeText(ModifyPwActivity.this, "原密码输入错误!", Toast.LENGTH_SHORT).show();	
					oldView.setText(null);
					return;
				}
				
				if(newStr.equals(oldStr)){
					Toast.makeText(ModifyPwActivity.this, "新密码和旧密码一样!", Toast.LENGTH_SHORT).show();	
					newView.setText(null);
					new1View.setText(null);
					return;
				}
				
				ModifyPasswordTask mModifyPasswordTask = new ModifyPasswordTask(ModifyPwActivity.this, "修改密码", "正在请求修改密码,请稍等...", BaseAsyncTask.PRE_TASK_NORMAL);
				BaseBo mBaseBo = new BaseBo();
				mBaseBo.requestUrl = Command.getAction(Command.COMMAND_MODIFY);
				requestPwStr = newStr;
				mBaseBo.maps.put("newPw", newStr);
				mBaseBo.maps.put("userName", mApplication.mSetting.account);
				mBaseBo.maps.put("password", mApplication.mSetting.password);
				mModifyPasswordTask.execute(mBaseBo);
			}else if(v == czBtn){
				oldView.setText(null);
				newView.setText(null);
				new1View.setText(null);
			}
		}
	};
	
	@Override
	protected void initialize() {
		// TODO Auto-generated method stub
//		setContentView(R.layout.modify);
		
		init();
		addHandler();
	}
	
	
	private void init(){
		oldView = (EditText)findViewById(R.id.modify_old);
		newView = (EditText)findViewById(R.id.modify_new);
		new1View = (EditText)findViewById(R.id.modify_new1);
		
		backBtn =  (Button)findViewById(R.id.modify_back);
		saveBtn =  (Button)findViewById(R.id.modify_save);
		czBtn =  (Button)findViewById(R.id.modify_cz);
	}
	
	private void addHandler(){
		backBtn.setOnClickListener(mClick);
		saveBtn.setOnClickListener(mClick);
		czBtn.setOnClickListener(mClick);
		
	}


	public void afterModifyTaskDoing() {
		// TODO Auto-generated method stub
		/**
		 * 弹出修改密码成功
		 * 修改缓存 密码设置
		 * 本地缓存密码 更改  要求下次登录
		 */
//		Toast.makeText(ModifyPwActivity.this, "密码修改成功!", Toast.LENGTH_SHORT).show();
		AlertDialog.Builder build = new AlertDialog.Builder(this);
		build.setTitle("密码修改")
				.setMessage("密码修改成功!")
				.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								
							}
						});
		build.create().show();
//		String newStr = newView.getText().toString();
		oldView.setText(null);
		newView.setText(null);
		new1View.setText(null);
		mApplication.mLoginUser.password = requestPwStr;
		
		mApplication.mSetting.isAutoLogin = false;
		mApplication.mSetting.password=requestPwStr;
		mApplication.mSetting.saveSettings(this);
	}
	
}
