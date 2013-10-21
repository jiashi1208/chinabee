package com.bee.log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


import android.app.Application;
import android.os.Environment;
import android.util.Log;
import android.util.SparseArray;

/**
 * A custom Android log class
 * 
 * @author syxc
 */
public final class LogUtil {

	// public static Config con;
	public static boolean logoff = false; // Log switch open, development,
											// released when closed(LogCat)
	public static int level = Log.ERROR; // Write file level
	private static String STORAGE_PATH = Environment
			.getExternalStorageDirectory().getPath();
	public static String saveDirName = "/log/";
	private static String saveDir;

	/**
	 * Custom Log output style
	 * 
	 * @param type
	 *            Log type
	 * @param tag
	 *            TAG
	 * @param msg
	 *            Log message
	 */
	public static void trace(final int type, String tag, final String msg) {
		// LogCat
		if (logoff) {

			// con = Config.getInstance();
			switch (type) {
			case Log.VERBOSE:
				Log.v(tag, msg);
				break;
			case Log.DEBUG:
				Log.d(tag, msg);
				break;
			case Log.INFO:
				Log.i(tag, msg);
				break;
			case Log.WARN:
				Log.w(tag, msg);
				break;
			case Log.ERROR:
				Log.e(tag, msg);
				break;
			}
		}
		// Write to file
		if (type >= level) {
			new Thread() {
				public void run() {
					writeLog(type, msg, true);
				}
			}.start();
		}
	}

	/**
	 * Custom Log output style
	 * 
	 * @param type
	 *            Log type
	 * @param tag
	 *            TAG
	 * @param msg
	 *            Log message
	 * @param isContinueWrite
	 */
	public static void trace(final int type, final String tag,
			final String msg, final boolean isContinueWrite) {
		// LogCat
		if (logoff) {

			// con = Config.getInstance();
			switch (type) {
			case Log.VERBOSE:
				Log.v(tag, msg);
				break;
			case Log.DEBUG:
				Log.d(tag, msg);
				break;
			case Log.INFO:
				Log.i(tag, msg);
				break;
			case Log.WARN:
				Log.w(tag, msg);
				break;
			case Log.ERROR:
				Log.e(tag, msg);
				break;
			}
		}
		// Write to file
		if (type >= level) {
			new Thread() {
				public void run() {
					writeLog(type, msg, isContinueWrite);
				}
			}.start();
		}
	}

	/**
	 * Write log file to the SDcard
	 * 
	 * @param type
	 * @param msg
	 */
	private static void writeLog(int type, String msg, boolean isContinueWrite) {
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return;
		}
		try {
			SparseArray<String> logMap = new SparseArray<String>();
			logMap.put(Log.VERBOSE, " VERBOSE ");
			logMap.put(Log.DEBUG, " DEBUG ");
			logMap.put(Log.INFO, " INFO ");
			logMap.put(Log.WARN, " WARN ");
			logMap.put(Log.ERROR, " ERROR ");

			final StackTraceElement tag = new Throwable().fillInStackTrace()
					.getStackTrace()[2];

			msg = new StringBuilder().append("\r\n")
					.append(getDateformat(Dateformater.SS.getValue()))
					.append(logMap.get(type)).append(tag.getClassName())
					.append(" - ").append(tag.getMethodName()).append("(): ")
					.append(msg).toString();

			final String fileName = new StringBuffer().append("test-")
					.append(getDateformat(Dateformater.DD.getValue()))
					.append(".log").toString();

			saveDir = STORAGE_PATH + saveDirName;
			recordLog(saveDir, fileName, msg, isContinueWrite);
		} catch (Exception e) {
			LogUtil.trace(Log.ERROR, "LogUtil: ", e.getMessage());
		}
	}

	/**
	 * Write log
	 * 
	 * @param logDir
	 *            Log path to save
	 * @param fileName
	 * @param msg
	 *            Log content
	 * @param bool
	 *            Save as type, false override save, true before file add save
	 */
	private static void recordLog(String logDir, String fileName, String msg,
			boolean bool) {
		try {
			createDir(logDir);
			final File saveFile = new File(new StringBuffer().append(logDir)
					.append("/").append(fileName).toString());
			if (!bool && saveFile.exists()) {
				saveFile.delete();
				saveFile.createNewFile();
				final FileOutputStream fos = new FileOutputStream(saveFile,
						bool);
				fos.write(msg.getBytes());
				fos.close();
			} else if (bool && saveFile.exists()) {
				final FileOutputStream fos = new FileOutputStream(saveFile,
						bool);
				fos.write(msg.getBytes());
				fos.close();
			} else if (bool && !saveFile.exists()) {
				saveFile.createNewFile();
				final FileOutputStream fos = new FileOutputStream(saveFile,
						bool);
				fos.write(msg.getBytes());
				fos.close();
			} else if (!bool && !saveFile.exists()) {
				saveFile.createNewFile();
				final FileOutputStream fos = new FileOutputStream(saveFile,
						bool);
				fos.write(msg.getBytes());
				fos.close();
			}
		} catch (IOException e) {
			recordLog(logDir, fileName, msg, bool);
		}
	}

	public static GlobalExceptionHandler processGlobalException(Application application,boolean isWriteIntoFile) {
		if (application != null) {
			GlobalExceptionHandler handler = new GlobalExceptionHandler(
					application,isWriteIntoFile);
			Thread.setDefaultUncaughtExceptionHandler(handler);
			return handler;
		}
		return null;
	}

	private static String getDateformat(String pattern) {
		final DateFormat format = new SimpleDateFormat(pattern);
		return format.format(new Date());
	}

	private static File createDir(String dir) {
		final File file = new File(dir);
		if (!file.exists()) {
			file.mkdirs();
		}
		return file;
	}

}
