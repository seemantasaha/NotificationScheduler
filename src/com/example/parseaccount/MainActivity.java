package com.example.parseaccount;

import android.os.Bundle;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.PushService;
import com.parse.SaveCallback;
 
public class MainActivity extends Activity {
 
	

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ParseAnalytics.trackAppOpened(getIntent());
 
		String app_id = "tHsAybkTTG0MsxtqweOyKzCYzY1NzguoJ179wQEW";
        String client_key = "SUYEyhd6a319pmocl8u6JtBmYWe3rkUdBtjLmlaK";
 
        // Add your initialization code here
        Parse.initialize(this, app_id, client_key);
 
        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicWriteAccess(false);
        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
        
        ParseUser.enableAutomaticUser();
        
        
        
		// Determine whether the current user is an anonymous user
		if (ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
			// If user is anonymous, send the user to Login Page
			Intent intent = new Intent(MainActivity.this,
					LoginSignupActivity.class);
			startActivity(intent);
			finish();
		} else {
			// If current user is NOT anonymous user
			// Get current user data from Parse.com
			ParseUser currentUser = ParseUser.getCurrentUser();
			if (currentUser != null) {
				// Send logged in users to Welcome.class
				Intent intent = new Intent(MainActivity.this, FriendsPageActivity.class);
				startActivity(intent);
				finish();
			} else {
				// Send user to LoginSignupActivity.class
				Intent intent = new Intent(MainActivity.this,
						LoginSignupActivity.class);
				startActivity(intent);
				finish();
			}
		}
 
	}
}