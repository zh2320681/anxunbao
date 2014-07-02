package com.axb.android.ui;

import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.axb.android.R;
import com.axb.android.dto.BaseBo;
import com.axb.android.dto.CaseDto;
import com.axb.android.dto.Page;
import com.axb.android.service.BaseAsyncTask;
import com.axb.android.service.Command;
import com.axb.android.service.RecordTask;
import com.axb.android.ui.adapter.RecordTaskAdapter;
import com.axb.android.ui.adapter.RecordTimeAdapter;

public class StudyRecordActivity extends BaseActivity {
	private static final int TIME_INDEX = 0x01;
	private static final int CASE_INDEX = 0x02;
	private static final int ANGUI_INDEX = 0x03;
	private static final int STUDY_INDEX = 0x04;

	private Button backBtn, timeBtn, caseBtn, anguiBtn, studyBtn;
	private ListView contentView;
	private View requestLayout;
	private TextView requestInfo;
	
	private int selectIndex;
	
	private RecordTimeAdapter mRecordTimeAdapter;
	private RecordTaskAdapter mRecordTaskAdapter;
	/*
	 * 分页(non-Javadoc)
	 * @see com.wei.android.anxun.ui.BaseActivity#onCreate(android.os.Bundle)
	 */
	private Page mPage;
	
	
	private OnClickListener myClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v == backBtn) {
				finish();
			} else if (v == timeBtn) {
				setBtnSelectByIndex(TIME_INDEX);
			} else if (v == caseBtn) {
				setBtnSelectByIndex(CASE_INDEX);
			} else if (v == anguiBtn) {
				setBtnSelectByIndex(ANGUI_INDEX);
			} else if (v == studyBtn) {
				setBtnSelectByIndex(STUDY_INDEX);
			}
		}
	};

	@Override
	protected void initialize() {
		// TODO Auto-generated method stub
//		setContentView(R.layout.studyrecord);

		init();
		addHandler();
		requestData();
	}

	private void init() {
		mPage = new Page(0, mApplication.mSetting.pageSize);
		
		backBtn = (Button) findViewById(R.id.sr_back);
		timeBtn = (Button) findViewById(R.id.sr_time);
		caseBtn = (Button) findViewById(R.id.sr_case);
		anguiBtn = (Button) findViewById(R.id.sr_angui);
		studyBtn = (Button) findViewById(R.id.sr_study);

		contentView = (ListView) findViewById(R.id.sr_content);

		requestLayout = findViewById(R.id.load_requestLayout);
		requestInfo = (TextView)findViewById(R.id.load_requestInfo);
		requestLayout.setVisibility(View.GONE);
		
		selectIndex = TIME_INDEX;
	}

	private void addHandler() {
		backBtn.setOnClickListener(myClick);
		timeBtn.setOnClickListener(myClick);
		caseBtn.setOnClickListener(myClick);
		anguiBtn.setOnClickListener(myClick);
		studyBtn.setOnClickListener(myClick);
		
		contentView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
				switch (selectIndex) {
				case TIME_INDEX:
					Intent ii = new Intent();
					ii.putExtra("isUnDone", false);
					ii.putExtra("taskTime", mRecordTimeAdapter.getData().get(arg2));
					ii.setClass(StudyRecordActivity.this, UndoneTaskActivity.class);
					startActivity(ii);
					break;
				case CASE_INDEX:
					Intent iii = new Intent();
					iii.setClass(StudyRecordActivity.this, CaseStudyActivity.class);
					iii.putExtra("TaskDto", mRecordTaskAdapter.getData().get(arg2));
					startActivity(iii);
					break;
				case ANGUI_INDEX:
					Intent anguiIntent = new Intent();
					anguiIntent.setClass(StudyRecordActivity.this, AnguiStudyActivity.class);
					anguiIntent.putExtra("TaskDto", mRecordTaskAdapter.getData().get(arg2));
					startActivity(anguiIntent);
					break;
				case STUDY_INDEX:			
					Intent i = new Intent();
					i.setClass(StudyRecordActivity.this, FileStudyActivity.class);
					i.putExtra("TaskDto", mRecordTaskAdapter.getData().get(arg2));
					startActivity(i);
					break;
				}
			}
		});
		
		
		contentView.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            	
            }
            
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem==0){
                	
                }
                if(visibleItemCount+firstVisibleItem==totalItemCount){
                	//如果还有数据
                    if(mPage.addPage()){
                    	requestData();
                    }
                }
            }
        });
		
	}

	private void setBtnSelectByIndex(int index) {
		int old = selectIndex;
		selectIndex = index;
		switch (old) {
		case TIME_INDEX:
			timeBtn.setBackgroundResource(R.drawable.down_nor);
			timeBtn.setTextColor(getResources().getColorStateList(R.color.undone_text1));
			break;
		case CASE_INDEX:
			caseBtn.setBackgroundResource(R.drawable.down_nor);
			caseBtn.setTextColor(getResources().getColorStateList(R.color.undone_text1));
			break;
		case ANGUI_INDEX:
			anguiBtn.setBackgroundResource(R.drawable.down_nor);
			anguiBtn.setTextColor(getResources().getColorStateList(R.color.undone_text1));
			break;
		case STUDY_INDEX:
			studyBtn.setBackgroundResource(R.drawable.down_nor);
			studyBtn.setTextColor(getResources().getColorStateList(R.color.undone_text1));
			break;
		}

		switch (selectIndex) {
		case TIME_INDEX:
			timeBtn.setBackgroundResource(R.drawable.down_pre);
			timeBtn.setTextColor(Color.WHITE);
			break;
		case CASE_INDEX:
			caseBtn.setBackgroundResource(R.drawable.down_pre);
			caseBtn.setTextColor(Color.WHITE);
			break;
		case ANGUI_INDEX:
			anguiBtn.setBackgroundResource(R.drawable.down_pre);
			anguiBtn.setTextColor(Color.WHITE);
			break;
		case STUDY_INDEX:
			studyBtn.setBackgroundResource(R.drawable.down_pre);
			studyBtn.setTextColor(Color.WHITE);
			break;
		}
		
		if(selectIndex == TIME_INDEX){
			initTimeList();
		}else{
			initTaskList();
		}
	}
	
	
	private void initTimeList(){
		if(mRecordTimeAdapter == null){
			mRecordTimeAdapter = new RecordTimeAdapter(this, mApplication.mDatabaseAdapter.getAllTaskTime());
			contentView.setAdapter(mRecordTimeAdapter);
		}else{
			contentView.setAdapter(mRecordTimeAdapter);
		}
	}
	
	private void initTaskList(){
		List<CaseDto> taskList = null;
		switch(selectIndex){
			case CASE_INDEX:
				taskList = mApplication.mDatabaseAdapter.getAllCaseTask();
				break;
			case ANGUI_INDEX:
				taskList = mApplication.mDatabaseAdapter.getAllAnGuiTask();
				break;
			case STUDY_INDEX:
				taskList = mApplication.mDatabaseAdapter.getAllFileTask();
				break;
		}
		
		if(mRecordTaskAdapter == null){
			mRecordTaskAdapter = new RecordTaskAdapter(this, taskList);
			contentView.setAdapter(mRecordTaskAdapter);
		}else{
			mRecordTaskAdapter.setData(taskList);
			contentView.setAdapter(mRecordTaskAdapter);
			mRecordTaskAdapter.notifyDataSetChanged();
		}
	}
	
	
	private void requestData(){
		BaseBo mBaseBo = new BaseBo();
		mBaseBo.requestUrl = Command.getAction(Command.COMMAND_RECORD);
		mBaseBo.maps.put("userName", mApplication.mSetting.account);
		mBaseBo.maps.put("password", mApplication.mSetting.password); 
		mBaseBo.maps.put("pageIndex", mPage.getPageIndex()+"");
		mBaseBo.maps.put("pageSize", mPage.getPageSize()+""); 
		RecordTask mRecordTask = new RecordTask(this, "学习记录", "正在请求学习记录,"
				+(mRecordTaskAdapter==null?"":"第("+mPage.getPageIndex()+"/"+mPage.getTotalIndex()+")页")+",请稍等.."
				, BaseAsyncTask.PRE_TASK_CUSTOM);
		mRecordTask.execute(mBaseBo);
	}
	
	public void taskSuccessDoing(List<CaseDto> lists, Page page){
		this.mPage = page;
		mApplication.mDatabaseAdapter.delAllTask();
		if(lists.size() == 0){
			return;
		}else{
			mApplication.mDatabaseAdapter.insertTask(lists);
			setBtnSelectByIndex(TIME_INDEX);
		}
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
		requestLayout.setVisibility(View.GONE);
	}
	
}
