package com.axb.android.ui;

import java.util.List;

import cn.shrek.base.util.rest.ZWAsyncTask;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.axb.android.dto.BaseResult;
import com.axb.android.dto.User;
import com.axb.android.service.Command;
import com.axb.android.service.MyDialogTaskHandler;

public class DepartRankingActivity extends BaseActivity {
	private boolean hasLoadUserRanking;
	private String departGuid;
	private String departName;

	@Override
	protected void addListener() {
		// TODO Auto-generated method stub
		super.addListener();
	}

	@Override
	protected void initialize() {
		// TODO Auto-generated method stub
		super.initialize();
		hasLoadUserRanking = getIntent().getBooleanExtra("hasLoadUserRanking",
				true);
		departGuid = getIntent().getStringExtra("departGuid");
		departName = getIntent().getStringExtra("departName");
	}

	/**
	 * ����Ĳ����û����а�
	 */
	private void requestUserRanking() {
		if (!hasLoadUserRanking) {
			ZWAsyncTask.excuteTaskWithParas(
					this,
					Command.getRestActionUrl(Command.COMMAND_USERS_RANKING),
					new TypeReference<BaseResult>(){},
					new MyDialogTaskHandler<BaseResult>("�û����а�",
							"���������û����а�,���Ե�...") {

						@Override
						public void minePostResult(BaseResult arg0) {
							// TODO Auto-generated method stub
							List<User> users = JSON.parseArray(arg0.data,
									User.class);
							mApplication.mDatabaseAdapter
									.saveUsersRanking(users);
							hasLoadUserRanking = true;

							loadUsersRanking();
						}
					}, mApplication.mLoginUser.loginname,
					mApplication.mLoginUser.password, departGuid);
		} else {
			loadUsersRanking();
		}
	}

	private void loadUsersRanking() {
		// ������Ҫ��ȡ
		List<User> users = mApplication.mDatabaseAdapter.getUsersByOrder(
				"Ҫ������ֶ���,����Ҳ�д��", null,null);
		// ������UI��ͼ
	}

}
