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
	 * 插入Menu
	 * 
	 * @param menus
	 */
	public void insertTask(CaseDto... mTaskDtos) {
		DBDao<CaseDto> dao = getDao(CaseDto.class);
		dao.insertObjs(mTaskDtos);
	}

	/**
	 * 插入CaseDto
	 * 
	 * @param menus
	 */
	public void insertTask(List<CaseDto> taskDtos) {
		DBDao<CaseDto> dao = getDao(CaseDto.class);
		dao.insertObjs(taskDtos);
	}

	/**
	 * 删除 all task
	 * 
	 * @param menus
	 */
	public void delAllTask() {
		getDao(CaseDto.class).deleteAll();
	}

	/**
	 * 得到所有task
	 */
	public List<CaseDto> getAllTask() {
		return getDao(CaseDto.class).queryAllObjs();
	}
	
	/**
	 * 得到所有task
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
	 * 得到所有案例task
	 */
	public List<CaseDto> getAllCaseTask() {
		DBDao<CaseDto> dao = getDao(CaseDto.class);
		QueryBuilder builder = dao.queryBuilder();
		builder.eq("isFinish", 1).and().eq("taskFlag", 1);
		builder.addOrderByCon("studyTime", false);
		return dao.queryObjs(builder);
	}

	
	/**
	 * 得到所有安规task
	 */
	public List<CaseDto> getAllAnGuiTask() {
		DBDao<CaseDto> dao = getDao(CaseDto.class);
		QueryBuilder builder = dao.queryBuilder();
		builder.eq("isFinish", 1).and().eq("taskFlag", 2);
		builder.addOrderByCon("studyTime", false);
		return dao.queryObjs(builder);
	}
	
	
	/**
	 * 得到所有文件task
	 */
	public List<CaseDto> getAllFileTask() {
		DBDao<CaseDto> dao = getDao(CaseDto.class);
		QueryBuilder builder = dao.queryBuilder();
		builder.eq("isFinish", 1).and().eq("taskFlag", 3);
		builder.addOrderByCon("studyTime", false);
		return dao.queryObjs(builder);
	}
	
	/**
	 * 得到当天时间的
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
	 * ###################### user排行榜 ##########################
	 */
	
	/**
	 * 保存Users的排行
	 * @param users
	 */
	public long saveUsersRanking(List<User> users){
		DBDao<User> dao = getDao(User.class);
		dao.deleteAll();
		return dao.insertObjs(users);
	}
	
	/**
	 * 得到 所有用户排行榜  排序
	 * @param orderColumn 排序的名称
	 * @param departGuid 部门的guid
	 * @param keyword 按名字查询
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
	 * ###################### Department排行榜 ##########################
	 */
	
	/**
	 * 保存DepartmentRankings的排行
	 * @param users
	 */
	public void saveDepartmentsRanking(List<DepartmentRanking> departs){
		DBDao<DepartmentRanking> dao = getDao(DepartmentRanking.class);
		dao.deleteAll();
		dao.insertObjs(departs);
	}
	
	/**
	 * 得到 所有DepartmentRanking排行榜  排序
	 * @param orderColumn 排序的名称
	 * @return
	 */
	public List<DepartmentRanking> getDepartmentRankingByOrder(String orderColumn){
		DBDao<DepartmentRanking> dao = getDao(DepartmentRanking.class);
		QueryBuilder queryBuilder = dao.queryBuilder();
		queryBuilder.addOrderByCon(orderColumn, true);
		return dao.queryObjs(queryBuilder);
	}
}
