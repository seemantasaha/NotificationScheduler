package com.example.parseaccount;

import com.parse.ParseUser;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;



@SuppressWarnings("deprecation")
public class NotificationsTabActivity extends Activity {
	// Declare Tab Variable
	ActionBar.Tab Tab1, Tab2;
	Fragment fragmentTab1;
	Fragment fragmentTab2;
	ProgressDialog progressDialog;
	TextView header;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notificationstab);
		
		header = (TextView) findViewById(R.id.header_username);
	    header.setText(header.getText()+ParseUser.getCurrentUser().getUsername());
		
		
		
		ActionBar actionBar = getActionBar();
 
		// Create Actionbar Tabs
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		fragmentTab1 = new NotificationsTab1();
		fragmentTab2 = new NotificationsTab2();
		
		// Set Tab Icon and Titles
		Tab1 = actionBar.newTab().setText("Notifications Out");
		Tab2 = actionBar.newTab().setText("Notifications In");

		// Begin the transaction
		/*FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.fragment_container, new FragmentTab1());
		ft.commit();*/
		
		// Set Tab Listeners
		Tab1.setTabListener(new TabListener() {
			
			@Override
			public void onTabUnselected(Tab tab, FragmentTransaction ft) {
				// TODO Auto-generated method stub
				ft.remove(fragmentTab1);
			}
			
			@Override
			public void onTabSelected(Tab tab, FragmentTransaction ft) {
				// TODO Auto-generated method stub
				ft.replace(R.id.fragment_container, fragmentTab1);
			}
			
			@Override
			public void onTabReselected(Tab tab, FragmentTransaction ft) {
				// TODO Auto-generated method stub
				//ft.replace(R.id.fragment_container, fragmentTab1);
				
			}
		});
		Tab2.setTabListener(new TabListener() {
			
			@Override
			public void onTabUnselected(Tab tab, FragmentTransaction ft) {
				// TODO Auto-generated method stub
				ft.remove(fragmentTab2);
			}
			
			@Override
			public void onTabSelected(Tab tab, FragmentTransaction ft) {
				// TODO Auto-generated method stub
				ft.replace(R.id.fragment_container, fragmentTab2);
				
			}
			
			@Override
			public void onTabReselected(Tab tab, FragmentTransaction ft) {
				// TODO Auto-generated method stub
				//ft.replace(R.id.fragment_container, fragmentTab2);
			}
		});
 
		// Add tabs to actionbar
		actionBar.addTab(Tab1);
		actionBar.addTab(Tab2);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return super.onCreateOptionsMenu(menu);
	} 
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_my_friends:
	        	Intent friend_intent = new Intent(NotificationsTabActivity.this,FriendsPageActivity.class);
	        	friend_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
	        	friend_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(friend_intent);
				finish();
	            return true;
	        case R.id.action_my_notifications:
	            return true;
	        case R.id.action_me:
	        	Intent change_pass_intent = new Intent(NotificationsTabActivity.this,MeActivity.class);
				startActivity(change_pass_intent);
				finish();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}