package com.example.parseaccount;

import java.security.Provider;

import com.google.android.gms.maps.model.LatLng;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class LocationService implements LocationListener{

	LocationManager locationManager;
	Criteria criteria;
	String provider;
	double lat,lng;
	LatLng lat_lng;
	
	Context context;
	
	LocationService(Context con) {
		// TODO Auto-generated constructor stub
		// Get the location manager
		context=con;
	    locationManager = (LocationManager) con.getSystemService(Context.LOCATION_SERVICE);
	    // Define the criteria how to select the locatioin provider -> use
	    // default
	    Criteria criteria = new Criteria();
	    provider = locationManager.getBestProvider(criteria, false);
	    Location location = locationManager.getLastKnownLocation(provider);
	    Toast.makeText(con, ""+location, 5).show();
	    // Initialize the location fields
	    if (location != null) {
	      System.out.println("Provider " + provider + " has been selected.");
	      onLocationChanged(location);
	    } else {
	      Log.d("Location","Location not available");
	    }
	    
	}
	
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		lat = (double) (location.getLatitude());
	    lng = (double) (location.getLongitude());
	    lat_lng = new LatLng(lat, lng);
	    Toast.makeText(context, ""+location, 5).show();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		Log.d("Provider_Enabled","Enabled new provider " + provider);
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		Log.d("Provider_Disabled","Disabled provider " + provider);
	}
	
	LatLng getLocation()
	{
		return lat_lng;
	}

}
