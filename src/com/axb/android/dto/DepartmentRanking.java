package com.axb.android.dto;

import cn.tt100.base.annotation.DatabaseField;
import cn.tt100.base.annotation.DatabaseTable;

@DatabaseTable(tableName="_DepartmentRanking")
public class DepartmentRanking extends Ranking {
	//���ŵ�guid ������Ա����
	@DatabaseField(id = true)
	public String departmentGuid;
	//���ŵ�����
	@DatabaseField()
	public String departmentName;
	
	@DatabaseField()
	public int levelNum;
}
