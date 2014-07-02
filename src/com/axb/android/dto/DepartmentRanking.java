package com.axb.android.dto;

import cn.shrek.base.annotation.DatabaseField;
import cn.shrek.base.annotation.DatabaseTable;

@DatabaseTable(tableName="_DepartmentRanking")
public class DepartmentRanking extends Ranking {
	//最基层的
	public static final int BASE_LEVEL = 4;
	
	//部门的guid 方便查成员排名
	@DatabaseField(id = true)
	public String departmentGuid;
	//部门的名称
	@DatabaseField()
	public String departmentName;
	
	public String parentGuid;
	
	@DatabaseField()
	public int levelNum;
}
