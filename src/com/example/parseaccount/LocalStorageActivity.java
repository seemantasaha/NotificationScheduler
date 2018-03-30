package com.example.parseaccount;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LocalStorageActivity extends Activity{

	private DBAdapter db;
	private Bitmap bmp;
	int noti_id=0;
	Context con;
	
	byte[] image_db;
	Button  save_image, save_db, allow;
	TextView SENDER, MESSAGE;
	ImageView image;
	private ProgressDialog progressDialog;
	
	String view_sender, view_message, view_date;
	byte[] view_image;
	private Bundle extras;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.local_storage);
		// TODO Auto-generated method stub
		db = new DBAdapter(getApplicationContext());
		db.open();
		 
		image_db = "no_image".getBytes();
		save_image = (Button)findViewById(R.id.save_gallery);
		save_image.setVisibility(View.GONE);
		save_db = (Button)findViewById(R.id.save_local);
		save_db.setVisibility(View.GONE);
		allow = (Button)findViewById(R.id.respond);
		
		SENDER = (TextView)findViewById(R.id.tv_sender);
		MESSAGE = (TextView)findViewById(R.id.tv_message);
		
		image = (ImageView)findViewById(R.id.iv_noti_img);

		noti_id=0;
		extras = getIntent().getExtras();
		if (extras != null) {
			noti_id = extras.getInt("ID");
			Log.d("Notification_Code",""+noti_id);
			
			if(extras.getString("Action").equals("VIEW"))
			{
				view_sender = extras.getString("view_sender");
				view_message = extras.getString("view_message");
				view_date = extras.getString("view_date");
				view_image = extras.getByteArray("view_image");
				save_image.setVisibility(View.GONE);
				save_db.setVisibility(View.GONE);
				allow.setVisibility(View.GONE);
			}
		}
		try{
			progressDialog = new ProgressDialog(LocalStorageActivity.this);
			progressDialog.setMessage("Please Wait...");
			progressDialog.setCancelable(false);
			progressDialog.show();
			
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
                	try{
                	Log.d("Current User",ParseUser.getCurrentUser().getUsername());
                	if(extras.getString("Action").equals("VIEW"))
        			{
                		progressDialog.dismiss();
          
                		SENDER.setText(view_sender + "\nDate:" + view_date);
                		MESSAGE.setText(view_message);
                		String str = new String(view_image, "UTF-8");
                		if(str.equals("no_image"))
                		{
                			image.setVisibility(View.GONE);
                		}
                		else
                		{
                			BitmapFactory.Options options = new BitmapFactory.Options();
                            bmp = BitmapFactory.decodeByteArray(view_image, 0, view_image.length, options);
                            if(bmp!=null)
                            {
                            	image.setImageBitmap(bmp);
                            }
                		}
        			}
                	else
                	{
                		view_sender = object.getString("Sender");
	                	SENDER.setText(SENDER.getText().toString() + object.getString("Sender"));
	                	MESSAGE.setText(object.getString("Message"));
	                	
	                	ParseFile fileObject = (ParseFile) object.get("Notification_Image");
	                    if(fileObject.getName().toString().equals("no_image"))
	                    {
	                    	progressDialog.dismiss();
	                    }
	                    else
	                    {
		                    fileObject.getDataInBackground(new GetDataCallback() {
										public void done(byte[] data,ParseException e) {
											
											progressDialog.dismiss();
											
		                                    if (e == null) {
		                                        Log.d("test","We've got data in data.");
		                                        BitmapFactory.Options options = new BitmapFactory.Options();
		                                        bmp = BitmapFactory.decodeByteArray(data, 0, data.length, options);
		                                        if(bmp!=null)
		                                        {
		                                        	image.setImageBitmap(bmp);
		                                        	if(SaveToSD(bmp))
		                                        	{
		                        	                	Toast.makeText(getApplicationContext(),
		                        								"Image downloaded to Gallery",
		                        								Toast.LENGTH_LONG).show();
		                                        	}
		                                        	
		                                        	
		                                        }
		                                    }
		                                }
		                    });
	                    }
	                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
        				Date d = Calendar.getInstance().getTime();
        				String c = dateFormat.format(d);
        				if(bmp!=null)
        				{
        					ByteArrayOutputStream stream = new ByteArrayOutputStream();
        				    bmp.compress(CompressFormat.JPEG, 70, stream);
        				    image_db = stream.toByteArray();
        				}
        			    
        			    try{
        			    	Log.d("image db", ""+image_db);
        			    	Log.d("current date", ""+c.toString());
        			    	db.insertemp(noti_id, SENDER.getText().toString(), MESSAGE.getText().toString(), image_db, c);
        			    	Toast.makeText(getApplicationContext(),"Notification saved to Local Storage",Toast.LENGTH_LONG).show();
        			    	db.close();
        			    }
        			    catch(SQLException ex)
        			    {
        			    	ex.printStackTrace();
        			    	Toast.makeText(getApplicationContext(),"Notification saving error!",Toast.LENGTH_LONG).show();
        			    }
                	}
                	
                    
                	}catch(Exception ex){
                		progressDialog.dismiss();
                		ex.printStackTrace();
                	}
                }
                	
	  		});
		}catch(Exception ex){
			progressDialog.dismiss();
		}
		
		save_image.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
                /*if(bmp==null)
                {
                	Toast.makeText(getApplicationContext(),
							"No Image to save in Gallery",
							Toast.LENGTH_LONG).show();
                }
                else 
                {
                	if(SaveToSD(bmp))
                	{
	                	Toast.makeText(getApplicationContext(),
								"Image downloaded to Gallery",
								Toast.LENGTH_LONG).show();
                	}
                }*/
			}
		});
		
		save_db.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
				Date d = Calendar.getInstance().getTime();
				String c = dateFormat.format(d);
				if(bmp!=null)
				{
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
				    bmp.compress(CompressFormat.JPEG, 70, stream);
				    image_db = stream.toByteArray();
				}
			    
			    try{
			    	Log.d("image db", ""+image_db);
			    	Log.d("current date", ""+c.toString());
			    	db.insertemp(noti_id, SENDER.getText().toString(), MESSAGE.getText().toString(), image_db, c);
			    	Toast.makeText(getApplicationContext(),"Notification saved to Local Storage",Toast.LENGTH_LONG).show();
			    	db.close();
			    }
			    catch(SQLException ex)
			    {
			    	ex.printStackTrace();
			    	Toast.makeText(getApplicationContext(),"Notification saving error!",Toast.LENGTH_LONG).show();
			    }*/
			}
		});
		
		allow.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Response to sender on receive
        		ParsePush parsePush = new ParsePush();
        		ParseQuery pQuery = ParseInstallation.getQuery(); // <-- Installation query
        		Log.d("Allow Sender", view_sender);
        		pQuery.whereEqualTo("username", view_sender); // <-- you'll probably want to target someone that's not the current user, so modify accordingly
        		
                parsePush.sendMessageInBackground("Notification Received by " + ParseUser.getCurrentUser().getUsername(), pQuery);
                
                Toast.makeText(getApplicationContext(),"Sender will be notified",Toast.LENGTH_LONG).show();
			}
		});
	}

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

            getApplicationContext().getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, values);
            imageSaved = true;
        }

        return imageSaved;
    }
}
