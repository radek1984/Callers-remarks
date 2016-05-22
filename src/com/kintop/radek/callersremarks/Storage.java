package com.kintop.radek.callersremarks;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;

import android.content.Context;
import android.util.Log;

public class Storage {

	static File getNumberDir(Context ctx, String dirNumber)
	{
		return new File(ctx.getFilesDir().getAbsolutePath() + File.separator + dirNumber);
	}

	static boolean saveFile(Context ctx, String dirNumber, String fileNote, byte []content)
	{
		boolean res = false; 
		//create subdirectory if it does not exist:
		File dir = new File(ctx.getFilesDir().getAbsolutePath() + File.separator + dirNumber);

		if(!dir.exists())
		{
			res = dir.mkdir();
			if(res)
			{
				Log.i("RRRRRR", "Dairectory "+dir.getAbsolutePath() +" created");
			}
			else
			{
				Log.e("RRRRRR", "Dairectory "+dir.getAbsolutePath() +" NOT created");
				return false;
			}
		}

		try
		{
			FileOutputStream ofs = new FileOutputStream(dir.getAbsolutePath() + File.separator + fileNote);
			ofs.write(content);
			ofs.close();
		}
		catch(Exception e)
		{
			Log.e("RRRRRR", "Failed to create file " + dir.getAbsolutePath() + File.separator + fileNote);
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public static void listDir(Context ctx, String dirNumber)
	{
		File dir = new File(ctx.getFilesDir().getAbsolutePath() + File.separator + dirNumber);
		String [] l = dir.list();
		
		Log.i("RRRRRR", "Listing Directory "+dir.getAbsolutePath());
		
		if(l != null)
		{
			for(int i = 0; i < l.length; i++)
			{
				Log.i("RRRRRR", "File: " + l[i]);
			}
		}
	}
}
