package com.kutaisi;

import java.io.File;

import android.os.Environment;

public class utils {

	static int check_device()
	{
		String state = Environment.getExternalStorageState();
		boolean mExternalStorageAvailable=false, mExternalStorageWriteable=false;

		if (Environment.MEDIA_MOUNTED.equals(state)) {
		    // We can read and write the media
		    mExternalStorageAvailable = mExternalStorageWriteable = true;
		    return 1;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
		    // We can only read the media
		    mExternalStorageAvailable = true;
		    mExternalStorageWriteable = false;
		    return 2;
		} else {
		    // Something else is wrong. It may be one of many other states, but all we need
		    //  to know is we can neither read nor write
		    mExternalStorageAvailable = mExternalStorageWriteable = false;
		    return -1;
		}
	}
	
	static boolean check_file(String fileName)
	{
		File root = new File(fileName);
		if (root.exists())
			return true;
		else			
			return false;
	}
	
}
