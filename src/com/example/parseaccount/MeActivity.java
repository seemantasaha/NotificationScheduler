package com.example.parseaccount;

import com.google.android.gms.fitness.request.StartBleScanRequest;
import com.parse.ParseUser;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MeActivity extends Activity{
	
	Button ChangePassword;
	Button logout;
	Button test;
	protected EditText Pass;
	protected EditText ConfirmPass;
	TextView header;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.me);
		
		header = (TextView) findViewById(R.id.header_username);
	    header.setText(header.getText()+ParseUser.getCurrentUser().getUsername());
		
		ChangePassword = (Button) findViewById(R.id.me);
		logout = (Button) findViewById(R.id.logout);
		test = (Button) findViewById(R.id.test_location);
		
		test.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(MeActivity.this,
		                "Text Location Buton Clicked", Toast.LENGTH_LONG).show();
				TestLocation test = new TestLocation(getApplicationContext());
				test.function();
			}
		});
		
		ChangePassword.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// TODO Auto-generated method stub
				// Set GUI of login screen
				final Dialog login = new Dialog(MeActivity.this);
				login.setContentView(R.layout.change_password_dialog);
				login.setTitle("Change Password");
				 
				// Init button of login GUI
				Button btnLogin = (Button) login.findViewById(R.id.btnLogin);
				Button btnCancel = (Button) login.findViewById(R.id.btnCancel);
				
				Pass = (EditText)login.findViewById(R.id.pass);
				ConfirmPass = (EditText)login.findViewById(R.id.confirm_pass);
				 
				// Attached listener for login GUI button
				btnLogin.setOnClickListener(new OnClickListener() {
				    @Override
				    public void onClick(View v) {
				    	if(Pass.getText().toString().equals("") || ConfirmPass.getText().toString().equals(""))
				    	{
				    		ParseUser currentUser = ParseUser.getCurrentUser();
				    		currentUser.setPassword(Pass.getText().toString());
				    		currentUser.saveInBackground();
				    		Toast.makeText(MeActivity.this,
					                "Empty Password Field", Toast.LENGTH_LONG).show();
				    	}
				    	else if(Pass.getText().toString().equals(ConfirmPass.getText().toString()))
				    	{
				    		ParseUser currentUser = ParseUser.getCurrentUser();
				    		currentUser.setPassword(Pass.getText().toString());
				    		currentUser.saveInBackground();
				    		login.dismiss();
				    		Toast.makeText(MeActivity.this,
					                "Password is Changed", Toast.LENGTH_LONG).show();
				    	}
				    	else
				        Toast.makeText(MeActivity.this,
				                "Password is not matched", Toast.LENGTH_LONG).show();
				    }
				});
				btnCancel.setOnClickListener(new OnClickListener() {
				    @Override
				    public void onClick(View v) {
				        login.dismiss();
				    }
				});
				 
				// Make dialog box visible.
				login.show();
			
			}
		});
		
		logout.setOnClickListener(new OnClickListener() {
			 
			public void onClick(View arg0) {
				// Logout current user
				ParseUser.logOut();
				finish();
				Intent login_page_intent = new Intent(MeActivity.this,LoginSignupActivity.class);
				startActivity(login_page_intent);
			}
		});
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
	        	Intent friend_intent = new Intent(MeActivity.this,FriendsPageActivity.class);
	        	friend_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
	        	friend_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(friend_intent);
				finish();
	            return true;
	        case R.id.action_my_notifications:
	        	Intent noti_view_intent = new Intent(MeActivity.this,NotificationsTabActivity.class);
				startActivity(noti_view_intent);
				finish();
	            return true;
	        case R.id.action_me:
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}
