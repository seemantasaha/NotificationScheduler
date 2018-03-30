package com.example.parseaccount;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class SignupActivity extends Activity {

	Button sign_up, select;
	EditText firstname, lastname, username, email, confirm_email, password, confirm_password;
	ImageView img;
	CheckBox accept;
	TextView tc;

	protected ProgressDialog progressDialog;
	private Bitmap bitmap;
	protected ParseFile img_file;
	private static final int PICK_IMAGE = 1;
	Toast toast;
	TextView text;
	private Pattern pattern;
	private Matcher matcher;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.signup);
		
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

		firstname = (EditText) findViewById(R.id.et_firstname);
		lastname = (EditText) findViewById(R.id.et_lastname);
		username = (EditText) findViewById(R.id.et_username);
		email = (EditText) findViewById(R.id.et_email);
		confirm_email = (EditText) findViewById(R.id.et_confirmemail);
		password = (EditText) findViewById(R.id.et_password);
		confirm_password = (EditText) findViewById(R.id.et_pass_confirm);
		
		accept = (CheckBox) findViewById(R.id.cb_accept);
		tc = (TextView) findViewById(R.id.tv_tc);
		
		tc.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LayoutInflater layoutInflater 
			     = (LayoutInflater)getBaseContext()
			      .getSystemService(LAYOUT_INFLATER_SERVICE);  
			    View popupView = layoutInflater.inflate(R.layout.tc_popup, null);  
			             final PopupWindow popupWindow = new PopupWindow(
			               popupView, 
			               LayoutParams.WRAP_CONTENT,  
			                     LayoutParams.WRAP_CONTENT);
			             Button btnDismiss = (Button)popupView.findViewById(R.id.ok);
			             btnDismiss.setOnClickListener(new Button.OnClickListener(){

			     @Override
			     public void onClick(View v) {
			      // TODO Auto-generated method stub
			      popupWindow.dismiss();
			     }});
			     popupWindow.showAtLocation(tc, Gravity.CENTER, 0, 0);
			}
		});

		img = (ImageView) findViewById(R.id.iv_Profile);

		select = (Button) findViewById(R.id.bt_select);
		select.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(
						Intent.createChooser(intent, "Select Picture"),
						PICK_IMAGE);
			}
		});

		sign_up = (Button) findViewById(R.id.bt_signup);
		sign_up.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isConnectingToInternet())
				{
					if (username.getText().toString().equals("")
							|| firstname.getText().toString().equals("")
							|| lastname.getText().toString().equals("")
							|| email.getText().toString().equals("")
							|| confirm_email.getText().toString().equals("")
							|| password.getText().toString().equals("")
							|| confirm_password.getText().toString().equals("")) {
						text.setText("Please complete the Sign Up Form");
						toast.show();

					} else if (!email.getText().toString()
							.equals(confirm_email.getText().toString())) {
						text.setText("Email does not match");
						toast.show();
					} else if (!password.getText().toString()
							.equals(confirm_password.getText().toString())) {
						text.setText("Password does not match");
						toast.show();
					} else if (!isEmailValid(email.getText().toString())) {
						text.setText("Email address is not valid");
						toast.show();
					} else if (!accept.isChecked()) {
						text.setText("Term and Conditions need to be accepted");
						toast.show();
					} else {
						
							ParseQuery<ParseUser> usr_query = ParseUser.getQuery();
							usr_query.whereEqualTo("username", username.getText().toString());
							
							usr_query.findInBackground(new FindCallback<ParseUser>() {
								@Override
					  			public void done(List<ParseUser> objects, ParseException e) {
									try{
										progressDialog = new ProgressDialog(SignupActivity.this);
										progressDialog.setMessage("Please Wait...");
										progressDialog.setCancelable(true);
										progressDialog.show();
										
										if(objects.size()>0)
										{
											progressDialog.dismiss();
											text.setText("Username Invalid! (Already Exists)");
											toast.show();
										}
										else
										{
											ParseQuery<ParseUser> usr_query = ParseUser.getQuery();
											usr_query.whereEqualTo("email", email.getText().toString());
											
											usr_query.findInBackground(new FindCallback<ParseUser>() {
												@Override
									  			public void done(List<ParseUser> objects, ParseException e) {
													try{	
														if(objects.size()>0)
														{
															progressDialog.dismiss();
															text.setText("Email Invalid! (Already Exists)");
															toast.show();
														}
														else
														{
															signup();
														}
													}
													catch(Exception ex_email)
													{
														signup();
													}
												}
											});
										}
									}
									catch(Exception ex_user)
									{
										ex_user.printStackTrace();
										ParseQuery<ParseUser> usr_query = ParseUser.getQuery();
										usr_query.whereEqualTo("email", email.getText().toString());
										
										usr_query.findInBackground(new FindCallback<ParseUser>() {
											@Override
								  			public void done(List<ParseUser> objects, ParseException e) {
												try{	
													if(objects.size()>0)
													{
														progressDialog.dismiss();
														text.setText("Email Invalid! (Already Exists)");
														toast.show();
													}
													else
													{
														signup();
													}
												}
												catch(Exception ex_email)
												{
													signup();
												}
											}
										});
									}
								}
							});
								// Save new user data into Parse.com Data Storage
							
							
						}
				}
				else
				{
					text.setText("No network Connection. Try again Later");
					toast.show();
				}
			}
		});
	}
	
	public boolean isEmailValid(String email)
    {
         String regExpn =
             "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                 +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                   +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                   +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                   +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                   +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

     CharSequence inputStr = email;

     pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
     matcher = pattern.matcher(inputStr);

     if(matcher.matches())
        return true;
     else
        return false;
    }
	
	protected void signup()
	{
		if (bitmap == null) {
			img.buildDrawingCache();
			bitmap = img.getDrawingCache();
		}
		

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
				stream);
		byte[] image = stream.toByteArray();
		img_file = new ParseFile("profile.jpg", image);
		img_file.saveInBackground(new SaveCallback() {
			public void done(ParseException e) {
				progressDialog.dismiss();
				if (e == null) {

					Log.d("Installation",
							""
									+ ParseInstallation
											.getCurrentInstallation()
											.get("username"));
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
								.saveInBackground(new SaveCallback() {
									@Override
									public void done(
											ParseException e) {
										try {
											if (e == null) {
												ParseUser user = new ParseUser();
												user.setUsername(username
														.getText()
														.toString());
												user.setPassword(password
														.getText()
														.toString());
												user.setEmail(email
														.getText()
														.toString());
												user.put(
														"FirstName",
														firstname
																.getText()
																.toString());
												user.put(
														"LastName",
														lastname.getText()
																.toString());
												user.put(
														"Profile_Picture",
														img_file);

												user.signUpInBackground(new SignUpCallback() {
													@Override
													public void done(
															ParseException e) {
														if (e == null) {
															text.setText("Thanks for registering! Please check your email to verify your account.");
															toast.show();

															Intent login_intent = new Intent(
																	SignupActivity.this,
																	LoginSignupActivity.class);
															login_intent
																	.putExtra(
																			"ONLY_LOGIN",
																			true);
															startActivity(login_intent);
															finish();
														} else {
															text.setText("Sign up Error:"
																	+ e.toString());
															toast.show();
														}
													}
												});

											} else {
												text.setText("Failed : Reinstall this application");
												toast.show();
											}

										} catch (Exception ex) {
											text.setText("Failed : Input is not valid");
											toast.show();
										}
									}
								});

					} else {
						text.setText("Device is already installed with another user. Can not sign up in this device");
						toast.show();
					}
				}
				else
				{
					text.setText("Failed : Input is not valid");
					toast.show();
				}
			}
		});
	}
	protected void onActivityResult(int requestCode, int resultCode,
			Intent imageReturnedIntent) {
		super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

		switch (requestCode) {
		case PICK_IMAGE:
			if (resultCode == RESULT_OK) {
				Uri selectedImage = imageReturnedIntent.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };

				Cursor cursor = getContentResolver().query(selectedImage,
						filePathColumn, null, null, null);
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String filePath = cursor.getString(columnIndex);
				cursor.close();

				bitmap = BitmapFactory.decodeFile(filePath);
				img.setImageBitmap(bitmap);
			}
		}
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
