package com.axb.android.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.axb.android.R;
import com.axb.android.dto.CaseDto;

public class SelfAdapter extends BaseAdapter {
	private Context mContext;
	private List<CaseDto> data;
	private LayoutInflater mInflater;

	public SelfAdapter(Context ctx, List<CaseDto> messageDtos) {
		this.mContext = ctx;
		this.data = messageDtos;
		mInflater = LayoutInflater.from(ctx);
	}

	public List<CaseDto> getData() {
		return data;
	}

	public void setData(List<CaseDto> data) {
		this.data = data;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public CaseDto getItem(int arg0) {
		// TODO Auto-generated method stub
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View content, ViewGroup arg2) {
		// TODO Auto-generated method stub
		CaseDto c = data.get(arg0);
		TagHolder mTagHolder = null;
		if(content == null){
			mTagHolder = new TagHolder();
			content = mInflater.inflate(R.layout.self_item, null);
			mTagHolder.titleView = (TextView) content.findViewById(R.id.si_title);
			mTagHolder.timeView = (TextView) content.findViewById(R.id.si_time);
			
			content.setTag(mTagHolder);
		}else{
			mTagHolder = (TagHolder)content.getTag();
		}
		// View fenge = view.findViewById(R.id.mi_fenge);
		mTagHolder.titleView.setText(c.title);
		
		mTagHolder.timeView.setText("("+c.studyCount+")");
		
//		mTagHolder.timeView.setText(CommonUtil.formartServerTime(c.addTime));
//		if(CommonUtil.judgeIntAvild(c.isFinish)){
//			mTagHolder.titleView.setTextColor(Color.GRAY);
//		}else{
//			
//			//Ϊѧ
//			mTagHolder.titleView.setTextColor(Color.BLACK);
//		}
	
		return content;
	}

	
	public void addData(List<CaseDto> addData){
		this.data.addAll(addData);
		notifyDataSetChanged();
	}
	
	
	public void clearData(){
		this.data.clear();
		notifyDataSetChanged();
	}
	
	
	class TagHolder {
//		ImageView iconView;
		TextView titleView,timeView;
	}
}

	
