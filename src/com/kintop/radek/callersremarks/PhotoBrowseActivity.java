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
	int ix = -1;

	private void displayImage(byte arr[])
	{
		if(arr == null)
		{
			iv.setImageDrawable(null);
			return;
		}

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
		ix = -1;
		if(list == null || list.length <= 0)
			Toast.makeText(this,
    				"No files for  " + number, Toast.LENGTH_LONG).show();
		else
			onNextPhotoButtonClick(null);
	}

	public void onNextPhotoButtonClick(View v)
	{
		byte [] arr = null;
		ix++;
		if(list != null && list.length > 0)
		{
			if(ix >= list.length || ix < 0)
				ix = 0;
			arr = Storage.loadFile(this,  number, list[ix]);
			displayImage(arr);
		}
		else
		{
			Toast.makeText(this,
    				"No files for  " + number, Toast.LENGTH_LONG).show();
		}
		
	}
	public void onDeletePhotoButtonClick(View v)
	{
		if(list != null && list.length > 0)
		{
			Storage.deleteFile(this,  number, list[ix]);

			ix = -1;
			list = Storage.getFilesListForNumber(this, number, "jpg");
			if(list == null || list.length <= 0)
			{
				Toast.makeText(this,
	    				"No files for  " + number, Toast.LENGTH_LONG).show();
				displayImage(null);
			}
			else
				onNextPhotoButtonClick(null);

		}
	}
}
