package com.ruin.view;




import com.example.ennew.BuildConfig;
import com.example.ennew.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class SearchDevicesView extends BaseView{
	
	public static final String TAG = "SearchDevicesView";
	public static final boolean D  = BuildConfig.DEBUG; 
	private CheckStateListener checkStateListener ;
	@SuppressWarnings("unused")
	private long TIME_DIFF = 1500;
	
	int[] lineColor = new int[]{0x7B, 0x7B, 0x7B};
	int[] innerCircle0 = new int[]{0xb9, 0xff, 0xFF};
	int[] innerCircle1 = new int[]{0xdf, 0xff, 0xFF};
	int[] innerCircle2 = new int[]{0xec, 0xff, 0xFF};
	
	int[] argColor = new int[]{0xF3, 0xf3, 0xfa};
	
	private float offsetArgs = 0;
	private boolean isSearching = false;
	private Bitmap bmpBg;
	private Bitmap bmpButton;
	private Bitmap bmpButton2;
	private Bitmap bmpNode;
	private Bitmap targetbg ;
	private Bitmap targetbtn ;
	private Bitmap targetbtn2 ;
	private Bitmap targetnode ;
	private boolean sign=true;
	private boolean isOpen=false;
	
	public boolean isSearching() {
		return isSearching;
	}

	public void setSearching(boolean isSearching) {
		this.isSearching = isSearching;
		offsetArgs = 0;
		invalidate();
	}

	public SearchDevicesView(Context context) {
		super(context);
		initBitmap();
	}
	
	public SearchDevicesView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initBitmap();
	}

	public SearchDevicesView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initBitmap();
	}
	
	private void initBitmap(){
		if(bmpBg == null){
			bmpBg = Bitmap.createBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.gplus_search_bg));
			
			
		}
		if(bmpButton == null){
			bmpButton = Bitmap.createBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.open));
			bmpButton2 = Bitmap.createBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.close));
			 
		}
		if(bmpNode == null){
			bmpNode = Bitmap.createBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.gplus_search_args));
			
		}
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);	
		if(sign) {
			targetnode = zoomImage(bmpNode, getWidth()/2, getHeight() / 2);
			targetbg = zoomImage(bmpBg, getWidth(), getHeight() );
			targetbtn = zoomImage(bmpButton, getWidth()/5, getHeight() / 5);
			targetbtn2 = zoomImage(bmpButton2, getWidth()/5, getHeight() / 5);
			sign=false;
		}
		Log.d("lhh", String.valueOf(getWidth())+"h"+String.valueOf(getHeight()));
		//Bitmap target1 = zoomImage(bitmap, getWidth(), getHeight() );
	    canvas.drawBitmap( targetbg, null, new Rect(0, 0, getWidth(), getHeight()), null );
		if(isSearching){
			
			//Bitmap target2 = zoomImage(bitmap1, getWidth()/2, getHeight()/2 );
			//Bitmap target2 = Bitmap.createBitmap(getWidth()/2, getHeight()/2, bitmap2.getConfig());
			Rect rMoon = new Rect(0,getHeight()/2,getWidth()/2,getHeight());
			canvas.rotate(offsetArgs,getWidth()/2,getHeight()/2);
			canvas.drawBitmap(targetnode,null,rMoon,null);
			Log.d("rec",String.valueOf(targetnode.getWidth()));
			offsetArgs = offsetArgs + 3;
			 canvas.drawBitmap(targetbtn2,  getWidth() / 2 - targetbtn2.getWidth() / 2, getHeight() / 2 - targetbtn2.getHeight() / 2, null);
		}else{
			
			canvas.drawBitmap(targetnode,  0 , getHeight() / 2, null);
			canvas.drawBitmap(targetbtn,  getWidth() / 2 - targetbtn.getWidth() / 2, getHeight() / 2 - targetbtn.getHeight() / 2, null);
		}
		//Bitmap target3 = zoomImage(bmpButton, getWidth()/6, getHeight() / 6);
		
			
	
		   
		
	

		
		if(isSearching) invalidate();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {	
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:		
			handleActionDownEvenet(event);
			return true;
		case MotionEvent.ACTION_MOVE: 
			return true;
		case MotionEvent.ACTION_UP:
			return true;
		}
		return super.onTouchEvent(event);
	}
	
	private void handleActionDownEvenet(MotionEvent event){
		RectF rectF = new RectF(getWidth() / 2 - bmpButton.getWidth() / 2, 
								getHeight() / 2 - bmpButton.getHeight() / 2, 
								getWidth() / 2 + bmpButton.getWidth() / 2, 
								getHeight() / 2 + bmpButton.getHeight() / 2);
		
		if(rectF.contains(event.getX(), event.getY())){
			if(D) Log.d(TAG, "click search device button");
			if(!isSearching()) {
				setSearching(true);
				checkStateListener.onCheckStateListener(true);
			}else{
				setSearching(false);
				checkStateListener.onCheckStateListener(false);
			}
		}
	}
	  public static Bitmap zoomImage(Bitmap bgimage, double newWidth, 
              double newHeight) { 

      float width = bgimage.getWidth(); 
      float height = bgimage.getHeight(); 

      Matrix matrix = new Matrix(); 
  
      float scaleWidth = ((float) newWidth) / width; 
      float scaleHeight = ((float) newHeight) / height; 
 
      matrix.postScale(scaleWidth, scaleHeight); 
      Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width, 
                      (int) height, matrix, true); 
      return bitmap; 
     }

	public CheckStateListener getCheckStateListener() {
		return checkStateListener;
	}

	public void setCheckStateListener(CheckStateListener checkStateListener) {
		this.checkStateListener = checkStateListener;
	}

	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	} 
	  
}