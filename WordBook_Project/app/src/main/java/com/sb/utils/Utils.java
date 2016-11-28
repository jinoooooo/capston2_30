package com.sb.utils;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

import com.sb.wordbook.R;

public class Utils {

	public static String getRootDir(Context ctx) {

		String rootName = ctx.getResources().getString(R.string.app_name);
		String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + rootName + "/";
		return rootPath;
	}

	public static String getCurrentTimeString() {
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		return format.format(new Date());
	}
}
