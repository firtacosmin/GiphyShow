package com.fcc.giphyshow.utils;

import android.app.Activity;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.fcc.giphyshow.BuildConfig;
import com.fcc.giphyshow.MainActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExceptionHandler implements
	Thread.UncaughtExceptionHandler {

	private final Activity myContext;


	private final String LINE_SEPARATOR = System.getProperty("line.separator");
	private static final String TAG = ExceptionHandler.class.getSimpleName();
	protected static String CRASH_ERROR_NO_INTENT_TITLE = "CRASH_ERROR_NO_INTENT_TITLE";
	private static final int MAX_NO_OF_CRACHES = 5;


	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd'-'HH:mm:ss");
	/*
	 * a counter for the number of crashes. 
	 * When this counter reaches MAX_NO_OF_CRACHES the application will 
	 * not restart but a toast will be displayed.
	 * */
	private static int numerOfCrashes = 0 ;
	
	public ExceptionHandler(Activity context, int noOfCrashes) {
		myContext = context;
		numerOfCrashes = noOfCrashes;
		Log.d(TAG, "number of crashes: " + numerOfCrashes);
	}

	public ExceptionHandler(MainActivity activity) {
		myContext = activity;


	}

	public void uncaughtException(Thread thread, Throwable exception) {
        exception.printStackTrace();
		StringWriter stackTrace = new StringWriter();
		exception.printStackTrace(new PrintWriter(stackTrace));
		StringBuilder errorReport = new StringBuilder();
		errorReport.append("************ CAUSE OF ERROR ************").append(LINE_SEPARATOR).append(LINE_SEPARATOR);
		errorReport.append(stackTrace.toString());
		
		errorReport.append("************ DEVICE INFORMATION ***********").append(LINE_SEPARATOR);
		errorReport.append("Brand: ");
		errorReport.append(Build.BRAND);
		errorReport.append(LINE_SEPARATOR);
		errorReport.append("Device: ");
		errorReport.append(Build.DEVICE);
		errorReport.append(LINE_SEPARATOR);
		errorReport.append("Model: ");
		errorReport.append(Build.MODEL);
		errorReport.append(LINE_SEPARATOR);
		errorReport.append("Id: ");
		errorReport.append(Build.ID);
		errorReport.append(LINE_SEPARATOR);
		errorReport.append("Product: ");
		errorReport.append(Build.PRODUCT);
		errorReport.append(LINE_SEPARATOR);
		errorReport.append("************ FIRMWARE ************").append(LINE_SEPARATOR);
		errorReport.append("SDK: ");
		errorReport.append(Build.VERSION.SDK_INT);
		errorReport.append(LINE_SEPARATOR);
		errorReport.append("Release: ");
		errorReport.append(Build.VERSION.RELEASE);
		errorReport.append(LINE_SEPARATOR);
		errorReport.append("Incremental: ");
		errorReport.append(Build.VERSION.INCREMENTAL);
		errorReport.append(LINE_SEPARATOR);


        Log.d(TAG, "CRASH intercepted");
        Log.e(TAG, "CRASH data: " + errorReport);
		writetoLogFile(errorReport.toString());

//		numerOfCrashes++;
//		if ( numerOfCrashes <= MAX_NO_OF_CRACHES ){
//			Log.d(TAG, "crash number :"+numerOfCrashes);
//			Intent intent = new Intent(myContext, MainActivity.class);
//			intent.putExtra("error", errorReport.toString());
//			intent.putExtra(CRASH_ERROR_NO_INTENT_TITLE,numerOfCrashes);
//			myContext.startActivity(intent);
//		}
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(10);
	}

	private void writetoLogFile(String sout) {
		try {

			String currentDateandTime = sdf.format(new Date());

			String dir = Environment.getExternalStorageDirectory() + "/duView";
			createDirIfNotExists(dir);

			String fileName = dir + "/log.txt";

			File file = new File(fileName);

			FileOutputStream fOut = new FileOutputStream(file,true);
			OutputStreamWriter osw = new OutputStreamWriter(fOut);
			osw.write("\n" + currentDateandTime + " ");
			osw.write(sout);
			osw.flush();
			osw.close();fOut.close();
		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			if (BuildConfig.DEBUG) e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			if (BuildConfig.DEBUG) e.printStackTrace();
		}
	}

	private String createDirIfNotExists(String path) {
			boolean ret = true;

			File file = new File(path);
			if (!file.exists())
			{
				if (!file.mkdirs()) {
					Log.e("Utils", "Problem creating  folder: " +  file.getAbsolutePath());
					return "";
				}
			}
			return file.getAbsolutePath();
	}

}