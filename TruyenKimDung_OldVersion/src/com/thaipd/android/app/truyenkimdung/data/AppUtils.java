package com.thaipd.android.app.truyenkimdung.data;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.TypedArray;

import com.android.thaipd.log.TLog;
import com.thaipd.android.app.truyenkimdung.R;
import com.thaipd.android.app.truyenkimdung.data.model.ChapterItem;
import com.thaipd.android.app.truyenkimdung.data.model.StoryItem;

public class AppUtils {
	
	private static ChapterItem mChapterItem = new ChapterItem();
	private static StoryItem mStoryItem = new StoryItem();
	
	public static final String READING_MODE_BLACK_BG = "ReadingModeBlackBG";
	
	public static final String STYLE_BG_DARK_OPEN = "<body style=\"background-color:black\">";
	public static final String STYLE_TXT_DARK_CLOSE = "</font>\n</body>";
	public static final String STYLE_TXT_DARK_OPEN = "</b></font></p><font color='white'>";
	public static final String STYLE_KW_BODY = "<body>";
	public static final String STYLE_KW_CONTENT = "</b></font></p>";
	public static final String STYLE_KW_CLOSE_BODY = "</body>";
	
	public static String STYLE_KW_FONT_FOOTER = "<font size='4' color='black'>";
	public static String STYLE_RP_FONT_FOOTER = "<font size='4' color='yellow'>";
	
	public static String REPLACE_HEADER = "<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"></head>";

	public static String TAG_HEADER = "<html>";
	
	private static ArrayList<ChapterItem> chapterList ;
	
	public static final String INTENT_SUB_PATH = "sub_path";
	
	public static final String PREFERENCE_NAME = "KIM_DUNG";
	
	
	public static final String ASSET_PATH = "file:///android_asset";
	public static final String INTRO_FILE = "tom_tat_truyen.html";
	
	public static final String TITLE_TAG = "<font size=\"4\" color=\"red\"><b>"; // 27
	public static final String DESC_TAG = "<font size=\"5\" color=\"blue\"><b>"; // 30
	public static final String END_TAG = "</b></font>";

	public static ChapterItem getmChapterItem() {
		return mChapterItem;
	}
	
	

	public static void setmChapterItem(ChapterItem mChapterItem) {
		AppUtils.mChapterItem = mChapterItem;
	}

	public static StoryItem getStoryItem() {
		return mStoryItem;
	}

	public static void setStoryItem(StoryItem mStoryItem) {
		AppUtils.mStoryItem = mStoryItem;
	}

	public static List<StoryItem> getAllStory(Context mContext) {
		ArrayList<StoryItem> list = new ArrayList<StoryItem>();
		String[] titles = mContext.getResources().getStringArray(R.array.story_title);
		String[] chapter = mContext.getResources().getStringArray(R.array.story_id);
		String[] desc = mContext.getResources().getStringArray(R.array.story_description);
		TypedArray images = mContext.getResources().obtainTypedArray(R.array.story_images);
		
		int size = titles.length;
//		StoryItem[] data = new StoryItem[size];
		
		for (int i = 0; i < size; i++) {
			list.add(new StoryItem(titles[i], Integer.valueOf(chapter[i]), images.getResourceId(i, -1), desc[i]));
		}
		images.recycle();
		return list;
	}
	
	
	public static List<ChapterItem> getAllChapter(Context ctx) {
		chapterList = new ArrayList<ChapterItem>();
		String storyPath = mStoryItem.getSubpath();
		try {
			AssetManager am = ctx.getAssets();
			String[] list = am.list(storyPath);
			
			
			BufferedReader reader = null;
			InputStreamReader is = null;
			
			String line = "";
			int startIndex;
			int endIndex;
			for (String name : list) {
				if (!name.equals(INTRO_FILE)) {
					is = new InputStreamReader(am.open(storyPath + "/" + name), "UTF-8");
					reader = new BufferedReader(is);
					
					String sub_title = "";
					String sub_desc = "";
					ChapterItem item = null;
					
					while ((line = reader.readLine()) != null) {
						
						
						
						if (line.contains(TITLE_TAG) && line.contains(END_TAG)) {
							
							item = new ChapterItem();
							item.setPath(storyPath + "/" + name);
							startIndex = line.indexOf(TITLE_TAG);
							endIndex = line.indexOf(END_TAG, startIndex);
							try {
								String sub = line.substring(startIndex + 30, endIndex);
								sub_title = StringEscapeUtils.unescapeHtml4(sub);
								
								if (sub_title.length() > 0) {
									item.setTitle(sub_title);
								}
							} catch (Exception e) {
								TLog.e(e);
							}
						}
						
						if (line.contains(DESC_TAG) && line.contains(END_TAG)) {
							startIndex = line.indexOf(DESC_TAG);
							endIndex = line.indexOf(END_TAG, startIndex);
							try {
								String sub = line.substring(startIndex + 31, endIndex);
								sub_desc = StringEscapeUtils.unescapeHtml4(sub);
								sub_desc = sub_desc.replaceAll("<br />", "");
								item.setDesc(sub_desc);
							} catch (Exception e) {
								TLog.e(e);
							}
							
							break;
						}
						
					}
					
					if (item != null && item.getTitle().length() > 0) {
						chapterList.add(item);
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			TLog.e(e);
		}
		
		return chapterList;
	}

	public static String getNextChapter(Context ctx, String currentChap) {
		int pos = getCurrentPos(ctx, currentChap); 
		String path = null;
		if ( pos != -1 && pos + 1 < chapterList.size() ) {
			path = chapterList.get(pos + 1).getPath();
			path = ASSET_PATH + "/" + path;
		} 
		return path;
	}
	
	private static int getCurrentPos(Context ctx, String path) {
		int currentPos = -1;
		path = path.replace(ASSET_PATH + "/", "");
		if (chapterList == null) {
			getAllChapter(ctx);
		}
		if (chapterList != null && chapterList.size() > 0) {

			ChapterItem item = null;
			for (int i = 0; i < chapterList.size(); i++) {
				item = chapterList.get(i);
				if (item.getPath().equals(path)) {
					currentPos = i;
					break;
				}
			}
		}
		return currentPos;
	}
	
	public static String getPreviousChapter(Context ctx, String currentChap) {
		int pos = getCurrentPos(ctx, currentChap); 
		String path = null;
		if ( pos != -1 && pos > 0) {
			path = chapterList.get(pos - 1).getPath();
			path = ASSET_PATH + "/" + path;
		} 
		return path;
	}
	
	public static String getRecentTitle(Context ctx, String filePath) {
		int pos = getCurrentPos(ctx, filePath);
		if ( pos != -1 && pos >= 0) {
			return chapterList.get(pos).toString();
		} 
		return "";
	}
	
	
}
