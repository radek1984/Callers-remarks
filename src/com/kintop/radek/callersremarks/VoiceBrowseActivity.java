package com.kintop.radek.callersremarks;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class VoiceBrowseActivity extends Activity
{
	private String number = null;
	private String [] list = null;
	private ListView lv = null;
	private ListAdapter la = null;
	private MediaPlayer mp = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_voice_browse);

		Intent i = getIntent();
		number = i.getExtras().getString("number");

		lv = (ListView)findViewById(R.id.listView_voice_list);
		
		list = Storage.getFilesListForNumber(this, number, "3gp");
		if(list == null || list.length <= 0)
		{
			Toast.makeText(this,
    				"No files for  " + number, Toast.LENGTH_LONG).show();
			return;
		}

		la = new ArrayAdapter<String>(this, R.layout.list_item_voice, list);
		lv.setAdapter(la);
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String fname =
						Storage.getNumberDir(VoiceBrowseActivity.this, number, (String)lv.getItemAtPosition(position));
				
				try
				{
					if(mp!=null && mp.isPlaying())
					{
						mp.stop();
						mp.release();
						mp = null;
					}
					mp = new MediaPlayer();
		            mp.setDataSource(fname);
		            mp.prepare();
		            mp.start();
		        } catch (IOException e) {
		        	Toast.makeText(VoiceBrowseActivity.this,
		    				"Could not play file " + fname, Toast.LENGTH_LONG).show();
		        }
			}
		});
	}
}
