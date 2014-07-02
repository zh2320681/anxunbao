package com.axb.android.ui;

import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.shrek.base.util.rest.ZWAsyncTask;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.axb.android.R;
import com.axb.android.dto.AnLiJianShang;
import com.axb.android.dto.BaseResult;
import com.axb.android.dto.CaseDto;
import com.axb.android.service.Command;
import com.axb.android.service.MyDialogTaskHandler;

public class CaseReferenceActivity extends BaseActivity {
	private Button backBtn;
	private LinearLayout contentView;

	private View requestLayout;
	private TextView requestInfo;

	@Override
	protected void initialize() {
		// setContentView(R.layout.case_reference);
		init();
		initClick();

		requestCaseReference();
	}

	/**
	 * 初始化布局
	 */
	private void init() {
		backBtn = (Button) findViewById(R.id.csfe_back);
		contentView = (LinearLayout) findViewById(R.id.csfe_contentView);

		requestLayout = findViewById(R.id.load_requestLayout);
		requestInfo = (TextView) findViewById(R.id.load_requestInfo);
		requestLayout.setVisibility(View.GONE);
	}

	private void initClick() {
		// TODO Auto-generated method stub
		backBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
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
		}
	}

	/**
	 * 隐藏请求时候 ui
	 * 
	 * @param info
	 */
	public void hideRequestUi() {
		requestLayout.setVisibility(View.GONE);
	}

	/**
	 * 请求案例
	 */
	private void requestCaseReference() {
		ZWAsyncTask
				.excuteTaskWithParas(
						CaseReferenceActivity.this,
						Command.getRestActionUrl(Command.COMMAND_CASE_REFERENCE),
						new TypeReference<BaseResult>() {
						},
						new MyDialogTaskHandler<BaseResult>("请求案例信息",
								"正在请求案例列表,请稍等..") {
							@Override
							public void minePostResult(BaseResult arg0) {
								// TODO Auto-generated method stub
								List<AnLiJianShang> caseRefList = JSON
										.parseArray(arg0.data,
												AnLiJianShang.class);
								taskSuccessDoing(caseRefList);
							}
						}, mApplication.mSetting.account,
						mApplication.mSetting.password);
	}

	/**
	 * 请求成功后 Action
	 * 
	 * @param caseRefList
	 */
	private void taskSuccessDoing(List<AnLiJianShang> caseRefList) {
		// TODO Auto-generated method stub
		contentView.removeAllViews();
		LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		llp.gravity = Gravity.CENTER;
		llp.bottomMargin = 0;
		llp.topMargin = 10;
		llp.leftMargin = 5;
		llp.rightMargin = 5;

		for (final AnLiJianShang caseRef : caseRefList) {
			View view = LayoutInflater.from(CaseReferenceActivity.this)
					.inflate(R.layout.case_reference_item, null);
			TextView time = (TextView) view.findViewById(R.id.csrfi_time);
			TextView title = (TextView) view.findViewById(R.id.csrfi_title);
			LinearLayout itemContentView = (LinearLayout) view
					.findViewById(R.id.csrfi_itemContentView);
			time.setText(caseRef.rq);
			title.setText(caseRef.title);
			int i = 0;
			for (final CaseDto caseDto : caseRef.allCases) {
				i++;
				View itemView = LayoutInflater.from(CaseReferenceActivity.this)
						.inflate(R.layout.case_reference_itemlist, null);
				final TextView itemName = (TextView) itemView
						.findViewById(R.id.csrfil_name);
				final TextView itemDetail = (TextView) itemView
						.findViewById(R.id.csrfil_detail);

				if (caseDto.studyTime != 0) {
					itemName.setTextColor(Color.GRAY);
					itemDetail.setTextColor(Color.GRAY);
				}
				View line = itemView.findViewById(R.id.csrfil_line);
				if (i == caseRef.allCases.size()) {
					line.setVisibility(View.GONE);
				}
				itemName.setText("借鉴案例" + i);
				itemDetail.setText(caseDto.title);
				itemView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent();
						intent.setClass(CaseReferenceActivity.this,
								CaseStudyActivity.class);
						intent.putExtra("TaskDto", caseDto);
						intent.putExtra("answerFlag",
								SelfListActivity.ALJJ_FLAG);
						intent.putExtra("aljjGuid", caseRef.guid);
						startActivity(intent);

						caseDto.studyTime = new Date().getTime();
						itemName.setTextColor(Color.GRAY);
						itemDetail.setTextColor(Color.GRAY);
						MainActivity.isUpdateCount = true;
					}
				});
				itemContentView.addView(itemView);
			}
			view.setLayoutParams(llp);
			contentView.addView(view);
		}
	}
}
