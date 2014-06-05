package com.axb.android.util;

import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MyWebViewClient extends WebViewClient {
	
	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {

		return super.shouldOverrideUrlLoading(view, url);
	}

	@Override
	public void onPageFinished(WebView view, String url) {

		view.getSettings().setJavaScriptEnabled(true);

		super.onPageFinished(view, url);
		// html�������֮����Ӽ���ͼƬ�ĵ��js����
		addImageClickListner(view);

	}

	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		view.getSettings().setJavaScriptEnabled(true);

		super.onPageStarted(view, url, favicon);
	}

	@Override
	public void onReceivedError(WebView view, int errorCode,
			String description, String failingUrl) {

		super.onReceivedError(view, errorCode, description, failingUrl);

	}

	// ע��js��������
	private void addImageClickListner(WebView contentWebView) {
		// ���js�����Ĺ��ܾ��ǣ��������е�img���㣬�����onclick�������ڻ���ִ�е�ʱ����ñ��ؽӿڴ���url��ȥ
		contentWebView.loadUrl("javascript:(function(){"
				+ "var objs = document.getElementsByTagName(\"img\"); "
				+ "for(var i=0;i<objs.length;i++)  " + "{"
				+ "    objs[i].onclick=function()  " + "    {  "
				+ "        window.imagelistner.openImage(this.src);  "
				+ "    }  " + "}" + "})()");
	}
}
