package com.axb.android.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * ������  ���� 
 * @author shrek
 *
 */
public class CaseQuestion implements Serializable{
	public String guid;
	public String questionname;  //����
	public String optionname; //ѡ��
	public int answerFlag;
	public String answer; //�ش�
	public String addUserGuid;
	public Date addTime;
	
	public UserAnswer mUserAnswer;
}
