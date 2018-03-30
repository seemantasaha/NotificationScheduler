package com.example.parseaccount;

import java.util.Calendar;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
 
public class Welcome extends Activity implements LocationListener{
 
	// Declare Variable
	Button logout,FriendList,Friend,NotificationList,Change_Password;
	ParseGeoPoint current_userLocation;
	String location_username;
	
	LocationManager locationManager;
	Location location;
	private static final long MIN_DISTANCE_FOR_UPDATE = 10;
	private static final long MIN_TIME_FOR_UPDATE = 1000 * 60 * 2;
	
	double latitude,longitude;
	protected EditText input;
	protected EditText email;
	protected EditText confirm_email;
	protected EditText pass;
	protected EditText confirm_pass;
	protected EditText Pass;
	protected EditText ConfirmPass;
 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Get the view from singleitemview.xml
		setContentView(R.layout.welcome);
		
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		
		String app_id = "tHsAybkTTG0MsxtqweOyKzCYzY1NzguoJ179wQEW";
        String client_key = "SUYEyhd6a319pmocl8u6JtBmYWe3rkUdBtjLmlaK";
 
        // Add your initialization code here
        Parse.initialize(this, app_id, client_key);
 
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        
        ParseUser currentUser = ParseUser.getCurrentUser();
 
		// Convert currentUser into String
		String struser = currentUser.getUsername().toString();
 
		// Locate TextView in welcome.xml
		TextView txtuser = (TextView) findViewById(R.id.txtuser);
 
		// Set the currentUser String into TextView
		txtuser.setText("You are logged in as " + struser);
 
		// Locate Button in welcome.xml
		logout = (Button) findViewById(R.id.logout);
		FriendList = (Button) findViewById(R.id.frnd_list);
		Friend = (Button) findViewById(R.id.friend);
		NotificationList = (Button) findViewById(R.id.bt_notificationlist);
		Change_Password = (Button) findViewById(R.id.me);
 
		// Logout Button Click Listener
		logout.setOnClickListener(new OnClickListener() {
 
			public void onClick(View arg0) {
				// Logout current user
				ParseUser.logOut();
				finish();
				Intent login_page_intent = new Intent(Welcome.this,LoginSignupActivity.class);
				startActivity(login_page_intent);
			}
		});
		
		FriendList.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*Intent chat_intent = new Intent(Welcome.this,ChatActivity.class);
				startActivity(chat_intent);*/
				Intent frndlist_intent = new Intent(Welcome.this,FriendsNotificationActivity.class);
				startActivity(frndlist_intent);
			}
		});
		
		Friend.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent friend_intent = new Intent(Welcome.this,FriendsTabActivity.class);
				startActivity(friend_intent);
			}
		});
		
		NotificationList.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent noti_view_intent = new Intent(Welcome.this,NotificationsTabActivity.class);
				startActivity(noti_view_intent);
			}
		});
		
		Change_Password.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent change_pass_intent = new Intent(Welcome.this,MeActivity.class);
				startActivity(change_pass_intent);
			}
		});
		
	}
	public Location getLocation(String provider) {
		if (locationManager.isProviderEnabled(provider)) {
			locationManager.requestLocationUpdates(provider,
					MIN_TIME_FOR_UPDATE, MIN_DISTANCE_FOR_UPDATE, (LocationListener) this);
			if (locationManager != null) {
				location = locationManager.getLastKnownLocation(provider);
				return location;
			}
		}
		return null;
	}
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
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