package com.example.parseaccount;

import java.text.SimpleDateFormat;
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
import android.database.Cursor;
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

public class NotificationsTab2 extends Fragment {
	
	public View rootView;
	TextView header,tv_receiver,tv_type;
	Button bt_view,bt_edit,bt_delete;
	LinearLayout main_layout;
	
	String type="",message="",location="",stdt="",endt="",sttm="",entm="";
	String[] receiver,create_date;
	int total_notifications=-1,noti_fnd=0;
	double rad=0.0;
	String img_file_name;
	String object_id;
	int noti_id;
	int[] noti_code;
	ProgressDialog progressDialog;
	OnClickListener clicks;
	int index;
	Intent noti_intent;
	String view_sender, view_message, view_date;
	byte[] view_image; 
	
	private int code_to_delete;
	String location_username;
	LinearLayout.LayoutParams params;
	LinearLayout.LayoutParams child_params;
	LinearLayout llRow;
	LinearLayout llCol;
	LinearLayout.LayoutParams col_params;
	protected LinearLayout llRowbt;
	private Object touch;
	protected char letter;
	protected char prev_letter;
	protected LinearLayout llLetter;
	private DBAdapter db;
	protected int del;
	//private SimpleDateFormat sdf;
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.notificationtab1, container, false);
        
	    
	    
	    noti_code = new int[32000];
	    main_layout = (LinearLayout) rootView.findViewById(R.id.main_layout);
	    
	    db = new DBAdapter(getActivity());
		db.open();
		
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

        receiver = new String[32000];
        create_date = new String[32000];
        total_notifications=-1;
        noti_fnd=0;
        noti_id=0;
	    ParseQuery<ParseObject> query = ParseQuery.getQuery("Notification");
		query.whereEqualTo("Receiver", ParseUser.getCurrentUser().getUsername());
		query.orderByAscending("updatedAt");
		
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
								if(objects.get(i).getInt("Received")==1)
								{
									noti_fnd=1;
									total_notifications++;
									receiver[total_notifications] = objects.get(i).getString("ReqFromName") + "(" + objects.get(i).getString("Sender") + ")";
									Date date = objects.get(i).getUpdatedAt();
									create_date[total_notifications] = date.toString();
									
									noti_code[total_notifications] = objects.get(i).getInt("Notification_Code");
								}
							}
							Log.d("noti_fnd",""+noti_fnd);
							if(noti_fnd!=1)
							{
								llRow = new LinearLayout(getActivity());
						        llRow.setOrientation(LinearLayout.HORIZONTAL);
						        llRow.setLayoutParams(params);
						        llRow.setBackgroundResource(R.drawable.linear_row);
						        llRow.setPadding(10, 10, 10, 10);
						        
						        tv_receiver = new TextView(getActivity());
						        tv_receiver.setText("No Notification");
						        tv_receiver.setTextSize(30);
					            tv_receiver.setTextColor(Color.BLACK);
						        tv_receiver.setTypeface(Typeface.MONOSPACE);
								llRow.addView(tv_receiver);
								main_layout.addView(llRow);
							}
							
				            String temp;
				            int temp_id;
				            Log.d("total_notifications",total_notifications+"");
				            for (int i = 0;  i < total_notifications ;  i++ )
				            {
				            	
				                for (int j = i + 1;  j <= total_notifications;  j++ )
				                {  
				                	/*Log.d("i",i+"");
						            Log.d("j",j+"");
				                	Log.d("receiver [ i ]",receiver [ i ]);
				                	Log.d("receiver [ j ]",receiver [ j ]);*/
				                         if ( receiver [ i ].compareToIgnoreCase( receiver [ j ] ) > 0 )
				                          {                                             // ascending sort
				                                      temp = receiver [ i ];
				                                      receiver [ i ] = receiver [ j ];    // swapping
				                                      receiver [ j ] = temp; 
				                                      
				                                      temp_id = noti_code[i];
				                                      noti_code[i] = noti_code[j];
				                                      noti_code[j] = temp_id;
				                                      
				                           } 
				                  } 
				             } 
				            
				            for(int i=0; i<=total_notifications; i++)
							{
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
					            tv_receiver.setText(receiver[i]);
					            tv_receiver.setTypeface(Typeface.MONOSPACE);
					            tv_receiver.setTextSize(20);
					            tv_receiver.setTextColor(Color.BLACK);
					            tv_receiver.setId(i);
					            llCol.addView(tv_receiver);
					            
					            tv_receiver=new TextView(getActivity());
					            tv_receiver.setText(create_date[i]);
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
					         // To put letters********************************************\
								Log.d("text",receiver[i]);
								letter = receiver[i].charAt(0);
								Log.d("letter",""+letter);
								
								if(Character.toUpperCase(letter) != Character.toUpperCase(prev_letter))
								{
									Log.d("hello","print a letter");
									llLetter = new LinearLayout(getActivity());
									llLetter.setOrientation(LinearLayout.HORIZONTAL);
									llLetter.setPadding(0, 0, 0, 0);
									llLetter.setLayoutParams(params);
									
									tv_receiver = new TextView(getActivity());
									tv_receiver.setText((""+letter).toUpperCase());
									tv_receiver.setTextSize(25);
									tv_receiver.setTextColor(Color.BLUE);
									tv_receiver.setTypeface(Typeface.MONOSPACE);
									tv_receiver.setPadding(2, 0, 0, 0);
									llLetter.addView(tv_receiver);
									main_layout.addView(llLetter);
								}
								prev_letter = letter;
								Log.d("prev_letter",""+prev_letter);
								//************************************************************
					            main_layout.addView(llRow);
					            main_layout.addView(llRowbt);
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
				}
			}
		});
		
		clicks=new OnClickListener() {

		    @Override
		    public void onClick(View v) {
		    	
		    	del = v.getId();
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
								// Delete notification from local database
								try{
									ParseQuery<ParseObject> query = ParseQuery.getQuery("Notification");
									query.whereEqualTo("Receiver", ParseUser.getCurrentUser().getUsername());
									query.whereEqualTo("Notification_Code", noti_code[del-4000]);
									query.findInBackground(new FindCallback<ParseObject>() {
										@Override
										public void done(List<ParseObject> objects, ParseException e) {
											objects.get(0).deleteInBackground();
										}
									});
						    		if(db.deleteRow(noti_code[del-4000]))
						    		{
						    			Toast.makeText(getActivity(), "Successfully Deleted", Toast.LENGTH_LONG).show();
						    		}
						    		else
						    		{
						    			Toast.makeText(getActivity(), "Delete is unsuccessfull", Toast.LENGTH_LONG).show();
						    		}
								}
								catch(Exception ex){}
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
							"Are you sure to delete Notification Detail?")
							.setPositiveButton("Yes",
									dialogClickListener)
							.setNegativeButton("No",
									dialogClickListener).show();
		    		
		    	}
		    	else if(v.getId()>=0 && v.getId()<2000)
		    	{
		    		//show notification info from local database
		    		Log.d("v.getId()",v.getId()+"");
		    		Log.d("noti_code[v.getId()]",noti_code[v.getId()]+"");
		    		Cursor cur = db.getAllemp2(noti_code[v.getId()]);
		    		if(cur.moveToFirst()!=false)
		    		{
		    			cur.moveToFirst();
					
							view_sender = cur.getString(cur.getColumnIndex("sender"));
							view_message = cur.getString(cur.getColumnIndex("message"));
							view_date = cur.getString(cur.getColumnIndex("in_date"));
							view_image = cur.getBlob(cur.getColumnIndex("image"));
							
							Intent intent_view = new Intent(getActivity(),LocalStorageActivity.class);
							intent_view.putExtra("Action", "VIEW");
							intent_view.putExtra("view_sender", view_sender);
							intent_view.putExtra("view_message", view_message);
							intent_view.putExtra("view_date", view_date);
							intent_view.putExtra("view_image", view_image);
							startActivity(intent_view);
		    		}
		    		else
		    		{
		    			Toast.makeText(getActivity(), "Not stored in local database", Toast.LENGTH_LONG).show();
		    		}
					
		    	}
		    	
		    }
		};
        
        return rootView;
	}

}
