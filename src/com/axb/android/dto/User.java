package com.axb.android.dto;

import java.util.Date;

import cn.shrek.base.annotation.DatabaseField;
import cn.shrek.base.annotation.DatabaseTable;

@DatabaseTable(tableName="_USER")
public class User extends Ranking{
	@DatabaseField(id = true)
	public String guid; //方便移值，都使用GUID
	public String loginname; //登录用户名
	public String password; //MD5加密的密码
	
	@DatabaseField(foreign=true,foreignColumnName="departmentGuid")
	public DepartmentRanking department; //部门ID
	
	public boolean isSecurityManager; //是否安全管理员1 是 0 否,如果是安全管理员就可以发布省和安排学习计划
	public String aqxy; //安全宣言,可自定义修改
	public Date addtime; //添加时间
	@DatabaseField(canBeNull = false)
	public String nickname; //昵称(直实姓名)
	public String userimg; //用户头像在手机上地址
	public String jobguid; //昵称(直实姓名)
	public boolean isJdgly; //是否节点管理员
	public boolean isSm;
//	@DatabaseField(canBeNull = false)
//	public String departmentName;//部门中文名
	
	public String jobName;
	
	public float studyRate;
	
	public String specialty;//个人专业选择
	public CaseDto dailyCase; // 每日一题
	
	public Date lastStudyTime;//最后学习时间
	public int noStudyDays;//多少天没学习
	public int dailyStudyCount;//学习多少天了
	
	/**
	 * 判断是否是省市县的 安全管理员
	 * @return
	 */
	public boolean isSSXSm(){
		return isSm && department!=null && department.levelNum != DepartmentRanking.BASE_LEVEL;
	}
}
