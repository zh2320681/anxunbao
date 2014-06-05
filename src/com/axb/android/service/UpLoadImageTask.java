package com.axb.android.service;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.Toast;

import com.axb.android.MyApplication;
import com.axb.android.ui.ImageOperatorActivity;
import com.axb.android.util.CommonUtil;




public class UpLoadImageTask extends AsyncTask<Bitmap,Integer,Boolean> {
	
	private Context mContext;
	private ProgressDialog mProgressDialog;
	private String fileName;
	
	public UpLoadImageTask(Context mContext,String fileName) {
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
		this.fileName = fileName;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setTitle("�ϴ�ͷ��");
		mProgressDialog.setMessage("�������������ϴ�ͷ��,���Ե�...");
		mProgressDialog.show();
		
	}
	
	@Override
	protected Boolean doInBackground(Bitmap... arg0) {
		// TODO Auto-generated method stub
		Bitmap sendImg = arg0[0];
		return uploadFile(fileName, sendImg);
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}

	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		mProgressDialog.dismiss();
		if(result){
			
			if(mContext != null ){
				Toast.makeText(mContext, "�ϴ��ɹ�!", Toast.LENGTH_LONG).show();
				if(mContext instanceof ImageOperatorActivity){
					ImageOperatorActivity mImageOperatorView = (ImageOperatorActivity)mContext;
					mImageOperatorView.afterTaskDoing();
				}
			}
		}else{
			Toast.makeText(mContext, "�ϴ�ʧ��,�ϴ�������·������ȷ!", Toast.LENGTH_LONG).show();
		}		
		
	}
	

    /* �ϴ��ļ���Server�ķ��� */

    private boolean uploadFile(String fileName,Bitmap sendImg){
    	boolean isUploadSuccess = true;
      String end = "\r\n";
      String twoHyphens = "--";
      String boundary = "*****";
      try{
        URL url =new URL(Command.getUploadImgPath());
        HttpURLConnection con=(HttpURLConnection)url.openConnection();
        /* ����Input��Output����ʹ��Cache */
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setUseCaches(false);
        /* ���ô��͵�method=POST */
        con.setRequestMethod("POST");
        /* setRequestProperty */
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Charset", "UTF-8");
        con.setRequestProperty("Content-Type",
                           "multipart/form-data;boundary="+boundary);
        /* ����DataOutputStream */
        DataOutputStream ds = new DataOutputStream(con.getOutputStream());
        ds.writeBytes(twoHyphens + boundary + end);
        ds.writeBytes("Content-Disposition: form-data; " +
                      "name=\"file1\";filename=\"" +
			fileName +"\"" + end);
        ds.writeBytes(end);   
        sendImg.compress(Bitmap.CompressFormat.PNG, 100, ds);
        
//        ByteArrayInputStream bis = new ByteArrayInputStream(CommonUtil.Bitmap2Bytes(sendImg));
//        /* ȡ���ļ���FileInputStream */
//        FileInputStream fStream = new FileInputStream(uploadFile);
//
//        /* ����ÿ��д��1024bytes */

//        int bufferSize = 1024;
//
//        byte[] buffer = new byte[bufferSize];
//
//
//        int length = -1;
////
////        /* ���ļ���ȡ������������ */
////
//        while((length = bis.read(buffer)) != -1){
//          /* ������д��DataOutputStream�� */
//          ds.write(buffer, 0, length);
//        }
        ds.writeBytes(end);
        ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
        /* close streams */
//        bis.close();
        ds.flush();    	 
        /* ȡ��Response���� */
        java.io.InputStream is = con.getInputStream();
        int ch;
        StringBuffer b =new StringBuffer();
        while( ( ch = is.read() ) != -1 ){
          b.append( (char)ch );
        }
        /* ��Response��ʾ��Dialog */
//        showDialog("�ϴ��ɹ�"+b.toString().trim());
        /* �ر�DataOutputStream */
        ds.close();
//        is.close();
        con.disconnect();
      }
      catch(Exception e)
      {
//        showDialog("�ϴ�ʧ��"+e);
//    	  Toast.makeText(mContext, "�ϴ�ʧ��!", Toast.LENGTH_LONG).show();
    	  e.printStackTrace();
    	  isUploadSuccess = false;
      }
//      sendImg.recycle();
//      System.gc();
      return isUploadSuccess;
    }
}
