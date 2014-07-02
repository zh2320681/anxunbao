package com.axb.android.ui;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.axb.android.R;
import com.axb.android.util.CommonUtil;
import com.axb.android.util.Setting;

public class SettingActivity extends BaseActivity {

	private Spinner fenye;
	private EditText webAddress, port;
	private Button save, cz;
	private Button backBtn;
	private Setting mySetting;

	@Override
	protected void initialize() {
		// TODO Auto-generated method stub
//		setContentView(R.layout.setting);
		init();
		addListener();

	}

	/**
	 * 初始化
	 */
	private void init() {
		mySetting = mApplication.mSetting;

		fenye = (Spinner) findViewById(R.id.setting_fenye);
		webAddress = (EditText) findViewById(R.id.setting_webaddress);
		port = (EditText) findViewById(R.id.setting_port);

		save = (Button) findViewById(R.id.setting_save);
		cz = (Button) findViewById(R.id.setting_cz);

		backBtn = (Button) findViewById(R.id.set_back);
		ArrayAdapter fenyeAdapter = ArrayAdapter.createFromResource(
				SettingActivity.this, R.array.pagesizes,
				android.R.layout.simple_spinner_item);
		fenyeAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		fenye.setAdapter(fenyeAdapter);

		webAddress.setText(mySetting.webServiceUrl);
		port.setText(mySetting.port + "");
		// System.out.println("bo.fenye===>"+bo.fenye);
		switch (mySetting.pageSize) {
		case 5:
			fenye.setSelection(0);
			break;
		case 6:
			fenye.setSelection(1);
			break;
		case 7:
			fenye.setSelection(2);
			break;
		case 8:
			fenye.setSelection(3);
			break;
		case 9:
			fenye.setSelection(4);
			break;
		case 10:
			fenye.setSelection(5);
			break;
		case 11:
			fenye.setSelection(6);
			break;
		case 12:
			fenye.setSelection(7);
			break;
		case 15:
			fenye.setSelection(8);
			break;
		}

	}

	/**
	 * 监听器
	 */
	protected void addListener() {
		cz.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				webAddress.setText(CommonUtil.getIp(mySetting.webServiceUrl));
				port.setText(CommonUtil.getPort(mySetting.webServiceUrl));
				switch (mySetting.pageSize) {
				case 5:
					fenye.setSelection(0);
					break;
				case 6:
					fenye.setSelection(1);
					break;
				case 7:
					fenye.setSelection(2);
					break;
				case 8:
					fenye.setSelection(3);
					break;
				case 9:
					fenye.setSelection(4);
					break;
				case 10:
					fenye.setSelection(5);
					break;
				case 11:
					fenye.setSelection(6);
					break;
				case 12:
					fenye.setSelection(7);
					break;
				case 15:
					fenye.setSelection(8);
					break;

				}
			}
		});
		
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String webStr = webAddress.getText().toString().trim();
				if (webStr.length() <= 0) {
					Toast.makeText(SettingActivity.this, "请输入新的服务器地址!",
							Toast.LENGTH_LONG).show();
					return;
				}
				String portStr = port.getText().toString().trim();
				if (portStr.length() <= 0) {
					Toast.makeText(SettingActivity.this, "请输入新的端口号!",
							Toast.LENGTH_LONG).show();
					return;
				}
				mySetting.pageSize = Integer.parseInt(fenye.getSelectedItem()
						.toString());
				mySetting.webServiceUrl = webStr;
				mySetting.port = Integer.parseInt(portStr);
				mySetting.saveSettings(SettingActivity.this);
				Toast.makeText(SettingActivity.this, "设置参数保存成功！", Toast.LENGTH_LONG)
						.show();
			}
		});

		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();

			}
		});
	}

	public void afterTaskDo() {
		mySetting.webServiceUrl = CommonUtil.spliceWebAddress(webAddress.getText().toString(), port.getText().toString());
		System.out.println(mySetting.webServiceUrl);
		mySetting.saveSettings(this);
	}

	public Spinner getFenye() {
		return fenye;
	}

	public void setFenye(Spinner fenye) {
		this.fenye = fenye;
	}

	public EditText getWebAddress() {
		return webAddress;
	}

	public void setWebAddress(EditText webAddress) {
		this.webAddress = webAddress;
	}

}
