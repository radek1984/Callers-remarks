package com.kintop.radek.callersremarks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

public class InOutCallActivity extends Activity {

	private String number = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_in_out_call);

		//WindowManager.LayoutParams lparams = getWindow().getAttributes();
		//lparams.y = 0;
		//getWindow().setAttributes(lparams);

		Intent i = getIntent();
		number = i.getExtras().getString("number");

		TextView tv = (TextView)findViewById(R.id.textView_photo_count);

		int photoCount = Storage.getFilesCountForNumber(this, number, "jpg");

		if(photoCount > 0)
			tv.setText("" + photoCount);
		else
			tv.setText("");

		TextView et = (TextView)findViewById(R.id.textView1);
		byte [] arr = Storage.loadFile(this, number, "txtnote.txt");
		if(arr != null)
		{
			et.setMovementMethod(new ScrollingMovementMethod());
			et.setText(new String(arr));
		}
		else
			et.setText("");

	}

	public void onPhotoButtonClick(View v)
	{
		if(number == null)
			return;
		Intent intent = new Intent(this, PhotoBrowseActivity.class);
		intent.putExtra("number", number);
		startActivity(intent);
	}
}
