package com.bee.app.utils;

import java.security.MessageDigest;

public class MD5 {

	public static final String getMD5(String str, String encoding)
			throws Exception {

		MessageDigest md = MessageDigest.getInstance("MD5");

		md.update(str.getBytes(encoding));

		byte[] result = md.digest();

		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < result.length; i++) {

			int val = result[i] & 0xff;

			sb.append(Integer.toHexString(val));

		}

		return sb.toString();

	}

}
