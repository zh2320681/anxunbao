package com.axb.android.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import cn.shrek.base.annotation.AutoInject;
import cn.shrek.base.ormlite.task.DBAsyncTask;
import cn.shrek.base.util.rest.ZWAsyncTask;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.axb.android.R;
import com.axb.android.dto.BaseResult;
import com.axb.android.dto.DepartmentRanking;
import com.axb.android.dto.Ranking;
import com.axb.android.dto.User;
import com.axb.android.service.Command;
import com.axb.android.service.MyDialogTaskHandler;
import com.axb.android.ui.adapter.RankingAdapter;

public class RankingActivity extends BaseActivity {
	// 是否已经加载过 用户和部门信息
	private boolean hasLoadUserRanking, hasLoadDepartRanking,
			hasLoadSMDepartRanking;

	private static final byte PER_RANKING = 0x01;// 个人排行
	private static final byte TEAM_RANKING = 0x02;// 团队排行
	private static final byte PER_SM_RANKING = 0x03;// 个人安全管理员排行

	private static final byte STUDY_ORDER = 0x03;// 自我学习
	private static final byte TASK_ORDER = 0x04;// 任务完成
	private static final byte DAILY_ORDER = 0x05;// 每日一题

	@AutoInject(idFormat = "ranking_?")
	private TextView titleView;

	@AutoInject(idFormat = "ranking_?",clickSelector = "mClick")
	private Button backBtn, teamBtn, perBtn, taskOkBtn, dailyCaseBtn,
			mStudyBtn;
	@AutoInject(idFormat = "ranking_?")
	private LinearLayout topLayout;

	@AutoInject(idFormat = "ranking_?")
	private TextView mlistName;

	@AutoInject(idFormat = "ranking_?")
	private EditText searchInput;

	@AutoInject(idFormat = "ranking_?")
	private ImageView searchBtn;

	@AutoInject(idFormat = "ranking_?")
	private ListView mList;
	private RankingAdapter mRankingAdapter;

	private int rankIndex, orderIndex;
	private String keyword;
	private List<DepartmentRanking> wholeDeparts, wholeSMDeparts;
	private List<User> wholeUsers;

	/**
	 * ############# intent 传过来###################
	 */
	private String departGuid; // 默认是自己
	private String departName; // 抬头

	protected OnClickListener mClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ranking_backBtn:
				// 返回
				finish();
				break;
			case R.id.ranking_teamBtn:
				// 团队排名
				// rankIndex = TEAM_RANKING;
				// orderIndex = TASK_ORDER;
				selectRanking(TEAM_RANKING, TASK_ORDER);
				break;
			case R.id.ranking_perBtn:
				// 个人排名
				selectRanking(isSSXSM()?PER_SM_RANKING:PER_RANKING, TASK_ORDER);
				break;
			case R.id.ranking_taskOkBtn:
				// 完成任务排名
				selectRanking(rankIndex, TASK_ORDER);
				break;
			case R.id.ranking_dailyCaseBtn:
				// 每日一题排名
				selectRanking(rankIndex, DAILY_ORDER);
				break;
			case R.id.ranking_mStudyBtn:
				// 自我学习排名
				selectRanking(rankIndex, STUDY_ORDER);
				break;
			case R.id.ranking_searchBtn:
				// 搜索关键字
				// finish();
				break;
			}
		}
	};

	@Override
	protected void addListener() {
		// TODO Auto-generated method stub
		super.addListener();

		searchInput.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				switch(rankIndex){
				case TEAM_RANKING:
					loadDepartsRanking(TEAM_RANKING);
					break;
				case PER_RANKING:
					loadUsersRanking();
					break;
				case PER_SM_RANKING:
					loadDepartsRanking(PER_SM_RANKING);
					break;
				}
//				if (rankIndex == TEAM_RANKING) {
//					loadDepartsRanking(TEAM_RANKING);
//				} else {
//					loadUsersRanking();
//				}

			}
		});

		mList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (rankIndex == PER_RANKING) {
					return;
				}

				Ranking rank = mRankingAdapter.getItem(arg2);
				if (rank instanceof DepartmentRanking) {
					DepartmentRanking depart = (DepartmentRanking) rank;
					Intent i = new Intent(RankingActivity.this,
							RankingActivity.class);
					i.putExtra("departGuid", depart.departmentGuid);
					i.putExtra("departName", depart.departmentName);
					startActivity(i);
				}

			}
		});
	}

	@Override
	protected void initialize() {
		// TODO Auto-generated method stub
		super.initialize();
		departGuid = getIntent().getStringExtra("departGuid");
		departName = getIntent().getStringExtra("departName");

		if (departGuid == null || "".equals(departGuid)) {
			// 首次进入
			topLayout.setVisibility(View.VISIBLE);
			rankIndex = isSSXSM() ? PER_SM_RANKING : PER_RANKING;
		} else {
			// 不是首次进入 隐藏 切换按钮
			topLayout.setVisibility(View.GONE);
			rankIndex = TEAM_RANKING;
		}
		hasLoadUserRanking = false;
		hasLoadDepartRanking = false;

		orderIndex = TASK_ORDER;
		// requestDepartRanking();
		selectRanking(rankIndex, orderIndex);

		titleView.setText(departName);
	}

	/**
	 * 请求所有用户排行榜
	 */
	private void requestUserRanking() {
		if (!hasLoadUserRanking) {
			ZWAsyncTask.excuteTaskWithParas(
					this,
					Command.getRestActionUrl(Command.COMMAND_USERS_RANKING),
					new TypeReference<BaseResult>() {
					},
					new MyDialogTaskHandler<BaseResult>("用户排行榜",
							"正在请求用户排行榜,请稍等...") {
						@Override
						public void minePostResult(final BaseResult baseResult) {
							// TODO Auto-generated method stub
							wholeUsers = JSON.parseArray(baseResult.data,
									User.class);
							hasLoadUserRanking = true;
							loadUsersRanking();
							// RankingDBTask task = new RankingDBTask() {
							// @Override
							// protected Integer doInBackground(
							// ZWDBHelper arg0, Object... arg1) {
							// // TODO Auto-generated method stub
							// mApplication.mDatabaseAdapter
							// .saveDepartmentsRanking(baseResult.departs);
							// return (int) mApplication.mDatabaseAdapter
							// .saveUsersRanking(users);
							// }
							//
							// @Override
							// protected void onPostExecute(Integer result) {
							// // TODO Auto-generated method stub
							// super.onPostExecute(result);
							// hasLoadUserRanking = true;
							// List<User> userList =
							// mApplication.mDatabaseAdapter
							// .getUsersByOrder(getOrderByField(),
							// null, keyword);
							// loadUsersRanking(userList);
							// }
							// };
							// task.execute();

						}
					}, mApplication.mLoginUser.loginname,
					mApplication.mLoginUser.password, departGuid);
		} else {
			loadUsersRanking();
		}
	}

	/**
	 * 
	 * @param rankIndex
	 *            团队排名 or 个人排名
	 * @param orderIndex
	 *            排序
	 */
	private void selectRanking(int rankIndexExr, int orderIndexExr) {
		String keywordExt = searchInput.getText().toString();
		if (rankIndex == rankIndexExr && orderIndex == orderIndexExr
				&& keywordExt != keyword && keywordExt != null
				&& keywordExt.equals(keyword)) {
			return;
		}
		if (rankIndex != rankIndexExr) {
			// 切换前做什么
			switch (rankIndex) {
			case PER_SM_RANKING:
			case PER_RANKING:
				perBtn.setBackgroundResource(R.drawable.ranking_down_nor);
				break;
			case TEAM_RANKING:
				teamBtn.setBackgroundResource(R.drawable.ranking_down_nor);
				break;
			}
		}

		if (orderIndex != orderIndexExr) {
			// 切换前做什么 原来的界面变化
			switch (orderIndex) {
			case STUDY_ORDER:
				mStudyBtn.setBackgroundResource(R.drawable.border_blue);
				break;
			case TASK_ORDER:
				taskOkBtn.setBackgroundResource(R.drawable.border_blue);
				break;
			case DAILY_ORDER:
				dailyCaseBtn.setBackgroundResource(R.drawable.border_blue);
				break;
			}
		}

		orderIndex = orderIndexExr;
		switch (orderIndex) {
		// 切换做什么 界面变化
		case STUDY_ORDER:
			mStudyBtn.setBackgroundResource(R.drawable.border_white);
			break;
		case TASK_ORDER:
			taskOkBtn.setBackgroundResource(R.drawable.border_white);
			break;
		case DAILY_ORDER:
			dailyCaseBtn.setBackgroundResource(R.drawable.border_white);
			break;
		}

		rankIndex = rankIndexExr;
		// 切换做什么
		switch (rankIndex) {
		case PER_SM_RANKING:
			perBtn.setBackgroundResource(R.drawable.ranking_down_pre);
			searchInput.setHint("请输入团队名查询");
			mlistName.setVisibility(View.GONE);
			requestSMDepartRanking();
			break;
		case PER_RANKING:
			perBtn.setBackgroundResource(R.drawable.ranking_down_pre);
			mlistName.setVisibility(View.VISIBLE);
			searchInput.setHint("请输入姓名查询");
			requestUserRanking();
			break;
		case TEAM_RANKING:
			teamBtn.setBackgroundResource(R.drawable.ranking_down_pre);
			mlistName.setVisibility(View.GONE);
			searchInput.setHint("请输入团队名查询");
			requestDepartRanking();
			break;
		}

	}

	/**
	 * 是否是 省市县的 安全管理员
	 * 
	 * @return
	 */
	private boolean isSSXSM() {
		boolean isSSXSm = mApplication.mLoginUser.isSSXSm();
		return isSSXSm && (departGuid == null || "".equals(departGuid));
	}

	/**
	 * 得到排序的属性名 更具 索引
	 * 
	 * @return
	 */
	private String getOrderByField() {
		String str = "taskRanking";
		switch (orderIndex) {
		case STUDY_ORDER:
			str = "selfNum";
			break;
		case TASK_ORDER:
			str = "taskRanking";
			break;
		case DAILY_ORDER:
			str = "dailyNum";
			break;
		}
		return str;
	}

	/**
	 * 个人排行，默认自我学习排行
	 */
	private void loadUsersRanking() {
		// 按排序要求取
		// List<User> userList =
		// mApplication.mDatabaseAdapter.getUsersByOrder("selfNum",null,null);
		// 按排序要求取
		List<User> cacheUsers = orderRanking(PER_RANKING);
		// 将本人加入到此List中
		int myRanking = -1;
		String username = mApplication.mLoginUser.nickname;
		User user = null;
		for (int i = 0; i < cacheUsers.size(); i++) {
			if (cacheUsers.get(i).nickname.equals(username)) {
				myRanking = i;
				user = cacheUsers.get(i);
				break;
			}
		}
		if (user != null) {
			cacheUsers.add(0, user);
		}
		// 下面做UI视图
		if (mRankingAdapter == null) {
			mRankingAdapter = new RankingAdapter(RankingActivity.this,
					cacheUsers, myRanking);
			mList.setAdapter(mRankingAdapter);
		} else {
			mRankingAdapter.setData(cacheUsers);
			mRankingAdapter.setMyRanking(myRanking);
			mRankingAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * 请求所有部门排行榜
	 */
	private void requestDepartRanking() {
		if (!hasLoadDepartRanking) {
			ZWAsyncTask.excuteTaskWithParas(
					this,
					Command.getRestActionUrl(Command.COMMAND_DEPART_RANKING),
					new TypeReference<BaseResult>() {
					},
					new MyDialogTaskHandler<BaseResult>("团队排行榜",
							"正在请求团队排行榜,请稍等...") {

						@Override
						public void minePostResult(BaseResult arg0) {
							// TODO Auto-generated method stub
							wholeDeparts = JSON.parseArray(arg0.data,
									DepartmentRanking.class);
							hasLoadDepartRanking = true;
							loadDepartsRanking(TEAM_RANKING);
						}
					}, mApplication.mLoginUser.loginname,
					mApplication.mLoginUser.password, departGuid);
		} else {
			loadDepartsRanking(TEAM_RANKING);
		}
	}

	/**
	 * 请求所有部门排行榜
	 */
	private void requestSMDepartRanking() {
		if (!hasLoadSMDepartRanking) {
			ZWAsyncTask.excuteTaskWithParas(
					this,
					Command.getRestActionUrl(Command.COMMAND_DEPART_RANKING),
					new TypeReference<BaseResult>() {
					},
					new MyDialogTaskHandler<BaseResult>("团队排行榜",
							"正在请求团队排行榜,请稍等...") {

						@Override
						public void minePostResult(BaseResult arg0) {
							// TODO Auto-generated method stub
							wholeSMDeparts = JSON.parseArray(arg0.data,
									DepartmentRanking.class);
							hasLoadSMDepartRanking = true;
							loadDepartsRanking(PER_SM_RANKING);
						}
					}, mApplication.mLoginUser.loginname,
					mApplication.mLoginUser.password,
					mApplication.mLoginUser.department.departmentGuid);
		} else {
			loadDepartsRanking(PER_SM_RANKING);
		}
	}

	/**
	 * 排序方法
	 * 
	 * @return
	 */
	private <T extends Ranking> List<T> orderRanking(int rankingIndex) {
		String keyword1 = searchInput.getText().toString();
		List<T> cacheList = new ArrayList<T>();

		List<T> iteration = null;
//		(List<T>) (rankingIndex == TEAM_RANKING ? wholeDeparts
//				: wholeUsers);
		switch (rankingIndex) {
		case TEAM_RANKING:
			iteration = (List<T>)wholeDeparts;
			break;
		case PER_RANKING:
			iteration = (List<T>)wholeUsers;
			break;
		case PER_SM_RANKING:
			iteration = (List<T>)wholeSMDeparts;
			break;
		default:
			break;
		}

		for (T t : iteration) {
			String name = "";
			if (t instanceof User) {
				name = ((User) t).nickname;
			} else if (t instanceof DepartmentRanking) {
				name = ((DepartmentRanking) t).departmentName;
			}
			if (name.indexOf(keyword1) == -1) {
				continue;
			}
			cacheList.add(t);
		}
		// 排序
		Collections.sort(cacheList, new Comparator<T>() {
			@Override
			public int compare(T lhs, T rhs) {
				// TODO Auto-generated method stub
				if (orderIndex == TASK_ORDER) {
					// 任务排序
					if (lhs.taskRanking < rhs.taskRanking) {
						return 1;
					} else if (lhs.taskRanking > rhs.taskRanking) {
						return -1;
					}
				} else if (orderIndex == DAILY_ORDER) {
					// 每日
					if (lhs.dailyNum < rhs.dailyNum) {
						return 1;
					} else if (lhs.dailyNum > rhs.dailyNum) {
						return -1;
					}
				} else if (orderIndex == STUDY_ORDER) {
					// 自学
					if (lhs.selfNum < rhs.selfNum) {
						return 1;
					} else if (lhs.selfNum > rhs.selfNum) {
						return -1;
					}
				}

				return 0;
			}

		});
		return cacheList;
	}

	// private List<DepartmentRanking> orderDeparts() {
	// String keyword1 = searchInput.getText().toString();
	// List<DepartmentRanking> cacheDeparts = new
	// ArrayList<DepartmentRanking>();
	// for (DepartmentRanking depart : wholeDeparts) {
	// if (depart.departmentName.indexOf(keyword1) == -1) {
	// continue;
	// }
	// cacheDeparts.add(depart);
	// }
	// // 排序
	// Collections.sort(cacheDeparts, new Comparator<DepartmentRanking>() {
	// @Override
	// public int compare(DepartmentRanking lhs, DepartmentRanking rhs) {
	// // TODO Auto-generated method stub
	// if (orderIndex == TASK_ORDER) {
	// // 任务排序
	// if (lhs.taskRanking < rhs.taskRanking) {
	// return 1;
	// } else if (lhs.taskRanking > rhs.taskRanking) {
	// return -1;
	// }
	// } else if (orderIndex == DAILY_ORDER) {
	// // 每日
	// if (lhs.dailyNum < rhs.dailyNum) {
	// return 1;
	// } else if (lhs.dailyNum > rhs.dailyNum) {
	// return -1;
	// }
	// } else if (orderIndex == STUDY_ORDER) {
	// // 自学
	// if (lhs.selfNum < rhs.selfNum) {
	// return 1;
	// } else if (lhs.selfNum > rhs.selfNum) {
	// return -1;
	// }
	// }
	//
	// return 0;
	// }
	//
	// });
	// return cacheDeparts;
	// }

	private void loadDepartsRanking(int rankingIndex) {
		/**
		 * 如果 该部门下面 木有子部门 则显示人员排行
		 */
		if (rankingIndex == TEAM_RANKING && departGuid != null
				&& !"".equals(departGuid) && wholeDeparts.size() == 0) {
			selectRanking(PER_RANKING, TASK_ORDER);
			return;
		}

		// 按排序要求取
		List<DepartmentRanking> cacheDeparts = orderRanking(rankingIndex);

		int myRanking = -1;
		DepartmentRanking mDepartmentRanking = null;
		// 没有关键字时候 插入本部门
		String keyword1 = searchInput.getText().toString();
		if (keyword1 == null || "".equals(keyword1)) {
			// 将本部门加入到此List中
			String mDepartmentName = mApplication.mLoginUser.department.departmentName;
			for (int i = 0; i < cacheDeparts.size(); i++) {
				if (cacheDeparts.get(i).departmentName.equals(mDepartmentName)) {
					myRanking = i;
					mDepartmentRanking = cacheDeparts.get(i);
					break;
				}
			}
			if (mDepartmentRanking != null) {
				cacheDeparts.add(0, mDepartmentRanking);
			}
		}

		// 下面做UI视图
		if (mRankingAdapter == null) {
			mRankingAdapter = new RankingAdapter(RankingActivity.this,
					cacheDeparts, myRanking);
			mList.setAdapter(mRankingAdapter);
		} else {
			mRankingAdapter.setData(cacheDeparts);
			mRankingAdapter.setMyRanking(myRanking);
			mRankingAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * ############################ DBTask内部类 ################################
	 */
	abstract class RankingDBTask extends DBAsyncTask {
		private ProgressDialog progressDialog = null;

		public RankingDBTask() {
			super(mApplication.mDatabaseAdapter, true);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = new ProgressDialog(RankingActivity.this);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.setTitle("数据缓存");
			progressDialog.setMessage("正在做数据缓存,请稍等...");
			progressDialog.setCancelable(false);

			progressDialog.show();
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (progressDialog != null && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
		}

	}

}
