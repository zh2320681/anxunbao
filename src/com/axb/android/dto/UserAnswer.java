package com.axb.android.dto;

import java.io.Serializable;

public class UserAnswer implements Serializable{
	
	public int answerflag; //�����-1��ѡ��2��ѡ��3�жϣ�4��д��
	public String questionsguid;
	public String userinputanswer; //�ı�
	public String usercheckboxanswer;//��ѡ
	public String userradioanswer; //��ѡ
	public int userjudgeanswer; //�ж���
//	public String userId;
	public String taskGuid;
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return questionsguid+","+answerflag+","+userradioanswer+","+usercheckboxanswer+","+userjudgeanswer+","+userinputanswer+","+taskGuid;
	}
}
