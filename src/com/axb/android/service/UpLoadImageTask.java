package com.axb.android.service;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.Toast;

import com.axb.android.ui.ImageOperatorActivity;




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
		mProgressDialog.setTitle("上传头像");
		mProgressDialog.setMessage("正在往服务器上传头像,请稍等...");
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
				Toast.makeText(mContext, "上传成功!", Toast.LENGTH_LONG).show();
				if(mContext instanceof ImageOperatorActivity){
					ImageOperatorActivity mImageOperatorView = (ImageOperatorActivity)mContext;
					mImageOperatorView.afterTaskDoing();
				}
			}
		}else{
			Toast.makeText(mContext, "上传失败,上传的网络路径不正确!", Toast.LENGTH_LONG).show();
		}		
		
	}
	

    /* 上传文件至Server的方法 */

    private boolean uploadFile(String fileName,Bitmap sendImg){
    	boolean isUploadSuccess = true;
      String end = "\r\n";
      String twoHyphens = "--";
      String boundary = "*****";
      try{
        URL url =new URL(Command.getUploadImgPath());
        HttpURLConnection con=(HttpURLConnection)url.openConnection();
        /* 允许Input、Output，不使用Cache */
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setUseCaches(false);
        /* 设置传送的method=POST */
        con.setRequestMethod("POST");
        /* setRequestProperty */
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Charset", "UTF-8");
        con.setRequestProperty("Content-Type",
                           "multipart/form-data;boundary="+boundary);
        /* 设置DataOutputStream */
        DataOutputStream ds = new DataOutputStream(con.getOutputStream());
        ds.writeBytes(twoHyphens + boundary + end);
        ds.writeBytes("Content-Disposition: form-data; " +
                      "name=\"file1\";filename=\"" +
			fileName +"\"" + end);
        ds.writeBytes(end);   
        sendImg.compress(Bitmap.CompressFormat.PNG, 100, ds);
        
//        ByteArrayInputStream bis = new ByteArrayInputStream(CommonUtil.Bitmap2Bytes(sendImg));
//        /* 取得文件的FileInputStream */
//        FileInputStream fStream = new FileInputStream(uploadFile);
//
//        /* 设置每次写入1024bytes */

//        int bufferSize = 1024;
//
//        byte[] buffer = new byte[bufferSize];
//
//
//        int length = -1;
////
////        /* 从文件读取数据至缓冲区 */
////
//        while((length = bis.read(buffer)) != -1){
//          /* 将资料写入DataOutputStream中 */
//          ds.write(buffer, 0, length);
//        }
        ds.writeBytes(end);
        ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
        /* close streams */
//        bis.close();
        ds.flush();    	 
        /* 取得Response内容 */
        java.io.InputStream is = con.getInputStream();
        int ch;
        StringBuffer b =new StringBuffer();
        while( ( ch = is.read() ) != -1 ){
          b.append( (char)ch );
        }
        /* 将Response显示于Dialog */
//        showDialog("上传成功"+b.toString().trim());
        /* 关闭DataOutputStream */
        ds.close();
//        is.close();
        con.disconnect();
      }
      catch(Exception e)
      {
//        showDialog("上传失败"+e);
//    	  Toast.makeText(mContext, "上传失败!", Toast.LENGTH_LONG).show();
    	  e.printStackTrace();
    	  isUploadSuccess = false;
      }
//      sendImg.recycle();
//      System.gc();
      return isUploadSuccess;
    }
}
