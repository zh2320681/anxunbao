package com.axb.android.dto;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * ����ģʽ(����ĸ�ʽ)
 * @author Administrator
 *
 */
public class BaseBo {
//	private static BaseBo myBo;
	
	public Map<String,String> maps;  //�����б�
	public String requestUrl="";//�����ַ
//	public String action;//������
//	public String guid;
	
	public BaseBo(){
		maps = new HashMap<String, String>();
//		UUID u=UUID.randomUUID();
//		guid= u.toString();
	}
	
//	public static BaseBo newInstance(){
//		if(myBo == null){
//			myBo = new BaseBo();
//		}
//		return new BaseBo();
//	}
	
	public void clearData(){
		requestUrl="";
		maps.clear();
//		maps.put("guid", guid);
	}
	
	public void clearDataWithGuid(){
		requestUrl="";
		maps.clear();
		UUID u=UUID.randomUUID();
//		guid= u.toString();
//		maps.put("guid", guid);
	}
	
	public void clearDataWithOutGuid(){
		requestUrl="";
		maps.clear();
//		maps.put("guid", guid);
	}
	
}
