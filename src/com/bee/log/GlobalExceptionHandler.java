package com.bee.log;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;

import android.app.Application;
import android.util.Log;

public class GlobalExceptionHandler implements UncaughtExceptionHandler {

	private static final String TAG = "GlobalExceptionHandler";
	
	private Application mApplication;
	private boolean isWriteIntoFile;
	private UncaughtException uncatchException;

	/**
	 * @param application 
	 * @param isWriteIntoFile  write exception into file
	 * */
	
	public GlobalExceptionHandler(Application application ,boolean isWriteIntoFile) {
		this.mApplication = application;
		this.isWriteIntoFile = isWriteIntoFile;
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (isWriteIntoFile) {
			writeExceptionIntoFile(ex);
		}
		
		uncatchException.uncatchException(thread, ex);
	}
	
	public void setUncatchExceptionListener(UncaughtException uncatchException){
		this.uncatchException = uncatchException;
	}

	private void writeExceptionIntoFile(Throwable ex) {
		String info = null;
		ByteArrayOutputStream bos = null;
		PrintStream printStream = null;
		try {
			bos = new ByteArrayOutputStream();
			printStream = new PrintStream(bos);
			ex.printStackTrace(printStream);
			byte[] data = bos.toByteArray();
			info = new String(data);
			data = null;
			LogUtil.trace(Log.ERROR, TAG, info);

			// kill application
		} catch (Exception e) {
			LogUtil.trace(Log.ERROR, TAG, e.getMessage());
			// kill application
		} finally {
			try {
				if (printStream != null) {
					printStream.close();
				}
				if (bos != null) {
					bos.close();
				}
			} catch (Exception e) {
				// kill application
			}
		}
	}
	
	public interface UncaughtException{
		public void uncatchException(Thread thread, Throwable ex);
	}
}
