package com.example.parseaccount;

import java.util.Calendar;

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
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

@SuppressLint("NewApi")
public class TimeLocationAlarm extends BroadcastReceiver{

	String noti_msg,sender;
	int enday,enmonth,enyear,enhour,enmin;
	double loc_lat,loc_lng,radius;
	

	Context con;
	private int noti_code;
	
	public void onReceive(Context context, Intent intent) {
	    // TODO Auto-generated method stub
		con = context;
		
	    //Toast.makeText(context, "Alarm worked on TimeLocationAlarm.", Toast.LENGTH_LONG).show();
	    
	    
	    
	    Bundle extras = intent.getExtras();
	    if (extras != null) {
	    	noti_msg = extras.getString("MESSAGE");
	    	
	    	sender = extras.getString("SENDER");
	    	
	    	enday = Integer.parseInt(extras.getString("END_DAY")) ;
	    	enmonth = Integer.parseInt(extras.getString("END_MONTH")) ;
	    	enyear = Integer.parseInt(extras.getString("END_YEAR")) ;
	    	enhour = Integer.parseInt(extras.getString("END_HOUR")) ;
	    	enmin = Integer.parseInt(extras.getString("END_MIN")) ;
	    	
	    	loc_lat = extras.getDouble("LOCATION_LAT");
	    	loc_lng = extras.getDouble("LOCATION_LNG");
	    	radius = extras.getDouble("RADIUS");
			noti_code = extras.getInt("NOTI_ID");
			
			//Toast.makeText(context, "Alarm Set."+"("+loc_lat+","+loc_lng+")", Toast.LENGTH_LONG).show();
		    
	    }
	    
	    
	    
	    Intent i = new Intent(con, RepeatLocationAlarm.class);
		i.putExtra("MESSAGE", noti_msg);
		i.putExtra("SENDER",sender);
		i.putExtra("LOCATION_LAT", loc_lat);
		i.putExtra("LOCATION_LNG",loc_lng);
		i.putExtra("RADIUS",radius);
		i.putExtra("NOTI_ID",noti_code);
		i.putExtra("END_DAY",enday);
		i.putExtra("END_MONTH",enmonth);
		i.putExtra("END_YEAR",enyear);
		i.putExtra("END_HOUR",enhour);
		i.putExtra("END_MIN",enmin);
		
		// We want the alarm to go off 1 second from now.
	    long firstTime = SystemClock.elapsedRealtime();
	    firstTime += 1 * 1000;//start 1 second after first register.

	    PendingIntent pendingIntent = PendingIntent.getBroadcast(con.getApplicationContext(), noti_code, i, PendingIntent.FLAG_UPDATE_CURRENT|  Intent.FILL_IN_DATA);

	    AlarmManager alarmManager = (AlarmManager) con.getSystemService(Context.ALARM_SERVICE);
	    
		
	    alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime,
		            60000, pendingIntent);
		    //Toast.makeText(con, "Alarm Set.", Toast.LENGTH_LONG).show();
	
	    
	}
	
	

}