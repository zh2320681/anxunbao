package com.axb.android.ui;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.shrek.base.util.rest.ZWAsyncTask;

import com.alibaba.fastjson.TypeReference;
import com.axb.android.R;
import com.axb.android.dto.BaseBo;
import com.axb.android.dto.BaseResult;
import com.axb.android.dto.SelectImageDto;
import com.axb.android.service.Command;
import com.axb.android.service.GetUserRateTask;
import com.axb.android.service.MyDialogTaskHandler;
import com.axb.android.service.UpdateUserFaceTask;
import com.axb.android.service.UpdateUserSignTask;
import com.axb.android.util.CommonUtil;

public class PersonInfoActivity extends BaseActivity {
	private static final int REQUEST_IMAGE = 0x02; // 图片浏览
	public static final int REQUEST_IMAGE_OPERATOR = 0x03; // 图片操作

	private static final int MAX_IMAGE_SIZE = 1024 * 1024;

	private TextView nameView, starView, rateView, companyView, jobView;
	private EditText signView;
	private Button editView, backBtn, switchUserBtn, lookOverBtn, modifyPwBtn;
	private Button lineBtn, captialConBtn, substationBtn, marketBtn;
	private String editText;
	private ImageView faceView;
	private String fileName;

	private boolean isLine, isCaptialCon, isSubstation, isMarket;

	@Override
	protected void initialize() {
		init();
		initPerInfo();
		addHandler();
		userRateTask();
	}

	private void init() {
		nameView = (TextView) findViewById(R.id.pi_name);
		starView = (TextView) findViewById(R.id.pi_star);
		rateView = (TextView) findViewById(R.id.pi_rate);
		companyView = (TextView) findViewById(R.id.pi_company);
		jobView = (TextView) findViewById(R.id.pi_job);

		signView = (EditText) findViewById(R.id.pi_sign);
		signView.setEnabled(false);

		editView = (Button) findViewById(R.id.pi_edit);
		switchUserBtn = (Button) findViewById(R.id.pi_switch);
		modifyPwBtn = (Button) findViewById(R.id.pi_modify);

		faceView = (ImageView) findViewById(R.id.pi_face);

		backBtn = (Button) findViewById(R.id.pi_back);
		lookOverBtn = (Button) findViewById(R.id.pi_lookover);

		lineBtn = (Button) findViewById(R.id.pi_line);
		captialConBtn = (Button) findViewById(R.id.pi_captialCon);
		substationBtn = (Button) findViewById(R.id.pi_substation);
		marketBtn = (Button) findViewById(R.id.pi_market);
	}

	/**
	 * 初始化个人信息
	 */
	private void initPerInfo() {
		if (mApplication.mLoginUser != null) {
			if (mApplication.mLoginUser.nickname != null) {
				nameView.setText(mApplication.mLoginUser.nickname);
			}
			if (mApplication.mLoginUser.department.departmentName != null) {
				companyView
						.setText(mApplication.mLoginUser.department.departmentName);
			}
			if (mApplication.mLoginUser.jobName != null) {
				jobView.setText(mApplication.mLoginUser.jobName);
			}
		}

		if (mApplication.mLoginUser != null
				&& mApplication.mLoginUser.aqxy != null
				&& !"".equals(mApplication.mLoginUser.aqxy.trim())) {
			signView.setText(mApplication.mLoginUser.aqxy);
		}

		mApplication.mImageLoader.newInstance(this,
				mApplication.mSetting.cacheDir);
		mApplication.mImageLoader.displayImage(Command.getUserImgPath()
				+ mApplication.mLoginUser.userimg, faceView, false);

		// 个人专业
		if (mApplication.mLoginUser.specialty == null
				|| "".equals(mApplication.mLoginUser.specialty)) {
			lineBtn.setBackgroundResource(R.drawable.checkbox_normal);
			captialConBtn.setBackgroundResource(R.drawable.checkbox_normal);
			substationBtn.setBackgroundResource(R.drawable.checkbox_normal);
			marketBtn.setBackgroundResource(R.drawable.checkbox_normal);
			isLine = isCaptialCon = isSubstation = isMarket = false;
		} else {
			if (mApplication.mLoginUser.specialty.contains("线路")) {
				lineBtn.setBackgroundResource(R.drawable.checkbox_pressed);
				isLine = true;
			} else {
				lineBtn.setBackgroundResource(R.drawable.checkbox_normal);
				isLine = false;
			}
			if (mApplication.mLoginUser.specialty.contains("基建")) {
				captialConBtn
						.setBackgroundResource(R.drawable.checkbox_pressed);
				isCaptialCon = true;
			} else {
				captialConBtn.setBackgroundResource(R.drawable.checkbox_normal);
				isCaptialCon = false;
			}
			if (mApplication.mLoginUser.specialty.contains("变电")) {
				substationBtn
						.setBackgroundResource(R.drawable.checkbox_pressed);
				isSubstation = true;
			} else {
				substationBtn.setBackgroundResource(R.drawable.checkbox_normal);
				isSubstation = false;
			}
			if (mApplication.mLoginUser.specialty.contains("营销")) {
				marketBtn.setBackgroundResource(R.drawable.checkbox_pressed);
				isMarket = true;
			} else {
				marketBtn.setBackgroundResource(R.drawable.checkbox_normal);
				isMarket = false;
			}
		}
	}

	private void addHandler() {
		backBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				updatePerInfo();
			}
		});

		editView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				editText = signView.getText().toString().trim();
				if (!signView.isEnabled()) {
					signView.setEnabled(true);
					editView.setText("完成");
					signView.requestFocus();
					if (editText == null || editText.equals("")) {
						signView.setHint("");
					}
					signView.setSelection(editText.length());
					return;
				}

				if (editText.equals(mApplication.mLoginUser.aqxy)) {
					Toast.makeText(PersonInfoActivity.this, "你未编辑安全宣言!",
							Toast.LENGTH_SHORT).show();
				} else {

					AlertDialog.Builder builder = new AlertDialog.Builder(
							PersonInfoActivity.this);
					builder.setTitle("更新宣言");
					builder.setMessage("是否更新您的宣言?");
					builder.setPositiveButton("确认",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									UpdateUserSignTask mUpdateUserSignTask = new UpdateUserSignTask(
											PersonInfoActivity.this, "更新宣言",
											"正在更新用户宣言信息,请稍等...");
									BaseBo mBaseBo = new BaseBo();
									mBaseBo.requestUrl = Command
											.getAction(Command.COMMAND_SIGN);
									mBaseBo.maps.put("userName",
											mApplication.mSetting.account);
									mBaseBo.maps.put("password",
											mApplication.mSetting.password);
									mBaseBo.maps.put("sign", editText);
									mUpdateUserSignTask.execute(mBaseBo);
								}
							});

					builder.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									finish();
								}
							});
					builder.create().show();

				}
			}
		});

		faceView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 隐式意图
				try {
					Intent intent = new Intent(
							Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					startActivityForResult(intent, REQUEST_IMAGE);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Toast.makeText(getApplicationContext(), "无法打开系统的图库!",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		modifyPwBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(PersonInfoActivity.this, ModifyPwActivity.class);
				startActivity(intent);
			}
		});

		switchUserBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						PersonInfoActivity.this);
				builder.setTitle("更换用户");
				builder.setMessage("是否确认更换用户?");
				builder.setPositiveButton("确认",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								mApplication.clearData();
								Intent i = getBaseContext().getPackageManager()
										.getLaunchIntentForPackage(
												getBaseContext()
														.getPackageName());
								i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(i);
							}
						});

				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}
						});
				builder.create().show();
			}
		});

		lookOverBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				i.setClass(PersonInfoActivity.this, StudyRecordActivity.class);
				startActivity(i);
			}
		});

		lineBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isLine) {
					lineBtn.setBackgroundResource(R.drawable.checkbox_normal);
					isLine = false;
				} else {
					lineBtn.setBackgroundResource(R.drawable.checkbox_pressed);
					isLine = true;
				}
			}
		});
		;
		captialConBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isCaptialCon) {
					captialConBtn
							.setBackgroundResource(R.drawable.checkbox_normal);
					isCaptialCon = false;
				} else {
					captialConBtn
							.setBackgroundResource(R.drawable.checkbox_pressed);
					isCaptialCon = true;
				}
			}
		});
		substationBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isSubstation) {
					substationBtn
							.setBackgroundResource(R.drawable.checkbox_normal);
					isSubstation = false;
				} else {
					substationBtn
							.setBackgroundResource(R.drawable.checkbox_pressed);
					isSubstation = true;
				}
			}
		});
		;
		marketBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isMarket) {
					marketBtn.setBackgroundResource(R.drawable.checkbox_normal);
					isMarket = false;
				} else {
					marketBtn
							.setBackgroundResource(R.drawable.checkbox_pressed);
					isMarket = true;
				}
			}
		});
	}

	private void userRateTask() {
		GetUserRateTask mGetUserRateTask = new GetUserRateTask(
				PersonInfoActivity.this, "用户完成率", "正在获取用户完成率信息,请稍等...");
		BaseBo mBaseBo = new BaseBo();
		mBaseBo.requestUrl = Command.getAction(Command.COMMAND_RATE);
		mBaseBo.maps.put("userName", mApplication.mSetting.account);
		mBaseBo.maps.put("password", mApplication.mSetting.password);
		mGetUserRateTask.execute(mBaseBo);
	}

	public void afterUpdateTaskDoing() {
		Toast.makeText(this, "安全宣言更新成功！", Toast.LENGTH_LONG).show();
		mApplication.mLoginUser.aqxy = editText;
		signView.setEnabled(false);
		editView.setText("编辑");
		if (editText == null || editText.equals("")) {
			signView.setHint("来设置一下自己的安全宣言吧！");
		}
	}

	public void afterFaceTaskDoing() {
		Toast.makeText(this, "头像更新成功！", Toast.LENGTH_LONG).show();
		mApplication.mLoginUser.userimg = fileName;
		// 情况缓冲
		mApplication.mImageLoader.clear();
		mApplication.mImageLoader.displayImage(Command.getUserImgPath()
				+ fileName, faceView, false);
	}

	public void afterRateTaskDoing(String rate) {
		float f = Float.parseFloat(rate);
		rateView.setText(CommonUtil.formatFloat(f * 100) + "%");
		starView.setText(CommonUtil.getStudyRateStar(f));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_IMAGE) {
			// 图片浏览 回来
			// 这个uri，就是返回的选中的图片
			if (data != null) {
				Uri uri = data.getData();
				Cursor cursor = this.getContentResolver().query(uri, null,
						null, null, null);
				if (cursor.moveToFirst()) {
					SelectImageDto dto = new SelectImageDto();
					dto.id = cursor.getInt(0);
					dto.path = cursor.getString(1);
					dto.size = cursor.getLong(2);
					dto.displayName = cursor.getString(3);
					dto.mimeType = cursor.getString(4);
					dto.width = cursor.getInt(18);
					dto.height = cursor.getInt(19);
					// 限制上传文件的大小
					File f = new File(dto.path);
					if (f.length() > MAX_IMAGE_SIZE) {
						Toast.makeText(PersonInfoActivity.this,
								"上传图片太大,不能作为头像!", Toast.LENGTH_SHORT).show();
						return;
					}
					cursor.close();
					// 跳转到 图片编辑
					Intent i = new Intent();
					i.setClass(PersonInfoActivity.this,
							ImageOperatorActivity.class);
					i.putExtra("data", dto);
					startActivityForResult(i, REQUEST_IMAGE_OPERATOR);
				} else {
					Toast.makeText(PersonInfoActivity.this, "您未选择头像图片!",
							Toast.LENGTH_SHORT).show();
				}
			}
		} else if (requestCode == REQUEST_IMAGE_OPERATOR
				&& resultCode == Activity.RESULT_OK) {
			fileName = data.getStringExtra("fileName");

			UpdateUserFaceTask mUpdateUserFaceTask = new UpdateUserFaceTask(
					PersonInfoActivity.this, "用户头像信息", "正在更新用户头像信息,请稍等...");
			BaseBo mBaseBo = new BaseBo();
			mBaseBo.requestUrl = Command.getAction(Command.COMMAND_FACE);
			mBaseBo.maps.put("userName", mApplication.mSetting.account);
			mBaseBo.maps.put("password", mApplication.mSetting.password);
			mBaseBo.maps.put("faceName", fileName);
			mUpdateUserFaceTask.execute(mBaseBo);
		}
	}

	/**
	 * 上传个人信息
	 */
	private boolean updatePerInfo() {
		StringBuffer sb = new StringBuffer();
		if (isLine) {
			sb.append((sb.length() == 0 ? "" : ",") + "线路");
		}

		if (isCaptialCon) {
			sb.append((sb.length() == 0 ? "" : ",") + "基建");
		}
		if (isSubstation) {
			sb.append((sb.length() == 0 ? "" : ",") + "变电");
		}

		if (isMarket) {
			sb.append((sb.length() == 0 ? "" : ",") + "营销");
		}

		final String specialty = sb.toString();
		if ((mApplication.mLoginUser.specialty == null && specialty.length() == 0)
				|| specialty.equals(mApplication.mLoginUser.specialty)) {
			finish();
			return false;
		}

		// UpdateSpecialtySignTask mUpdateSpecialtySignTask = new
		// UpdateSpecialtySignTask(
		// PersonInfoActivity.this, "用户专业信息", "正在提交用户专业信息,请稍等...");
		// BaseBo mBaseBo = new BaseBo();
		// mBaseBo.requestUrl = Command.getAction(Command.COMMAND_FACE);
		// mBaseBo.maps.put("userName", mApplication.mSetting.account);
		// mBaseBo.maps.put("password", mApplication.mSetting.password);
		// mBaseBo.maps.put("specialty", specialty);
		// mUpdateSpecialtySignTask.execute(mBaseBo);

		AlertDialog.Builder build = new AlertDialog.Builder(
				PersonInfoActivity.this);
		build.setTitle("专业信息提交").setMessage("你的专业信息已经修改,是否提交?")
				.setIcon(R.drawable.alert3)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						updateUserSpecInfo(specialty);
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				}).create();
		build.show();

		return true;
	}

	private void updateUserSpecInfo(final String specialty) {
		ZWAsyncTask
				.excuteTaskWithParas(
						this,
						Command.getRestActionUrl(Command.COMMAND_SPECIALTY_COMMIT),
						new TypeReference<BaseResult>() {
						},
						new MyDialogTaskHandler<BaseResult>("用户排行榜",
								"正在请求用户排行榜,请稍等...") {

							@Override
							public void minePostResult(BaseResult arg0) {
								// TODO Auto-generated method stub
								if (arg0.isSuccess()) {
									Toast.makeText(getApplicationContext(),
											"用户专业信息提交成功!", Toast.LENGTH_SHORT)
											.show();
									mApplication.mLoginUser.specialty = specialty;
								} else {
									Toast.makeText(getApplicationContext(),
											"用户专业信息提交失败!", Toast.LENGTH_SHORT)
											.show();
								}
								finish();
							}
						}, mApplication.mSetting.account,
						mApplication.mSetting.password, specialty);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return updatePerInfo();
		}
		return super.onKeyDown(keyCode, event);
	}
}
