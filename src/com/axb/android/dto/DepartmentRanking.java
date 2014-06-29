package com.axb.android.dto;

import cn.tt100.base.annotation.DatabaseField;
import cn.tt100.base.annotation.DatabaseTable;

@DatabaseTable(tableName="_DepartmentRanking")
public class DepartmentRanking extends Ranking {
	//������
	public static final int BASE_LEVEL = 4;
	
	//���ŵ�guid ������Ա����
	@DatabaseField(id = true)
	public String departmentGuid;
	//���ŵ�����
	@DatabaseField()
	public String departmentName;
	
	public String parentGuid;
	
	@DatabaseField()
	public int levelNum;
}
