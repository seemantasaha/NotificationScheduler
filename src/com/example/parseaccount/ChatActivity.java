package com.example.parseaccount;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



public class ChatActivity extends Activity {

	  private String sUserId;
	  public static final String USER_ID_KEY = "userId"; 
	  EditText msg;
	  Button send;

	@Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_chat);
	    
	    
	    
	    msg = (EditText) findViewById(R.id.etMessage);

	    // Log in button click handler
	    send = (Button) findViewById(R.id.btSend);
	    send.setOnClickListener(new OnClickListener() {
	      public void onClick(View v) {
	        // Starts an intent of the log in activity
	    	  sUserId = ParseUser.getCurrentUser().getObjectId();
	    	  Toast.makeText(getApplicationContext(),sUserId, Toast.LENGTH_LONG).show();
	    	  String data = msg.getText().toString();
              ParseObject message = new ParseObject("Message");
              message.put(USER_ID_KEY, sUserId);
              message.put("body", data);
              message.saveInBackground(new SaveCallback() {
                  public void done(ParseException e) {
                      
                      if (e == null) {
							// Show a simple Toast message upon successful registration
                    	  Toast.makeText(ChatActivity.this, "Successfully created message on Parse",
                                  Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(getApplicationContext(),
									"Message Saving Error:"+e.toString(), Toast.LENGTH_LONG)
									.show();
						}
                  }
              });
              msg.setText("");
	      }
	    });
	}
}