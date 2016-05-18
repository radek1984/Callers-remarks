package com.kintop.radek.callersremarks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	ListView callsList = null;

	private void populateList(String[] numbers)
	{
		CallsListAdapter cla = new CallsListAdapter(this, numbers);
		callsList.setAdapter(cla);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		callsList = (ListView)findViewById(R.id.callslList);

		String[] nums = CallsManager.getCallers(getApplicationContext());

		if(nums != null)
		{
			populateList(nums);
		}
		else
		{
			Toast.makeText(this, "Could not find any connections", Toast.LENGTH_LONG).show();
		}
	}

	public void onCallButtonClick(View bttn)
	{
		String tel_nuum = (String)bttn.getTag();
		Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tel_nuum.trim()));
        startActivity(callIntent);
	}
	public void onTxtNoteButtonClick(View bttn)
	{
		
	}
	public void onPhotoNoteButtonClick(View bttn)
	{
	    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    //takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
	     //       Uri.withAppendedPath(mLocationForPhotos, targetFilename));

	    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
	        startActivityForResult(takePictureIntent, 0);
	    }

	}
	public void onVoiceNoteButtonClick(View bttn)
	{
		
	}
}

class CallsListAdapter<T> extends  BaseAdapter
{
	Context context;
	T[] values;
	CallsListAdapter(Context ctx, T[] arr)
	{
		values = arr;
		context = ctx;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
		.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View vi = convertView;
		if (vi == null)
			vi = inflater.inflate(R.layout.list_item, null);
		TextView textview = (TextView) vi.findViewById(R.id.textView_number);
		textview.setText((String)values[position]);
		ImageButton button = (ImageButton)vi.findViewById(R.id.imageButton_call);
		button.setTag(new String((String)values[position]));
		
		return vi;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return values.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return values[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
}
