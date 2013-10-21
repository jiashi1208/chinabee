package com.bee.app;

import java.lang.Thread.UncaughtExceptionHandler;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.apache.commons.httpclient.HttpException;

import com.bee.R;

import android.content.Context;
import android.widget.Toast;

public class AppException extends Exception implements UncaughtExceptionHandler {
	
	
	
	private static final boolean Debug = false;
	
	/** 定义异常类型 */
	public final static byte TYPE_NETWORK 	= 0x01;
	public final static byte TYPE_SOCKET	= 0x02;
	public final static byte TYPE_HTTP_CODE	= 0x03;
	public final static byte TYPE_HTTP_ERROR= 0x04;
	public final static byte TYPE_XML	 	= 0x05;
	public final static byte TYPE_IO	 	= 0x06;
	public final static byte TYPE_RUN	 	= 0x07;

	
	
	
	private byte type;
	private int code;

	private AppException(byte type, int code, Exception excp) {
		super(excp);
		this.type = type;
		this.code = code;		
		if(Debug){
			/*this.saveErrorLog(excp);*/
		}
	}
	

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		// TODO Auto-generated method stub
		
	}

	public static AppException http(int statusCode) {
		// TODO Auto-generated method stub
		return new AppException(TYPE_HTTP_CODE, statusCode, null);
	}
	
	public static AppException http(Exception e) {
		return new AppException(TYPE_HTTP_ERROR, 0 ,e);
	}
	
	public static AppException network(Exception e) {
		if(e instanceof UnknownHostException || e instanceof ConnectException){
			return new AppException(TYPE_NETWORK, 0, e);
		}
		else if(e instanceof HttpException){
			return http(e);
		}
		else if(e instanceof SocketException){
			return socket(e);
		}
		return http(e);
	}
	
	public static AppException socket(Exception e) {
		return new AppException(TYPE_SOCKET, 0 ,e);
	}
	
	/**
	 * 提示友好的错误信息
	 * @param ctx
	 */
	public void makeToast(Context ctx){
		switch(this.getType()){
		case TYPE_HTTP_CODE:
			String err = ctx.getString(R.string.http_status_code_error, this.getCode());
			Toast.makeText(ctx, err, Toast.LENGTH_SHORT).show();
			break;
		case TYPE_HTTP_ERROR:
			Toast.makeText(ctx, R.string.http_exception_error, Toast.LENGTH_SHORT).show();
			break;
		case TYPE_SOCKET:
			Toast.makeText(ctx, R.string.socket_exception_error, Toast.LENGTH_SHORT).show();
			break;
		case TYPE_NETWORK:
			Toast.makeText(ctx, R.string.network_not_connected, Toast.LENGTH_SHORT).show();
			break;
		case TYPE_XML:
			Toast.makeText(ctx, R.string.xml_parser_failed, Toast.LENGTH_SHORT).show();
			break;
		case TYPE_IO:
			Toast.makeText(ctx, R.string.io_exception_error, Toast.LENGTH_SHORT).show();
			break;
		case TYPE_RUN:
			Toast.makeText(ctx, R.string.app_run_code_error, Toast.LENGTH_SHORT).show();
			break;
		}
	}
	
	public int getType() {
		return this.type;
	}
	
	public int getCode() {
		return this.code;
	}
	
	public static AppException xml(Exception e) {
		return new AppException(TYPE_XML, 0, e);
	}

}
