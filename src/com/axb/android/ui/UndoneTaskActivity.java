package com.axb.android.ui;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.axb.android.R;
import com.axb.android.dto.BaseBo;
import com.axb.android.dto.CaseDto;
import com.axb.android.service.BaseAsyncTask;
import com.axb.android.service.Command;
import com.axb.android.service.UnDoneTask;

public class UndoneTaskActivity extends BaseActivity {
	public static boolean isUpdateData; //�Ƿ�������� 
	
	private static final int CASE_FLAG = 1;
	private static final int ANGUI_FLAG = 2;
	private static final int FILE_FLAG = 3;
	
	private Button backBtn;
	private LinearLayout caseContent,anguiContent,fileContent;
	private View msgContent;
	private TextView msgInfoView,caseInfoView,anguiInfoView,fileInfoView,titleView;
	
	private WeakReference<LayoutInflater> mInflaterCache;
	
	private View requestLayout;
	private TextView requestInfo;
	
	private boolean isUnDone;
	private long taskTime;
	
	@Override
	protected void initialize() {
		// TODO Auto-generated method stub
//		super.onBaseCreate(savedInstanceState);
		
		isUnDone = getIntent().getBooleanExtra("isUnDone", false);
				
		isUpdateData = true;
		
		init();
		addHandler();
	}
	
	
	private void init(){
		
		backBtn = (Button)findViewById(R.id.ut_back);
		
		caseContent = (LinearLayout)findViewById(R.id.ut_caseContent);
		anguiContent = (LinearLayout)findViewById(R.id.ut_anguiContent);
		fileContent = (LinearLayout)findViewById(R.id.ut_fileContent);
		
		msgContent = findViewById(R.id.ut_msgLayout);
		
		msgInfoView = (TextView)findViewById(R.id.ut_msg);
		titleView = (TextView)findViewById(R.id.ut_title);
		
		caseInfoView = (TextView)findViewById(R.id.ut_caseInfo);
		anguiInfoView = (TextView)findViewById(R.id.ut_anguiInfo);
		fileInfoView = (TextView)findViewById(R.id.ut_fileInfo);
		
		requestLayout = findViewById(R.id.load_requestLayout);
		requestInfo = (TextView)findViewById(R.id.load_requestInfo);
		requestLayout.setVisibility(View.GONE);
		
		if(isUnDone){
			titleView.setText("δ������");
		}else{
			titleView.setText("ѧϰ��¼");
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
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(isUpdateData){
			isUpdateData = false;
			if(caseContent != null){
				int caseNum = caseContent.getChildCount();
				while(caseNum > 1){
					caseContent.removeViewAt(1);
					caseNum = caseContent.getChildCount();
				}				
			}
			
			if(anguiContent != null){
//				int anguiNum = anguiContent.getChildCount();
//				for(int i = 1 ;i<anguiNum;i++){
//					anguiContent.removeViewAt(i);
//				}
				int anguiNum = anguiContent.getChildCount();
				while(anguiNum > 1){
					anguiContent.removeViewAt(1);
					anguiNum = anguiContent.getChildCount();
				}
			}
			
			if(fileContent != null){
//				int fileNum = fileContent.getChildCount();
//				for(int i = 1 ;i<fileNum;i++){
//					fileContent.removeViewAt(i);
//				}
				
				int fileNum = fileContent.getChildCount();
				while(fileNum > 1){
					fileContent.removeViewAt(1);
					fileNum = fileContent.getChildCount();
				}
			}
			
			requestData();
		}
	}
	
	private void initTaskData(List<CaseDto> data){
		LayoutInflater mLayoutInflater = null;
		if(mInflaterCache == null){
			mLayoutInflater = LayoutInflater.from(this);
			mInflaterCache = new WeakReference<LayoutInflater>(mLayoutInflater);
		}else{
			mLayoutInflater = mInflaterCache.get();
			if(mLayoutInflater == null){
				mLayoutInflater = LayoutInflater.from(this);
				mInflaterCache = new WeakReference<LayoutInflater>(mLayoutInflater);
			}
		}
		for(final CaseDto t : data){
			View view = mLayoutInflater.inflate(R.layout.undone_item, null);
			TextView infoView = (TextView)view.findViewById(R.id.ui_info);
			switch(t.taskFlag){
				case CASE_FLAG:
					infoView.setText("����"+caseContent.getChildCount()+": "+t.title);
					caseContent.addView(view);
					view.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							Intent i = new Intent();
							i.setClass(UndoneTaskActivity.this, CaseStudyActivity.class);
							i.putExtra("TaskDto", t);
							i.putExtra("answerFlag",SelfListActivity.NOTOK_FLAG);
							startActivity(i);
						}
					});
					break;
				case ANGUI_FLAG:
					infoView.setText("����"+anguiContent.getChildCount()+": "+t.title);
					anguiContent.addView(view);
					view.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							Intent i = new Intent();
							i.setClass(UndoneTaskActivity.this, AnguiStudyActivity.class);
							i.putExtra("TaskDto", t);
							i.putExtra("answerFlag",SelfListActivity.NOTOK_FLAG);
							startActivity(i);
						}
					});
					break;
				case FILE_FLAG:
					infoView.setText("�ļ�"+fileContent.getChildCount()+": "+t.title);
					fileContent.addView(view);
					view.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							Intent i = new Intent();
							i.setClass(UndoneTaskActivity.this, FileStudyActivity.class);
							i.putExtra("TaskDto", t);
							i.putExtra("answerFlag",SelfListActivity.NOTOK_FLAG);
							startActivity(i);
						}
					});
					break;
			}
			
		}
		if(caseContent.getChildCount() == 1){
			caseInfoView.setText("����Ҫ��ɵİ���");
		}else{
			caseInfoView.setText("����"+(caseContent.getChildCount()-1)+"��δѧ����,���ڹ涨ʱ�������");
		}
		
		if(anguiContent.getChildCount() == 1){
			anguiInfoView.setText("����Ҫ��ɵİ���");
		}else{
			anguiInfoView.setText("����"+(anguiContent.getChildCount()-1)+"��δѧ����,���ڹ涨ʱ�������");
		}
		if(fileContent.getChildCount() == 1){
			fileInfoView.setText("����Ҫ��ɵ��ļ�");
		}else{
			fileInfoView.setText("����"+(fileContent.getChildCount()-1)+"��δѧ�ļ�,���ڹ涨ʱ�������");
		}
				
	}
	
	

	private void requestData(){
		if(isUnDone){
			BaseBo mBaseBo = new BaseBo();
			mBaseBo.requestUrl = Command.getAction(Command.COMMAND_UNDONE);
			mBaseBo.maps.put("userName", mApplication.mSetting.account);
			mBaseBo.maps.put("password", mApplication.mSetting.password); 
			UnDoneTask mUnDoneTask = new UnDoneTask(this, "δ������", "��������δ���������Ϣ,���Ե�.."
					, BaseAsyncTask.PRE_TASK_CUSTOM);
			mUnDoneTask.execute(mBaseBo);
		}else{
			msgContent.setVisibility(View.GONE);
			
			taskTime = getIntent().getLongExtra("taskTime", 0);
			initTaskData(mApplication.mDatabaseAdapter.getTaskByTime(taskTime));
		}
	}
	
	public void taskSuccessDoing(List<CaseDto> lists){
		initTaskData(lists);
		mApplication.unReadTask = lists.size();
		if(lists.size() > 0){
			Date date = new Date(lists.get(0).endTime);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			msgInfoView.setText("����Ա��Ϣ: ����"+sdf.format(date)+"ǰ����");
			
		}
	}
	/**
	 * ��ʾ����ʱ�� ui
	 * @param info
	 */
	public void showRequestUi(String info) {
		requestInfo.setText(info);
		if (!requestLayout.isShown()) {
			requestLayout.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * ��������ʱ�� ui
	 * @param info
	 */
	public void hideRequestUi() {
		requestLayout.setVisibility(View.GONE);
	}
	
	
}
