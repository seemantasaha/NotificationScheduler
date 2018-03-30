package com.example.parseaccount;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.Settings.Secure;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
 
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
 
public class LoginSignupActivity extends Activity {
	// Declare Variables
	Button loginbutton;
	Button signup;
	Button Forgot_Password;
	String usernametxt;
	String passwordtxt;
	EditText password;
	EditText username;
	Boolean only_login = false;
	EditText input;
	ProgressDialog progressDialog;
	Toast toast;
	TextView text;
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Get the view from main.xml
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.loginsignup);
		
		//Custom  Toast----------------------------------------------------------------------------
		 LayoutInflater inflater = getLayoutInflater();
         // Inflate the Layout
         View layout = inflater.inflate(R.layout.my_custom_toast,
                                        (ViewGroup) findViewById(R.id.custom_toast_layout));

         text = (TextView) layout.findViewById(R.id.textToShow);
         // Set the Text to show in TextView
         text.setText("My Custom Toast in Center of Screen");

         toast = new Toast(getApplicationContext());
         toast.setGravity(Gravity.BOTTOM, 0, 0);
         toast.setDuration(Toast.LENGTH_LONG);
         toast.setView(layout);
        //-----------------------------------------------------------------------------------------
		
		Bundle extras = getIntent().getExtras();
        if (extras != null) {
        	only_login = extras.getBoolean("ONLY_LOGIN");
        }
		// Locate EditTexts in main.xml
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
 
		// Locate Buttons in main.xml
		loginbutton = (Button) findViewById(R.id.login);
		signup = (Button) findViewById(R.id.signup);
		Forgot_Password = (Button) findViewById(R.id.forgot_password);
		
		if(only_login==true)
		{
			signup.setVisibility(View.GONE);
		}
		else
		{
			signup.setVisibility(View.VISIBLE);
		}
 
		// Login Button Click Listener
		loginbutton.setOnClickListener(new OnClickListener() {
 
			public void onClick(View arg0) {
				// Retrieve the text entered from the EditText
				if(isConnectingToInternet())
				{
					progressDialog = new ProgressDialog(
							LoginSignupActivity.this);
					progressDialog.setMessage("Please Wait...");
					progressDialog.setCancelable(true);
					progressDialog.show();

					usernametxt = username.getText().toString();
					passwordtxt = password.getText().toString();

					if (usernametxt.equals("") || passwordtxt.equals("")) {
						text.setText("Username or Password Field can not be empty");
						toast.show();
						progressDialog.dismiss();
					} else {
						ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
						userQuery.whereEqualTo("username", usernametxt);

						userQuery
								.findInBackground(new FindCallback<ParseUser>() {

									@Override
									public void done(
											List<ParseUser> user_objects,
											ParseException e) {
										// TODO Auto-generated method stub
										try {
											Log.d("userQuery", ""
													+ user_objects.size());
											Log.d("Password", ""
													+ user_objects.get(0)
															.getUsername());
											Log.d("Password",
													""
															+ user_objects
																	.get(0)
																	.getString(
																			"password"));
											if (user_objects.size() == 0) {

											}

											else {
												// Send data to Parse.com for
												// verification
												ParseUser.logInInBackground(
														usernametxt,
														passwordtxt,
														new LogInCallback() {
															@Override
															public void done(
																	ParseUser user,
																	ParseException e) {
																progressDialog
																		.dismiss();
																if (user != null) {
																	// If user
																	// exist and
																	// authenticated,
																	// send user
																	// to
																	// Welcome.class
																	if (user.getBoolean("emailVerified") == true) {
																		
																		if (ParseInstallation
																				.getCurrentInstallation().get(
																						"username") == null) {

																			// Installing user for push
																			// notification
																			// Installing user for push
																			// notification
																			ParseACL install_acl = new ParseACL();
																			install_acl.setPublicWriteAccess(false);
																			install_acl.setPublicReadAccess(true);
																			//String android_id = Secure.getString(getApplicationContext().getContentResolver(),Secure.ANDROID_ID);
																			ParseInstallation installation = ParseInstallation.getCurrentInstallation();
																			//installation.put("userId",android_id);
																			installation.put("username",
																					username.getText()
																							.toString());
																			installation.setACL(install_acl);
																			
																			
																			installation
																					.saveInBackground();
																		}

																		Intent intent = new Intent(
																				LoginSignupActivity.this,
																				FriendsPageActivity.class);
																		startActivity(intent);

																		text.setText("Successfully logged in");
																		toast.show();
																		finish();
																	} else {
																		text.setText("Email address for this account is not verified");
																		toast.show();
																	}
																} else {
																	text.setText("Invalid Password");
																	toast.show();
																}
															}
														});

											}

										} catch (Exception ex) {
											progressDialog.dismiss();
											text.setText("Sorry, we cannot find that user");
											toast.show();
										}
									}
								});
					}
				}
				else
				{
					text.setText("No network Connection. Try again Later");
					toast.show();
				}
				
			}
		});
		
		Forgot_Password.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//ParseUser.requestPasswordResetInBackground(email);
				//need to implement a popup in which user will provide email to reset password
				//need to implmen password change too.
				AlertDialog.Builder alert = new AlertDialog.Builder(LoginSignupActivity.this);

				alert.setTitle("Password Reset");
				alert.setMessage("Provide Email Address");

				// Set an EditText view to get user input 
				input = new EditText(LoginSignupActivity.this);
				input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
				alert.setView(input);

				alert.setPositiveButton("RESET", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					  String value = input.getText().toString();
					  ParseUser.requestPasswordResetInBackground(value);
				  }
				});

				alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				  public void onClick(DialogInterface dialog, int whichButton) {
				    // Canceled.
				  }
				});

				alert.show();
			}
		});
		
		// Sign up Button Click Listener
		signup.setOnClickListener(new OnClickListener() {
 
			public void onClick(View arg0) {
				Intent signup_intent = new Intent(LoginSignupActivity.this,SignupActivity.class);
				startActivity(signup_intent);
			}
		});
 
	}
	public boolean isConnectingToInternet(){
		ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting())
        {   
            return true;
        } else
        {
            return false;
        }
    }
}