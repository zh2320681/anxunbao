package com.axb.android.dto;

import cn.shrek.base.ZWBo;
import cn.shrek.base.annotation.DatabaseField;
/**
 * ���а� ��
 * @author shrek
 *
 */
public class Ranking extends ZWBo {
	//��������� 80.0 %
	
	@DatabaseField
	public double taskRanking;
	//ÿ��һ�� �����Ŀ
	@DatabaseField
	public int dailyNum;
	//��ѧ�����Ŀ
	@DatabaseField
	public int selfNum;
}
