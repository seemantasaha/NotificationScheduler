package com.example.parseaccount;

import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
 
public class FriendsTab1 extends Fragment {
	
	public String sUserId,sentBy,user,requested_user,ReqFromName,ReqToName;
	  public static final String USER_ID_KEY = "userId"; 
	  EditText req_user;
	  TextView User;
	  Button req_send,req_accept,req_reject,req_block;
	  int accept,reject,block;
	  
	  TextView tv_user,tv_curent_user,header;
	    Button bt_cancel,bt_unblock;

	  ProgressDialog progressDialog;
	    LinearLayout lf;
	    public View rootView;
		protected LinearLayout llRow;
		protected LayoutParams params;
		protected int frnd_fnd = 0;
		protected LayoutParams bt_params,tv_params;
		protected Handler handler;
	    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.friendstab1, container, false);
        
        handler = new Handler();
        lf = (LinearLayout) rootView.findViewById(R.id.llF);
	    
	    req_user = (EditText) rootView.findViewById(R.id.etReqUser);
	    
	    //header = (TextView) rootView.findViewById(R.id.header_username);
	    //header.setText(header.getText()+ParseUser.getCurrentUser().getUsername());

	    //Friend Requests
	    Log.e("Current User", ParseUser.getCurrentUser().getUsername());

		params = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        
        bt_params = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        bt_params.setMargins(0, 0, 5, 0);
        
        tv_params = new LinearLayout.LayoutParams(
                500, LayoutParams.WRAP_CONTENT);
        
	    progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();
	    
	    ParseQuery<ParseObject> query = ParseQuery.getQuery("Friends");
		query.whereEqualTo("ReqFrom", ParseUser.getCurrentUser().getUsername());
		
		
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				// TODO Auto-generated method stub
					
				
				try{
					Log.d("Query",""+objects.size());
					if (e == null) {
						if(objects.size() != 0){
							for(int i=0;i<objects.size();i++)
							{
					            // Access the array of results here
								accept = objects.get(i).getInt("Accept");
		  			            reject = objects.get(i).getInt("Reject");
		  			            block = objects.get(i).getInt("Block");
		  			            Log.d("number",""+i);
		  			            if(accept==0 && reject==0 && block==0)
					            {
		  			            	frnd_fnd=1;
						            user = objects.get(i).getString("ReqUser");
						            sentBy = objects.get(i).getString("ReqFrom");
						            
						            
						            LinearLayout llRow = new LinearLayout(getActivity());
						            llRow.setOrientation(LinearLayout.HORIZONTAL);
						            llRow.setLayoutParams(params);
						            llRow.setBackgroundResource(R.drawable.linear_row);
						            llRow.setPadding(10, 10, 10, 10);
						            
						            tv_user=new TextView(getActivity());
						            tv_user.setText(user);
						            tv_user.setTextSize(30);
						            tv_user.setTextColor(Color.BLACK);
						            tv_user.setTypeface(Typeface.MONOSPACE);
						            tv_user.setTextColor(Color.BLACK);
						            tv_user.setLayoutParams(tv_params);
						            tv_user.setId(i);
						            llRow.addView(tv_user);
						            
						            bt_cancel = new Button(getActivity());
						            bt_cancel.setText("Cancel");
						            bt_cancel.setId(i);
						            bt_cancel.setLayoutParams(bt_params);
						            bt_cancel.setTypeface(Typeface.MONOSPACE);
						            bt_cancel.setBackgroundColor(0xFF88AAFF);
						            bt_cancel.setTextColor(Color.WHITE);
						            bt_cancel.setOnClickListener(clicks);
						            llRow.addView(bt_cancel);
						            lf.addView(llRow);
					            }
		  			            else if(accept==0 && reject==0 && block==1)
					            {
		  			            	frnd_fnd=1;
						            user = objects.get(i).getString("ReqUser");
						            sentBy = objects.get(i).getString("ReqFrom");
						            
						            
						            LinearLayout llRow = new LinearLayout(getActivity());
						            llRow.setOrientation(LinearLayout.HORIZONTAL);
						            llRow.setLayoutParams(params);
						            llRow.setBackgroundResource(R.drawable.linear_row);
						            llRow.setPadding(10, 10, 10, 10);
						            
						            tv_user=new TextView(getActivity());
						            tv_user.setText(user);
						            tv_user.setTextSize(30);
						            tv_user.setTextColor(Color.BLACK);
						            tv_user.setTypeface(Typeface.MONOSPACE);
						            tv_user.setLayoutParams(tv_params);
						            tv_user.setId(i);
						            llRow.addView(tv_user);
						            
						            bt_unblock = new Button(getActivity());
						            bt_unblock.setText("Unblock");
						            bt_unblock.setId(1000+i);
						            bt_unblock.setBackgroundColor(0xFF88AAFF);
						            bt_unblock.setTypeface(Typeface.MONOSPACE);
						            bt_unblock.setTextColor(Color.WHITE);
						            bt_unblock.setLayoutParams(bt_params);
						            bt_unblock.setOnClickListener(clicks);
						            llRow.addView(bt_unblock);
					            }
							}
							Log.d("Freinf Req Found",""+frnd_fnd);
							if(frnd_fnd!=1)
							{
								llRow = new LinearLayout(getActivity());
						        llRow.setOrientation(LinearLayout.HORIZONTAL);
						        llRow.setLayoutParams(params);
						        llRow.setBackgroundResource(R.drawable.linear_row);
						        llRow.setPadding(10, 10, 10, 10);
						        
								tv_user = new TextView(getActivity());
								tv_user.setText("No Friend Request Sent");
					            tv_user.setTypeface(Typeface.MONOSPACE);
								tv_user.setGravity(Gravity.CENTER);
								tv_user.setTextSize(30);
								
								llRow.addView(tv_user);
								lf.addView(llRow);
							}
						}
			        } else {
				    }
					}catch(Exception ex)
					{
						
					}
				
				progressDialog.dismiss();
			}
		});
		
		// Freind Request Sending
	    req_send = (Button) rootView.findViewById(R.id.btReqSend);
	    
	    req_send.setOnClickListener(new OnClickListener() {
	      public void onClick(View v) {
	        // Starts an intent of the log in activity
	    	  
	    	  
	    	  sUserId = ParseUser.getCurrentUser().getObjectId();
	    	  Toast.makeText(getActivity(),sUserId, Toast.LENGTH_LONG).show();
	    	  requested_user = req_user.getText().toString();
	    	  
	    	if(requested_user.equals(ParseUser.getCurrentUser().getUsername()))
	  		{//when current user send request to current user
	    		Toast.makeText(getActivity(),
						"You can not be friend of Yours", Toast.LENGTH_LONG)
						.show();
	  		}
	    	else
	    	{//other users
		  		ParseQuery<ParseUser> query_user = ParseUser.getQuery();
		  		Log.d("query_user", query_user.getClassName());
		  		Log.d("query_user", query_user.getClassName());
		  		query_user.whereEqualTo("username", requested_user);
		  		Log.d("user",""+requested_user);
	
		  		query_user.findInBackground(new FindCallback<ParseUser>() {
		  			@Override
		  			public void done(List<ParseUser> user_objects, ParseException e) {
		  				// TODO Auto-generated method stub
		  				try{
		  				Log.d("Query",""+user_objects.size());
		  				if (user_objects.size() == 0) {//requested user is not a user of the app
		  					Toast.makeText(getActivity(),
		  							"Username not found!", Toast.LENGTH_LONG)
		  							.show();
		  		        }
		  				else
		  				{//user exists
		  					ParseQuery<ParseObject> query_frnds = ParseQuery.getQuery("Friends");
		  		    		query_frnds.whereEqualTo("ReqUser", requested_user);
		  		    		query_frnds.whereEqualTo("ReqFrom", ParseUser.getCurrentUser().getUsername());
		  		    		
		  		    		query_frnds.findInBackground(new FindCallback<ParseObject>() {
		  		    			@Override
		  		    			public void done(List<ParseObject> frnds_objects, ParseException e) {
		  		    				// TODO Auto-generated method stub
		  		    				Log.d("Query",""+frnds_objects.size());
		  		    				if (e == null) {
		  		    					if(frnds_objects.size() != 0){
		  		    			            // Access the array of results here
		  		    						accept = frnds_objects.get(0).getInt("Accept");
		  		    			            reject = frnds_objects.get(0).getInt("Reject");
		  		    			            block = frnds_objects.get(0).getInt("Block");
		  		    			            Log.d("States",""+accept+","+reject+","+block);
		  		    			            if(accept==0 && reject==1 && block==0)
		  		    			            {
									              frnds_objects.get(0).put("Accept", 0);
									              frnds_objects.get(0).put("Reject", 0);
									              frnds_objects.get(0).put("Block", 0);
									              frnds_objects.get(0).saveInBackground();
									              
									              Toast.makeText(getActivity(), "Successfully Friend Request Sent",
						                                  Toast.LENGTH_SHORT).show();
		  		    			            }
		  		    			            else if(accept==1){
												Toast.makeText(getActivity(),
														"You are already friend to this user.", Toast.LENGTH_LONG)
														.show();
											}
		  		    			            else if(block==1){
												Toast.makeText(getActivity(),
														"You can not be friend with this user.", Toast.LENGTH_LONG)
														.show();
											}
		  		    			            else
		  		    			            {
		  		    			            	Toast.makeText(getActivity(),
														"You are already friends or request sent.", Toast.LENGTH_LONG)
														.show();
		  		    			            }
		  		    					}
		  		    					else {//Friend request is on processing between users [VICE VERSA]
		  		    						ParseQuery<ParseObject> query_frnds = ParseQuery.getQuery("Friends");
		  				  		    		query_frnds.whereEqualTo("ReqFrom", requested_user);
		  				  		    		query_frnds.whereEqualTo("ReqUser", ParseUser.getCurrentUser().getUsername());
		  				  		    		
		  				  		    		query_frnds.findInBackground(new FindCallback<ParseObject>() {
		  			  		    			@Override
		  			  		    			public void done(List<ParseObject> frnds_objects, ParseException e) {
		  			  		    				// TODO Auto-generated method stub
		  			  		    				Log.d("Query",""+frnds_objects.size());
		  			  		    				if (e == null) {
		  			  		    					if(frnds_objects.size() != 0){
		  			  		    					accept = frnds_objects.get(0).getInt("Accept");
				  		    			            reject = frnds_objects.get(0).getInt("Reject");
				  		    			            block = frnds_objects.get(0).getInt("Block");	
		  			  		    					if(reject==1 || block==1)
		  			  		    					{//Request users friend request was rejected or blocked earlier
			  			  		    					Log.d("Friend","Request users friend request was rejected earlier");
			  			  		    					
			  			  		    					  frnds_objects.get(0).put(USER_ID_KEY, sUserId);
			  			  		    					  frnds_objects.get(0).put("ReqFrom", ParseUser.getCurrentUser().getUsername());
			  			  		    					  frnds_objects.get(0).put("ReqUser", requested_user);
			  			  		    					  frnds_objects.get(0).put("Accept",0);
			  			  		    					  frnds_objects.get(0).put("Reject",0);
			  			  		    					  frnds_objects.get(0).put("Block",0);
			  			  		    					  
			  			  		    					  frnds_objects.get(0).saveInBackground();
			  			  		    					  
			  			  		    					Toast.makeText(getActivity(), "Successfully Friend Request Sent",
								                                  Toast.LENGTH_SHORT).show();
		  			  		    					}
		  			  		    					else
		  			  		    					{
		  			  		    						if(accept==1)
		  			  		    						{
			  			  		    						Toast.makeText(getActivity(),
																"You are already friend to this user.", Toast.LENGTH_LONG)
																.show();
		  			  		    						}
		  			  		    						else
		  			  		    						{
		  			  		    							Toast.makeText(getActivity(),
																"Friend request is already sent to this user.", Toast.LENGTH_LONG)
																.show();
		  			  		    						}
		  			  		    					}
		  			  		    					}
		  			  		    					else
		  			  		    					{//no row in the Friends Class but Requested user is valid
		  			  		    					ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
		  											userQuery.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
		  											
		  											userQuery.findInBackground(new FindCallback<ParseUser>() {
		  												@Override
		  												public void done(List<ParseUser> user_objects, ParseException e) {
		  													// TODO Auto-generated method stub
		  													if(e==null)
		  													{
		  														try{
		  															ReqFromName = user_objects.get(0).getString("FirstName") + " " 
		  																	+ user_objects.get(0).getString("LastName");
		  															
		  															ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
		  				  											userQuery.whereEqualTo("username", requested_user);
		  				  											
		  				  											userQuery.findInBackground(new FindCallback<ParseUser>() {
		  				  												@Override
		  				  												public void done(List<ParseUser> user_objects, ParseException e) {
		  				  													// TODO Auto-generated method stub
		  				  													if(e==null)
		  				  													{
		  				  														ReqToName = user_objects.get(0).getString("FirstName") + " " 
				  																	+ user_objects.get(0).getString("LastName");
		  				  														
			  				  													Log.d("Friend","No friend request sent to this user");
					  						  		    			        	ParseACL acl = new ParseACL(); 
					  										  					ParseObject friends = new ParseObject("Friends");
					  												              friends.put(USER_ID_KEY, sUserId);
					  												              friends.put("ReqFrom", ParseUser.getCurrentUser().getUsername());
					  												              friends.put("ReqFromName", ReqFromName);
					  												              friends.put("ReqUser", requested_user);
					  												              friends.put("ReqUserName", ReqToName);
					  												              friends.put("Accept", 0);
					  												              friends.put("Reject", 0);
					  												              friends.put("Block", 0);
					  												              friends.put("MUTE", true);
					  												              friends.put("SUPERTRUST", false);
					  												              acl.setPublicWriteAccess(true);
					  												              acl.setPublicReadAccess(true);
					  												              friends.setACL(acl);
					  												              friends.saveInBackground(new SaveCallback() {
					  												                  public void done(ParseException e) {
					  												                      
					  												                      if (e == null) {
					  																			// Show a simple Toast message upon successful registration
					  												                    	  Toast.makeText(getActivity(), "Successfully Friend Request Sent",
					  												                                  Toast.LENGTH_SHORT).show();
					  																		} else {
					  																			Toast.makeText(getActivity(),
					  																					"Friend Request Sending Error:"+e.toString(), Toast.LENGTH_LONG)
					  																					.show();
					  																		}
					  												                  }
					  												              });
		  				  													}
		  				  												}
		  				  											});
		  															
		  														}catch(Exception ex){}
		  													}
		  												}
		  											});
		  			  		    					
		  			  		    					}
		  			  		    				}
		  			  		    			}
		  				  		    		});
		  				  		    		
		  		    						
										}
		  		    					}
		  		    				else {
										Toast.makeText(getActivity(),
												"Friend Request Quering Error:"+e.toString(), Toast.LENGTH_LONG)
												.show();
									}
		  		    			}
		  		    				});
		  					}
		  				}catch(Exception ex){}
		  				}
		  			});  
	    		}
	    	req_user.setText("");
	      }
	    });
		
		
		
		return rootView;
    }
    
    OnClickListener clicks=new OnClickListener() {

	    @Override
	    public void onClick(View v) {

	    	Toast.makeText(getActivity(),
						"Button Clicked:"+v.getId(), Toast.LENGTH_LONG)
					.show();
	    	
	        if(v.getId()>=0 && v.getId()<1000)  
	        {
	        	tv_curent_user = (TextView) rootView.findViewById(v.getId());
	        	update_in_friends("Cancel",tv_curent_user.getText().toString());
	        	Toast.makeText(getActivity(),
						"From User:"+tv_curent_user.getText().toString(), Toast.LENGTH_LONG)
					.show();
	        }
	        else if(v.getId()>=1000 && v.getId()<2000)  
	        {
	        	tv_curent_user = (TextView) rootView.findViewById(v.getId()-1000);
	        	update_in_friends("Unblock",tv_curent_user.getText().toString());
	        	Toast.makeText(getActivity(),
						"From User:"+tv_curent_user.getText().toString(), Toast.LENGTH_LONG)
					.show();
	        }
	    }

	    void update_in_friends(final String state,final String req_from)
		{
			ParseQuery<ParseObject> query = ParseQuery.getQuery("Friends");
	  		query.whereEqualTo("ReqUser", ParseUser.getCurrentUser().getUsername());
	  		query.whereEqualTo("ReqFrom", req_from);
	  		Log.d("Current User",ParseUser.getCurrentUser().getUsername());
	  		Log.d("Request from User",req_from);
	  		query.findInBackground(new FindCallback<ParseObject>() {
	  			@Override
	  			public void done(List<ParseObject> objects, ParseException e) {
	  				// TODO Auto-generated method stub
	  				try{
	  				Log.d("Query",""+objects.size());
	  				if (e == null) {
	  					if(objects.size() != 0){
	  			            // Access the array of results here
	  						Log.d("Info",""+objects.get(0).getString("ReqFrom")+"->"+state);
	  						
	  						if(state.equals("Cancel"))
	  						{
	  							objects.get(0).deleteInBackground();
	  							Toast.makeText(getActivity(),
	  									"Friend Request Cancelled", Toast.LENGTH_LONG)
										.show();
	  						}
	  						
	  						if(state.equals("Unblock"))
	  						{
	  							objects.get(0).put("Block", 0);
	  							objects.get(0).saveInBackground();
	  							Toast.makeText(getActivity(),
	  									"Friend Unblocked", Toast.LENGTH_LONG)
										.show();
	  						}
	  					}
	  		        } else {
	  		        	Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
	  			    }
	  				}catch(Exception ex){}
	  			}
	  		});
		}
	};
 
}