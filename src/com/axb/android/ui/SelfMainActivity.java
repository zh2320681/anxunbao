package com.axb.android.ui;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import cn.shrek.base.util.rest.ZWAsyncTask;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.axb.android.R;
import com.axb.android.dto.BaseResult;
import com.axb.android.dto.SelfstudyNum;
import com.axb.android.service.Command;
import com.axb.android.service.MyDialogTaskHandler;
import com.axb.android.util.CommonUtil;

public class SelfMainActivity extends BaseActivity {
	public static boolean isUpdateCount;//是否更新count
	
	private View caseView,angui1View,angui2View,angui3View,fileView;
	private Button backBtn;
	
	private TextView caseNum,angui1LineNum,angui2BdNum,angui3JjNum,fileNum;
	
	private View requestLayout;
	private TextView requestInfo;
	
	private OnClickListener myClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(v == caseView){
				Intent i = new Intent();
				i.setClass(SelfMainActivity.this, SelfListActivity.class);
				i.putExtra("taskFlag", SelfListActivity.FLAG_CASE);
				startActivity(i);
			}else if(v == angui1View){
				Intent i = new Intent();
				i.setClass(SelfMainActivity.this, SelfListActivity.class);
				i.putExtra("taskFlag", SelfListActivity.FLAG_ANGUI);
				i.putExtra("anguiFlag", 1);
				startActivity(i);
			}else if(v == angui2View){
				Intent i = new Intent();
				i.setClass(SelfMainActivity.this, SelfListActivity.class);
				i.putExtra("taskFlag", SelfListActivity.FLAG_ANGUI);
				i.putExtra("anguiFlag", 2);
				startActivity(i);
			}else if(v == angui3View){
				Intent i = new Intent();
				i.setClass(SelfMainActivity.this, SelfListActivity.class);
				i.putExtra("taskFlag", SelfListActivity.FLAG_ANGUI);
				i.putExtra("anguiFlag", 3);
				startActivity(i);
			}else if(v == fileView){
				Intent i = new Intent();
				i.setClass(SelfMainActivity.this, SelfListActivity.class);
				i.putExtra("taskFlag", SelfListActivity.FLAG_FILE);
				startActivity(i);
			}else if(v == backBtn){
				finish();
			}
		}
	};
	
	
	@Override
	protected void initialize() {
		init();
		isUpdateCount = true;
	}
	private void init(){
		caseView = findViewById(R.id.sm_case);
		angui1View = findViewById(R.id.sm_angui1);
		angui2View = findViewById(R.id.sm_angui2);
		angui3View = findViewById(R.id.sm_angui3);
		fileView = findViewById(R.id.sm_file);
		
		caseNum = (TextView) findViewById(R.id.sm_caseNum);
		angui1LineNum = (TextView) findViewById(R.id.sm_angui1LineNum);
		angui2BdNum = (TextView) findViewById(R.id.sm_angui2BdNum);
		angui3JjNum = (TextView) findViewById(R.id.sm_angui3JjNum);
		fileNum = (TextView) findViewById(R.id.sm_fileNum);
		
		
		requestLayout = findViewById(R.id.load_requestLayout);
		requestInfo = (TextView)findViewById(R.id.load_requestInfo);
		requestLayout.setVisibility(View.GONE);
		
		backBtn = (Button)findViewById(R.id.sm_back);
		
		caseView.setOnClickListener(myClick);
		angui1View.setOnClickListener(myClick);
		angui2View.setOnClickListener(myClick);
		angui3View.setOnClickListener(myClick);
		fileView.setOnClickListener(myClick);
		backBtn.setOnClickListener(myClick);
	}
	

	/**
	 * 请求学习数量
	 */
	private void initDataNum() {
		
		ZWAsyncTask.excuteTaskWithParas(SelfMainActivity.this,
				Command.getRestActionUrl(Command.COMMAND_SELFSTUDY_NUM),
				new TypeReference<BaseResult>(){}, new MyDialogTaskHandler<BaseResult>("请求自学数信息", "正在请求自学信息,请稍等..") {

					@Override
					public void minePostResult(BaseResult arg0) {
						// TODO Auto-generated method stub
						SelfstudyNum mSelfstudyNum = JSON.parseObject(arg0.data, SelfstudyNum.class);
						
						String anli = "已自学了"+mSelfstudyNum.anli_count+"个案例";
						String angui_xl = "已自学了"+mSelfstudyNum.angui_xl_count+"个安规";
						String angui_bd = "已自学了"+mSelfstudyNum.angui_bd_count+"个安规";
						String angui_jj = "已自学了"+mSelfstudyNum.angui_jj_count+"个安规";
						String file = "已自学了"+mSelfstudyNum.wenjian_count+"个文件";
						
						caseNum.setText(CommonUtil.changeTextColor(anli,4,anli.length()-3));
						angui1LineNum.setText(CommonUtil.changeTextColor(angui_xl,4,anli.length()-3));
						angui2BdNum.setText(CommonUtil.changeTextColor(angui_bd,4,anli.length()-3));
						angui3JjNum.setText(CommonUtil.changeTextColor(angui_jj,4,anli.length()-3));
						fileNum.setText(CommonUtil.changeTextColor(file,4,file.length()-3));
					}
				},mApplication.mSetting.account,mApplication.mSetting.password);
	}
	/**
	 * 显示请求时候 ui
	 * @param info
	 */
	public void showRequestUi(String info) {
		requestInfo.setText(info);
		if (!requestLayout.isShown()) {
			requestLayout.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 隐藏请求时候 ui
	 * @param info
	 */
	public void hideRequestUi() {
		requestLayout.setVisibility(View.GONE);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(isUpdateCount){
			isUpdateCount = false;
			initDataNum();
		}
	}
}
