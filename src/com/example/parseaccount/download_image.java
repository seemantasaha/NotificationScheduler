package com.example.parseaccount;

import java.io.File;
import java.io.FileOutputStream;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class download_image extends BroadcastReceiver{
	private Bitmap bmp;
	int noti_id;
	Context con;
	
		private boolean SaveToSD(Bitmap sourceBitmap) {

	        String imageName = null;

	        boolean imageSaved = false;

	        if (sourceBitmap != null && !sourceBitmap.isRecycled()) {
	            File storagePath = new File(
	                    Environment.getExternalStorageDirectory() + "/JUMLY/");

	            if (!storagePath.exists()) {
	                storagePath.mkdirs();
	            }

	            int count = storagePath.list().length;

	            Log.i("SaveToSD count", "" + count);

	            imageName = String.valueOf(count + 1) + "_jumly";

	            FileOutputStream out = null;
	            File imageFile = new File(storagePath, String.format("%s.jpg",
	                    imageName));
	            try {
	                out = new FileOutputStream(imageFile);
	                imageSaved = sourceBitmap.compress(Bitmap.CompressFormat.JPEG,
	                        90, out);
	                out.flush();
	                out.close();
	            } catch (Exception e) {
	                Log.e("SaveToSD ", "Unable to write the image to gallery" + e);

	            }

	            ContentValues values = new ContentValues(3);
	            values.put(Images.Media.TITLE, imageName);
	            values.put(Images.Media.MIME_TYPE, "image/jpeg");
	            values.put("_data", imageFile.getAbsolutePath());

	            con.getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, values);
	            imageSaved = true;
	        }

	        return imageSaved;
	    }
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		con = context;
		Bundle extras = intent.getExtras();
		if (extras != null) {
			noti_id = extras.getInt("ID");
		}
		try{
			ParseQuery<ParseObject> query = ParseQuery.getQuery("Notification");
	  		query.whereEqualTo("Receiver", ParseUser.getCurrentUser().getUsername());
	  		query.whereEqualTo("Notification_Code", noti_id);
	  		
	  		Log.d("Current User",ParseUser.getCurrentUser().getUsername());
	  		Log.d("Notification_Code",""+noti_id);
	  		
	  		query.getFirstInBackground(new GetCallback<ParseObject>() {
                public void done(ParseObject object,ParseException e) {
                    // TODO Auto-generated method stub
                    // Locate the column named "ImageName" and set
                    // the string
                	Log.d("Current User",ParseUser.getCurrentUser().getUsername());
                    ParseFile fileObject = (ParseFile) object.get("Notification_Image");
                    fileObject.getDataInBackground(new GetDataCallback() {
                                

								public void done(byte[] data,
                                        ParseException e) {
                                    if (e == null) {
                                        Log.d("test","We've got data in data.");
                                        bmp = BitmapFactory.decodeByteArray(data, 0,data.length);
                                        if(SaveToSD(bmp))
                                        {
                                        	Toast.makeText(con,
                    								"Image downloaded to Gallery",
                    								Toast.LENGTH_LONG).show();
                                        }
                                        else
                                        {
                                        }
                                    }
                                }
                    });
                }
	  		});
		}catch(Exception ex){}

	
	}

}
	