package com.thaipd.android.app.truyenkimdung;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;
import com.thaipd.android.app.truyenkimdung.custom.ScrollWebView;
import com.thaipd.android.app.truyenkimdung.data.AppUtils;

public class ReadingPageActivity extends BaseActivity implements
		OnClickListener {

	private final int FONT_SIZE_MAX = 40;
	private final int FONT_SIZE_MIN = 10;
	private final String FONT_KEY = "Font-size";
	
	private ResideMenu mResideMenu;
	private ResideMenuItem itemHome;
	private ResideMenuItem itemBack;
	private ResideMenuItem itemNextChap;
	private ResideMenuItem itemPreChap;
	private ResideMenuItem itemReadmode;
	private ScrollWebView mWebView;
	
	private TextView tv_title;
	private ImageView iv_Next;
	private ImageView iv_Pre;
	private RelativeLayout layout_banner;
//	private boolean isShow

	private GestureDetector gestureDetector;

	private String file_path = "";
	private String position_X = "X";
	private String position_Y = "Y";
	private String prefix = "";
	private int font_size = 15;
	
	AdView adView;
	AdRequest adRequest;

	private boolean isDarkBackground = true;
	
	@SuppressLint("ClickableViewAccessibility")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_reading);
				
		mWebView = (ScrollWebView) findViewById(R.id.webview_read);
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				// Save Data to preference
				prefix = AppUtils.getStoryItem().getSubpath();

				String oldPath = getPref(prefix);
				if (oldPath != null && oldPath.equals(file_path)) {
					scrollToPreviousSession();
				} else /*if (oldPath != null && !oldPath.equals(file_path)) */{
					putPref(prefix, file_path);
				}
				
				Toast.makeText(ReadingPageActivity.this, getString(R.string.double_tap_msg) + ", " + getString(R.string.volume_key_press_msg),
						Toast.LENGTH_LONG).show();
			}
		});
		
		mWebView.setOnScrollChangedCallback(new OnScrollChangedCallback() {
			
			@Override
			public void onScroll(int y, int oldy) {
				if (oldy > y && layout_banner != null && layout_banner.getVisibility() == View.GONE) {
					showBanner();
				}
				
			}
		});
		
		
		mWebView.getSettings().setBuiltInZoomControls(true);
		mWebView.getSettings().setDisplayZoomControls(false);
		
		mWebView.setInitialScale(0);
		mWebView.getSettings().setLoadWithOverviewMode(true);
		font_size = getPrefInt(FONT_KEY);
		if (font_size > FONT_SIZE_MIN && font_size < FONT_SIZE_MAX) {
			mWebView.getSettings().setDefaultFontSize(font_size);
		} else {
			font_size = mWebView.getSettings().getDefaultFontSize();
		}
		
		Intent i = getIntent();
		if (i != null) {
			file_path = i.getStringExtra(AppUtils.INTENT_SUB_PATH);
		}

		gestureDetector = new GestureDetector(this, new GestureListener());

		mWebView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return gestureDetector.onTouchEvent(event);
			}
		});
		
		iv_Next = (ImageView) findViewById(R.id.iv_nextChap);
		iv_Pre = (ImageView) findViewById(R.id.iv_preChap);
		tv_title = (TextView) findViewById(R.id.tv_name);
		
		layout_banner = (RelativeLayout) findViewById(R.id.layout_banner);
		iv_Next.setOnClickListener(this);
		iv_Pre.setOnClickListener(this);

		isDarkBackground = getPrefBoolean(AppUtils.READING_MODE_BLACK_BG);
		
		generateData();
		
		
		initMenu();
		
	}
	
	@Override
	protected void onResume() {
		/**
		 * Google ads
		 */
		startAdmob();
		super.onResume();
	}	
	
	private void startAdmob() {
		if (adView == null) {
			adView = (AdView) this.findViewById(R.id.adView);
		}
		
		if (adRequest == null) {
			adRequest = new AdRequest.Builder().build();
		}
		adView.loadAd(adRequest);
	}
	
	private void stopAdMod() {
		if (adView == null) {
			adView.destroy();
		}
		
		if (adRequest == null) {
			adRequest = null;
		}
		try {
			adView.loadAd(adRequest);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onDestroy() {
		stopAdMod();
		super.onDestroy();
	}
	
	private void generateData() {
		
		if (isDarkBackground) {
			String tempPath = getContentByStyle();
			if (tempPath.contains("file://")) {
				mWebView.loadUrl(tempPath);
			} else 
			mWebView.loadUrl("file://" + tempPath);
		} else {
			mWebView.loadUrl(file_path);
		}
		String title = AppUtils.getRecentTitle(this, file_path);
		tv_title.setText(Html.fromHtml(title));
		/*// Save Data to preference
		prefix = AppUtils.getStoryItem().getSubpath();

		String oldPath = getPref(prefix);
		if (oldPath != null && oldPath.equals(file_path)) {
			scrollToPreviousSession();
		} else if (oldPath != null && !oldPath.equals(file_path)) {
			putPref(prefix, file_path);
		}*/

/*		Toast.makeText(this, getString(R.string.double_tap_msg) + ", " + getString(R.string.volume_key_press_msg),
				Toast.LENGTH_LONG).show();*/
	}

	private void scrollToPreviousSession() {
		int valueX = 0;
		int valueY = 0;
		String x = getPref(prefix + position_X);
		if (x != null && x.length() > 0) {
			valueX = Integer.parseInt(x);
		}

		String y = getPref(prefix + position_Y);
		if (y != null && y.length() > 0) {
			valueY = Integer.parseInt(y);
		}

		if ((valueX + valueY) > 0) {
			mWebView.setAnimationCacheEnabled(false);
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

	}

	private void initMenu() {

		mResideMenu = new ResideMenu(this);
		mResideMenu.setBackground(R.drawable.menu_background);
		mResideMenu.attachToActivity(this);
		
		itemHome = new ResideMenuItem(this, R.drawable.icon_home, "Trang Chủ");
		if (getPrefBoolean(AppUtils.READING_MODE_BLACK_BG)) {
			itemReadmode = new ResideMenuItem(this, R.drawable.icon_black_text, "Đọc nền trắng");
		} else {
			itemReadmode = new ResideMenuItem(this, R.drawable.icon_black_bg, "Đọc nền đen");
		}
		 
		itemBack = new ResideMenuItem(this, R.drawable.icon_back, "Quay lại");
		itemNextChap = new ResideMenuItem(this, R.drawable.icon_next, "Hồi sau");
		itemPreChap = new ResideMenuItem(this, R.drawable.icon_previous,
				"Hồi trước");

		itemHome.setOnClickListener(this);
		itemBack.setOnClickListener(this);
		itemPreChap.setOnClickListener(this);
		itemNextChap.setOnClickListener(this);
		itemReadmode.setOnClickListener(this);

		mResideMenu.addMenuItem(itemHome, ResideMenu.DIRECTION_RIGHT);
		mResideMenu.addMenuItem(itemBack, ResideMenu.DIRECTION_RIGHT);
		mResideMenu.addMenuItem(itemReadmode, ResideMenu.DIRECTION_RIGHT);
		mResideMenu.addMenuItem(itemPreChap, ResideMenu.DIRECTION_RIGHT);
		mResideMenu.addMenuItem(itemNextChap, ResideMenu.DIRECTION_RIGHT);

		mResideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_LEFT);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return mResideMenu.dispatchTouchEvent(ev);
	}

	private class GestureListener extends
			GestureDetector.SimpleOnGestureListener {

		@Override
		public boolean onDoubleTap(MotionEvent e) {
			// TODO Auto-generated method stub
			nextPage();
			return true;
		}

	}

	private void nextPage() {
		if (mWebView != null) {
			mWebView.pageDown(false);
		}
	}

	@Override
	public void onBackPressed() {
		if (mResideMenu != null && mResideMenu.isOpened()) {

			mResideMenu.closeMenu();

		} else {
			finish();
		}
	}

	private void savePreviewScroll() {
		int x = mWebView.getScrollX();
		int y = mWebView.getScrollY();
		putPref(prefix + position_X, String.valueOf(x));
		putPref(prefix + position_Y, String.valueOf(y));
	}

	@Override
	protected void onPause() {
		savePreviewScroll();
		stopAdMod();
		super.onPause();
	}

	@Override
	public void onClick(View v) {
		if (v == itemHome) {
			this.finish();
		} else if (v == itemBack) {
			onBackPressed();
		} else if (v == itemNextChap) {
			gotoNextChap();
			
		} else if (v == itemPreChap) {
			gotoPreChap();
		} else if (v == itemReadmode) {
			mResideMenu.closeMenu();
			isDarkBackground = !isDarkBackground;
			putPref(AppUtils.READING_MODE_BLACK_BG, isDarkBackground);
			generateData();
			initMenu();
		} else if (v == iv_Next) {
			gotoNextChap();
		} else if (v == iv_Pre) {
			gotoPreChap();
		}  
		
		if (mResideMenu != null & mResideMenu.isOpened()) {
			mResideMenu.closeMenu();
		}
	}

	private void gotoNextChap() {
		String nextChap = AppUtils.getNextChapter(this, file_path);
		if (nextChap != null && nextChap.length() > 0) {
			file_path = nextChap;
			generateData();
		} else {
			Toast.makeText(this, getString(R.string.no_next_msg), Toast.LENGTH_SHORT).show();
			return;
		}
	}
	
	private void gotoPreChap() {
		String preChap = AppUtils.getPreviousChapter(this, file_path);
		if (preChap != null && preChap.length() > 0) {
			file_path = preChap;
			generateData();
		} else {
			Toast.makeText(this, getString(R.string.no_pre_msg), Toast.LENGTH_SHORT).show();
			return;
		}
	}
	
	private String getContentByStyle() {
		String path = file_path.substring(22);
		AssetManager am = this.getAssets();
		BufferedReader reader = null;
		InputStreamReader is = null;
		
		StringBuilder builder = new StringBuilder();
			try {
				is = new InputStreamReader(am.open(path), "UTF-8");
				reader = new BufferedReader(is);
				String line = "";
				
				while ((line = reader.readLine()) != null) {
//					String rs = "";
				if (isDarkBackground) {
					if (line.contains(AppUtils.STYLE_KW_BODY)) {
						line = line.replace(AppUtils.STYLE_KW_BODY,
								AppUtils.STYLE_BG_DARK_OPEN);
					} 
					if (line.contains(AppUtils.STYLE_KW_CONTENT)) {
						line = line.replace(AppUtils.STYLE_KW_CONTENT, AppUtils.STYLE_TXT_DARK_OPEN);
					} 
					if (line.contains(AppUtils.STYLE_KW_CLOSE_BODY)) {
						line = line.replace(AppUtils.STYLE_KW_CLOSE_BODY,
								AppUtils.STYLE_TXT_DARK_CLOSE);
					} 
					if (line.contains(AppUtils.TAG_HEADER)) {
						line = line.replace(AppUtils.TAG_HEADER,
								AppUtils.REPLACE_HEADER);

					}
					
					if (line.contains(AppUtils.STYLE_KW_FONT_FOOTER)) {
						line = line.replace(AppUtils.STYLE_KW_FONT_FOOTER,
								AppUtils.STYLE_RP_FONT_FOOTER);
					}
				}
					builder.append(line + "\n");
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (is != null) {
					try {
						is.close();
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			
		}
		return createFile(builder.toString());
	}
	
	private String createFile(String content) {
		FileWriter fr = null;
		BufferedWriter br = null;
		String filePath = getRootPath() + "temp.html";
		
		try {
			fr = new FileWriter(filePath, false);
			br = new BufferedWriter(fr);
			br.write(content);
			br.newLine();
		} catch (Exception e) {
			Log.e("THAI", e.toString());
		} finally {
			try {
				if(br != null) br.close();
				if(fr != null) fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return filePath;
	}
	
	private String getRootPath() {
		String state = Environment.getExternalStorageState();
		File root;
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			root = ReadingPageActivity.this.getExternalFilesDir(null);
		} else {
			root = getFilesDir();
		}
		
		String main = "";
		
		if (root == null) {
			main = getApplicationContext().getFilesDir().getAbsolutePath();
		} else {
			main = root.getAbsolutePath();
		}
		
		String myPath = main + "/Temp/";
		File dir = new File(myPath);
		if (dir != null && dir.exists() == false) {
			dir.mkdirs();
		}
		return myPath;
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
			putPref(FONT_KEY, font_size);
		}
		
	}
	
	/**
	 * 
	 * @author sev_user
	 * For webview listener
	 *
	 */
	public interface OnScrollChangedCallback {
		void onScroll(int y, int oldY);
	}
	
	
	private void hideBanner() {
		if (layout_banner.getVisibility() == View.VISIBLE) {
			Animation animation = new TranslateAnimation(0, 0, 0, 100);
			animation.setDuration(500);
			layout_banner.startAnimation(animation);
			layout_banner.setVisibility(View.GONE);
		}
	}
	
	private void showBanner() {
		if (layout_banner.getVisibility() == View.GONE) {
			Animation animation = new TranslateAnimation(0, 0, 100, 0);
			animation.setDuration(500);
			layout_banner.startAnimation(animation);
			layout_banner.setVisibility(View.VISIBLE);
		}
		mWebView.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				hideBanner();
			}
		}, 2000);
	}
}
