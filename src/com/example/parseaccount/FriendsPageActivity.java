package com.example.parseaccount;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.ApplicationErrorReport.AnrInfo;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ToggleButton;

public class FriendsPageActivity extends Activity {
	
	LinearLayout main_layout;
	TextView tv_user, tv_curent_user;
	CheckBox ck_send,ck_checked;
	ToggleButton mute,supertrust,mute_val,supertrust_val;
	OnClickListener clicks;
	Button manage;
	
	String[] frnds;
	
	LinearLayout llRow,llname,llLetter;
	RelativeLayout llbtn;
	LinearLayout.LayoutParams params;
	int frnd_fnd=0;
	int total_friend=-1;
	private TextView header;
	
	ProgressDialog progressDialog;
	private LayoutParams params_tgl;
	protected int no_frnd;
	private String[] text,prev_text;
	char letter;
	char prev_letter;
	protected int it=0;
	RelativeLayout.LayoutParams p;
	private Button send_notification;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friends_page);
		
		header = (TextView) findViewById(R.id.header_username);
	    header.setText(header.getText()+ParseUser.getCurrentUser().getUsername());
		
		manage = (Button)findViewById(R.id.manage_frnds);
		send_notification = (Button) findViewById(R.id.noti_send);
		text = new String[32000];
		prev_text = new String[32000];
		
		main_layout = (LinearLayout) findViewById(R.id.ll_frnds_row);
		
		params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params_tgl = new LayoutParams(
				LayoutParams.MATCH_PARENT,      
		        LayoutParams.WRAP_CONTENT
		);
		params_tgl.setMargins(10, 5, 50, 5);
		
		p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		    
		progressDialog=new ProgressDialog(FriendsPageActivity.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();
        
        frnds = new String[32000];
        frnd_fnd=0;
    	total_friend=-1;
        
        List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
        
		ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Friends");
		query1.whereEqualTo("ReqFrom", ParseUser.getCurrentUser().getUsername());
		
		ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Friends");
		query2.whereEqualTo("ReqUser", ParseUser.getCurrentUser().getUsername());
		
		queries.add(query1);
		queries.add(query2); 
		
		ParseQuery<ParseObject> superQuery =  ParseQuery.or(queries);
		//superQuery.orderByAscending("FirstName");
		
		superQuery.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				try{
					Log.d("Query1",""+objects.size());
					if (e == null) {
						if(objects.size() != 0){
							for(int i=0;i<objects.size();i++)
							{
								if(objects.get(i).getInt("Accept")==1)
								{
									frnd_fnd=1;
									total_friend++;
									if(objects.get(i).getString("ReqUser").equals(ParseUser.getCurrentUser().getUsername())){
										frnds[total_friend] = objects.get(i).getString("ReqFrom");
										text[total_friend] = objects.get(i).getString("ReqFromName") + " <" + objects.get(i).getString("ReqFrom") + ">";
									}
									else
									{
										frnds[total_friend] = objects.get(i).getString("ReqUser");
										text[total_friend] = objects.get(i).getString("ReqUserName") + " <" + objects.get(i).getString("ReqUser") + ">";
									}
									Log.d("text",text[total_friend]);
								}
							}
							Log.d("Freinf Req Found",""+frnd_fnd);
							if(frnd_fnd!=1)
							{
								llRow = new LinearLayout(getApplicationContext());
						        llRow.setOrientation(LinearLayout.HORIZONTAL);
						        llRow.setLayoutParams(params);
						        llRow.setBackgroundResource(R.drawable.linear_row);
						        llRow.setPadding(10, 10, 10, 10);
						        
								tv_user = new TextView(getApplicationContext());
								tv_user.setText("No friends added yet");
								tv_user.setTextSize(30);
								tv_user.setTypeface(Typeface.MONOSPACE);
								llRow.addView(tv_user);
								main_layout.addView(llRow);
							}
							
							Log.d("text.length",""+text.length);
				            String temp;

				            for (int i = 0;  i < total_friend ;  i++ )
				            {
				            	
				                for (int j = i + 1;  j <= total_friend;  j++ )
				                {  
				                	Log.d("text.length",text [ i ]+"  "+text [ j ]);
				                         if ( text [ i ].compareToIgnoreCase( text [ j ] ) > 0 )
				                          {                                             // ascending sort
				                                      temp = text [ i ];
				                                      text [ i ] = text [ j ];    // swapping
				                                      text [ j ] = temp; 
				                                      
				                           } 
				                   } 
				             } 
							
							for(int i=0; i<=total_friend; i++)
							{
								llRow = new LinearLayout(getApplicationContext());
								llRow.setOrientation(LinearLayout.HORIZONTAL);
								llRow.setBackgroundResource(R.drawable.linear_row);
								llRow.setPadding(2, 0, 20, 5);
								llRow.setGravity(Gravity.CENTER_VERTICAL);
								llRow.setLayoutParams(params);
								
								
								llname = new LinearLayout(getApplicationContext());
								llname.setOrientation(LinearLayout.HORIZONTAL);
								llname.setLayoutParams(params_tgl);
								
								tv_user = new TextView(getApplicationContext());
								tv_user.setText(text[i]);
								tv_user.setTextColor(Color.BLACK);
								tv_user.setTextSize(20);
								tv_user.setTypeface(Typeface.MONOSPACE);
								tv_user.setGravity(Gravity.LEFT);
								tv_user.setPadding(2, 0, 10, 0);
								tv_user.setId(i);
								llname.addView(tv_user);
								
								llbtn = new RelativeLayout(getApplicationContext());
								llbtn.setLayoutParams(p);
								
								ck_send = new CheckBox(getApplicationContext());
								ck_send.setBackgroundColor(Color.BLUE);
								ck_send.setId(3000+i);
								
								llbtn.addView(ck_send);
								// To put letters********************************************\
								Log.d("text",text[i]);
								letter = text[i].charAt(0);
								Log.d("letter",""+letter);
								
								if(Character.toUpperCase(letter) != Character.toUpperCase(prev_letter))
								{
									Log.d("hello","print a letter");
									llLetter = new LinearLayout(getApplicationContext());
									llLetter.setOrientation(LinearLayout.HORIZONTAL);
									llLetter.setPadding(0, 0, 0, 0);
									llLetter.setLayoutParams(params_tgl);
									
									tv_user = new TextView(getApplicationContext());
									tv_user.setText((""+letter).toUpperCase());
									tv_user.setTextSize(25);
									tv_user.setTextColor(Color.BLUE);
									tv_user.setTypeface(Typeface.MONOSPACE);
									tv_user.setPadding(2, 0, 0, 0);
									llLetter.addView(tv_user);
									main_layout.addView(llLetter);
								}
								prev_text[i] = text[i];
								prev_letter = prev_text[i].charAt(0);
								Log.d("prev_letter",""+prev_letter);
								it++;
								//************************************************************
								
								llRow.addView(llbtn);
								llRow.addView(llname);
								main_layout.addView(llRow);
							}
						}
						else
						{
						}
					}
					else
					{
					}
				}catch(Exception ex)
				{
					ex.printStackTrace();
				}
				
				progressDialog.dismiss();
			}
		});
		
		
		manage.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent frineds_manage_intent = new Intent(FriendsPageActivity.this, FriendsTabActivity.class);
				startActivity(frineds_manage_intent);
			}
		});
		
		send_notification.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				/*Toast.makeText(getApplicationContext(),
						"Button Clicked:" + v.getId(), Toast.LENGTH_LONG)
						.show();*/
				Log.d("Total Friends", ""+(total_friend+1));
				String frnd_usernames = "";
				no_frnd = 1;
				for(int i=0;i<=total_friend;i++)
				{
					Log.d("i", ""+i);
					ck_checked = (CheckBox)findViewById(3000+i);
					if(ck_checked.isChecked())
					{
						frnd_usernames = frnd_usernames + text[i] + ",";
						no_frnd = 0;
					}
				}

				Log.d("Friends", frnd_usernames);
				if(no_frnd==1)
				{
					Toast.makeText(getApplicationContext(),
							"Please select a friend", Toast.LENGTH_LONG)
							.show();
				}
				else{
					Intent noti_intent = new Intent(FriendsPageActivity.this,SetNotificationActivity.class);
					noti_intent.putExtra("USERNAME", frnd_usernames);
					noti_intent.putExtra("TASK", "NO");
					startActivity(noti_intent);
				}

			}
		});send_notification.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				/*Toast.makeText(getApplicationContext(),
						"Button Clicked:" + v.getId(), Toast.LENGTH_LONG)
						.show();*/
				Log.d("Total Friends", ""+(total_friend+1));
				String frnd_usernames = "";
				no_frnd = 1;
				for(int i=0;i<=total_friend;i++)
				{
					Log.d("i", ""+i);
					ck_checked = (CheckBox)findViewById(3000+i);
					if(ck_checked.isChecked())
					{
						frnd_usernames = frnd_usernames + text[i] + ",";
						no_frnd = 0;
					}
				}

				Log.d("Friends", frnd_usernames);
				if(no_frnd==1)
				{
					Toast.makeText(getApplicationContext(),
							"Please select a friend", Toast.LENGTH_LONG)
							.show();
				}
				else{
					Intent noti_intent = new Intent(FriendsPageActivity.this,SetNotificationActivity.class);
					noti_intent.putExtra("USERNAME", frnd_usernames);
					noti_intent.putExtra("TASK", "NO");
					startActivity(noti_intent);
				}

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
	            return true;
	        case R.id.action_my_notifications:
	        	Intent noti_view_intent = new Intent(FriendsPageActivity.this,NotificationsTabActivity.class);
				startActivity(noti_view_intent);
	            return true;
	        case R.id.action_me:
	        	Intent change_pass_intent = new Intent(FriendsPageActivity.this,MeActivity.class);
				startActivity(change_pass_intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	public void onBackPressed() {
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog,
					int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					finish();
					break;

				case DialogInterface.BUTTON_NEGATIVE:
					// No button clicked
					break;
				}
			}
		};
		
		AlertDialog.Builder builder = new AlertDialog.Builder(FriendsPageActivity.this);
		builder.setMessage(
				"Are you sure you wish to exit?")
				.setPositiveButton("Yes",
						dialogClickListener)
				.setNegativeButton("No",
						dialogClickListener).show();
	}
}
