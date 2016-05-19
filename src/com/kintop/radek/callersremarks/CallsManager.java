package com.kintop.radek.callersremarks;

import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.util.Log;

public class CallsManager {

	//lista osob dzwoniacych do nas i do tych do ktorych dzwonilismy:
	public static String[] getCallers(Context ctx)
	{
		Set<String> phones_nums = new HashSet<String>();

		Cursor cur = ctx.getContentResolver().query(CallLog.Calls.CONTENT_URI,
										new String[]{CallLog.Calls.NUMBER},
										null, null, null);
		if(cur == null || cur.getCount() <= 0)
		{
			return null;
		}

		Log.i("RRRRRR", " liczba numerow: " + cur.getCount() + " lcol " + cur.getColumnCount()
		+ "col name: " + cur.getColumnName(cur.getColumnIndex(CallLog.Calls.NUMBER)));

		while(cur.moveToNext())
		{
			try
			{
				int index = cur.getColumnIndex(CallLog.Calls.NUMBER);
				String s = cur.getString(index);
				phones_nums.add(s);
			}
			catch(IllegalArgumentException ille)
			{
				Log.e("RRRRRR", " illegal arg");
			}
			catch(Exception e)
			{
				Log.e("RRRRRR", " 2");
				e.printStackTrace();
				cur.close();
				return null;
			}
		}

		cur.close();
		return phones_nums.toArray(new String[1]);
	}
}
