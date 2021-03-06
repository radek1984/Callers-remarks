package com.kintop.radek.callersremarks;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;

import android.content.Context;
import android.util.Log;

public class Storage {

	static File getNumberDir(Context ctx, String dirNumber)
	{
		return new File(ctx.getFilesDir().getAbsolutePath() + File.separator + dirNumber);
	}

	static String getNumberDir(Context ctx, String dirNumber, String fname)
	{
		return getNumberDir(ctx, dirNumber).getAbsolutePath() + File.separator + fname;
	}

	static boolean deleteFile(Context ctx, String dirNumber, String fileNote)
	{
		File file = new File(ctx.getFilesDir().getAbsolutePath() + File.separator + dirNumber
								+ File.separator + fileNote);	

		if(!file.exists())
			return false;
		
		return file.delete();
	}

	static void createDirIfNotExists(Context ctx, String dirNumber)
	{
		File dir = getNumberDir(ctx, dirNumber);

		if(!dir.exists())
		{
			boolean res = dir.mkdir();
			if(res)
			{
				Log.i("RRRRRR", "Dairectory "+dir.getAbsolutePath() +" created");
			}
			else
			{
				Log.e("RRRRRR", "Dairectory "+dir.getAbsolutePath() +" NOT created");
			}
		}
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

		FileOutputStream ofs = null;
		try
		{
			ofs = new FileOutputStream(dir.getAbsolutePath() + File.separator + fileNote,
							false);
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

	public static byte [] loadFile(Context ctx, String dirNumber, String fileNote)
	{
		byte [] buff = null;
		File dir = new File(ctx.getFilesDir().getAbsolutePath() + File.separator + dirNumber);
		try
		{
			File input = new File(dir, fileNote);
			buff = new byte[(int)input.length()];
			FileInputStream ifs = new FileInputStream(input);
			ifs.read(buff);
			ifs.close();
		}
		catch(Exception e)
		{
			return null;
		}
		return buff;
	}
	
	public static String [] getFilesListForNumber(Context ctx, String dirNumber, final String extension)
	{
		File dir = new File(ctx.getFilesDir().getAbsolutePath() + File.separator + dirNumber);
		FilenameFilter filter = new FilenameFilter()
									{
										public boolean accept(File dir, String name)
										{
											return name.endsWith("." + extension);
										}
									};

		return dir.list(filter);
	}

	public static int getFilesCountForNumber(Context ctx, String dirNumber, final String extension)
	{
		String[] list = getFilesListForNumber(ctx, dirNumber, extension);
		if(list == null)
			return 0;
		return list.length;
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
