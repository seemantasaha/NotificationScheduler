package com.example.parseaccount;

import java.util.Calendar;
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
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

@SuppressLint("NewApi")
public class LocationAlarm extends BroadcastReceiver implements LocationListener{

	Context con;
	LocationManager locationManager;

	private Criteria criteria;
	private String provider;
	private Location myLocation;
	
	
	int noti_code;
	String noti_msg,sender;
	double loc_lat,loc_lng,radius;
	private double latitude;
	private double longitude;
	private ParseQuery<ParseObject> query;
	
	public void onReceive(Context context, Intent intent) {
	    // TODO Auto-generated method stub
		con = context;
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		
		
	
	    //Toast.makeText(context, "Alarm worked.", Toast.LENGTH_LONG).show();
	    
	 
	    
	    Bundle extras = intent.getExtras();
	    if (extras != null) {
	    	noti_msg = extras.getString("MESSAGE");
	    	
	    	sender = extras.getString("SENDER");
	    	
	    	loc_lat = extras.getDouble("LAT");
	    	loc_lng = extras.getDouble("LNG");
	    	radius = extras.getDouble("RADIUS");
	    	
			noti_code = extras.getInt("NOTI_ID");
	    }
	 // Create a criteria object to retrieve provider
	    criteria = new Criteria();
	    // Get the name of the best provider
	    provider = locationManager.getBestProvider(criteria, true);
	    // Get Current Location
	    myLocation = locationManager.getLastKnownLocation(provider);
	    if (myLocation != null) {
	    	onLocationChanged(myLocation);
	    }
	        
	    
	    locationManager.requestLocationUpdates(provider, 40000, 1, this);
		        
        if(myLocation!=null)
        {
        	// Get latitude of the current location
        	latitude = myLocation.getLatitude();

        	// Get longitude of the current location
        	longitude = myLocation.getLongitude();
        }
        //Toast.makeText(con, latitude+","+longitude,Toast.LENGTH_SHORT).show();
		Log.d("Distance",""+latitude+","+longitude);
		
		float[] result = new float[1];
		Location.distanceBetween (loc_lat, loc_lng, latitude, longitude, result);

		//Toast.makeText(con, "distance = "+result[0],Toast.LENGTH_SHORT).show();
		Log.d("Distance",""+result[0]);
		
		//Toast.makeText(con, "noti_code%1000 = "+(noti_code%1000),Toast.LENGTH_SHORT).show();
		
		if (result[0] < radius) {
			
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
	                			showNotification(con, noti_code);
		  					}
		  				}
	  				}catch(Exception ex){}
	  			}
	  		});
			
			showNotification(con, noti_code);
			
			PendingIntent pendingIntent = PendingIntent.getBroadcast(con.getApplicationContext(), noti_code, intent, PendingIntent.FLAG_UPDATE_CURRENT|  Intent.FILL_IN_DATA);

		    AlarmManager alarmManager = (AlarmManager) con.getSystemService(Context.ALARM_SERVICE);
		    
		    alarmManager.cancel(pendingIntent);
		}
	    
	    
	    
	}
	
	
	
	private void showNotification(Context context,int NOTIF_ID) {
		
	    NotificationManager notofManager = (NotificationManager)context. getSystemService(Context.NOTIFICATION_SERVICE);
	  // Notification note = new Notification(R.drawable.face,"NEW ACTIVITY", System.currentTimeMillis());
	    Intent notificationIntent = new Intent(context, LocalStorageActivity.class);
	    Log.d("Notification Code in Notification Function", ""+NOTIF_ID);
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
	
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		myLocation = location;
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

}