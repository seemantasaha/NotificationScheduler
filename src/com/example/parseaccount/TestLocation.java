package com.example.parseaccount;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class TestLocation implements LocationListener{

	LocationManager locationManager;
	Criteria criteria;
	
	Handler handler;
	Runnable task;
	String provider;
	Location myLocation;
	Context con1;
	private double latitude;
	private double longitude;
	Context con;
	
	public TestLocation(Context context) {
		// TODO Auto-generated constructor stub
		con1 = context;
		locationManager = (LocationManager) con1.getSystemService(Context.LOCATION_SERVICE);
		handler = new Handler();
	}
	
	public void function() {
		
		task = new Runnable() {
			public void run() {
				Boolean loc_fnd = locationBasedAlarm(con1); //function for time based notification
				if(loc_fnd)
				{
					handler.removeCallbacks(task);
				}
				else
				{
					handler.postDelayed(this, 10000);
				}
			}
		};
		handler.postDelayed(task, 0);//check every 1 minute
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

	
	Boolean locationBasedAlarm(Context con2) {
		// TODO Auto-generated method stub
		
		criteria = new Criteria();
        // Get the name of the best provider
        provider = locationManager.getBestProvider(criteria, true);
        // Get Current Location
        myLocation = locationManager.getLastKnownLocation(provider);
        if (myLocation != null) {
        	onLocationChanged(myLocation);
        }
        
		
		locationManager.requestLocationUpdates(provider, 1000, 0, this);
		
        
        if(myLocation!=null)
        {
        	// Get latitude of the current location
        	latitude = myLocation.getLatitude();

        	// Get longitude of the current location
        	longitude = myLocation.getLongitude();
        }
        Toast.makeText(con2, latitude+","+longitude,
				Toast.LENGTH_SHORT).show();
		Log.d("Distance",""+latitude+","+longitude);
		
		return false;
	}


}
