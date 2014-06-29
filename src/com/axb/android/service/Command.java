package com.axb.android.service;



public class Command {
	/** ��ȡ��ʱʱ��,��λ:���� */
	public static final int READ_TIME_OUT = 30000;
	/** ���ӳ�ʱʱ�� ,��λ:���� */
	public static final int CONN_TIME_OUT = 10000;
	
	public static String SERVICE_URL = "http://www.anxunbao.cn:8081";
	
//	private static String suffix = "/jsonserver/service.json?action=";
	private static String SUFFIX_SUFFIX = "/AnXunBao";
	private static String SUFFIX_PREFIX = "/service.json?action=";
	
	/** ��½���� */
	public static final int COMMAND_LOGIN = 0x01;
	/** ��Ϣ���� */
	public static final int COMMAND_MESSAGE = 0x02;
	/** ѧϰ��¼���� */
	public static final int COMMAND_RECORD = 0x03;
	/** δ������� */
	public static final int COMMAND_UNDONE = 0x04;
	/** ��Ϣ��Ϊ�Ѷ� */
	public static final int COMMAND_MSG_READ = 0x05;
	/** ��������Ϣ ���� */
	public static final int COMMAND_MAIN_COUNT = 0x06;
	/** ��������Ŀ  �ʹ� */
	public static final int COMMAND_QUESTIONS = 0x07;
	/** �ݴ� */
	public static final int COMMAND_SAVE = 0x08;
	/** δ��������ύ */
	public static final int COMMAND_COMMIT = 0x09;
	/** �ļ� */
	public static final int COMMAND_FILE = 0x0a;
	/** ǩ�� */
	public static final int COMMAND_SIGN = 0x0b;
	/** ���� */
	public static final int COMMAND_RATE = 0x0c;
	/** tͷ�� */
	public static final int COMMAND_FACE = 0x0d;
	/** ���� */
	public static final int COMMAND_LEVEL = 0x0e;
	/** �������� */
	public static final int COMMAND_CASE_CONTENT = 0x0f;
	/** msg���� */
	public static final int COMMAND_MSG_CONTENT = 0x10;
	/** ��ѧ�б����� */
	public static final int COMMAND_SELF = 0x11;
	/** ��ѧ�ļ�������� */
	public static final int COMMAND_SELF_FILE = 0x12;
	/** ��ѧ�ݴ� */
	public static final int COMMAND_SELF_SAVE = 0x13;
	/** ��ѧ�ύ */
	public static final int COMMAND_SELF_COMMIT = 0x14;
	/** �޸����� */
	public static final int COMMAND_MODIFY = 0x15;
	/** �汾 */
	public static final int COMMAND_VERSION = 0x16;
	/** ����ѧϰ���֣�ѧϰ���� */
	public static final int COMMAND_SELFSTUDY_NUM = 0x17;
	/** ������� */
	public static final int COMMAND_CASE_REFERENCE = 0x18;
	/** �û����� */
	public static final int COMMAND_USERS_RANKING = 0x19;
	/** �Ŷ����� */
	public static final int COMMAND_DEPART_RANKING = 0x1a;
	/** ÿ��һ���ύ */
	public static final int COMMAND_DAILY_COMMIT = 0x1b;
	/** �����ύ */
	public static final int COMMAND_ALJJ_COMMIT = 0x1c;
	/** רҵ�ύ */
	public static final int COMMAND_SPECIALTY_COMMIT = 0x1d;
	
	/**
	 * �õ�����
	 */
	public static String getAction(int id) {
		String action = "";
		switch (id) {
		case COMMAND_LOGIN:
			/** ��½���� */
			action = SERVICE_URL + SUFFIX_SUFFIX + SUFFIX_PREFIX + "login";
			break;
		case COMMAND_MESSAGE:
			/** ��Ϣ���� */
			action = SERVICE_URL + SUFFIX_SUFFIX + SUFFIX_PREFIX + "message";
			break;
		case COMMAND_RECORD:
			/** ѧϰ��¼���� */
			action = SERVICE_URL + SUFFIX_SUFFIX + SUFFIX_PREFIX + "record";
			break;
		case COMMAND_UNDONE:
			/** δ������� */
			action = SERVICE_URL + SUFFIX_SUFFIX + SUFFIX_PREFIX + "unDone";
			break;
		case COMMAND_MSG_READ:
			action = SERVICE_URL + SUFFIX_SUFFIX + SUFFIX_PREFIX + "msgRead";
			break;
		case COMMAND_MAIN_COUNT:
			action = SERVICE_URL + SUFFIX_SUFFIX + SUFFIX_PREFIX + "main";
			break;
		case COMMAND_QUESTIONS:
			action = SERVICE_URL + SUFFIX_SUFFIX + SUFFIX_PREFIX + "questions";
			break;
		case COMMAND_SAVE:
			action = SERVICE_URL + SUFFIX_SUFFIX + SUFFIX_PREFIX + "save";
			break;
		case COMMAND_COMMIT:
			action = SERVICE_URL + SUFFIX_SUFFIX + SUFFIX_PREFIX + "commit";
			break;
		case COMMAND_FILE:
			action = SERVICE_URL + SUFFIX_SUFFIX + SUFFIX_PREFIX + "file";
			break;
		case COMMAND_SIGN:
			action = SERVICE_URL + SUFFIX_SUFFIX + SUFFIX_PREFIX + "sign";
			break;
		case COMMAND_RATE:
			action = SERVICE_URL + SUFFIX_SUFFIX + SUFFIX_PREFIX + "rate";
			break;
		case COMMAND_FACE:
			action = SERVICE_URL + SUFFIX_SUFFIX + SUFFIX_PREFIX + "face";
			break;
		case COMMAND_LEVEL:
			action = SERVICE_URL + SUFFIX_SUFFIX + SUFFIX_PREFIX + "all_rate";
			break;	
		case COMMAND_CASE_CONTENT:
			action = SERVICE_URL + SUFFIX_SUFFIX + SUFFIX_PREFIX + "case_content";
			break;
		case COMMAND_MSG_CONTENT:
			action = SERVICE_URL + SUFFIX_SUFFIX + SUFFIX_PREFIX + "msg_content";
			break;
		case COMMAND_SELF:
			action = SERVICE_URL + SUFFIX_SUFFIX + SUFFIX_PREFIX + "self";
			break;
		case COMMAND_SELF_FILE:
			/** ��ѧ�ļ�������� */
			action = SERVICE_URL + SUFFIX_SUFFIX + SUFFIX_PREFIX + "self_file";
			break;
		case COMMAND_SELF_SAVE:
			action = SERVICE_URL + SUFFIX_SUFFIX + SUFFIX_PREFIX + "self_save";
			break;	
		case COMMAND_SELF_COMMIT:
			action = SERVICE_URL + SUFFIX_SUFFIX + SUFFIX_PREFIX + "self_commit";
			break;	
		case COMMAND_DAILY_COMMIT:
			action = SERVICE_URL + SUFFIX_SUFFIX + SUFFIX_PREFIX + "daily_save";
			break;	
		case COMMAND_ALJJ_COMMIT:
			action = SERVICE_URL + SUFFIX_SUFFIX + SUFFIX_PREFIX + "aljj_save";
			break;	
		case COMMAND_MODIFY:
			action = SERVICE_URL + SUFFIX_SUFFIX + SUFFIX_PREFIX + "modify";
			break;	
		case COMMAND_VERSION:
			action = SERVICE_URL + SUFFIX_SUFFIX + SUFFIX_PREFIX + "version";
			break;		
		case COMMAND_SELFSTUDY_NUM:
			action = SERVICE_URL + SUFFIX_SUFFIX + SUFFIX_PREFIX +"self_count";
			break;	
		case COMMAND_CASE_REFERENCE:
			action = SERVICE_URL + SUFFIX_SUFFIX + SUFFIX_PREFIX +"aljs";
			break;	
		}
		
		return action;
	}
		
	/**
	 * ���ͼƬ�ϴ���·��
	 * @return
	 */
	public static String getUploadImgPath(){
		return SERVICE_URL + SUFFIX_SUFFIX + "/updateFaceImg" ;
	}
	
	/**��ȡ�û���ͼƬ��Ŀ¼
	 * @return
	 */
	public static String getUserImgPath(){
		return SERVICE_URL + SUFFIX_SUFFIX + "/faceImg/" ;
	}
	
	/**
	 * ���ͼƬ�ϴ���·��
	 * @return
	 */
	public static String getDownLoadPath(String fileName){
		return SERVICE_URL + SUFFIX_SUFFIX + "/download/"+fileName ;
	}
	
	/**
	 * rest����Ĺ���
	 * @return
	 */
	private static String getCommonRestUrl(){
		return SERVICE_URL + SUFFIX_SUFFIX + SUFFIX_PREFIX ;
	}
	
	public static String getRestActionUrl(int id) {
		String action = "";
		switch (id) {
		case COMMAND_LOGIN:
			/** ��½���� */
			action = getCommonRestUrl() + "login&userName={userName}&password={password}";
			break;
		case COMMAND_MESSAGE:
			/** ��Ϣ���� */
			action = SERVICE_URL + SUFFIX_SUFFIX + SUFFIX_PREFIX + "message";
			break;
		case COMMAND_RECORD:
			/** ѧϰ��¼���� */
			action = SERVICE_URL + SUFFIX_SUFFIX + SUFFIX_PREFIX + "record";
			break;
		case COMMAND_UNDONE:
			/** δ������� */
			action = SERVICE_URL + SUFFIX_SUFFIX + SUFFIX_PREFIX + "unDone";
			break;
		case COMMAND_MSG_READ:
			action = SERVICE_URL + SUFFIX_SUFFIX + SUFFIX_PREFIX + "msgRead";
			break;
		case COMMAND_MAIN_COUNT:
			action = SERVICE_URL + SUFFIX_SUFFIX + SUFFIX_PREFIX + "main";
			break;
		case COMMAND_QUESTIONS:
			action = SERVICE_URL + SUFFIX_SUFFIX + SUFFIX_PREFIX + "questions";
			break;
		case COMMAND_SAVE:
			action = SERVICE_URL + SUFFIX_SUFFIX + SUFFIX_PREFIX + "save";
			break;
		case COMMAND_COMMIT:
			action = SERVICE_URL + SUFFIX_SUFFIX + SUFFIX_PREFIX + "commit";
			break;
		case COMMAND_FILE:
			action = SERVICE_URL + SUFFIX_SUFFIX + SUFFIX_PREFIX + "file";
			break;
		case COMMAND_SIGN:
			action = SERVICE_URL + SUFFIX_SUFFIX + SUFFIX_PREFIX + "sign";
			break;
		case COMMAND_RATE:
			action = SERVICE_URL + SUFFIX_SUFFIX + SUFFIX_PREFIX + "rate";
			break;
		case COMMAND_FACE:
			action = SERVICE_URL + SUFFIX_SUFFIX + SUFFIX_PREFIX + "face";
			break;
		case COMMAND_LEVEL:
			action = SERVICE_URL + SUFFIX_SUFFIX + SUFFIX_PREFIX + "all_rate";
			break;	
		case COMMAND_CASE_CONTENT:
			action = SERVICE_URL + SUFFIX_SUFFIX + SUFFIX_PREFIX + "case_content";
			break;
		case COMMAND_MSG_CONTENT:
			action = SERVICE_URL + SUFFIX_SUFFIX + SUFFIX_PREFIX + "msg_content";
			break;
		case COMMAND_SELF:
			action = SERVICE_URL + SUFFIX_SUFFIX + SUFFIX_PREFIX + "self";
			break;
		case COMMAND_SELF_FILE:
			/** ��ѧ�ļ�������� */
			action = SERVICE_URL + SUFFIX_SUFFIX + SUFFIX_PREFIX + "self_file";
			break;
		case COMMAND_SELF_SAVE:
			action = SERVICE_URL + SUFFIX_SUFFIX + SUFFIX_PREFIX + "self_save";
			break;	
		case COMMAND_SELF_COMMIT:
			action = SERVICE_URL + SUFFIX_SUFFIX + SUFFIX_PREFIX + "self_commit";
			break;	
		case COMMAND_DAILY_COMMIT:
			action = SERVICE_URL + SUFFIX_SUFFIX + SUFFIX_PREFIX + "daily_save";
			break;	
		case COMMAND_ALJJ_COMMIT:
			action = SERVICE_URL + SUFFIX_SUFFIX + SUFFIX_PREFIX + "aljj_save";
			break;	
		case COMMAND_MODIFY:
			action = SERVICE_URL + SUFFIX_SUFFIX + SUFFIX_PREFIX + "modify";
			break;	
		case COMMAND_VERSION:
			action = SERVICE_URL + SUFFIX_SUFFIX + SUFFIX_PREFIX + "version";
			break;		
		case COMMAND_SELFSTUDY_NUM:
			action = SERVICE_URL + SUFFIX_SUFFIX + SUFFIX_PREFIX +"self_count&userName={userName}&password={password}";
			break;	
		case COMMAND_CASE_REFERENCE:
			action = SERVICE_URL + SUFFIX_SUFFIX + SUFFIX_PREFIX +"aljs&userName={userName}&password={password}";
			break;	
		case COMMAND_USERS_RANKING: 
			action = SERVICE_URL + SUFFIX_SUFFIX + SUFFIX_PREFIX +"all_rate&userName={userName}&password={password}&departGuid={departGuid}";
			break;
		case COMMAND_DEPART_RANKING:
			action = SERVICE_URL + SUFFIX_SUFFIX + SUFFIX_PREFIX +"all_depart_rate&userName={userName}&password={password}&departGuid={departGuid}";
			break;
		case COMMAND_SPECIALTY_COMMIT:
			action = SERVICE_URL + SUFFIX_SUFFIX + SUFFIX_PREFIX +"modify_spec&userName={userName}&password={password}&specialty={specialty}";
			break;
		}
		
		return action;
	}
}
