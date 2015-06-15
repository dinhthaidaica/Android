package com.thaipd.android.app.votacthien;

import android.content.Context;

public class AppUtils {
	
	private static Context mContext;
	private static AppUtils object;
	
	public static final String INTENT_POSITION = "intent_position";
	public static final String INTENT_DATA = "intend_data";
	
	public static AppUtils getInstance(Context ctx) {
		mContext = ctx;
		if (object == null) {
			object = new AppUtils();
		}
		return object;
	}
	
	private AppUtils() {
		
	}
	
	public String processRawData(String rawData, boolean darkMode) {
		
		final String BODY = "<body>";
		final String REP_BODY = "<body style=\"background-color:black\"><font color='white'>";
		final String END_BODY = "</body>";
		final String REP_END_BODY = "<br></br><br></br><br></br>"
									+ "<p align='center'><font color='blue' size='4'><b>Câu chuyện tiếp tục thế nào, mời ấn nút mũi bên phải để xem tiếp phần sau.</b></font></p>"
									+ "<br></br><br></br><br></br>";
		final String REP_END_BODY_DARK = "<br></br><br></br><br></br>"
				+ "<p align='center'><font color='green' size='4'><b>Câu chuyện tiếp tục thế nào, mời ấn nút mũi bên phải để xem tiếp phần sau.</b></font></p>"
				+ "<br></br><br></br><br></br>"; 
		StringBuilder builder = new StringBuilder();
		String tmp = "";
		if (darkMode) {
			tmp = rawData.replace(BODY, REP_BODY);
			builder.append(tmp.replace(END_BODY, REP_END_BODY_DARK));
		} else {
			tmp = rawData.replace(END_BODY, REP_END_BODY);
			builder.append(tmp);
		}
		
		return builder.toString();
	}
	
	public String getChapterNumber(String content) {
		final String TAG_NUM = "<p align=\"center\" class=\"style32\">";
		final String TAG_STRONG = "<strong>";
		final String TAG_STRONG_END = "</strong>";
//		final String TAG_NUM_END = "</p>";
		
		int idStart = content.indexOf(TAG_NUM);
		idStart = content.indexOf(TAG_STRONG, idStart + 34) + 8;
		int idEnd = content.indexOf(TAG_STRONG_END, idStart);
		String rs = "";
		try {
			rs = content.substring(idStart, idEnd);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return rs;
	}
	
	public String getChapterName(String content) {
		String rs = "";
		final String TAG_NAME = "<p align=\"center\" class=\"style28\">";
		final String TAG_NAME_END = "</p>";
		
		int idStart = content.indexOf(TAG_NAME);
		idStart = content.indexOf(TAG_NAME, idStart + 34) + 34;
		int idEnd = content.indexOf(TAG_NAME_END, idStart);
		try {
			rs = content.substring(idStart, idEnd);
		} catch (Exception e) {
			
		}
		return rs;
		
	}
	
}
