package com.kintop.radek.callersremarks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

public class IncomingCallReceiver extends BroadcastReceiver {
	public IncomingCallReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent)
	{
		Intent i = null;
		/*TelephonyManager tmgr = 
				(TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
				*/
		String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

		if(number == null)
			return;

		i = new Intent(context, InOutCallActivity.class);
		i.putExtra("number", number);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        try
        {
        	Thread.sleep(1200);
        }
        catch(Exception e)
        {}
		context.startActivity(i);
	}
}
