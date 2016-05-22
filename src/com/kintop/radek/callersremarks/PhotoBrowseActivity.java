package com.kintop.radek.callersremarks;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;


public class PhotoBrowseActivity extends Activity
{
	ImageView iv = null;
	String [] list = null;
	String number;
	int ix = 0;

	private void displayImage(byte arr[])
	{
		if(arr == null)
			return;
		iv.setRotation(90.0f);
		iv.setScaleType(ScaleType.CENTER_CROP);
		iv.setImageBitmap(BitmapFactory.decodeByteArray(arr, 0, arr.length));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_browse);

		Intent i = getIntent();
		number = i.getExtras().getString("number");
		iv = (ImageView)findViewById(R.id.imageView_Photo);
		list = Storage.getFilesListForNumber(this, number, "jpg");
		ix = 0;
		if(list == null || list.length <= 0)
			Toast.makeText(this,
    				"No files for  " + number, Toast.LENGTH_LONG).show();
		else
			onNextPhotoButtonClick(null);
	}

	public void onNextPhotoButtonClick(View v)
	{
		byte [] arr = null;
		if(list != null && list.length > 0)
		{
			if(ix >= list.length)
				ix = 0;
			arr = Storage.loadFile(this,  number, list[ix]);
			displayImage(arr);
			ix++;
		}
		else
		{
			Toast.makeText(this,
    				"No files for  " + number, Toast.LENGTH_LONG).show();
		}
		
	}
}
