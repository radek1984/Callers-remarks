package com.kintop.radek.callersremarks;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private final int CTX_MENU_ACTION_ADD = 0;
	private final int CTX_MENU_ACTION_BROWSE = 1;
	ListView callsList = null;
	Menu menu = null;
	private void populateList(CallsManager.CallId[] numbers)
	{
		CallsListAdapter cla = new CallsListAdapter(this, numbers);
		callsList.setAdapter(cla);
	}

	private void handleAddNote(String num, ListItemButtonData.ButtonType type)
	{
		Intent intent = null;
		switch(type)
		{
			case TXT_NOTE:
				intent = new Intent(this, TextNoteEditActivity.class);
				break;
			case PHOTO_NOTE:
				intent = new Intent(this, TakePhotoActivity.class);
				break;
			case VOICE_NOTE:
				intent = new Intent(this, RecordVoiceActivity.class);
				break;
			default:
				return;
		}
		if(intent != null)
		{
			intent.putExtra("number", num);
			startActivity(intent);
		}
	}

	private void handleBrowseNotes(String num, ListItemButtonData.ButtonType type)
	{
		Intent intent = null;

		switch(type)
		{
			case TXT_NOTE:
				break;
			case PHOTO_NOTE:
				intent = new Intent(this, PhotoBrowseActivity.class);
				break;
			case VOICE_NOTE:
				intent = new Intent(this, VoiceBrowseActivity.class);
				break;
		}
		if(intent != null)
		{
			intent.putExtra("number", num);
			startActivity(intent);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		this.menu = menu;
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_menu, menu);
	    menu.getItem(1).setVisible(false);
	    return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		CallsManager.CallId[] nums = null;
	    switch (item.getItemId())
	    {
	        case R.id.menu_item_no_filter:
	        	menu.getItem(0).setVisible(true);
	    		nums = CallsManager.getCallers(getApplicationContext(), false);
	        	break;
	        case R.id.menu_item_filter:
	        	menu.getItem(1).setVisible(true);
	    		nums = CallsManager.getCallers(getApplicationContext(), true);
	        	break;
	    }

		if(nums != null)
		{
			populateList(nums);
		    item.setVisible(false);
		}
		else
			Toast.makeText(this, "Could not find any connections", Toast.LENGTH_LONG).show();

	    return true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		callsList = (ListView)findViewById(R.id.frame_layout_cam);

		CallsManager.CallId[] nums = CallsManager.getCallers(getApplicationContext(), false);

		if(nums != null)
			populateList(nums);
		else
			Toast.makeText(this, "Could not find any connections", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
	{
		super.onCreateContextMenu(menu, v, menuInfo);
		ImageButton button = (ImageButton)v;
		ListItemButtonData data =  (ListItemButtonData)button.getTag();
		((CallsListAdapter)callsList.getAdapter()).setCurrent_selection(data);
		
		String num =
			((CallsManager.CallId)(callsList.getAdapter().getItem(data.listItemposition))).number;

		menu.setHeaderTitle("Choose action for " + num);

		if(data.type == ListItemButtonData.ButtonType.TXT_NOTE)
		{
			handleAddNote(num, data.type);
		}
		else
		{
			menu.add(0, CTX_MENU_ACTION_ADD, 0, "Add " + data.type.toString() + " note");
			menu.add(0, CTX_MENU_ACTION_BROWSE, 0, "Browse " + data.type.toString() + " notes");
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		ListItemButtonData data = ((CallsListAdapter)callsList.getAdapter()).getCurrent_selection();
		String dirNum = 
				((CallsManager.CallId)(callsList.getAdapter().getItem(data.listItemposition))).number;
	    //Intent i = null;

	    try
	    {
			if (item.getItemId() == CTX_MENU_ACTION_ADD)
			{handleAddNote(dirNum, data.type);
			/*
				switch(data.type)
				{
					case TXT_NOTE:
						i = new Intent(Intent.ACTION_EDIT);
						Uri uri = Uri.parse("file:///sdcard/somefile.txt");
						i.setDataAndType(uri, "text/plain");
						startActivityForResult(i, 0);
						break;
					case PHOTO_NOTE:
						i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						i.putExtra(MediaStore.EXTRA_OUTPUT,
								Uri.withAppendedPath(Uri.fromFile(Storage.getNumberDir(this, dirNum)), 
										System.currentTimeMillis() + ".jpg"));

						Log.i("RRRRRR", "Storage get: " + Storage.getNumberDir(this, dirNum));
						Log.i("RRRRRR", "Storage URI: " + 
								Uri.withAppendedPath(Uri.fromFile(Storage.getNumberDir(this, dirNum)), 
										System.currentTimeMillis() + ".jpg").toString());
						
					    if (i.resolveActivity(getPackageManager()) != null)
					        startActivityForResult(i, 0);
					    else
					    	Toast.makeText(this, "You can't take a photo with this device", Toast.LENGTH_LONG).show();
					    break;
					case VOICE_NOTE:
						i = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
						if (i.resolveActivity(getPackageManager()) != null)
					        startActivityForResult(i, 0);
						else
							Toast.makeText(this, "You can't record voice with this device",Toast.LENGTH_LONG).show();
						break;
				}*/
			}
			if (item.getItemId() == CTX_MENU_ACTION_BROWSE)
			{
				handleBrowseNotes(dirNum, data.type);
			}
	    }
		catch (ActivityNotFoundException e)
		{
			Toast.makeText(this, "Could not launch helper application",
					Toast.LENGTH_LONG).show();
		}

		return true;
	}

	public void onNoteButtonClick(View bttn)
	{
		openContextMenu(bttn);
	}

	public void onCallButtonClick(View bttn)
	{
		String tel_nuum = (String)bttn.getTag();
		Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel_nuum.trim()));
		startActivityForResult(callIntent, 0);
	}
}

class CallsListAdapter extends BaseAdapter
{
	Activity act;
	CallsManager.CallId[] values;
	ListItemButtonData current_selection;
	public ListItemButtonData getCurrent_selection() {
		return current_selection;
	}

	public void setCurrent_selection(ListItemButtonData current_selection) {
		this.current_selection = current_selection;
	}

	CallsListAdapter(Activity a, CallsManager.CallId[] arr)
	{
		values = arr;
		act = a;
		current_selection = null;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) act.getBaseContext()
		.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View vi = convertView;
		if (vi == null)
			vi = inflater.inflate(R.layout.list_item, null);
		TextView textview = (TextView) vi.findViewById(R.id.textView_number);
		textview.setText(values[position].number);

		textview = (TextView) vi.findViewById(R.id.textView_cintact_name);
		if(values[position].name != null)
		{
			textview.setText(values[position].name);
			textview.setTextColor(0xffffffff);
		}
		else
		{
			textview.setText("Not in contacts");
			textview.setTextColor(0xffff0000);
		}

		textview = (TextView) vi.findViewById(R.id.photo_count1);
		int count = Storage.getFilesCountForNumber(act, values[position].number, "jpg");
		
		if(count <= 0)
			textview.setText("");
		else
			textview.setText("" + count);

		textview = (TextView) vi.findViewById(R.id.voise_count1);
		count = Storage.getFilesCountForNumber(act, values[position].number, "3gp");

		if(count <= 0)
			textview.setText("");
		else
			textview.setText("" + count);

		ImageButton button = (ImageButton)vi.findViewById(R.id.imageButton_call);
		button.setTag(new String(values[position].number));

		button = (ImageButton)vi.findViewById(R.id.imageButton_browse_txt);
		button.setTag(new ListItemButtonData(ListItemButtonData.ButtonType.TXT_NOTE, position));
		act.registerForContextMenu(button);

		button = (ImageButton)vi.findViewById(R.id.imageButton_browse_photo);
		button.setTag(new ListItemButtonData(ListItemButtonData.ButtonType.PHOTO_NOTE, position));
		act.registerForContextMenu(button);

		button = (ImageButton)vi.findViewById(R.id.imageButton_browse_voice);
		button.setTag(new ListItemButtonData(ListItemButtonData.ButtonType.VOICE_NOTE, position));
		act.registerForContextMenu(button);
		
		return vi;
	}

	@Override
	public int getCount() {
		return values.length;
	}

	@Override
	public Object getItem(int position) {
		return values[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}

class ListItemButtonData
{
	enum ButtonType
	{
		TXT_NOTE
		{
		    @Override
		    public String toString()
		    {
		      return "text";
		    }
		},
		PHOTO_NOTE
		{
		    @Override
		    public String toString()
		    {
		      return "photo";
		    }
		},
		VOICE_NOTE
		{
		    @Override
		    public String toString()
		    {
		      return "voice";
		    }
		}
	}
	public ListItemButtonData(ButtonType t, int pos)
	{
		type = t;
		listItemposition = pos;
	}
	public ButtonType type;
	public int listItemposition;
}