package com.axb.android.ui;

import java.util.List;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.axb.android.R;
import com.axb.android.dto.BaseBo;
import com.axb.android.dto.CaseDto;
import com.axb.android.dto.Page;
import com.axb.android.service.BaseAsyncTask;
import com.axb.android.service.Command;
import com.axb.android.service.GetSelfListTask;
import com.axb.android.ui.adapter.SelfAdapter;

public class SelfListActivity extends BaseActivity {
	public static boolean isUpdateData; //是否更新数据 
	
	public static final int FLAG_CASE = 0x01;//案例
	public static final int FLAG_ANGUI = 0x02;//安规
	public static final int FLAG_FILE = 0x03;//文件
	
//	public static final int NOTOK_FLAG = 0;//规定任务
//	public static final int ALJJ_FLAG= 1;//案例借鉴
//	public static final int DAILY_FLAG = 2;//每日一题
//	public static final int SELF_FLAG = 3;//自我学习
	
	public static final int NOTOK_FLAG = 0;//规定任务
	public static final int ALJJ_FLAG= 3;//案例借鉴
	public static final int DAILY_FLAG = 2;//每日一题
	public static final int SELF_FLAG = 1;//自我学习
	
	private int taskFlag,anguiFlag;
	
	private Button backBtn,searchBtn;
//	private ImageView clearBtn;
	private TextView titleView;
	private EditText inputView;
	
	private ListView mContent;
	private View requestLayout;
	private TextView requestInfo;
	private SelfAdapter mSelfAdapter;
	
	private Spinner sp1,sp2;
	private ArrayAdapter<CharSequence> sp1Adapter,sp2Adapter;
	private String[] sp1Array,sp2Array;
//	private boolean isInputChange;
	/*
	 * 分页(non-Javadoc)
	 * @see com.wei.android.anxun.ui.BaseActivity#onCreate(android.os.Bundle)
	 */
	private Page mPage;
	private String keyword;
	
	private OnClickListener myClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v == backBtn){
				finish();
			}else if(v == searchBtn){
				//查询
				keyword = inputView.getText().toString();
				requestData();
			}
//			else if(v == clearBtn){
//				//查询
//				keyword = null;
//				inputView.setText(null);
//				
//				sp1.setSelection(0);
//				sp2.setSelection(0);
//			}
		}
	};
	
	
	@Override
	protected void initialize() {
//		// TODO Auto-generated method stub
//		setContentView(R.layout.self_list);
		
		taskFlag = getIntent().getIntExtra("taskFlag", FLAG_CASE);
		
		anguiFlag = getIntent().getIntExtra("anguiFlag", FLAG_CASE);
		
		isUpdateData = true;
		
		init();
//		requestData();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(isUpdateData){
			isUpdateData = false;
			mPage.setPageIndex(0);
			requestData();
		}
	}
	
	private void init(){	
		mPage = new Page(0, mApplication.mSetting.pageSize);
		
		backBtn = (Button)findViewById(R.id.sl_back);
		searchBtn = (Button)findViewById(R.id.sl_search);
//		clearBtn = (ImageView)findViewById(R.id.sl_clear);
		titleView = (TextView)findViewById(R.id.sl_title);
		inputView = (EditText)findViewById(R.id.sl_input);
		
		sp1 = (Spinner)findViewById(R.id.sl_sp1);
		sp2 = (Spinner)findViewById(R.id.sl_sp2);
		
		sp1Adapter =  ArrayAdapter.createFromResource(
				SelfListActivity.this, R.array.sp1,
				android.R.layout.simple_spinner_item);
		sp1Adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		sp2Adapter =  ArrayAdapter.createFromResource(
				SelfListActivity.this, R.array.sp2,
				android.R.layout.simple_spinner_item);
		sp2Adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp1.setAdapter(sp1Adapter);
		sp2.setAdapter(sp2Adapter);
		
		sp1Array = getResources().getStringArray(R.array.sp1);
		sp2Array = getResources().getStringArray(R.array.sp2);
		
//		isInputChange = false;
		
		switch(taskFlag){
			case FLAG_CASE:
				titleView.setText("案例学习");
				sp1.setVisibility(View.VISIBLE);
				sp2.setVisibility(View.VISIBLE);
				break;
			case FLAG_ANGUI:
				titleView.setText("安规学习");
				sp1.setVisibility(View.GONE);
				sp2.setVisibility(View.GONE);
				break;
			case FLAG_FILE:
				titleView.setText("文件学习");
				sp1.setVisibility(View.GONE);
				sp2.setVisibility(View.GONE);
				break;
		}
		
		
		mContent = (ListView)findViewById(R.id.sl_list);
//		content.setVisibility(View.GONE);
		requestLayout = findViewById(R.id.load_requestLayout);
		requestInfo = (TextView)findViewById(R.id.load_requestInfo);
		requestLayout.setVisibility(View.GONE);
		
		backBtn.setOnClickListener(myClick);
		searchBtn.setOnClickListener(myClick);
//		clearBtn.setOnClickListener(myClick);
		
		mContent.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				switch(taskFlag){
				case FLAG_CASE:
					Intent caseIntent = new Intent();
					caseIntent.setClass(SelfListActivity.this, CaseStudyActivity.class);
					caseIntent.putExtra("TaskDto", mSelfAdapter.getItem(arg2));
					caseIntent.putExtra("answerFlag", SelfListActivity.SELF_FLAG);
					startActivity(caseIntent);
					break;
				case FLAG_ANGUI:
					Intent anguiIntent = new Intent();
					anguiIntent.setClass(SelfListActivity.this, AnguiStudyActivity.class);
					anguiIntent.putExtra("TaskDto", mSelfAdapter.getItem(arg2));
					anguiIntent.putExtra("answerFlag", SelfListActivity.SELF_FLAG);
					startActivity(anguiIntent);
					break;
				case FLAG_FILE:
					Intent fileIntent = new Intent();
					fileIntent.setClass(SelfListActivity.this, FileStudyActivity.class);
					fileIntent.putExtra("TaskDto", mSelfAdapter.getItem(arg2));
					fileIntent.putExtra("answerFlag", SelfListActivity.SELF_FLAG);
					startActivity(fileIntent);
					break;
				}
			}
			
		});
		
		mContent.setOnScrollListener(new OnScrollListener() {
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
		
		OnItemSelectedListener mOnItemSelectedListener = new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				StringBuffer sb = new StringBuffer();
				if(sp1.getSelectedItemPosition() != 0){
					sb.append(sp1.getSelectedItem().toString());
				}
				
				if(sp2.getSelectedItemPosition() != 0){
					sb.append((sb.length()>0?",":"")+sp2.getSelectedItem().toString());
				}
				inputView.setText(sb.toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		};
		sp1.setOnItemSelectedListener(mOnItemSelectedListener);
		sp2.setOnItemSelectedListener(mOnItemSelectedListener);
		
		inputView.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				boolean isExist = false;
				String string = s.toString();
				for(String str : sp1Array){
					if(string.contains(str)){
						isExist = true;
						break;
					}
				}
				
				if(!isExist){
//					isInputChange = true;
					sp1.setSelection(0);
				}
				
				isExist = false;
				for(String str : sp2Array){
					if(string.contains(str)){
						isExist = true;
						break;
					}
				}
				
				if(!isExist){
//					isInputChange = true;
					sp2.setSelection(0);
				}
			}
		});
	}
	
	
	
	
	private void requestData(){
		BaseBo mBaseBo = new BaseBo();
		mBaseBo.requestUrl = Command.getAction(Command.COMMAND_SELF);
		mBaseBo.maps.put("userName", mApplication.mSetting.account);
		mBaseBo.maps.put("password", mApplication.mSetting.password); 
		mBaseBo.maps.put("pageIndex", mPage.getPageIndex()+"");
		mBaseBo.maps.put("pageSize", mPage.getPageSize()+""); 
		mBaseBo.maps.put("taskFlag", taskFlag+""); 
		mBaseBo.maps.put("keyword", keyword); 
		mBaseBo.maps.put("anguiFlag", anguiFlag+""); 
		GetSelfListTask mGetSelfListTask = new GetSelfListTask(this, "消息数据", 
				"正在请求学习记录"+(mSelfAdapter==null?"":"第("+mPage.getPageIndex()+"/"+mPage.getTotalIndex()+")页")+",请稍等.."
				, BaseAsyncTask.PRE_TASK_CUSTOM);
		mGetSelfListTask.execute(mBaseBo);
	}
	
	
	public void taskSuccessDoing(List<CaseDto> lists, Page page){
		this.mPage = page;
		if(lists.size() == 0){
			mContent.setVisibility(View.GONE);
			return;
		}
		mContent.setVisibility(View.VISIBLE);
		inputView.setText(keyword);
		
		if(mSelfAdapter == null){
			mSelfAdapter = new SelfAdapter(this, lists);
			mContent.setAdapter(mSelfAdapter);
		}else if(page.getPageIndex() == 0){
			//第一页
			mSelfAdapter.setData(lists);
			mSelfAdapter.notifyDataSetChanged();
		}else{
			mSelfAdapter.addData(lists);
			mSelfAdapter.notifyDataSetChanged();
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
