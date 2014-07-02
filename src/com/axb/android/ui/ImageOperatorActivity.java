package com.axb.android.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.FloatMath;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
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
				//发送文件
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
		 * 加载图片
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
			//图片居中
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
		    	private float startDis;//开始距离
		    	private PointF midPoint;//中间点
		    	
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction() & MotionEvent.ACTION_MASK) {
					case MotionEvent.ACTION_DOWN://手指压下屏幕
						mode = DRAG;
						currentMatrix.set(is.getImageMatrix());//记录ImageView当前的移动位置
						startPoint.set(event.getX(), event.getY());
						break;

					case MotionEvent.ACTION_MOVE://手指在屏幕移动，该 事件会不断地触发
						if(mode == DRAG){
							float dx = event.getX() - startPoint.x;//得到在x轴的移动距离
							float dy = event.getY() - startPoint.y;//得到在y轴的移动距离
							matrix.set(currentMatrix);//在没有进行移动之前的位置基础上进行移动
							matrix.postTranslate(dx, dy);
						}else if(mode == ZOOM){//缩放
							float endDis = distance(event);//结束距离
							if(endDis > 10f){
								float scale = endDis / startDis;//得到缩放倍数
								matrix.set(currentMatrix);
								matrix.postScale(scale, scale, midPoint.x, midPoint.y);
							}
						}				
						break;
						
					case MotionEvent.ACTION_UP://手指离开屏
					case MotionEvent.ACTION_POINTER_UP://有手指离开屏幕,但屏幕还有触点（手指）
						mode = 0;
						break;
						
					case MotionEvent.ACTION_POINTER_DOWN://当屏幕上已经有触点（手指），再有一个手指压下屏幕
						mode = ZOOM;
						startDis = distance(event);
						if(startDis > 10f){
							midPoint = mid(event);
							currentMatrix.set(is.getImageMatrix());//记录ImageView当前的缩放倍数
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
     * 计算两点之间的距离
     * @param event
     * @return
     */
	public static float distance(MotionEvent event) {
		float dx = event.getX(1) - event.getX(0);
		float dy = event.getY(1) - event.getY(0);
		return FloatMath.sqrt(dx*dx + dy*dy);
	}
	/**
	 * 计算两点之间的中间点
	 * @param event
	 * @return
	 */
	public static PointF mid(MotionEvent event){
		float midX = (event.getX(1) + event.getX(0)) / 2;
		float midY = (event.getY(1) + event.getY(0)) / 2;
		return new PointF(midX, midY);
	}

}
