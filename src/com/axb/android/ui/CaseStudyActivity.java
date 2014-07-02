package com.axb.android.ui;

import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.axb.android.R;
import com.axb.android.dto.BaseBo;
import com.axb.android.dto.CaseDto;
import com.axb.android.dto.CaseQuestion;
import com.axb.android.service.BaseAsyncTask;
import com.axb.android.service.Command;
import com.axb.android.service.CommitCaseAnswerTask;
import com.axb.android.service.GetCaseContentTask;
import com.axb.android.service.GetCaseQuestionsTask;
import com.axb.android.service.SaveCaseAnswerTask;
import com.axb.android.util.CommonUtil;
import com.axb.android.util.LoadPaperUtil;

public class CaseStudyActivity extends BaseActivity {
	private static final int CASE_INDEX = 0x01;
	private static final int ANALYSIS_INDEX = 0x02;
	private static final int ANSWER_INDEX = 0x03;
	
	private int selectIndex;

//	private boolean isSelf; // 是否是自学
	private int answerFlag;//跳转标识
	private String aljjGuid;

	private View caseLayout, analysisLayout, answerLayout, commitLayout;
	private Button backBtn, caseeBtn, analysisBtn, answerBtn, commitBtn,
			saveBtn;
	private WebView caseShowView;

	private View requestLayout;
	private TextView requestInfo;
	private LinearLayout questionLayout;

	private CaseDto mCaseDto;
	private WeakReference<LayoutInflater> mInflaterCache;

	private boolean isHasLoadAnswer = false, isHasLoadQuestion = false;
	/**
	 * 答案
	 */
	private LinearLayout answerContentLayout;
	private View answerImg;

	private OnClickListener myClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v == backBtn) {
				finish();
			} else if (v == caseeBtn) {
				setBtnSelectByIndex(CASE_INDEX);
			} else if (v == analysisBtn) {
				setBtnSelectByIndex(ANALYSIS_INDEX);
			} else if (v == answerBtn) {
				setBtnSelectByIndex(ANSWER_INDEX);
			} else if (v == commitBtn) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						CaseStudyActivity.this);
				builder.setTitle("提交答案");
				builder.setMessage("是否提交答案?");
				builder.setPositiveButton("提交",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								try {
									CommitCaseAnswerTask mCommitCaseAnswerTask = new CommitCaseAnswerTask(CaseStudyActivity.this, "提交答案","正在提交答案,请稍等..",BaseAsyncTask.PRE_TASK_CUSTOM);
									BaseBo mBaseBo = getTaskBaseBo(true);
									mCommitCaseAnswerTask.execute(mBaseBo);
								} catch (Exception e) {
									Toast.makeText(CaseStudyActivity.this,"你还有题目没有做,不能提交!", Toast.LENGTH_LONG).show();
								}
							}

						});

				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
							}
						});
				builder.create().show();
				return;
			} else if (v == saveBtn) {
				try {
					SaveCaseAnswerTask mSaveCaseAnswerTask = new SaveCaseAnswerTask(
							CaseStudyActivity.this, "保存答案", "正在保存答案,请稍等..",
							BaseAsyncTask.PRE_TASK_CUSTOM);
					BaseBo mBaseBo = getTaskBaseBo(false);
					mSaveCaseAnswerTask.execute(mBaseBo);
				} catch (Exception e) {
//					 Toast.makeText(CaseStudyActivity.this, "你还有题目没有做,不能提交!",Toast.LENGTH_LONG).show();
				}
				return;
			}
		}
	};

	@Override
	protected void initialize() {
		// TODO Auto-generated method stub
//		setContentView(R.layout.casestudy);

		mCaseDto = (CaseDto) getIntent().getSerializableExtra("TaskDto");
		answerFlag = getIntent().getIntExtra("answerFlag", SelfListActivity.SELF_FLAG);
		
		try {
			aljjGuid = getIntent().getStringExtra("aljjGuid");
		} catch (Exception e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
		init();
		addHandler();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		try {
			caseShowView.stopLoading();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
	}

	private void init() {
		caseLayout = findViewById(R.id.cs_case);
		analysisLayout = findViewById(R.id.cs_analysisLayout);
		answerLayout = findViewById(R.id.cs_answerLayout);

		backBtn = (Button) findViewById(R.id.cs_back);
		caseeBtn = (Button) findViewById(R.id.cs_caseBtn);
		analysisBtn = (Button) findViewById(R.id.cs_analysisBtn);
		answerBtn = (Button) findViewById(R.id.cs_answerBtn);
		commitBtn = (Button) findViewById(R.id.cs_commit);
		saveBtn = (Button) findViewById(R.id.cs_save);

		commitLayout = findViewById(R.id.cs_commitLayout);

		caseShowView = (WebView) findViewById(R.id.cs_case);
		
		String content = mCaseDto.getContent();
		
		if (content != null) {
			CommonUtil.webViewCommon(this, caseShowView, content);
		}

		requestLayout = findViewById(R.id.load_requestLayout);
		requestInfo = (TextView) findViewById(R.id.load_requestInfo);
		requestLayout.setVisibility(View.GONE);

		questionLayout = (LinearLayout) findViewById(R.id.cs_questionLayout);

		answerContentLayout = (LinearLayout) findViewById(R.id.cs_answerContent);
		answerImg = findViewById(R.id.cs_answerImg);

		setBtnSelectByIndex(CASE_INDEX);
	}

	private void addHandler() {
		backBtn.setOnClickListener(myClick);
		caseeBtn.setOnClickListener(myClick);
		analysisBtn.setOnClickListener(myClick);
		answerBtn.setOnClickListener(myClick);
		commitBtn.setOnClickListener(myClick);
		
		switch (answerFlag) {
			case 0:
			case 1:
				saveBtn.setVisibility(View.VISIBLE);	
				saveBtn.setOnClickListener(myClick);
				break;
			case 2:
			case 3:
				saveBtn.setVisibility(View.GONE);
				break;
			}
	}

	private void setBtnSelectByIndex(int index) {
		if (selectIndex == index) {
			return;
		}
		int old = selectIndex;
		selectIndex = index;
		switch (old) {
		case CASE_INDEX:
			caseeBtn.setBackgroundResource(R.drawable.down_nor);
			caseeBtn.setTextColor(getResources().getColorStateList(R.color.undone_text1));
			caseLayout.setVisibility(View.GONE);
			break;
		case ANALYSIS_INDEX:
			analysisBtn.setBackgroundResource(R.drawable.down_nor);
			analysisBtn.setTextColor(getResources().getColorStateList(R.color.undone_text1));
			analysisLayout.setVisibility(View.GONE);
			break;
		case ANSWER_INDEX:
			answerBtn.setBackgroundResource(R.drawable.down_nor);
			answerBtn.setTextColor(getResources().getColorStateList(
					R.color.undone_text1));
			answerLayout.setVisibility(View.GONE);
			break;
		}

		switch (selectIndex) {
		case CASE_INDEX:
			caseeBtn.setBackgroundResource(R.drawable.down_pre);
			caseeBtn.setTextColor(Color.WHITE);
			caseLayout.setVisibility(View.VISIBLE);

			String content = mCaseDto.getContent();
			if (content == null) {
				GetCaseContentTask mGetCaseContentTask = new GetCaseContentTask(
						CaseStudyActivity.this, "获取内容", "正在获取案例内容,请稍等..",
						BaseAsyncTask.PRE_TASK_NORMAL);
				BaseBo mBaseBo = new BaseBo();
				mBaseBo.requestUrl = Command.getAction(Command.COMMAND_CASE_CONTENT);
				mBaseBo.maps.put("userName", mApplication.mSetting.account);
				mBaseBo.maps.put("password", mApplication.mSetting.password);
				mBaseBo.maps.put("caseGuid", mCaseDto.guid);
				mBaseBo.maps.put("answerFlag", answerFlag+"");
				if(aljjGuid!=null){
					mBaseBo.maps.put("aljjGuid", aljjGuid);
				}
				mGetCaseContentTask.execute(mBaseBo);
			}
			break;
		case ANALYSIS_INDEX:
			analysisBtn.setBackgroundResource(R.drawable.down_pre);
			analysisBtn.setTextColor(Color.WHITE);
			analysisLayout.setVisibility(View.VISIBLE);
//			if (CommonUtil.judgeIntAvild(mCaseDto.isFinish)) {
//				// 如果已经完成 不可以提交
//				commitLayout.setVisibility(View.GONE);
//			} else {
			commitLayout.setVisibility(View.VISIBLE);
//			}
			getCaseQuestion();
			break;
		case ANSWER_INDEX:
			answerBtn.setBackgroundResource(R.drawable.down_pre);
			answerBtn.setTextColor(Color.WHITE);
			answerLayout.setVisibility(View.VISIBLE);
//			 if (!isHasLoadAnswer) {
//			if (CommonUtil.judgeIntAvild(mCaseDto.isFinish)) {
				// 如果已经完成 可以看答案
				answerContentLayout.setVisibility(View.VISIBLE);
				answerImg.setVisibility(View.GONE);

				getCaseQuestion();
				//
				// LayoutInflater mInflater = getInflater();
				//
				// for (CaseQuestion mCaseQuestion : mCaseDto.allCaseQuestion) {
				// int count = answerContentLayout.getChildCount() + 1;
				// TextView answerItemView = (TextView) mInflater.inflate(
				// R.layout.answer_item, null);
				// answerItemView.setText(count + "、 "
				// + mCaseQuestion.answer);
				// answerContentLayout.addView(answerItemView);
				// }
				// isHasLoadAnswer = true;
//			}
//			 else {
//			 answerContentLayout.setVisibility(View.GONE);
//			 answerImg.setVisibility(View.VISIBLE);
//			 }
//			 }

			break;
		}
	}

	/**
	 * 得到答案
	 */
	private void getCaseQuestion() {
		// !CommonUtil.judgeIntAvild(mCaseDto.isFinish)
		if (mCaseDto != null && mCaseDto.allCaseQuestion == null) {
			GetCaseQuestionsTask mGetCaseQuestionsTask = new GetCaseQuestionsTask(
					this, "获取分析题目", "正在设获取分析题目,请稍等...",
					BaseAsyncTask.PRE_TASK_CUSTOM);
			BaseBo mBaseBo = new BaseBo();
			mBaseBo.requestUrl = Command.getAction(Command.COMMAND_QUESTIONS);
			mBaseBo.maps.put("userName", mApplication.mSetting.account);
			mBaseBo.maps.put("password", mApplication.mSetting.password);
			mBaseBo.maps.put("caseGuid", mCaseDto.guid);
			mBaseBo.maps.put("taskGuid", mCaseDto.taskGuid);
			mBaseBo.maps.put("answerFlag",answerFlag+"");
			mGetCaseQuestionsTask.execute(mBaseBo);
		} else {
			loadCurrentView();
		}
	}

	// private void caseFinishSwitch(){
	// if(CommonUtil.judgeIntAvild(mCaseDto.isFinish)){
	// //如果已经完成
	//
	// }else{
	//
	// }
	// }

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
		requestLayout.setVisibility(View.GONE);
	}

	public void afterTaskDoing(List<CaseQuestion> data) {
		mCaseDto.allCaseQuestion = data;
		// 加载界面
		loadCurrentView();
	}

	public void afterContentTaskDoing(String content) {
		mCaseDto.setContent(content);
		CommonUtil.webViewCommon(this, caseShowView, content);
	}

	/**
	 * 加载答案页
	 */
	private void loadCurrentView() {
		if (selectIndex == ANALYSIS_INDEX) {
			if (!isHasLoadQuestion) {
				LoadPaperUtil.loadPaperQuestions(this, getInflater(), mCaseDto,
						questionLayout, false);
				isHasLoadQuestion = true;
			}
		} else {
			if (!isHasLoadAnswer) {
				LayoutInflater mInflater = getInflater();

				for (CaseQuestion mCaseQuestion : mCaseDto.allCaseQuestion) {
					int count = answerContentLayout.getChildCount() / 2 + 1;
					TextView answerItemView = (TextView) mInflater.inflate(
							R.layout.answer_item, null);
					answerItemView.setText(count + "、 "
							+ mCaseQuestion.questionname);

					TextView answerItemView1 = (TextView) mInflater.inflate(
							R.layout.answer_item, null);
					// answerItemView1.setText("答案:  "+mCaseQuestion.answer);
					answerItemView1.setText(mCaseQuestion.answer);

					answerContentLayout.addView(answerItemView);
					answerContentLayout.addView(answerItemView1);
				}

				// LoadPaperUtil.loadPaperQuestions(this, getInflater(),
				// mCaseDto, answerContentLayout,true);
				isHasLoadAnswer = true;
			}

		}
	}

	public void afterCommitTaskDoing() {
		
		// 加载界面
		mCaseDto.isFinish = 1;
		if (answerFlag == SelfListActivity.SELF_FLAG) {
			// 自我学习
			SelfListActivity.isUpdateData = true;
			SelfMainActivity.isUpdateCount = true;
		} else if(answerFlag == SelfListActivity.NOTOK_FLAG){
			// 通知 界面更新
			MainActivity.isUpdateCount = true;
			// 通知未完任务更新
			UndoneTaskActivity.isUpdateData = true;
		}else if(answerFlag == SelfListActivity.DAILY_FLAG){
			//将每日一题设置已经学习
			mApplication.mLoginUser.noStudyDays = 0;
			mApplication.mLoginUser.lastStudyTime = new Date();
			mApplication.mLoginUser.dailyStudyCount++;
		}

		// 自动跳到答案页面
		setBtnSelectByIndex(ANSWER_INDEX);
		// disable 答案 不可编辑
		//LoadPaperUtil.setUserAnswerLayoutDisable(questionLayout);
	}

	/**
	 * 得到layout
	 * 
	 * @param data
	 */
	private LayoutInflater getInflater() {
		LayoutInflater mInflater = null;
		if (mInflaterCache == null) {
			mInflater = LayoutInflater.from(this);
			mInflaterCache = new WeakReference<LayoutInflater>(mInflater);
		} else {
			mInflater = mInflaterCache.get();
			if (mInflater == null) {
				mInflater = LayoutInflater.from(this);
				mInflaterCache = new WeakReference<LayoutInflater>(mInflater);
			}
		}
		return mInflater;
	}
	
	
	/**
	 * 获取BaseBo
	 * @return
	 * @throws Exception 
	 */
	private BaseBo getTaskBaseBo(boolean isCommit) throws Exception {
		BaseBo mBaseBo = new BaseBo();
		mBaseBo.maps.put("userName",mApplication.mSetting.account);
		mBaseBo.maps.put("password",mApplication.mSetting.password);
		mBaseBo.maps.put("split",LoadPaperUtil.SPLIT_CHAR1);
		mBaseBo.maps.put("caseGuid",mCaseDto.guid);
		mBaseBo.maps.put("answer", LoadPaperUtil.getUserAnswer(questionLayout, mCaseDto.taskGuid,isCommit));
		switch (answerFlag) {
			case SelfListActivity.NOTOK_FLAG:
				//未完成任务
				if(isCommit){
					//提交
					mBaseBo.requestUrl = Command.getAction(Command.COMMAND_COMMIT);
				}else{
					//保存
					mBaseBo.requestUrl = Command.getAction(Command.COMMAND_SAVE);
				}
				mBaseBo.maps.put("taskGuid",mCaseDto.taskGuid);
				break;
			case SelfListActivity.SELF_FLAG:
				//自学
				if(isCommit){
					mBaseBo.requestUrl = Command.getAction(Command.COMMAND_SELF_COMMIT);
				}else{
					mBaseBo.requestUrl = Command.getAction(Command.COMMAND_SELF_SAVE);
				}
				
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
