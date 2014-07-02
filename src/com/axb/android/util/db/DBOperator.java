package com.axb.android.util.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import cn.shrek.base.ormlite.ZWDBHelper;
import cn.shrek.base.ormlite.dao.DBDao;
import cn.shrek.base.ormlite.stmt.QueryBuilder;

import com.axb.android.dto.CaseDto;
import com.axb.android.dto.DepartmentRanking;
import com.axb.android.dto.User;

public class DBOperator extends ZWDBHelper {

	public DBOperator(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub
		createTables(arg0, DepartmentRanking.class,CaseDto.class,User.class);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		dropTables(arg0, DepartmentRanking.class,CaseDto.class,User.class);
		onCreate(arg0);
	}
	
	/**
	 * ����Menu
	 * 
	 * @param menus
	 */
	public void insertTask(CaseDto... mTaskDtos) {
		DBDao<CaseDto> dao = getDao(CaseDto.class);
		dao.insertObjs(mTaskDtos);
	}

	/**
	 * ����CaseDto
	 * 
	 * @param menus
	 */
	public void insertTask(List<CaseDto> taskDtos) {
		DBDao<CaseDto> dao = getDao(CaseDto.class);
		dao.insertObjs(taskDtos);
	}

	/**
	 * ɾ�� all task
	 * 
	 * @param menus
	 */
	public void delAllTask() {
		getDao(CaseDto.class).deleteAll();
	}

	/**
	 * �õ�����task
	 */
	public List<CaseDto> getAllTask() {
		return getDao(CaseDto.class).queryAllObjs();
	}
	
	/**
	 * �õ�����task
	 */
	public List<Long> getAllTaskTime() {
		List<Long> times = new ArrayList<Long>();
		DBDao<CaseDto> dao = getDao(CaseDto.class);
		QueryBuilder builder = dao.queryBuilder();
		builder.eq("isFinish", 1);
		builder.addOrderByCon("studyTime", false);
		builder.addSelectColumn("studyTime");
		List<CaseDto> taskDtos = dao.queryObjs(builder);
		for(CaseDto dto : taskDtos){
			times.add(dto.studyTime);
		}
		return times;
	}
	
	/**
	 * �õ����а���task
	 */
	public List<CaseDto> getAllCaseTask() {
		DBDao<CaseDto> dao = getDao(CaseDto.class);
		QueryBuilder builder = dao.queryBuilder();
		builder.eq("isFinish", 1).and().eq("taskFlag", 1);
		builder.addOrderByCon("studyTime", false);
		return dao.queryObjs(builder);
	}

	
	/**
	 * �õ����а���task
	 */
	public List<CaseDto> getAllAnGuiTask() {
		DBDao<CaseDto> dao = getDao(CaseDto.class);
		QueryBuilder builder = dao.queryBuilder();
		builder.eq("isFinish", 1).and().eq("taskFlag", 2);
		builder.addOrderByCon("studyTime", false);
		return dao.queryObjs(builder);
	}
	
	
	/**
	 * �õ������ļ�task
	 */
	public List<CaseDto> getAllFileTask() {
		DBDao<CaseDto> dao = getDao(CaseDto.class);
		QueryBuilder builder = dao.queryBuilder();
		builder.eq("isFinish", 1).and().eq("taskFlag", 3);
		builder.addOrderByCon("studyTime", false);
		return dao.queryObjs(builder);
	}
	
	/**
	 * �õ�����ʱ���
	 * @return
	 */
	public List<CaseDto> getTaskByTime(long times) {
		DBDao<CaseDto> dao = getDao(CaseDto.class);
		QueryBuilder builder = dao.queryBuilder();
		builder.eq("isFinish", 1).and().eq("studyTime", times);
		builder.addOrderByCon("studyTime", false);
		builder.addOrderByCon("taskFlag", true);
		return dao.queryObjs(builder);
	}
	
	
	/**
	 * ###################### user���а� ##########################
	 */
	
	/**
	 * ����Users������
	 * @param users
	 */
	public long saveUsersRanking(List<User> users){
		DBDao<User> dao = getDao(User.class);
		dao.deleteAll();
		return dao.insertObjs(users);
	}
	
	/**
	 * �õ� �����û����а�  ����
	 * @param orderColumn ���������
	 * @param departGuid ���ŵ�guid
	 * @param keyword �����ֲ�ѯ
	 * @return
	 */
	public List<User> getUsersByOrder(String orderColumn,String departGuid,String keyword){
		DBDao<User> dao = getDao(User.class);
		QueryBuilder queryBuilder = dao.queryBuilder();
		if(departGuid != null){
			queryBuilder.eq("departmentid", departGuid);
		}
		if(keyword !=null && !"".equals(keyword)){
			queryBuilder.like("nickname", keyword, true, true);
		}
		queryBuilder.addOrderByCon(orderColumn, false);
		return dao.queryJoinObjs(queryBuilder);
	}
	
	
	/**
	 * ###################### Department���а� ##########################
	 */
	
	/**
	 * ����DepartmentRankings������
	 * @param users
	 */
	public void saveDepartmentsRanking(List<DepartmentRanking> departs){
		DBDao<DepartmentRanking> dao = getDao(DepartmentRanking.class);
		dao.deleteAll();
		dao.insertObjs(departs);
	}
	
	/**
	 * �õ� ����DepartmentRanking���а�  ����
	 * @param orderColumn ���������
	 * @return
	 */
	public List<DepartmentRanking> getDepartmentRankingByOrder(String orderColumn){
		DBDao<DepartmentRanking> dao = getDao(DepartmentRanking.class);
		QueryBuilder queryBuilder = dao.queryBuilder();
		queryBuilder.addOrderByCon(orderColumn, true);
		return dao.queryObjs(queryBuilder);
	}
}
