package com.axb.android.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.FloatMath;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.axb.android.R;
import com.axb.android.dto.SelectImageDto;
import com.axb.android.service.UpLoadImageTask;

public class ImageOperatorActivity extends BaseActivity  {
	
	public static final int	RESPONSE_UPDATE_SUCCESS = 0x01;	
	
	private ImageView is;
	private Button backButton,sendBtn;
	private SelectImageDto mSelectImageDto;
	private Bitmap bitmap;
	private String fileName;
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void initialize() {
		// TODO Auto-generated method stub
		mSelectImageDto = (SelectImageDto)getIntent().getSerializableExtra("data");

		is = (ImageView) findViewById(R.id.img_view_img);

		backButton = (Button) findViewById(R.id.img_back);
		sendBtn = (Button) findViewById(R.id.img_send);
		
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		sendBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//�����ļ�
				int index = mSelectImageDto.displayName.lastIndexOf('.');
//				String fileName ;
				if (index == -1 || index == 0){
					fileName = mApplication.mLoginUser.guid+".png";
				}else{
					fileName = mApplication.mLoginUser.guid+mSelectImageDto.displayName.substring(index, mSelectImageDto.displayName.length());
				}
				UpLoadImageTask mUpLoadImageTask = new UpLoadImageTask(ImageOperatorActivity.this, fileName);
				mUpLoadImageTask.execute(bitmap);
			}
		});
		
		/**
		 * ����ͼƬ
		 */
		if(mSelectImageDto != null
				&& mSelectImageDto.path!=null){
//			File imgFile = new File(mSelectImageDto.path);
//			if(imgFile.exists()){
				
//			}
//			new BitmapDrawable(mSelectImageDto.path).getBitmap();
			bitmap = BitmapFactory.decodeFile(mSelectImageDto.path);
//			bitmap.compress(CompressFormat., quality, stream);
			is.setImageBitmap(bitmap);
//			is.setBackgroundColor(0xFFcfddea);
			//ͼƬ����
			Matrix m = is.getImageMatrix();
			Display display = ((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
			int screenWidth = display.getWidth();
			int screenHeight = display.getHeight();
			float offectX,offectY;
			if(mSelectImageDto.width >= screenWidth){
				offectX = 0;
			}else{
				offectX = (screenWidth - mSelectImageDto.width)/2;
			}
			
			if(mSelectImageDto.height >= screenHeight){
				offectY = 0;
			}else{
				offectY = (screenHeight - mSelectImageDto.height)/2;
			}
			m.postTranslate(offectX, offectY);
			is.setImageMatrix(m);
			
			is.setOnTouchListener(new OnTouchListener() {		
				private PointF startPoint = new PointF();
		    	private Matrix matrix = new Matrix();
		    	private Matrix currentMatrix = new Matrix();
		    	private int mode = 0;
		    	private static final int DRAG = 1;
		    	private static final int ZOOM = 2;
		    	private float startDis;//��ʼ����
		    	private PointF midPoint;//�м��
		    	
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction() & MotionEvent.ACTION_MASK) {
					case MotionEvent.ACTION_DOWN://��ָѹ����Ļ
						mode = DRAG;
						currentMatrix.set(is.getImageMatrix());//��¼ImageView��ǰ���ƶ�λ��
						startPoint.set(event.getX(), event.getY());
						break;

					case MotionEvent.ACTION_MOVE://��ָ����Ļ�ƶ����� �¼��᲻�ϵش���
						if(mode == DRAG){
							float dx = event.getX() - startPoint.x;//�õ���x����ƶ�����
							float dy = event.getY() - startPoint.y;//�õ���y����ƶ�����
							matrix.set(currentMatrix);//��û�н����ƶ�֮ǰ��λ�û����Ͻ����ƶ�
							matrix.postTranslate(dx, dy);
						}else if(mode == ZOOM){//����
							float endDis = distance(event);//��������
							if(endDis > 10f){
								float scale = endDis / startDis;//�õ����ű���
								matrix.set(currentMatrix);
								matrix.postScale(scale, scale, midPoint.x, midPoint.y);
							}
						}				
						break;
						
					case MotionEvent.ACTION_UP://��ָ�뿪��
					case MotionEvent.ACTION_POINTER_UP://����ָ�뿪��Ļ,����Ļ���д��㣨��ָ��
						mode = 0;
						break;
						
					case MotionEvent.ACTION_POINTER_DOWN://����Ļ���Ѿ��д��㣨��ָ��������һ����ָѹ����Ļ
						mode = ZOOM;
						startDis = distance(event);
						if(startDis > 10f){
							midPoint = mid(event);
							currentMatrix.set(is.getImageMatrix());//��¼ImageView��ǰ�����ű���
						}
						break;
					}
					is.setImageMatrix(matrix);
					return true;
				}
			});
		}else{
			
		}
//		mHandler.sendEmptyMessage(MSG_LOADFILE);
				
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
//		mHandler.removeMessages(MSG_LOADFILE);
		
	}
	
	public void afterTaskDoing(){
		Intent i  = new Intent();
		i.putExtra("fileName", fileName);
		setResult(Activity.RESULT_OK,i);
		finish();
	}


//	@Override
//	public View makeView() {
//		final ImageView i = new ImageView(this){	
//			boolean isFirst = false;
//			@Override
//			public void setImageDrawable(Drawable drawable) {
//				// TODO Auto-generated method stub
//				super.setImageDrawable(drawable);
//				
//				if(drawable instanceof BitmapDrawable  
//						&& !isFirst){
//					isFirst = true;
//					Matrix matrix = new Matrix();
////					System.out.println(getMeasuredWidth()+":"+getMeasuredWidth());
//					Bitmap mBitmap = ((BitmapDrawable)drawable).getBitmap();
////					System.out.println(mBitmap.getWidth()+"====="+mBitmap.getHeight());
//					float scan = 1.0f;
//					if(mBitmap.getWidth() > mBitmap.getHeight()){
//						scan = getMeasuredWidth()*1.0f/mBitmap.getWidth();
//					}else{
//						scan = getMeasuredHeight()*1.0f/mBitmap.getHeight();
//					}
//					
////					Bitmap mBitmap1 = ((BitmapDrawable)getDrawable()).getBitmap();
////					System.out.println(mBitmap1.getWidth()+"====="+mBitmap1.getHeight());
//					matrix.set(getImageMatrix());
//					scan = Math.max(scan, 1)*1.5f;
//					matrix.postScale(scan, scan);
////					matrix.postTranslate(0,0);
////					matrix.postTranslate((getMeasuredWidth()-mBitmap.getWidth())/2,
////							(getMeasuredHeight()-mBitmap.getHeight())/2);
//					setImageMatrix(matrix);
//				}
//				
//				
//				
//			}
//			
//			
//		};
		
		
//		Matrix currentMatrix = new Matrix();
//		currentMatrix.
		
		
//	}



	  /**
     * ��������֮��ľ���
     * @param event
     * @return
     */
	public static float distance(MotionEvent event) {
		float dx = event.getX(1) - event.getX(0);
		float dy = event.getY(1) - event.getY(0);
		return FloatMath.sqrt(dx*dx + dy*dy);
	}
	/**
	 * ��������֮����м��
	 * @param event
	 * @return
	 */
	public static PointF mid(MotionEvent event){
		float midX = (event.getX(1) + event.getX(0)) / 2;
		float midY = (event.getY(1) + event.getY(0)) / 2;
		return new PointF(midX, midY);
	}

}
