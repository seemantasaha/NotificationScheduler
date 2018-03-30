package com.example.parseaccount;

import java.util.List;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

@SuppressLint("NewApi")
public class TimeAlarm extends BroadcastReceiver{

	String noti_msg,sender;
	ParseQuery<ParseObject> query;
	private Bitmap bmp;
	Context con_text;
	Intent in_tent;
	int noti_code;
	public void onReceive(Context context, Intent intent) {
	    // TODO Auto-generated method stub
		con_text=context;
		in_tent=intent;
	    //Toast.makeText(context, "Alarm worked.", Toast.LENGTH_LONG).show();
	    
	    Bundle extras = intent.getExtras();
	    		if (extras != null) {
	    			noti_code = extras.getInt("NOTI_ID");
	    			sender = extras.getString("SENDER");
	    		}
	    		
	    		query = ParseQuery.getQuery("Notification");
		  		query.whereEqualTo("Receiver", ParseUser.getCurrentUser().getUsername());
		  		query.whereEqualTo("Read", 0);
		  		query.whereEqualTo("Notification_Code",noti_code);
		  		
		  		Log.d("Current User",ParseUser.getCurrentUser().getUsername());
		  		Log.d("Notification_Code", ""+noti_code);
		  		
		  		query.findInBackground(new FindCallback<ParseObject>() {
		  			@Override
		  			public void done(List<ParseObject> objects, ParseException e) {
		  				// TODO Auto-generated method stub
		  				try{
			  				Log.d("Query",""+objects.size());
			  				if (e == null) {
			  					if(objects.size() != 0){
		       
		                			Log.d("Notification_Code", ""+noti_code);
		                			objects.get(0).put("Read", 1);
		                			objects.get(0).saveInBackground();
		                		    showNotification(con_text,noti_msg,noti_code);
			  					}
			  				}
		  				}catch(Exception ex){}
		  			}
		  		});
	    
	    
	    
	}
	
	private void showNotification(Context context, String noti_msg, int NOTIF_ID) {
	    NotificationManager notofManager = (NotificationManager)context. getSystemService(Context.NOTIFICATION_SERVICE);
	  // Notification note = new Notification(R.drawable.face,"NEW ACTIVITY", System.currentTimeMillis());
	    Intent notificationIntent = new Intent(context, LocalStorageActivity.class);
	    Log.d("Notification Code in Notification Function", ""+ NOTIF_ID);
	    notificationIntent.putExtra("Action", "NO");
	    notificationIntent.putExtra("ID", NOTIF_ID);
	    PendingIntent contentIntent = PendingIntent.getActivity(context,0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
	    Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
	    Notification notification = new Notification.Builder(context)
        .setSound(uri)
        .setSmallIcon(R.drawable.jumly)
        //.setStyle(new Notification.BigPictureStyle().bigPicture(bmp))
        .setContentText(noti_msg)
        .setAutoCancel(true)
        .build();
	    notification.setLatestEventInfo(context, "New Notification from " + sender, noti_msg, contentIntent);
	    notofManager.notify(NOTIF_ID,notification);
	
	} 

}