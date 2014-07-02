package com.axb.android.util;

import java.io.File;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.axb.android.service.Command;

/**
 * 设置类
 * 
 * @author Administrator
 * 
 */
public class Setting {
	private static Setting mySetting;
	public String webServiceUrl = "http://58.210.169.162"; // 服务器地址www.anxunbao.cn
	public int port; // 服务器地址
	public int pageSize; // 分页
	public String account, password;
	public boolean isSavePwd;// 是否保存密码
	public boolean isAutoLogin; //是否自动登录
	
	private String cacheDirName;
	public static File cacheDir; // 缓存路径

	private Setting(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				"settings", Context.MODE_PRIVATE);
		webServiceUrl = sharedPreferences.getString("webServiceUrl",
				webServiceUrl);
		port = sharedPreferences.getInt("port", 9005); //8081

		Command.SERVICE_URL = webServiceUrl + ":" + port;

		account = sharedPreferences.getString("account", "");
		password = sharedPreferences.getString("password", "");
		isSavePwd = sharedPreferences.getBoolean("isSavePwd", false);
		pageSize = sharedPreferences.getInt("pageSize", 20);

		isAutoLogin = sharedPreferences.getBoolean("isAutoLogin", false);
		cacheDirName = sharedPreferences.getString("cacheDirName",
				"anxunbao");

		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			cacheDir = new File(
					android.os.Environment.getExternalStorageDirectory(),
					cacheDirName);
		} else {
			cacheDir = context.getCacheDir();
		}

		if (!cacheDir.exists()) {
			cacheDir.mkdirs();
		}
	}


	public static Setting newIntance(Context context) {
		if (mySetting == null) {
			mySetting = new Setting(context);
		}
		return mySetting;
	}

	public void saveSettings(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				"settings", Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();// 获取编辑器
		editor.putString("webServiceUrl", webServiceUrl);

		editor.putString("account", account);
		editor.putString("password", password);
		editor.putBoolean("isSavePwd", isSavePwd);

		editor.putInt("port", port);
		editor.putInt("pageSize", pageSize);

		editor.putBoolean("isAutoLogin", isAutoLogin);
		
		editor.commit();
		Command.SERVICE_URL = webServiceUrl + ":" + port;
	}

	
}
