package com.thaipd.android.app.votacthien;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.startapp.android.publish.StartAppSDK;
import com.xolem.android.app.tinhsuvtt.data.ChapterItem;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.TOCReference;
import nl.siegmann.epublib.epub.EpubReader;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class BaseActivity extends Activity{

	private final String DATA = "VO_TAC_THIEN_DATA";
	
	int chapId;
	int currentIndex = -1;
	AssetManager mAssetManager;
	Book mBook;
	List<ChapterItem> chapterData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
				requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		if (getActionBar() != null) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}

	}
	
	protected void initStartApp() {
		StartAppSDK.init(this, "101733571", "201407263", true);
	}
	
	protected void initDa() {
		mAssetManager = getAssets();
		try {
			InputStream epubInputStream = mAssetManager.open("books/softskilldeveloper.epub");
			
			// load book from input stream
			mBook = (new EpubReader()).readEpub(epubInputStream);
			
			chapterData = getTableOfContent(mBook.getTableOfContents().getTocReferences(), 2);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	private List<ChapterItem> getTableOfContent(List<TOCReference> tocReferences, int depth) {
		
		
		
		if (tocReferences == null) {
			return null;
		}
		ArrayList<ChapterItem> list = new ArrayList<ChapterItem>();
		for (TOCReference tocReference : tocReferences) {
			
			chapId++;
			
			if (chapId > 2) {
				ChapterItem item = new ChapterItem();
				String title = tocReference.getTitle();
				int id = chapId;
				
				item.setId(id);
				item.setmTOC(tocReference);
				item.setTitle(title);
				list.add(item);
			}
			
			
			getTableOfContent(tocReference.getChildren(), depth + 1);
		}
		
		
		
		return list;
	}
	
	/**
	 * get Chapter's content
	 * @param tocReference
	 * @return
	 */
	protected String getChapContent(TOCReference tocReference) {
		StringBuilder builder = new StringBuilder();
		
		try {
			InputStream is = tocReference.getResource().getInputStream();
			BufferedReader r = new BufferedReader(new InputStreamReader(is));
			String line = "";
			while ((line = r.readLine()) != null) {
				builder.append(line);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return builder.toString();
	}
/*
	*//**
	 * 
	 * Part of create temp data
	 * 
	 *//*
	
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
			root = BaseActivity.this.getExternalFilesDir(null);
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
	}*/
	
	protected void putPref(String key, String value) {
		if (key == null || value == null || key.length() == 0 || value.length() == 0) {
			return;
		}
		SharedPreferences.Editor editor = getSharedPreferences(DATA, MODE_PRIVATE).edit();
		editor.putString(key, value);
		
		editor.commit();
	}
	
	protected void putPref(String key, boolean value) {
		if (key == null || key.length() == 0) {
			return;
		}
		SharedPreferences.Editor editor = getSharedPreferences(DATA, MODE_PRIVATE).edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	
	protected void putPref(String key, int value) {
		SharedPreferences.Editor editor = getSharedPreferences(DATA, MODE_PRIVATE).edit();
		editor.putInt(key, value);
		editor.commit();
	}
	
	protected boolean getPrefBoolean(String key) {
		SharedPreferences prefs = getSharedPreferences(DATA, MODE_PRIVATE); 
		return prefs.getBoolean(key, false);
	}
	
	protected int getPrefInt(String key) {
		SharedPreferences prefs = getSharedPreferences(DATA, MODE_PRIVATE);
		return prefs.getInt(key, -1);
	}
	
	protected String getPref(String key) {
		SharedPreferences prefs = getSharedPreferences(DATA, MODE_PRIVATE); 
		
		return prefs.getString(key, null);
	}
}
