package com.thaipd.android.app.truyenkimdung;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;

import com.android.thaipd.log.TLog;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;
import com.thaipd.android.app.truyenkimdung.data.AppUtils;

public class BaseActivity extends Activity {

	protected ResideMenu mResideMenu;
	protected ResideMenuItem itemHome;
	protected ResideMenuItem itemLicense;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		if (getActionBar() != null) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		} else {
			initialMenu();
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}

	}
	
	protected void closeMenu() {
		if (mResideMenu != null && mResideMenu.isOpened()) {

			mResideMenu.closeMenu();

		}
	}

	protected void initialMenu() {

		mResideMenu = new ResideMenu(this);
		mResideMenu.setBackground(R.drawable.menu_background);
		mResideMenu.attachToActivity(this);

		itemHome = new ResideMenuItem(this, R.drawable.icon_home, "Trang chủ");
		itemLicense = new ResideMenuItem(this, R.drawable.icon_license, "License");
		
		itemLicense.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		itemHome.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onBackPressed();
				
			}
		});
		mResideMenu.addMenuItem(itemHome, ResideMenu.DIRECTION_RIGHT);

		mResideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_LEFT);
	}

	@Override
	public void onBackPressed() {
		if (mResideMenu != null && mResideMenu.isOpened()) {

			mResideMenu.closeMenu();

		} else {
			super.onBackPressed();
		}
	}
	
	public void putPref(String key, String value) {
		if (key == null || value == null || key.length() == 0 || value.length() == 0) {
			return;
		}
		SharedPreferences.Editor editor = getSharedPreferences(AppUtils.PREFERENCE_NAME, MODE_PRIVATE).edit();
		editor.putString(key, value);
		TLog.d("Put " + key + " VALUE = " + value);
		editor.commit();
	}
	
	public void putPref(String key, boolean value) {
		if (key == null || key.length() == 0) {
			return;
		}
		SharedPreferences.Editor editor = getSharedPreferences(AppUtils.PREFERENCE_NAME, MODE_PRIVATE).edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	
	public void putPref(String key, int value) {
		SharedPreferences.Editor editor = getSharedPreferences(AppUtils.PREFERENCE_NAME, MODE_PRIVATE).edit();
		editor.putInt(key, value);
		editor.commit();
	}
	
	public boolean getPrefBoolean(String key) {
		SharedPreferences prefs = getSharedPreferences(AppUtils.PREFERENCE_NAME, MODE_PRIVATE); 
		return prefs.getBoolean(key, false);
	}
	
	public int getPrefInt(String key) {
		SharedPreferences prefs = getSharedPreferences(AppUtils.PREFERENCE_NAME, MODE_PRIVATE);
		return prefs.getInt(key, 0);
	}
	
	public String getPref(String key) {
		SharedPreferences prefs = getSharedPreferences(AppUtils.PREFERENCE_NAME, MODE_PRIVATE); 
		TLog.i("GET " + key + " VALUE = " + prefs.getString(key, null));
		return prefs.getString(key, null);
	}
}
