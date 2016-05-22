package com.kintop.radek.callersremarks;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

public class TakePhotoActivity extends Activity {

	private Camera camHandle = null;
	private CameraView camHandleView = null;
	private String number = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_take_photo);

        try
        {
        	camHandle = Camera.open();
        }
        catch (Exception e)
        {
        	Toast.makeText(this, "Could not initiate camera", Toast.LENGTH_LONG).show();
        	return;
        }

        Intent i = getIntent();
        Bundle b = i.getExtras();
        number = (String)b.get("number");

        if(camHandle != null)
        {
        	Parameters params = camHandle.getParameters();
        	params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        	camHandle.setParameters(params);
            camHandleView = new CameraView(this, camHandle);
            FrameLayout camera_view = (FrameLayout)findViewById(R.id.frame_layout_cam);
            camera_view.addView(camHandleView);
        }
	}
	
	public void onTakePhotoButtonClick(View v)
	{
    	camHandle.autoFocus(new AutoFocusCallback()
		{
			@Override
			public void onAutoFocus(boolean success, Camera camera)
			{
				camHandle.takePicture(null, null,
						new PictureCallback()
						{
							@Override
					        public void onPictureTaken(byte[] data, Camera camera)
					        {
								String fname = System.currentTimeMillis() + ".jpg";
					        	if(!Storage.saveFile(TakePhotoActivity.this, number, fname, data))
					        	{
					        		Toast.makeText(TakePhotoActivity.this,
					        				"Could not save " + fname, Toast.LENGTH_LONG).show();
					        	}
					        	else
					        	{
					        		camHandle.startPreview();
					        	}
					        }
					    }
				);

			}
		});
		
	}
}


class CameraView extends SurfaceView implements SurfaceHolder.Callback
{
	private SurfaceHolder surfaceHolder;
	private Camera camHandle;
	
	public CameraView(Context context, Camera camera)
	{
	    super(context);
	    camHandle = camera;
	    camHandle.setDisplayOrientation(90);
	    surfaceHolder = getHolder();
	    surfaceHolder.addCallback(this);
	    surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_NORMAL);
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder surfaceHolder)
	{
	    try
	    {
	        camHandle.setPreviewDisplay(surfaceHolder);
	        camHandle.startPreview();
	    }
	    catch (IOException e)
	    {
	    	Toast.makeText(getContext(), "Could not start preview", Toast.LENGTH_LONG).show();
	    }
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3)
	{
	    if(surfaceHolder.getSurface() == null)
	        return;
	
	    try
	    {
	        camHandle.stopPreview();
	    }
	    catch(Exception e)
	    {
	    }
	
	    try
	    {
	        camHandle.setPreviewDisplay(surfaceHolder);
	        camHandle.startPreview();
	    }
	    catch(IOException e)
	    {
	    	Toast.makeText(getContext(), "Could not REstart preview", Toast.LENGTH_LONG).show();
	    }
	}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder surfaceHolder)
	{
	    camHandle.stopPreview();
	    camHandle.release();
	}
}