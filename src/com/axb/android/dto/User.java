package com.axb.android.dto;

import java.util.Date;

import cn.shrek.base.annotation.DatabaseField;
import cn.shrek.base.annotation.DatabaseTable;

@DatabaseTable(tableName="_USER")
public class User extends Ranking{
	@DatabaseField(id = true)
	public String guid; //������ֵ����ʹ��GUID
	public String loginname; //��¼�û���
	public String password; //MD5���ܵ�����
	
	@DatabaseField(foreign=true,foreignColumnName="departmentGuid")
	public DepartmentRanking department; //����ID
	
	public boolean isSecurityManager; //�Ƿ�ȫ����Ա1 �� 0 ��,����ǰ�ȫ����Ա�Ϳ��Է���ʡ�Ͱ���ѧϰ�ƻ�
	public String aqxy; //��ȫ����,���Զ����޸�
	public Date addtime; //���ʱ��
	@DatabaseField(canBeNull = false)
	public String nickname; //�ǳ�(ֱʵ����)
	public String userimg; //�û�ͷ�����ֻ��ϵ�ַ
	public String jobguid; //�ǳ�(ֱʵ����)
	public boolean isJdgly; //�Ƿ�ڵ����Ա
	public boolean isSm;
//	@DatabaseField(canBeNull = false)
//	public String departmentName;//����������
	
	public String jobName;
	
	public float studyRate;
	
	public String specialty;//����רҵѡ��
	public CaseDto dailyCase; // ÿ��һ��
	
	public Date lastStudyTime;//���ѧϰʱ��
	public int noStudyDays;//������ûѧϰ
	public int dailyStudyCount;//ѧϰ��������
	
	/**
	 * �ж��Ƿ���ʡ���ص� ��ȫ����Ա
	 * @return
	 */
	public boolean isSSXSm(){
		return isSm && department!=null && department.levelNum != DepartmentRanking.BASE_LEVEL;
	}
}
