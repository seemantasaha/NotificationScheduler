package com.example.parseaccount;

import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils.Permissions.Friends;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;



public class FriendsActivity extends Activity {

	  public String sUserId,sentBy,user,requested_user,ReqFromName,ReqToName;
	  public static final String USER_ID_KEY = "userId"; 
	  EditText req_user;
	  TextView User;
	  Button req_send,req_accept,req_reject,req_block;
	  int accept,reject,block;
	  
	  TextView tv_user,tv_curent_user,header;
	    Button bt_accept,bt_reject,bt_block;

	  ProgressDialog progressDialog;
	    LinearLayout lf;

	@Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_friends);
	    
	    lf = (LinearLayout) findViewById(R.id.llF);
	    
	    req_user = (EditText) findViewById(R.id.etReqUser);
	    
	    header = (TextView) findViewById(R.id.header_username);
	    header.setText(header.getText()+ParseUser.getCurrentUser().getUsername());

	    //Friend Requests
	    Log.e("Current User", ParseUser.getCurrentUser().getUsername());
	    
	    progressDialog=new ProgressDialog(FriendsActivity.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();
	    
	    ParseQuery<ParseObject> query = ParseQuery.getQuery("Friends");
		query.whereEqualTo("ReqUser", ParseUser.getCurrentUser().getUsername());
		
		
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				// TODO Auto-generated method stub
					
				OnClickListener clicks=new OnClickListener() {

				    @Override
				    public void onClick(View v) {

				    	Toast.makeText(getApplicationContext(),
									"Button Clicked:"+v.getId(), Toast.LENGTH_LONG)
								.show();
				    	
				        if(v.getId()>=0 && v.getId()<1000)  
				        {
				        	tv_curent_user = (TextView) findViewById(v.getId());
				        	update_in_friends("Accept",tv_curent_user.getText().toString());
				        	Toast.makeText(getApplicationContext(),
									"From User:"+tv_curent_user.getText().toString(), Toast.LENGTH_LONG)
								.show();
				        }
				        else if(v.getId()>=1000 && v.getId()<2000)  
				        {
				        	tv_curent_user = (TextView) findViewById(v.getId()-1000);
				        	update_in_friends("Reject",tv_curent_user.getText().toString());
				        	Toast.makeText(getApplicationContext(),
									"From User:"+tv_curent_user.getText().toString(), Toast.LENGTH_LONG)
								.show();
				        }
				        else if(v.getId()>=2000 && v.getId()<3000)  
				        {
				        	tv_curent_user = (TextView) findViewById(v.getId()-2000);
				        	update_in_friends("Block",tv_curent_user.getText().toString());
				        	Toast.makeText(getApplicationContext(),
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
				  						
				  						if(state.equals("Accept"))
				  						{
				  							objects.get(0).put("Accept", 1);
				  							objects.get(0).put("MUTE", true);
				  							objects.get(0).put("SUPERTRUST", false);
				  							Toast.makeText(getApplicationContext(),
				  									"Accept Action Performed", Toast.LENGTH_LONG)
													.show();
				  						}
				  						
				  						if(state.equals("Reject"))
				  						{
				  							objects.get(0).put("Reject", 1);
				  							Toast.makeText(getApplicationContext(),
				  									"Reject Action Performed", Toast.LENGTH_LONG)
													.show();
				  						}
				  						
				  						if(state.equals("Block"))
				  						{
				  							objects.get(0).put("Block", 1);
				  							Toast.makeText(getApplicationContext(),
				  									"Block Action Performed", Toast.LENGTH_LONG)
													.show();
				  						}
				  						objects.get(0).saveInBackground();
					      			  
				  					}
				  		        } else {
				  		        	Toast.makeText(FriendsActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
				  			    }
				  				}catch(Exception ex){}
				  			}
				  		});
					}
				};
				
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
					            user = objects.get(i).getString("ReqUser");
					            sentBy = objects.get(i).getString("ReqFrom");
					            Toast.makeText(FriendsActivity.this, sentBy, Toast.LENGTH_SHORT).show();
					            /*User.setText(sentBy);
					            req_accept.setEnabled(true);
					            req_reject.setEnabled(true);
					            req_block.setEnabled(true);*/
					            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
					            
					            LinearLayout.LayoutParams bt_params = new LinearLayout.LayoutParams(
					                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					            bt_params.setMargins(0, 0, 5, 0);
					            
					            LinearLayout llRow = new LinearLayout(getApplicationContext());
					            llRow.setOrientation(LinearLayout.HORIZONTAL);
					            llRow.setLayoutParams(params);
					            llRow.setBackgroundResource(R.drawable.linear_row);
					            llRow.setPadding(10, 10, 10, 10);
					            
					            tv_user=new TextView(getApplicationContext());
					            tv_user.setText(sentBy);
					            tv_user.setTextSize(30);
					            tv_user.setTextColor(Color.BLACK);
					            tv_user.setId(i);
					            llRow.addView(tv_user);
					            
					            bt_accept = new Button(getApplicationContext());
					            bt_accept.setText("Accept");
					            bt_accept.setId(i);
					            bt_accept.setBackgroundColor(Color.BLACK);
					            bt_accept.setTextColor(Color.GREEN);
					            bt_accept.setLayoutParams(bt_params);
					            bt_accept.setOnClickListener(clicks);
					            llRow.addView(bt_accept);
					            
					            
					            bt_reject = new Button(getApplicationContext());
					            bt_reject.setText("Reject");
					            bt_reject.setId(1000+i);
					            bt_reject.setBackgroundColor(Color.BLACK);
					            bt_reject.setTextColor(Color.GREEN);
					            bt_reject.setLayoutParams(bt_params);
					            bt_reject.setOnClickListener(clicks);
					            llRow.addView(bt_reject);
					            
					            bt_block = new Button(getApplicationContext());
					            bt_block.setText("Block");
					            bt_block.setId(2000+i);
					            bt_block.setBackgroundColor(Color.BLACK);
					            bt_block.setTextColor(Color.GREEN);
					            bt_block.setLayoutParams(bt_params);
					            bt_block.setOnClickListener(clicks);
					            llRow.addView(bt_block);
					            
					            lf.addView(llRow);
				            }
							}
						}
			        } else {
			        	Toast.makeText(FriendsActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
				    }
					}catch(Exception ex)
					{
						
					}
				
				progressDialog.dismiss();
			}
		});
		
		
		
	    // Freind Request Sending
	    req_send = (Button) findViewById(R.id.btReqSend);
	    
	    req_send.setOnClickListener(new OnClickListener() {
	      public void onClick(View v) {
	        // Starts an intent of the log in activity
	    	  
	    	  
	    	  sUserId = ParseUser.getCurrentUser().getObjectId();
	    	  Toast.makeText(getApplicationContext(),sUserId, Toast.LENGTH_LONG).show();
	    	  requested_user = req_user.getText().toString();
	    	  
	    	if(requested_user.equals(ParseUser.getCurrentUser().getUsername()))
	  		{//when current user send request to current user
	    		Toast.makeText(getApplicationContext(),
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
		  					Toast.makeText(getApplicationContext(),
		  							"Requested User is not valid", Toast.LENGTH_LONG)
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
									              
									              Toast.makeText(FriendsActivity.this, "Successfully Friend Request Sent",
						                                  Toast.LENGTH_SHORT).show();
		  		    			            }
		  		    			            else if(accept==1){
												Toast.makeText(getApplicationContext(),
														"You are already friend to this user.", Toast.LENGTH_LONG)
														.show();
											}
		  		    			            else if(block==1){
												Toast.makeText(getApplicationContext(),
														"You can not be friend with this user.", Toast.LENGTH_LONG)
														.show();
											}
		  		    			            else
		  		    			            {
		  		    			            	Toast.makeText(getApplicationContext(),
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
			  			  		    					  
			  			  		    					Toast.makeText(FriendsActivity.this, "Successfully Friend Request Sent",
								                                  Toast.LENGTH_SHORT).show();
		  			  		    					}
		  			  		    					else
		  			  		    					{
		  			  		    						if(accept==1)
		  			  		    						{
			  			  		    						Toast.makeText(getApplicationContext(),
																"You are already friend to this user.", Toast.LENGTH_LONG)
																.show();
		  			  		    						}
		  			  		    						else
		  			  		    						{
		  			  		    							Toast.makeText(getApplicationContext(),
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
					  												                    	  Toast.makeText(FriendsActivity.this, "Successfully Friend Request Sent",
					  												                                  Toast.LENGTH_SHORT).show();
					  																		} else {
					  																			Toast.makeText(getApplicationContext(),
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
										Toast.makeText(getApplicationContext(),
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
	}
}