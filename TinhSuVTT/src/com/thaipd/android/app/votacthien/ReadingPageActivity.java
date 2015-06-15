package com.thaipd.android.app.votacthien;


import com.startapp.android.publish.banner.Banner;
import com.startapp.android.publish.banner.BannerListener;
import com.xolem.android.app.tinhsuvtt.R;
import com.xolem.android.app.tinhsuvtt.custom.ScrollWebView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ReadingPageActivity extends BaseActivity implements OnClickListener {
	
	
	private boolean needToShowBanner = true;
	private final String message1 = "Chạm 2 lần vào màn hình để chuyển chế độ đ�?c. ";
	private final String message2 = "Dùng phím tăng giảm âm lượng để đi�?u chỉnh font chữ. ";

	private final int FONT_SIZE_MAX = 50;
	private final int FONT_SIZE_MIN = 4;
	private final String DATA_FONT = "Font-size";
	public static final String DATA_PAGE = "Page";
	private final String DATA_MODE = "DarkMode";
	private final String DATA_POSITION_X = "PosX";
	private final String DATA_POSITION_Y = "PosY";
	
	
	
	private TextView tv_title;
	private ImageView ivNext;
	private ImageView ivPre;
	private RelativeLayout layout_banner;
	private ScrollWebView mWebView;
	private boolean isContinueReading = false;

	
	private GestureDetector gestureDetector;

	private int font_size = 15;
	
	@SuppressLint("ClickableViewAccessibility")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.layout_reading);
		initDa();
		initStartApp();
		Banner banner = (com.startapp.android.publish.banner.Banner) findViewById(R.id.startAppBanner); 
		banner.setBannerListener(new BannerListener() {
			
			@Override
			public void onReceiveAd(View arg0) {
				Log.i("StartApp", "onReceiveAd");
				
			}
			
			@Override
			public void onFailedToReceiveAd(View arg0) {
				 Log.d("StartApp", "onFailedToReceiveAd");
				
			}
			
			@Override
			public void onClick(View arg0) {
				Log.d("StartApp", "Click");
				
			}
		});
		Intent i = getIntent();
		if (i != null) {
			currentIndex = i.getIntExtra(AppUtils.INTENT_POSITION, 0);
			if (currentIndex == getPrefInt(DATA_PAGE)) {
				isContinueReading = true;
			}
		}
		
		mWebView = (ScrollWebView) findViewById(R.id.webview_read);
		ivNext = (ImageView) findViewById(R.id.iv_nextChap);
		ivPre = (ImageView) findViewById(R.id.iv_preChap);
		tv_title = (TextView) findViewById(R.id.tv_name);
		layout_banner = (RelativeLayout) findViewById(R.id.layout_banner);
		
		/*
		 * WebView
		 */
		mWebView.getSettings().setBuiltInZoomControls(true);
		mWebView.getSettings().setDisplayZoomControls(false);
		
		mWebView.setInitialScale(0);
		mWebView.getSettings().setLoadWithOverviewMode(true);
		
		mWebView.setOnScrollChangedCallback(new OnScrollChangedCallback() {
			
			@Override
			public void onScroll(int y, int oldy) {
				if (needToShowBanner && oldy > y && layout_banner != null && layout_banner.getVisibility() == View.GONE) {
					showBanner();
				} else if (oldy < y) {
					needToShowBanner = true;
					hideBanner();
				}
				
			}
		});
		
		gestureDetector = new GestureDetector(this, new GestureListener());
		mWebView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return gestureDetector.onTouchEvent(event);
			}
		});
		
		loadContent(false);
		/*
		 * ImageView
		 */
		ivNext.setOnClickListener(this);
		ivPre.setOnClickListener(this);
		
		banner.bringToFront();
		banner.showBanner();
	}
	
	
	@Override
	protected void onPause() {
		savePreviewScroll();
		putPref(DATA_PAGE, currentIndex);
		super.onPause();
	}
	
	private void loadContent(boolean isReload) {
		if (currentIndex > -1 && currentIndex < chapterData.size()) {

			String rawData = getChapContent(chapterData.get(currentIndex)
					.getmTOC());
			String processedData = AppUtils.getInstance(this).processRawData(rawData, getPrefBoolean(DATA_MODE));
			mWebView.loadData(processedData, "text/html; charset=UTF-8", null);
			
			
			String sub1 = AppUtils.getInstance(this).getChapterNumber(rawData);
			String sub2 = AppUtils.getInstance(this).getChapterName(rawData);
			if (sub2 != null && sub2.length() > 0 && sub2.trim().length() > 0) {
				tv_title.setText(sub1 + " : " + sub2);
			} else {
				tv_title.setText(sub1);
			}
			
			// TODO : scroll here
			if (isContinueReading) {
				scrollToPreviousSession(isReload);
			} else {
				Toast.makeText(this, message1 + message2, Toast.LENGTH_LONG).show();
			}

		} else {
			Toast.makeText(this, "�?ã xảy ra lỗi, hãy thoát ứng dụng và vào lại.", Toast.LENGTH_SHORT).show();
		}

	}
	
	private void savePreviewScroll() {
		int x = mWebView.getScrollX();
		int y = mWebView.getScrollY();
		putPref(DATA_POSITION_X, String.valueOf(x));
		putPref(DATA_POSITION_Y, String.valueOf(y));
	}
	
	
	private void showBanner() {
		if (layout_banner.getVisibility() == View.GONE) {
			Animation animation = new TranslateAnimation(0, 0, 100, 0);
			animation.setDuration(500);
			layout_banner.startAnimation(animation);
			layout_banner.setVisibility(View.VISIBLE);
		}
	}
	
	private void hideBanner() {
		if (layout_banner.getVisibility() == View.VISIBLE) {
			Animation animation = new TranslateAnimation(0, 0, 0, 100);
			animation.setDuration(500);
			layout_banner.startAnimation(animation);
			layout_banner.setVisibility(View.GONE);
			
		}
	}
		
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_VOLUME_UP:
			updateFontScale(true);
			return true;
			
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			updateFontScale(false);
			return true;

		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.iv_nextChap:
			currentIndex ++;
			break;
			
		case R.id.iv_preChap:
			currentIndex --;
			break;
			
		}
//		savePreviewScroll();
		isContinueReading = false;
		loadContent(false);
		
	}
	
	private void updateFontScale(boolean isIncrease) {
		if ((font_size + 2 > FONT_SIZE_MAX && isIncrease )|| (font_size - 2< FONT_SIZE_MIN && !isIncrease)) {
			Toast.makeText(this, "Hạn chế zoom", Toast.LENGTH_SHORT).show();
		} else {
			if (isIncrease) {
				font_size += 2;
			} else {
				font_size -= 2;
			}
			mWebView.getSettings().setDefaultFontSize(font_size);
			mWebView.invalidate();
			putPref(DATA_FONT, font_size);
		}
		
	}
	
	
	private void scrollToPreviousSession(boolean isReload) {
		int valueX = 0;
		int valueY = 0;
		String x = getPref(DATA_POSITION_X);
		if (x != null && x.length() > 0) {
			valueX = Integer.parseInt(x);
		}

		String y = getPref(DATA_POSITION_Y);
		if (y != null && y.length() > 0) {
			valueY = Integer.parseInt(y);
		}

		if (isReload) {
			final int pos_x = valueX;
			final int pos_y = valueY;
			mWebView.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					mWebView.scrollTo(pos_x, pos_y);
					
				}
			}, 300);
			
			return;
		}
		if ((valueX + valueY) > 0) {
			if (Build.VERSION_CODES.JELLY_BEAN_MR2 < Build.VERSION.SDK_INT) {
				
				mWebView.scrollTo(valueX, valueY);
				
			} else {
				final int pos_x = valueX;
				final int pos_y = valueY;
				mWebView.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						mWebView.scrollTo(pos_x, pos_y);
						
					}
				}, 100);
			}
		}
		Toast.makeText(this, message1 + message2, Toast.LENGTH_LONG).show();

	}
	
	private class GestureListener extends
	GestureDetector.SimpleOnGestureListener {

		@Override
		public boolean onDoubleTap(MotionEvent e) {
			// TODO Auto-generated method stub
			isContinueReading = true;
			savePreviewScroll();
			boolean currentMode = getPrefBoolean(DATA_MODE);
			putPref(DATA_MODE, !currentMode);
			loadContent(true);
			return true;
		}

}
	
	public interface OnScrollChangedCallback {
		void onScroll(int y, int oldY);
	}
}
