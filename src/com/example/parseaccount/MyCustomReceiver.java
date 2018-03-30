package com.example.parseaccount;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;
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
import android.app.DialogFragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MyCustomReceiver extends BroadcastReceiver implements LocationListener{
	private static final String TAG = "MyCustomReceiver";
	
	String notification_type="";
	String message="";
	String location="";
	Double radius=0.0;
	String startDate="";
	String startTime="";
	String endDate="";
	String endTime="";
	
	String sender;
	
	String stday,stmonth,styear,enday,enmonth,enyear,sthour,stmin,enhour,enmin;

	Context con;
	
	Bitmap bmp;
	
	LocationManager locationManager;
	Criteria criteria;
	
	Handler handler;
	Runnable task[];
	private static final long MIN_DISTANCE_FOR_UPDATE = 10;
	private static final long MIN_TIME_FOR_UPDATE = 1000 * 60 * 2;
	
	double latitude=0.0,longitude=0.0;
	ParseQuery<ParseObject> query;
	
	public int noti_code=0;
	
	NotificationManager notofManager;
	String action;
	ParseQuery<ParseObject> obj;

	private String provider;

	private Location myLocation;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		con = context;
		handler = new Handler();
		Log.d("int max val", ""+(Integer.MAX_VALUE - 4));
		task = new Runnable[1000];
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

	    notofManager = (NotificationManager)context. getSystemService(Context.NOTIFICATION_SERVICE);
	    

	    
		try {
			if (intent == null)
			{
				Log.d(TAG, "Receiver intent null");
			}
			else
			{
				String action = intent.getAction();
				Log.d(TAG, "got action " + action );
				if (action.equals("com.example.parseaccount.UPDATE_STATUS"))
				{
					String channel = intent.getExtras().getString("com.parse.Channel");
					JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));

					Log.d(TAG, "got action " + action + " on channel " + channel + " with:");
					
						String value = json.getString("customdata");
						Log.d("value",value);
						if( value.equals("delete"))
						{
							query = ParseQuery.getQuery("Notification");
					  		query.whereEqualTo("Receiver", ParseUser.getCurrentUser().getUsername());
					  		query.whereEqualTo("Read", 0);
					  		query.whereEqualTo("Notification_Code",json.get("NotificationCode"));
					  		
					  		Log.d("Current User",ParseUser.getCurrentUser().getUsername());
					  		
					  		query.getFirstInBackground(new GetCallback<ParseObject>() {
				                public void done(ParseObject object,ParseException e) {
				                	if(e!=null)
				                	{
				                		try{
				                			noti_code = object.getInt("Notification_Code");
				                			if(notification_type.equals("Immediate"))
				  							{
									            notofManager.cancel(noti_code);
				  							}
				  							else if(notification_type.equals("Time"))
				  							{
				  								timeBasedAlarm(con,object.getString("Sender"), message, noti_code ,startDate,startTime,"delete"); //function for time based notification
				  							}
				  							else if(notification_type.equals("Location"))
				  							{
				  								locationBasedAlarm(con,sender, message,location,radius,noti_code,"delete");
				  							}
				  							else if(notification_type.equals("Time and Location"))
				  							{
				  								timelocationBasedAlarm(con,object.getString("Sender"), location,radius,message,object.getInt("Notification_Code"), startDate,startTime,endDate,endTime,"delete");
				  							}
				                			
				                			object.deleteInBackground();//to delete the row in parse
				                		}catch(Exception ex){}
				                	}
				                }
					  		});
					  		
					  		
						}
						
						else if (value.equals("edit"))
						{
							try{
								query = ParseQuery.getQuery("Notification");
						  		query.whereEqualTo("Receiver", ParseUser.getCurrentUser().getUsername());
						  		query.whereEqualTo("Read", 0);
						  		
						  		Log.d("Current User",ParseUser.getCurrentUser().getUsername());
						  		
						  		
					                                        //img.setImageBitmap(bmp);
					                                        query.findInBackground(new FindCallback<ParseObject>() {
					                                        	@Override
					        						  			public void done(List<ParseObject> objects, ParseException e) {
					        						  				// TODO Auto-generated method stub
					        						  				Log.d("Query",""+objects.size());
					        						  				if (e == null) {
					        						  					if(objects.size() != 0){
					        						  						for(int i=0;i<objects.size();i++)
					        						  						{
					        						  							
					        						  							
					        						  							notification_type = objects.get(i).getString("Notification_Type");
					        						  							Log.d("Notification Type",notification_type);
					        						  							
					        						  							message = objects.get(i).getString("Message");
					        						  							Log.d("Message",message);
					        						  							
					        						  							startDate = objects.get(i).getString("StartDate");
					        					  								startTime = objects.get(i).getString("StartTime");
					        					  								endDate = objects.get(i).getString("EndDate");
					        					  								endTime = objects.get(i).getString("EndTime");
					        						  							
					        					  								location = objects.get(i).getString("Location");
					        					  								radius = objects.get(i).getDouble("Map_Radius");
					        					  								
					        					  								sender = objects.get(i).getString("Sender");
					        					  								
					        					  								noti_code = objects.get(i).getInt("Notification_Code");
					        					  								Log.d("Notification Code", ""+noti_code);
					        						  							
					        						  							
					        					  								sender = objects.get(i).getString("Sender");
					        					  								
					        					  										objects.get(i).put("Received", 1);
					        					  										objects.get(i).saveInBackground();
						        						  							
					        					  										if(notification_type.equals("Immediate"))
								        						  						{
					        					  											objects.get(i).put("Read", 1);
						        					  										objects.get(i).saveInBackground();
								        											    }
					        					  								
		        										                                 ParseFile fileObject = (ParseFile) objects.get(i).get("Notification_Image_Thumbnail");
		        										                                 fileObject.getDataInBackground(new GetDataCallback() {
		        										                                             

		        										             								public void done(byte[] data,
		        										                                                     ParseException e) {
		        										                                                 if (e == null) {
		        										                                                     Log.d("test","We've got data in data.");
		        										                                                     bmp = BitmapFactory.decodeByteArray(data, 0,data.length);
					        										                     
					        										                    	 					        										                    	 
					        										                                                     
					        										                                                    if(notification_type.equals("Immediate"))
					     									        						  							{
							     									        											    showNotification(con,message,noti_code);
					     									        											          
					     									        						  							}
					     									        						  							else if(notification_type.equals("Time"))
					     									        						  							{
					     									        						  								timeBasedAlarm(con,sender, message, noti_code ,startDate,startTime,"create"); //function for time based notification
					     									        						  							}
					     									        						  							else if(notification_type.equals("Location"))
					     									        						  							{
					     									        						  								locationBasedAlarm(con,sender, message,location,radius,noti_code,"create"); 
			     									        						  										
					     									        						  							}
					     									        						  							else if(notification_type.equals("Time and Location"))
					     									        						  							{
					     									        						  								timelocationBasedAlarm(con,sender, location,radius,message,noti_code, startDate,startTime,endDate,endTime,"create");
					     									        						  							}
					        										                                                 }
					        										             								}
					        										                                 });
					        										                    	 }
					        										                                 
					        										                     }
					        						  								}
					        						  							}
					        						  						});
							}catch(Exception ex){}
						}
						
						else if (value.equals("silent"))
						{
							try{
								query = ParseQuery.getQuery("Notification");
						  		query.whereEqualTo("Receiver", ParseUser.getCurrentUser().getUsername());
						  		query.whereEqualTo("Received", 0);
						  		query.whereEqualTo("Read", 0);
						  		
						  		Log.d("Current User",ParseUser.getCurrentUser().getUsername());
						  		
						  		
					                                        //img.setImageBitmap(bmp);
					                                        query.findInBackground(new FindCallback<ParseObject>() {
					                                        	@Override
					        						  			public void done(List<ParseObject> objects, ParseException e) {
					        						  				// TODO Auto-generated method stub
					        						  				Log.d("Query",""+objects.size());
					        						  				if (e == null) {
					        						  					if(objects.size() != 0){
					        						  						for(int i=0;i<objects.size();i++)
					        						  						{
					        						  							
					        						  							
					        						  							notification_type = objects.get(i).getString("Notification_Type");
					        						  							Log.d("Notification Type",notification_type);
					        						  							
					        						  							message = objects.get(i).getString("Message");
					        						  							Log.d("Message",message);
					        						  							
					        						  							startDate = objects.get(i).getString("StartDate");
					        					  								startTime = objects.get(i).getString("StartTime");
					        					  								endDate = objects.get(i).getString("EndDate");
					        					  								endTime = objects.get(i).getString("EndTime");
					        						  							
					        					  								location = objects.get(i).getString("Location");
					        					  								radius = objects.get(i).getDouble("Map_Radius");
					        					  								
					        					  								sender = objects.get(i).getString("Sender");
					        					  								
					        					  								noti_code = objects.get(i).getInt("Notification_Code");
					        					  								Log.d("Notification Code", ""+noti_code);
					        						  							
					        						  							
					        					  								sender = objects.get(i).getString("Sender");
					        					  								
					        					  										objects.get(i).put("Received", 1);
					        					  										objects.get(i).saveInBackground();
						        						  							
					        					  										if(notification_type.equals("Immediate"))
								        						  						{
					        					  											objects.get(i).put("Read", 1);
						        					  										objects.get(i).saveInBackground();
								        											    }
					        					  								
		        										                                 ParseFile fileObject = (ParseFile) objects.get(i).get("Notification_Image_Thumbnail");
		        										                                 fileObject.getDataInBackground(new GetDataCallback() {
		        										                                             

		        										             								public void done(byte[] data,
		        										                                                     ParseException e) {
		        										                                                 if (e == null) {
		        										                                                     Log.d("test","We've got data in data.");
		        										                                                     bmp = BitmapFactory.decodeByteArray(data, 0,data.length);
					        										                     
					        										                    	 					        										                    	 
					        										                                                     
					        										                                                    if(notification_type.equals("Immediate"))
					     									        						  							{
							     									        											    showNotification(con,message,noti_code);
					     									        											          
					     									        						  							}
					     									        						  							else if(notification_type.equals("Time"))
					     									        						  							{
					     									        						  								timeBasedAlarm(con,sender, message, noti_code ,startDate,startTime,"create"); //function for time based notification
					     									        						  							}
					     									        						  							else if(notification_type.equals("Location"))
					     									        						  							{
					     									        						  								locationBasedAlarm(con,sender, message,location,radius,noti_code,"create"); 
			     									        						  										
					     									        						  							}
					     									        						  							else if(notification_type.equals("Time and Location"))
					     									        						  							{
					     									        						  								timelocationBasedAlarm(con,sender, location,radius,message,noti_code, startDate,startTime,endDate,endTime,"create");
					     									        						  							}
					        										                                                 }
					        										             								}
					        										                                 });
					        										                    	 }
					        										                                 
					        										                     }
					        						  								}
					        						  							}
					        						  						});
							}catch(Exception ex){}
						}
					
				}
			}

		} catch (JSONException e) {
			Log.d(TAG, "JSONException: " + e.getMessage());
		}
		
		
	}

	
	
	void locationBasedAlarm(Context con2, String sender, String noti_msg, String location, Double radius, int noti_code, String action) {
		// TODO Auto-generated method stub
		
		String[] locationSplit = location.split(",");
		double loc_lat = Double.parseDouble(locationSplit[0]) ;
		double loc_lng = Double.parseDouble(locationSplit[1]) ;
		//Toast.makeText(con2, "locationBasedAlarm",Toast.LENGTH_SHORT).show();
	
		
		
		Intent i = new Intent(con2, LocationAlarm.class);
		i.putExtra("MESSAGE", noti_msg);
		i.putExtra("SENDER",sender);
		i.putExtra("LAT", loc_lat);
		i.putExtra("LNG",loc_lng);
		i.putExtra("RADIUS",radius);
		i.putExtra("NOTI_ID",noti_code);
		
		// We want the alarm to go off 1 second from now.
	    long firstTime = SystemClock.elapsedRealtime();
	    firstTime += 1 * 1000;//start 1 second after first register.

	    PendingIntent pendingIntent = PendingIntent.getBroadcast(con2.getApplicationContext(), noti_code, i, PendingIntent.FLAG_UPDATE_CURRENT|  Intent.FILL_IN_DATA);

	    AlarmManager alarmManager = (AlarmManager) con2.getSystemService(Context.ALARM_SERVICE);
	    
		
		
	    if(action.equals("delete"))
		{
			alarmManager.cancel(pendingIntent);
		}
	    else
	    {
	    	alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime,
		            60000, pendingIntent);
		    //Toast.makeText(con2, "Alarm Set.", Toast.LENGTH_LONG).show();
	    }
	}



	protected void timelocationBasedAlarm(Context context, String sender, String location, Double radius, String noti_msg, int req_code, String stDate, String stTime, String enDate, String enTime, String action) {
		// TODO Auto-generated method stub
		String[] locationSplit = location.split(",");
		double loc_lat = Double.parseDouble(locationSplit[0]) ;
		double loc_lng = Double.parseDouble(locationSplit[1]) ;
		
		String[] stDateSplit = stDate.split("-");
		stday = stDateSplit[0];
		stmonth = stDateSplit[1];
		styear = stDateSplit[2];
		
		String[] endDateSplit = endDate.split("-");
		enday = endDateSplit[0];
		enmonth = endDateSplit[1];
		enyear = endDateSplit[2];
		
		String[] stTimeSplit = stTime.split(":");
		sthour = stTimeSplit[0];
		stmin = stTimeSplit[1];
		
		String[] endTimeSplit = endTime.split(":");
		enhour = endTimeSplit[0];
		enmin = endTimeSplit[1];
		
		
		Calendar cal=Calendar.getInstance();
		cal.set(Calendar.MONTH,Integer.parseInt(stmonth)-1); //month index starts from 0
		cal.set(Calendar.YEAR,Integer.parseInt(styear));
		cal.set(Calendar.DAY_OF_MONTH,Integer.parseInt(stday));
		cal.set(Calendar.HOUR_OF_DAY,Integer.parseInt(sthour));
		cal.set(Calendar.MINUTE,Integer.parseInt(stmin));
		
		Intent intent = new Intent(context, TimeLocationAlarm.class);
		intent.putExtra("MESSAGE", noti_msg);
		intent.putExtra("SENDER",sender);
		intent.putExtra("END_DAY", enday);
		intent.putExtra("END_MONTH", enmonth);
		intent.putExtra("END_YEAR", enyear);
		intent.putExtra("END_HOUR", enhour);
		intent.putExtra("END_MIN", enmin);
		intent.putExtra("LOCATION_LAT", loc_lat);
		intent.putExtra("LOCATION_LNG", loc_lng);
		intent.putExtra("RADIUS", radius);
		intent.putExtra("NOTI_ID",req_code);
		
	    PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), req_code, intent, PendingIntent.FLAG_UPDATE_CURRENT|  Intent.FILL_IN_DATA);

	    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

	    if(action.equals("delete"))
		{
			alarmManager.cancel(pendingIntent);
		}
	    else
	    {
	    	alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),pendingIntent );
		    //Toast.makeText(context, "Alarm Set."+stmonth+" "+styear+" "+stday+" "+sthour+" "+stmin+"("+loc_lat+","+loc_lng+")", Toast.LENGTH_LONG).show();
	    }
	    
	}

	protected void timeBasedAlarm(Context context, String sender, String noti_msg, int req_code, String stDate, String stTime,String action) {
		// TODO Auto-generated method stub
		
		String[] stDateSplit = stDate.split("-");
		stday = stDateSplit[0];
		stmonth = stDateSplit[1];
		styear = stDateSplit[2];
		
		String[] endDateSplit = endDate.split("-");
		enday = endDateSplit[0];
		enmonth = endDateSplit[1];
		enyear = endDateSplit[2];
		
		String[] stTimeSplit = stTime.split(":");
		sthour = stTimeSplit[0];
		stmin = stTimeSplit[1];
		
		String[] endTimeSplit = endTime.split(":");
		enhour = endTimeSplit[0];
		enmin = endTimeSplit[1];
		
		
		Calendar cal=Calendar.getInstance();
		cal.set(Calendar.MONTH,Integer.parseInt(stmonth)-1); //month index starts from 0
		cal.set(Calendar.YEAR,Integer.parseInt(styear));
		cal.set(Calendar.DAY_OF_MONTH,Integer.parseInt(stday));
		cal.set(Calendar.HOUR_OF_DAY,Integer.parseInt(sthour));
		cal.set(Calendar.MINUTE,Integer.parseInt(stmin));
		
		Intent intent = new Intent(context, TimeAlarm.class);
		intent.putExtra("MESSAGE", noti_msg);
		intent.putExtra("SENDER",sender);
		intent.putExtra("NOTI_ID",req_code);
	    PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), req_code , intent, PendingIntent.FLAG_UPDATE_CURRENT|  Intent.FILL_IN_DATA);

	    AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
	    
	    if(action.equals("delete"))
		{
			alarmManager.cancel(pendingIntent);
		}
	    else
	    {
	    	alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),pendingIntent );
		    //Toast.makeText(context, "Alarm Set."+stmonth+" "+styear+" "+stday+" "+sthour+" "+stmin, Toast.LENGTH_LONG).show();
	    }
	}

	private void showNotification(Context context, String noti_msg, int NOTIF_ID) {
	  // Notification note = new Notification(R.drawable.face,"NEW ACTIVITY", System.currentTimeMillis());
	    Intent notificationIntent = new Intent(context, LocalStorageActivity.class);
	    Log.d("Notification Code in Notification Function", ""+NOTIF_ID);
	    Log.d("Message",message);
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
	
	public Location getLocation(String provider) {
		if (locationManager.isProviderEnabled(provider)) {
			locationManager.requestLocationUpdates(provider,
					MIN_TIME_FOR_UPDATE, MIN_DISTANCE_FOR_UPDATE, this);
			if (locationManager != null) {
				Location location_data = locationManager.getLastKnownLocation(provider);
				return location_data;
			}
		}
		return null;
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
