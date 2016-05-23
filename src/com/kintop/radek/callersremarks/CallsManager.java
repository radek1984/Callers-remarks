package com.kintop.radek.callersremarks;

import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.util.Log;

public class CallsManager
{
	public static class CallId
	{
		public String name;
		public String number;

		@Override
		public boolean equals(Object o)
		{
			if(number != null && o != null)
				return number.equals(((CallId)o).number);
			
			if(number != null && o == null)
				return number.equals(null);

			if(number == null && o == null)
				return true;
			
			return o.equals(this);
	    }
		@Override
		public int hashCode()
		{
			if(number != null)
				return number.hashCode();
			return 0;
		}
	}
	//lista osob dzwoniacych do nas i do tych do ktorych dzwonilismy:
	public static CallId[] getCallers(Context ctx)
	{
		Set<CallId> phones_nums = new HashSet<CallId>();

		Cursor cur = ctx.getContentResolver().query(CallLog.Calls.CONTENT_URI,
										new String[]{CallLog.Calls.NUMBER,
														CallLog.Calls.CACHED_NAME},
										null, null, null);
		if(cur == null || cur.getCount() <= 0)
		{
			return null;
		}

		Log.i("RRRRRR", " nums : " + cur.getCount() + " lcol " + cur.getColumnCount()
		+ "col name: " + cur.getColumnName(cur.getColumnIndex(CallLog.Calls.NUMBER)));

		while(cur.moveToNext())
		{
			try
			{
				int index = cur.getColumnIndex(CallLog.Calls.NUMBER);
				String snum = cur.getString(index);
				index = cur.getColumnIndex(CallLog.Calls.CACHED_NAME);
				String sname = cur.getString(index);
				CallId id = new CallId();
				id.number = snum;
				id.name = sname;
				phones_nums.add(id);
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
		return phones_nums.toArray(new CallId[1]);
	}
}
