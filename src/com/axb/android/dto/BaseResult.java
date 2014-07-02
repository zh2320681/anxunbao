package com.axb.android.dto;

import java.util.List;

import cn.shrek.base.ZWBo;

public class BaseResult extends ZWBo {
	public String success;
	public String errmessage; 
	public String errcode;
	//Êý¾Ý
	public Object mineData;
	//´úÂë»º´æ	
	public String data;
	public int unReadMsg;
	public int unReadTask;
	
	public Page page;
	public List<DepartmentRanking> departs;
	
	public boolean isSuccess(){
		return success!=null && success.toLowerCase().equals("true");
	}
}
