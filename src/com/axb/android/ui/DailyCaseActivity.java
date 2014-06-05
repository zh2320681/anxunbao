package com.axb.android.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.axb.android.R;
import com.axb.android.dto.User;
import com.axb.android.util.CommonUtil;

public class DailyCaseActivity extends BaseActivity {

	private Button backBtn;
	private TextView timeView, detailView;
	private LinearLayout dailyCase;

	private OnClickListener mClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.dc_back:
				finish();
				break;
			case R.id.dc_dailyCase:
				// һ��
				Intent intent = new Intent();
				int flag = mApplication.mLoginUser.dailyCase.taskFlag;
				if (flag == 1) {
					// ����
					intent.setClass(DailyCaseActivity.this,
							CaseStudyActivity.class);
				} else if (flag == 2) {
					// ����
					intent.setClass(DailyCaseActivity.this,
							AnguiStudyActivity.class);
				} else if (flag == 3) {
					// �ļ�
					intent.setClass(DailyCaseActivity.this,
							FileStudyActivity.class);
				}
				intent.putExtra("answerFlag", SelfListActivity.DAILY_FLAG);
				intent.putExtra("TaskDto", mApplication.mLoginUser.dailyCase);
				startActivity(intent);
				break;
			}

		}
	};

	@Override
	protected void initialize() {

		backBtn = (Button) findViewById(R.id.dc_back);
		timeView = (TextView) findViewById(R.id.dc_time);
		detailView = (TextView) findViewById(R.id.dc_detail);
		dailyCase = (LinearLayout) findViewById(R.id.dc_dailyCase);

	}

	@Override
	protected void addListener() {
		backBtn.setOnClickListener(mClick);
		dailyCase.setOnClickListener(mClick);
	}

	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(mApplication.mLoginUser ==null
				|| mApplication.mLoginUser.dailyCase == null){
			AlertDialog.Builder build = new AlertDialog.Builder(this);
			build.setTitle("רҵ��Ϣ�ύ").setMessage("�޷��������רҵ�ҵ�ÿ��һ��!")
					.setIcon(R.drawable.alert3)
					.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							finish();
						}
					}).create();
			build.show();
		}else{
			setCaseInfo();
		}
		
	}
	/**
	 * ����ÿ��һ���ֵ
	 */
	private void setCaseInfo() {
		if (mApplication.mLoginUser.lastStudyTime == null) {
			timeView.setText("�㻹û��ѧϰ��Ŷ!");
		} else {
			String studyTime = "�Ѿ�����ѧϰ"
					+ mApplication.mLoginUser.dailyStudyCount + "��,";
			String noStudyTime = "����" + mApplication.mLoginUser.noStudyDays
					+ "��δѧϰ";
			timeView.setText(CommonUtil.changeTextColor(
					studyTime + noStudyTime, studyTime.length() + 2,
					studyTime.length() + noStudyTime.length() - 4));
		}

		detailView.setText(mApplication.mLoginUser.dailyCase.title);
	}
}
