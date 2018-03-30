package com.example.parseaccount;

import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class NotificationsTab1 extends Fragment {
	
	public View rootView;
	TextView header,tv_receiver,tv_type;
	Button bt_view,bt_edit,bt_delete;
	LinearLayout main_layout;
	
	String receiver="",type="",create_date="",message="",location="",stdt="",endt="",sttm="",entm="";
	double rad=0.0;
	String img_file_name;
	String object_id;
	int noti_id;
	
	ProgressDialog progressDialog;
	OnClickListener clicks;
	int index;
	Intent noti_intent;
	int fnd=0;
	private int code_to_delete;
	String location_username;
	LinearLayout.LayoutParams params;
	LinearLayout.LayoutParams child_params;
	LinearLayout llRow;
	LinearLayout llCol;
	LinearLayout.LayoutParams col_params;
	protected LinearLayout llRowbt;
	private Object touch;
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.notificationtab1, container, false);
        
	    
	    main_layout = (LinearLayout) rootView.findViewById(R.id.main_layout);
	    
	    progressDialog = new ProgressDialog(getActivity());
		progressDialog.setMessage("Please Wait...");
		progressDialog.setCancelable(false);
		progressDialog.show();

		params = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        
        child_params = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        child_params.setMargins(10, 0, 5, 0);
		
        col_params = new LinearLayout.LayoutParams(
                400, LayoutParams.WRAP_CONTENT);
	    
	    ParseQuery<ParseObject> query = ParseQuery.getQuery("Notification");
		query.whereEqualTo("Sender", ParseUser.getCurrentUser().getUsername());
		query.orderByAscending("StartDate");
		
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
									fnd=1;
									receiver = objects.get(i).getString("ReqUserName") + "(" + objects.get(i).getString("Receiver") + ")";
									Date date = objects.get(i).getCreatedAt();
									create_date = date.toString();
									
						            
						            llRow = new LinearLayout(getActivity());
						            llRow.setOrientation(LinearLayout.HORIZONTAL);
						            llRow.setLayoutParams(params);
						            llRow.setBackgroundResource(R.drawable.linear_row);
						            llRow.setPadding(10, 10, 10, 10);
						            llRow.setOnClickListener(clicks);
						            llRow.setId(8000+i);
						            
						            
						            
						            llCol = new LinearLayout(getActivity());
						            llCol.setOrientation(LinearLayout.VERTICAL);
						            llCol.setLayoutParams(params);
						            
						            tv_receiver=new TextView(getActivity());
						            tv_receiver.setText(receiver);
						            tv_receiver.setTypeface(Typeface.MONOSPACE);
						            tv_receiver.setTextColor(Color.BLACK);
						            tv_receiver.setTextSize(20);
						            tv_receiver.setTextColor(Color.BLACK);
						            tv_receiver.setId(i);
						            llCol.addView(tv_receiver);
						            
						            tv_receiver=new TextView(getActivity());
						            tv_receiver.setText(create_date);
						            tv_receiver.setTypeface(Typeface.MONOSPACE);
						            tv_receiver.setTextSize(15);
						            tv_receiver.setTextColor(Color.BLACK);
						            tv_receiver.setId(i);
						            llCol.addView(tv_receiver);
						            
						            llRow.addView(llCol);
						            
						            llRowbt = new LinearLayout(getActivity());
						            llRowbt.setOrientation(LinearLayout.HORIZONTAL);
						            llRowbt.setLayoutParams(params);
						            llRowbt.setGravity(Gravity.RIGHT);
						            llRowbt.setId(10000+i);
						            
						            bt_view = new Button(getActivity());
						            bt_view.setText("VIEW");
						            bt_view.setId(i);
						            bt_view.setTypeface(Typeface.MONOSPACE);
						            bt_view.setBackgroundColor(0xFF88AAFF);
						            bt_view.setTextColor(Color.WHITE);
						            bt_view.setLayoutParams(child_params);
						            bt_view.setOnClickListener(clicks);
						            llRowbt.addView(bt_view);
						            
						            bt_edit = new Button(getActivity());
						            bt_edit.setText("EDIT");
						            bt_edit.setId(2000+i);
						            bt_edit.setTypeface(Typeface.MONOSPACE);
						            bt_edit.setBackgroundColor(0xFF88AAFF);
						            bt_edit.setTextColor(Color.WHITE);
						            bt_edit.setLayoutParams(child_params);
						            bt_edit.setOnClickListener(clicks);
						            llRowbt.addView(bt_edit);
						            
						            bt_delete = new Button(getActivity());
						            bt_delete.setText("DELETE");
						            bt_delete.setId(4000+i);
						            bt_delete.setTypeface(Typeface.MONOSPACE);
						            bt_delete.setBackgroundColor(0xFF88AAFF);
						            bt_delete.setTextColor(Color.WHITE);
						            bt_delete.setLayoutParams(child_params);
						            bt_delete.setOnClickListener(clicks);
						            llRowbt.addView(bt_delete);
						            
						            llRowbt.setVisibility(View.GONE);
						            
						            main_layout.addView(llRow);
						            main_layout.addView(llRowbt);
								}
							}
							Log.d("fnd",""+fnd);
							if(fnd!=1)
							{
								llRow = new LinearLayout(getActivity());
						        llRow.setOrientation(LinearLayout.HORIZONTAL);
						        llRow.setLayoutParams(params);
						        llRow.setBackgroundResource(R.drawable.linear_row);
						        llRow.setPadding(10, 10, 10, 10);
						        
						        tv_receiver = new TextView(getActivity());
						        tv_receiver.setText("No Notification");
						        tv_receiver.setTextSize(30);
						        tv_receiver.setTypeface(Typeface.MONOSPACE);
								llRow.addView(tv_receiver);
								
								main_layout.addView(llRow);
							}
						}
						else
						{
							llRow = new LinearLayout(getActivity());
					        llRow.setOrientation(LinearLayout.HORIZONTAL);
					        llRow.setLayoutParams(params);
					        llRow.setBackgroundResource(R.drawable.linear_row);
					        llRow.setPadding(10, 10, 10, 10);
					        
					        tv_receiver = new TextView(getActivity());
					        tv_receiver.setText("No Notification");
					        tv_receiver.setTextSize(30);
					        tv_receiver.setTypeface(Typeface.MONOSPACE);
							llRow.addView(tv_receiver);
							main_layout.addView(llRow);
						}
					}catch(Exception ex){
						ex.printStackTrace();
					}
				}
				else
				{
					Toast.makeText(getActivity(),
								e.toString(), Toast.LENGTH_LONG)
							.show();
				}
			}
		});
		
		clicks=new OnClickListener() {

		    @Override
		    public void onClick(View v) {
		    	
		    	
		    	noti_intent = new Intent(getActivity(),SetNotificationActivity.class);
		    	if(v.getId()>=8000 && v.getId()<10000)
		    	{
		    		LinearLayout temp_lay = (LinearLayout)rootView.findViewById(v.getId()+2000);
		    		if(temp_lay.getVisibility() == View.VISIBLE)
		    		{
		    			temp_lay.setVisibility(View.GONE);
		    		}
		    		else
		    		{
		    			temp_lay.setVisibility(View.VISIBLE);
		    		}
		    	}
		    	else if(v.getId()>=4000 && v.getId()<6000)
		    	{
		    		progressDialog = new ProgressDialog(getActivity());
					progressDialog.setMessage("Please Wait...");
					progressDialog.setCancelable(false);
					progressDialog.show();
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
					    		
					    		LinearLayout temp_lay = (LinearLayout)rootView.findViewById(index);
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
							getActivity());
					builder.setMessage(
							"Notification will be deleted when data updated on Receiver's Side. Are you sure to delete?")
							.setPositiveButton("Yes",
									dialogClickListener)
							.setNegativeButton("No",
									dialogClickListener).show();
		    		
		    	}
		    	else
		    	{
		    		progressDialog = new ProgressDialog(getActivity());
					progressDialog.setMessage("Please Wait...");
					progressDialog.setCancelable(false);
					progressDialog.show();
					
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
		     		query.orderByAscending("StartDate");
		     		
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
		     					}catch(Exception ex){
		     						ex.printStackTrace();
		     					}
		     				}
		     			}
		     		});
		    	}
		    }
		};
        
        return rootView;
	}

}
