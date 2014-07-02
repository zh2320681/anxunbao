package com.axb.android.dto;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.List;

import cn.shrek.base.ZWBo;
import cn.shrek.base.annotation.DatabaseField;
import cn.shrek.base.annotation.DatabaseTable;

@DatabaseTable(tableName="caseDB")
public class CaseDto extends ZWBo implements Serializable{
	
	@DatabaseField(id = true)
	public String guid;
	
	@DatabaseField
	public int taskFlag;
//	public String content;
	@DatabaseField
	public String title;
	
	@DatabaseField
	public long addTime;
	
	@DatabaseField
	public int isFinish;
	
	@DatabaseField
	public long studyTime;
	
	@DatabaseField
	public int iszc;
	
	@DatabaseField
	public String taskGuid;
	
	public long endTime;
	public List<CaseQuestion> allCaseQuestion;
	public int studyCount;//学过的次数
	
	public WeakReference<String> contentCache;
	
	
	public String getContent(){
//		String content = null;
		if(contentCache == null ||
				contentCache.get() == null){
			return null;
		}
		return contentCache.get();
	}
	
	
	public void setContent(String content){
		contentCache = new WeakReference<String>(content);
	}
	
}
