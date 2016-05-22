package com.kintop.radek.callersremarks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TextNoteEditActivity extends Activity
{

	private final String noteName = "txtnote.txt";
	private String number = null;
	private EditText et = null;
	private Button bt = null;

	private void loadNoteContent()
	{
		byte[] arr = Storage.loadFile(this, number, noteName);
		
		if(arr != null)
		{
			String txt = new String(arr);
			et.setText(txt);
		}
		else
		{
			Toast.makeText(this,
    				"Could not load txt note for " + number, Toast.LENGTH_LONG).show();
		}
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_text_note_edit);
		
		Intent i = getIntent();
		number = i.getExtras().getString("number");
		bt = (Button)findViewById(R.id.button_update_text_note);
		et = (EditText)findViewById(R.id.editText_Note);
		loadNoteContent();

		et.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}

			@Override
			public void afterTextChanged(Editable s)
			{
				bt.setText("Update *");
			}
			
		});
		bt.requestFocusFromTouch();
	}
	
	public void onUpdateButtonClick(View v)
	{
		String txt = et.getText().toString();
		if(!Storage.saveFile(this, number, noteName, txt.getBytes()))
		{
			Toast.makeText(this,
    				"Could not save txt note for " + number, Toast.LENGTH_LONG).show();
		}
		else
			bt.setText("Update");
	}
}
