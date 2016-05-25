package com.kintop.radek.callersremarks;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class RecordVoiceActivity extends Activity 
									implements MediaRecorder.OnErrorListener,
									MediaRecorder.OnInfoListener
{

	private MediaRecorder mr = null;
	private String number = null;
	private boolean recording = false;
	private Button bt = null;

	private void stopRecorder()
	{
		try
		{
			mr.stop();
		}
		catch(Exception e)
		{}
		recording = false;
	}
	private boolean startRecorder()
	{
		mr.setAudioSource(MediaRecorder.AudioSource.MIC);
		mr.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		String fname = Storage.getNumberDir(this, number,
				System.currentTimeMillis() + ".3gp");
		Storage.createDirIfNotExists(this, number);
		mr.setOutputFile(fname);
		mr.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		mr.setMaxDuration(5*60*1000); 
		try
		{
			mr.prepare();
			mr.start();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Toast.makeText(this,
				"Could not initialise recorder ", Toast.LENGTH_LONG).show();
			return false;
		}
		
		return recording = true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record_voice);

		Intent i = getIntent();
		number = i.getExtras().getString("number");

		mr = new  MediaRecorder();
		mr.setOnErrorListener(this);
		mr.setOnInfoListener(this);
		
		bt = (Button)findViewById(R.id.button_record);
		bt.setText("Record");
		recording = false;
	}
	
	public void onRecordButtonClick(View v)
	{
		if(!recording)
		{
			if(startRecorder())
				bt.setText("Stop recording");
		}
		else
		{
			stopRecorder();
			bt.setText("Record");
		}
	}

	@Override
	public void onInfo(MediaRecorder mr, int what, int extra)
	{
		stopRecorder();
		bt.setText("Record");
		if(what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED)
			Toast.makeText(this,
					"Time up", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onError(MediaRecorder mr, int what, int extra)
	{
		stopRecorder();
		bt.setText("Record");
	}
}
