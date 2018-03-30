package com.example.parseaccount;

import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

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
 
public class FriendsTab2 extends Fragment {
	
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
	    public View rootView;
		protected LinearLayout llRow;
		protected LayoutParams params;
		protected int frnd_fnd = 0;
		protected LayoutParams bt_params,tv_params;
		protected Handler handler;
	    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.friendstab2, container, false);
        
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
		query.whereEqualTo("ReqUser", ParseUser.getCurrentUser().getUsername());
		
		
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				// TODO Auto-generated method stub
					
				OnClickListener clicks=new OnClickListener() {

				    @Override
				    public void onClick(View v) {

				    	
				        if(v.getId()>=0 && v.getId()<1000)  
				        {
				        	tv_curent_user = (TextView) rootView.findViewById(v.getId());
				        	update_in_friends("Accept",tv_curent_user.getText().toString());
				        }
				        else if(v.getId()>=1000 && v.getId()<2000)  
				        {
				        	tv_curent_user = (TextView) rootView.findViewById(v.getId()-1000);
				        	update_in_friends("Reject",tv_curent_user.getText().toString());
				        }
				        else if(v.getId()>=2000 && v.getId()<3000)  
				        {
				        	tv_curent_user = (TextView) rootView.findViewById(v.getId()-2000);
				        	update_in_friends("Block",tv_curent_user.getText().toString());
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
				  							Toast.makeText(getActivity(),
				  									"Friend Request Accepted", Toast.LENGTH_LONG)
													.show();
				  						}
				  						
				  						if(state.equals("Reject"))
				  						{
				  							objects.get(0).put("Reject", 1);
				  							Toast.makeText(getActivity(),
				  									"Friend Request Rejected", Toast.LENGTH_LONG)
													.show();
				  						}
				  						
				  						if(state.equals("Block"))
				  						{
				  							objects.get(0).put("Block", 1);
				  							Toast.makeText(getActivity(),
				  									"Friend Request Blocked", Toast.LENGTH_LONG)
													.show();
				  						}
				  						objects.get(0).saveInBackground();
					      			  
				  					}
				  		        } else {
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
		  			            	frnd_fnd=1;
						            user = objects.get(i).getString("ReqUser");
						            sentBy = objects.get(i).getString("ReqFrom");
						            
						            
						            LinearLayout llRow = new LinearLayout(getActivity());
						            llRow.setOrientation(LinearLayout.HORIZONTAL);
						            llRow.setLayoutParams(params);
						            llRow.setBackgroundResource(R.drawable.linear_row);
						            llRow.setPadding(10, 10, 10, 10);
						            
						            tv_user=new TextView(getActivity());
						            tv_user.setText(sentBy);
						            tv_user.setTextSize(30);
						            tv_user.setTextColor(Color.BLACK);
						            tv_user.setTypeface(Typeface.MONOSPACE);
						            tv_user.setId(i);
						            llRow.addView(tv_user);
						            
						            bt_accept = new Button(getActivity());
						            bt_accept.setText("Accept");
						            bt_accept.setId(i);
						            bt_accept.setLayoutParams(bt_params);
						            bt_accept.setTypeface(Typeface.MONOSPACE);
						            bt_accept.setBackgroundColor(0xFF88AAFF);
						            bt_accept.setTextColor(Color.WHITE);
						            bt_accept.setOnClickListener(clicks);
						            llRow.addView(bt_accept);
						            
						            
						            bt_reject = new Button(getActivity());
						            bt_reject.setText("Reject");
						            bt_reject.setId(1000+i);
						            bt_reject.setLayoutParams(bt_params);
						            bt_reject.setBackgroundColor(0xFF88AAFF);
						            bt_reject.setTypeface(Typeface.MONOSPACE);
						            bt_reject.setTextColor(Color.WHITE);
						            bt_reject.setOnClickListener(clicks);
						            llRow.addView(bt_reject);
						            
						            bt_block = new Button(getActivity());
						            bt_block.setText("Block");
						            bt_block.setId(2000+i);
						            bt_block.setLayoutParams(bt_params);
						            bt_block.setBackgroundColor(0xFF88AAFF);
						            bt_block.setTypeface(Typeface.MONOSPACE);
						            bt_block.setTextColor(Color.WHITE);
						            bt_block.setOnClickListener(clicks);
						            llRow.addView(bt_block);
						            
						            lf.addView(llRow);
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
								tv_user.setText("No Friend Request Received");
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
		return rootView;
    }
 
}