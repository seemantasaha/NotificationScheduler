package com.example.parseaccount;

import java.io.File;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.internal.ll;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class NotificationViewActivity extends Activity{
	
	TextView header,tv_receiver,tv_type;
	Button bt_view,bt_edit,bt_delete;
	LinearLayout main_layout;
	
	String receiver="",type="",message="",location="",stdt="",endt="",sttm="",entm="";
	double rad=0.0;
	String img_file_name;
	String object_id;
	int noti_id;
	
	ProgressDialog progressDialog;
	OnClickListener clicks;
	int index;
	Intent noti_intent;
	
	private int code_to_delete;
	String location_username;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notification_view);
		
		header = (TextView) findViewById(R.id.header_username);
	    header.setText(header.getText()+ParseUser.getCurrentUser().getUsername());
	    
	    main_layout = (LinearLayout) findViewById(R.id.main_layout);
	    
	    progressDialog = new ProgressDialog(NotificationViewActivity.this);
		progressDialog.setMessage("Please Wait...");
		progressDialog.setCancelable(false);
		progressDialog.show();
		
		
	    
	    ParseQuery<ParseObject> query = ParseQuery.getQuery("Notification");
		query.whereEqualTo("Sender", ParseUser.getCurrentUser().getUsername());
		
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				progressDialog.dismiss();
				
				    
				if(e==null)
				{
					try
					{
						Log.d("Query",""+objects.size());
		  					
						if(objects.size() != 0){
							for(int i=0;i<objects.size();i++)
							{
								if(objects.get(i).getInt("Received")==0)
								{
									receiver = objects.get(i).getString("Receiver");
									type = objects.get(i).getString("Notification_Type");
									
									LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
						            
						            LinearLayout.LayoutParams child_params = new LinearLayout.LayoutParams(
						                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
						            child_params.setMargins(10, 0, 5, 0);
						            
						            LinearLayout llRow = new LinearLayout(getApplicationContext());
						            llRow.setOrientation(LinearLayout.HORIZONTAL);
						            llRow.setLayoutParams(params);
						            llRow.setBackgroundResource(R.drawable.linear_row);
						            llRow.setPadding(10, 10, 10, 10);
						            llRow.setId(i);
						            
						            LinearLayout.LayoutParams col_params = new LinearLayout.LayoutParams(
						                    400, LayoutParams.WRAP_CONTENT);
						            LinearLayout llCol = new LinearLayout(getApplicationContext());
						            llCol.setOrientation(LinearLayout.VERTICAL);
						            llCol.setLayoutParams(col_params);
						            
						            tv_receiver=new TextView(getApplicationContext());
						            tv_receiver.setText(receiver);
						            tv_receiver.setTextSize(20);
						            tv_receiver.setTextColor(Color.BLACK);
						            tv_receiver.setId(i);
						            llCol.addView(tv_receiver);
						            
						            tv_receiver=new TextView(getApplicationContext());
						            tv_receiver.setText(type);
						            tv_receiver.setTextSize(15);
						            tv_receiver.setTextColor(Color.BLACK);
						            tv_receiver.setId(i);
						            llCol.addView(tv_receiver);
						            
						            llRow.addView(llCol);
						            
						            bt_view = new Button(getApplicationContext());
						            bt_view.setText("VIEW");
						            bt_view.setId(i);
						            bt_view.setBackgroundColor(Color.BLACK);
						            bt_view.setTextColor(Color.GREEN);
						            bt_view.setLayoutParams(child_params);
						            bt_view.setOnClickListener(clicks);
						            llRow.addView(bt_view);
						            
						            bt_edit = new Button(getApplicationContext());
						            bt_edit.setText("EDIT");
						            bt_edit.setId(2000+i);
						            bt_edit.setBackgroundColor(Color.BLACK);
						            bt_edit.setTextColor(Color.GREEN);
						            bt_edit.setLayoutParams(child_params);
						            bt_edit.setOnClickListener(clicks);
						            llRow.addView(bt_edit);
						            
						            bt_delete = new Button(getApplicationContext());
						            bt_delete.setText("DELETE");
						            bt_delete.setId(4000+i);
						            bt_delete.setBackgroundColor(Color.BLACK);
						            bt_delete.setTextColor(Color.GREEN);
						            bt_delete.setLayoutParams(child_params);
						            bt_delete.setOnClickListener(clicks);
						            llRow.addView(bt_delete);
						            
						            main_layout.addView(llRow);
								}
							}
						}
						
					}catch(Exception ex){}
				}
				else
				{
					Toast.makeText(getApplicationContext(),
								e.toString(), Toast.LENGTH_LONG)
							.show();
				}
			}
		});
		
		
		clicks=new OnClickListener() {

		    @Override
		    public void onClick(View v) {
		    	progressDialog = new ProgressDialog(NotificationViewActivity.this);
				progressDialog.setMessage("Please Wait...");
				progressDialog.setCancelable(false);
				progressDialog.show();
		    	
		    	noti_intent = new Intent(NotificationViewActivity.this,SetNotificationActivity.class);
		    	
		    	if(v.getId()>=4000 && v.getId()<6000)
		    	{
		    		index = v.getId()-4000;
		    		noti_intent.putExtra("TASK","DELETE");

		    		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							switch (which) {
							case DialogInterface.BUTTON_POSITIVE:
								// Set Push Notification for User to remove alarm
					    		ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
					    		userQuery.whereEqualTo("username", receiver);

					    		userQuery.findInBackground(new FindCallback<ParseUser>() {

					    			

									@Override
					    			public void done(List<ParseUser> user_objects, ParseException e) {
					    				// TODO Auto-generated method stub
					    				Log.d("userQuery", "" + user_objects.size());
					    				location_username = user_objects.get(0).getUsername();
					    				code_to_delete =  user_objects.get(0).getInt("Notification_Code");
					    				Log.d("username", location_username);
					    			}
					    		});

					    		// Find devices associated with these users
					    		ParsePush parsePush = new ParsePush();
					    		ParseQuery pQuery = ParseInstallation.getQuery(); 
					    		pQuery.whereEqualTo("username", location_username); 

					    		JSONObject data = null;
					    		try {
					    			data = new JSONObject(
					    					"{\"action\": \"com.example.parseaccount.UPDATE_STATUS\", \"customdata\": \"delete\" }");
					    			data.put("NotificationCode", code_to_delete);
					    		} catch (JSONException je) {
					    			je.printStackTrace();
					    		}

					    		parsePush.sendDataInBackground(data, pQuery);
					    		
					    		LinearLayout temp_lay = (LinearLayout)findViewById(index);
					    		temp_lay.setVisibility(View.GONE);
					    		progressDialog.dismiss();
								break;

							case DialogInterface.BUTTON_NEGATIVE:
								// No button clicked
					    		progressDialog.dismiss();
								break;
							}
						}
					};

					AlertDialog.Builder builder = new AlertDialog.Builder(
							NotificationViewActivity.this);
					builder.setMessage(
							"Notification will be deleted when data updated on Receiver's Side. Are you sure to delete?")
							.setPositiveButton("Yes",
									dialogClickListener)
							.setNegativeButton("No",
									dialogClickListener).show();
		    		
		    	}
		    	else
		    	{
		    		if(v.getId()>=0 && v.getId()<2000)
			    	{
			    		index = v.getId();
			    		noti_intent.putExtra("TASK","INFO");
			    	}
			    	else if(v.getId()>=2000 && v.getId()<4000)
			    	{
			    		index = v.getId()-2000;
			    		noti_intent.putExtra("TASK","EDIT");
			    	}
		    		
		    	
		    		
		    	 	ParseQuery<ParseObject> query = ParseQuery.getQuery("Notification");
		     		query.whereEqualTo("Sender", ParseUser.getCurrentUser().getUsername());
		     		
		     		query.findInBackground(new FindCallback<ParseObject>() {
		     			@Override
		     			public void done(List<ParseObject> objects, ParseException e) {
		     				progressDialog.dismiss();
		     				
		     				    
		     				if(e==null)
		     				{
		     					try
		     					{
		     						Log.d("Query",""+objects.size());
		     						Log.d("Index",""+index);	
		     						if(objects.size() != 0){
		     								if(objects.get(index).getInt("Received")==0)
		     								{
		     									receiver = objects.get(index).getString("Receiver");
		     									type = objects.get(index).getString("Notification_Type");
		     									message = objects.get(index).getString("Message");
		     									location = objects.get(index).getString("Location");
		     									rad = objects.get(index).getDouble("Map_Radius");
		     									stdt = objects.get(index).getString("StartDate");
		     									endt = objects.get(index).getString("EndDate");
		     									sttm = objects.get(index).getString("StartTime");
		     									entm = objects.get(index).getString("EndTime");
		     									object_id = objects.get(index).getObjectId().toString();
		     									noti_id = objects.get(index).getInt("Notification_Code");
		     									img_file_name = objects.get(index).getParseFile("Notification_Image").getName();
		        	
									        	
												
												noti_intent.putExtra("TYPE", type);
												noti_intent.putExtra("USERNAME", receiver);
												noti_intent.putExtra("MESSAGE", message);
												noti_intent.putExtra("LOCATION", location);
												noti_intent.putExtra("RADIUS", rad);
												noti_intent.putExtra("STARTDATE", stdt);
												noti_intent.putExtra("ENDDATE", endt);
												noti_intent.putExtra("STARTTIME", sttm);
												noti_intent.putExtra("ENDTIME", entm);
												noti_intent.putExtra("IMAGE", img_file_name);
												noti_intent.putExtra("OBJECTID", object_id);
												noti_intent.putExtra("NOTIFICATION_CODE",noti_id);
												startActivity(noti_intent);
		     								}
		     							}
		     					}catch(Exception ex){}
		     				}
		     			}
		     		});
		    	}
		    }
		};
	}

}
