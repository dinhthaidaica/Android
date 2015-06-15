/**
 * Copyright (C) 2012 Samsung Electronics Co., Ltd. All rights reserved.
 *
 * Mobile Communication Division,
 * Digital Media & Communications Business, Samsung Electronics Co., Ltd.
 *
 * This software and its documentation are confidential and proprietary
 * information of Samsung Electronics Co., Ltd.  No part of the software and
 * documents may be copied, reproduced, transmitted, translated, or reduced to
 * any electronic medium or machine-readable form without the prior written
 * consent of Samsung Electronics.
 *
 * Samsung Electronics makes no representations with respect to the contents,
 * and assumes no responsibility for any errors that might appear in the
 * software and documents. This publication and the contents hereof are subject
 * to change without notice.
 */

package com.android.thaipd.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;

public class TLog {

	private static final boolean SHOW_LOG = true;
	private static final boolean SAVE_LOG = true;
	
	private static final int V = 0;
	private static final int D = 1;
	private static final int I = 2;
	private static final int W = 3;
	private static final int E = 4;
	
	public static final int ENVIRONTMENT = 2048;
	public static final int OTHERS = 40;
	
	private static final String TAG = "DinhThaiDaiCa";

	static class LogInfo {
		public String filename = null;
		public String className = null;
		public String methodName = null;
		public String description = null;
		public int lineNumber = 0;

		public String toString() {
			String shotClassName = className.substring(
					className.lastIndexOf(".") + 1, className.length());
			return shotClassName + ":" + methodName + "(" + lineNumber + ")";
		}
	}

	private static LogInfo getLogInfo(int index) {
		LogInfo info = new LogInfo();
		Throwable stack = new Throwable().fillInStackTrace();
		StackTraceElement[] trace = stack.getStackTrace();
		if (index + 1 < trace.length) {
			if (trace[index].getFileName() != null) {
				info.filename = trace[index].getFileName().replaceAll(".java", "");
			}
			else {
				info.filename = "-";
			}
			
			
			if (info.filename.compareTo("ActivityFont") == 0 || info.filename.compareTo("ActivityThread") == 0 ) {

				if (trace[index+1].getFileName() != null) {
					info.filename = trace[index].getFileName().replaceAll(".java", "");
				}
				else {
					info.filename = "-";
				}
				
				info.className = trace[index+1].getClassName();
				info.methodName = trace[index+1].getMethodName();
				info.lineNumber = trace[index+1].getLineNumber();
			}
			else {
				info.className = trace[index].getClassName();
				info.methodName = trace[index].getMethodName();
				info.lineNumber = trace[index].getLineNumber();
			}
			
		}
		
		for (int i = trace.length-1 ; i >=3  ; i--) {
			
			
			String className = trace[i].getClassName();
			String methodName = trace[i].getMethodName();
			int lineNumber = trace[i].getLineNumber();
			
			if (className.contains("cloudprint")) {
				String shotClassName = className.substring(
						className.lastIndexOf(".") + 1, className.length());
				info.description = new StringBuilder().append(info.description).
						append(shotClassName).append(':').
						append(methodName).append('(').append(lineNumber).append(')').
						toString();
			}
		}

		return info;
	}

	

	public static void v(Object... values) {
		int kind = OTHERS;
		try {
			kind = (Integer) values[0];
		} catch (Exception e) {
			// TODO: handle exception
		}
		print(kind , V, values);
	}

	public static void d(Object... values) {
		int kind = OTHERS;
		try {
			kind = (Integer) values[0];
		} catch (Exception e) {
			// TODO: handle exception
		}
		print(kind, D, values);
	}

	public static void i(Object... values) {
		int kind = OTHERS;
		try {
			kind = (Integer) values[0];
		} catch (Exception e) {
			// TODO: handle exception
		}
		print(kind, I, values);
	}

	public static void w(Object... values) {
		int kind = OTHERS;
		try {
			kind = (Integer) values[0];
		} catch (Exception e) {
			// TODO: handle exception
		}
		print(kind, W, values);
	}

	public static void e(Exception e) {
		int kind = OTHERS;
		print(kind, E, e);
	}

	private static void print(int kind, int type, Object... values) {

		
		LogInfo info = null;
		String strValus = "";
		
		if( values != null && values.length > 0){
			if(values.length > 0 && values[0] != null && values[0].toString().compareTo("+") == 0){
				info = getLogInfo(4);
				LogInfo preInfo = getLogInfo(3);
				values[0] = preInfo.methodName;
			}
			else{
				info = getLogInfo(3);
			}

			if (kind == ENVIRONTMENT) {
				if (values.length == 1) {
					strValus = "";
				} else {
					ArrayList<Object> n = new ArrayList<Object>();
					for (int i = 0; i < values.length - 1; i++) {
						n.add(values[i+1]);
					}
					int size = n.size();
					Object[] m = new Object[size];
					for (int i = 0; i < size; i++) {
						m[i] = n.get(i);
					}
					strValus += " " + Arrays.toString(m);
				}
				
			} else {
				strValus += " " + Arrays.toString(values);
			}
			
		}else{
			info = getLogInfo(3);
		}

		if (SHOW_LOG) {
			String strPrintString = info.methodName + "(" + info.lineNumber + ") " + strValus;

			switch (type) {
			case V:
				android.util.Log.v(TAG, info.filename + " : " + strPrintString);
				break;
			case D:
				android.util.Log.d(TAG, info.filename + " : " + strPrintString);
				break;
			case I:
				android.util.Log.i(TAG, info.filename + " : " + strPrintString);
				break;
			case W:
				android.util.Log.w(TAG, info.filename + " : " + strPrintString);
				break;
			case E:
				android.util.Log.e(TAG, info.filename + " : " + strPrintString);
				
				if (values[0] instanceof Exception) {
					Exception e = (Exception) values[0];
					e.printStackTrace();
				}
				break;
			}
		}

/*		if (SAVE_LOG) {

			String strType = "";

			switch (type) {
			case V:
				strType = "(V)";
				break;
			case D:
				strType = "(D)";
				break;
			case I:
				strType = "(I)";
				break;
			case W:
				strType = "(W)";
				break;
			case E:
				strType = "(E)";
				break;
			}

			String strPrintString = DateUtil.getCurrentTime() + "\t" 
							+ strType + "\t" 
							+ info.filename + "\t"
							+ info.methodName + "(" + info.lineNumber + ")" + "\t"
							+ strValus;

			
			String strLogFilePath = makeLogFile(kind) null;// init the path Of log file

			if (strLogFilePath != null) {
				File logFileChek = new File(strLogFilePath);
				if (logFileChek.length() >= Constants.max_sizetrue) {
					RandomAccessFile mFile = null;
					try {
						mFile = new RandomAccessFile(
								strLogFilePath, "rw");
						try {
							mFile.setLength(0);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} finally {
						if (mFile != null) {
							try {
								mFile.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					
				}
				
				FileWriter logFile = null;
				BufferedWriter out = null;
				
				try {
					logFile = new FileWriter(strLogFilePath, true);
					out = new BufferedWriter(logFile);

					out.write(strPrintString);
					out.newLine();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						if(out != null) out.close();
						if(logFile != null) logFile.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}*/
	}

/*	private static String makeLogFile(int type) {
		String filePath = null;
		String root = FileManager.MAIN_FOLDER;
		if (root == null) {
			File rootFile = CloudPrintApplication.getCloudPrintAppContext().getExternalFilesDir(null);
			root = rootFile.getAbsolutePath();
		}
		String logPath = FileManager.getInstance(CloudPrintApplication.getCloudPrintAppContext()).getLogFolder();
		File f = new File(logPath);
		if (f != null) {

			if (f.exists() == false || f.isDirectory() == false) {

				boolean result = f.mkdirs();

				if (result == true) {
					filePath = logPath + "/" + Constants.LOG_NAME + "_" + Constants.LOG_FILE;
				}
			} else {
				filePath = logPath + "/" + Constants.LOG_NAME + "_" + Constants.LOG_FILE;
			}

		}
		return filePath;
	}*/

}
