package com.example.parseaccount;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.internal.ck;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.support.v4.app.FragmentManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

@SuppressLint("NewApi")
public class SetNotificationActivity extends FragmentActivity implements LocationListener{

	// Google Map
	int notification_setting_successfull = 0;
	GoogleMap googleMap;
	CircleOptions circleOptions;
	Circle circle, prev_circle;
	FragmentManager fm;
	int push_notification_code;
	MarkerOptions notification_marker;
	double noti_lat, noti_lng;
	int noti_set = 0;
	DatePicker datePicker2, datePicker1;
	TimePicker timePicker1, timePicker2;
	Spinner spinner;
	EditText Message, Radius;
	LatLng noti_latLng;
	Button Select_Image, Set_Notification;
	TextView tv_time;

	String users = "", dateWindow, timeWindow;
	String[] receiver;
	double rad;
	int tp1_hour, tp1_min, tp2_hour, tp2_min;
	int day1, day2, month1, month2, year1, year2;
	long diff_date, diff_time;
	String formatedTime1 = "", formatedTime2 = "", formatedDate1 = "",
			formatedDate2 = "";
	LinearLayout LL_MAP, LL_RADIUS, LL_DATE_TIME;
	TextView toTime;
	
	RadioGroup time;
	CheckBox ck_location;

	private static final double EARTH_RADIUS = 6378100.0;
	private int offset;

	int strokeColor = 0xffff0000; // red outline
	int shadeColor = 0x44ff0000; // opaque red fill

	String location_username;
	protected File imageFile;
	protected Bitmap bitmap = null;
	ParseFile img_file,img_file_thumbnail;
	private TextView header;
	private static final int PICK_IMAGE = 1;
	public static ScrollView myScroll;
	public View mOriginalContentView;
	public static String task = "";
	ProgressDialog progressDialog;
	MarkerOptions marker;
	int noti_code = 0;

	String object_id = "", type = "", message = "", location = "", stdt = "",
			endt = "", sttm = "", entm = "";
	double Rad = 0.0;
	String img_file_name;
	int noti_id;

	ImageView img;
	double edit_lat, edit_lng;
	Marker prev_marker;
	ParseACL acl_push;

	String notification_type = "";
	private Bitmap thumbnail;
	private ParseObject noti;
	private RadioButton radioButton;
	Context con;
	int remove_focus_flag;
	
	protected LocationManager locationManager;
	Location loc;
	private static final long MIN_DISTANCE_FOR_UPDATE = 10;
	private static final long MIN_TIME_FOR_UPDATE = 1000 * 60 * 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notification_setting);
		con = getApplicationContext();
		
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		
		remove_focus_flag = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN;
		
		myScroll = (ScrollView) findViewById(R.id.sv_top);
		
		tv_time = (TextView)findViewById(R.id.tv_time);
		time = (RadioGroup)findViewById(R.id.rb_time);
		ck_location = (CheckBox)findViewById(R.id.ck_loc);

		header = (TextView) findViewById(R.id.header_username);
		header.setText(header.getText()
				+ ParseUser.getCurrentUser().getUsername());

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			task = extras.getString("TASK");
			users = extras.getString("USERNAME");
			receiver = users.split(",");
			type = extras.getString("TYPE");
			message = extras.getString("MESSAGE");
			location = extras.getString("LOCATION");
			Rad = extras.getDouble("RADIUS");
			stdt = extras.getString("STARTDATE");
			endt = extras.getString("ENDDATE");
			sttm = extras.getString("STARTTIME");
			entm = extras.getString("ENDTIME");
			object_id = extras.getString("OBJECTID");
			noti_id = extras.getInt("NOTIFICATION_CODE");
			img_file_name = extras.getString("IMAGE");
		}

		// Log.d("Bundle",object_id);
		Radius = (EditText) findViewById(R.id.et_radius);
		Message = (EditText) findViewById(R.id.et_msg);

		toTime = (TextView) findViewById(R.id.tv_time_to);

		datePicker2 = (DatePicker) findViewById(R.id.dp2);
		datePicker1 = (DatePicker) findViewById(R.id.dp1);

		timePicker1 = (TimePicker) findViewById(R.id.tp1);
		timePicker1.setIs24HourView(true);
		timePicker1.setCurrentHour(Calendar.getInstance().get(
				Calendar.HOUR_OF_DAY));

		timePicker2 = (TimePicker) findViewById(R.id.tp2);
		timePicker2.setIs24HourView(true);
		timePicker2.setCurrentHour(Calendar.getInstance().get(
				Calendar.HOUR_OF_DAY));


		LL_MAP = (LinearLayout) findViewById(R.id.ll_map);
		LL_RADIUS = (LinearLayout) findViewById(R.id.ll_map_radius);
		LL_DATE_TIME = (LinearLayout) findViewById(R.id.ll_date_time);
		
		//default case
		LL_DATE_TIME.setVisibility(View.GONE);
		datePicker2.setVisibility(View.GONE);
		timePicker1.setVisibility(View.GONE);
		toTime.setVisibility(View.GONE);
		datePicker1.setVisibility(View.GONE);
		timePicker2.setVisibility(View.GONE);
		
		LL_MAP.setVisibility(View.GONE);
		
		
		
		time.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
            	
            	hideSoftKeyboard(SetNotificationActivity.this, group);

				radioButton = (RadioButton) findViewById(checkedId);
            
				if (radioButton.getText().equals("Schedule")) {
					LL_DATE_TIME.setVisibility(View.VISIBLE);
					datePicker2.setVisibility(View.VISIBLE);
					timePicker1.setVisibility(View.VISIBLE);
					toTime.setVisibility(View.VISIBLE);
					datePicker1.setVisibility(View.VISIBLE);
					timePicker2.setVisibility(View.VISIBLE);
				} 
				else
				{
					LL_DATE_TIME.setVisibility(View.GONE);
					datePicker2.setVisibility(View.GONE);
					timePicker1.setVisibility(View.GONE);
					toTime.setVisibility(View.GONE);
					datePicker1.setVisibility(View.GONE);
					timePicker2.setVisibility(View.GONE);
				}
            }

        });

		ck_location.setOnClickListener(new OnClickListener() {

		      @Override
		      public void onClick(View v) {
		                //is chkIos checked?
		    	  hideSoftKeyboard(SetNotificationActivity.this, v);
		    	
		        if (((CheckBox) v).isChecked()) {
		        	LL_MAP.setVisibility(View.VISIBLE);
		        }
		        else 
		        	LL_MAP.setVisibility(View.GONE);

		      }
		});
		
		int checkedId = time.getCheckedRadioButtonId();
		radioButton = (RadioButton) findViewById(checkedId);
		
		img = (ImageView) findViewById(R.id.iv_image);
		img.setVisibility(View.GONE);

		Select_Image = (Button) findViewById(R.id.bt_select_img);
		Select_Image.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				hideSoftKeyboard(SetNotificationActivity.this, v);
				
				if (Select_Image.getText().equals("Attach Image")) {
					Intent intent = new Intent();
					intent.setType("image/*");
					intent.setAction(Intent.ACTION_GET_CONTENT);
					startActivityForResult(
							Intent.createChooser(intent, "Select Picture"),
							PICK_IMAGE);
				} else {
					Select_Image.setText("Attach Image");
					img.setVisibility(View.GONE);
					bitmap = null;
				}
			}
		});
		
		Set_Notification = (Button) findViewById(R.id.bt_set_noti);
		Set_Notification.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// get date and time window*************************************
				progressDialog = new ProgressDialog(SetNotificationActivity.this);
				progressDialog.setMessage("Please Wait...");
				progressDialog.setCancelable(false);
				progressDialog.show();

				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

				day2 = datePicker2.getDayOfMonth();
				month2 = datePicker2.getMonth();
				year2 = datePicker2.getYear() - 1900; // simpledateformat year
														// is relative to 1900

				day1 = datePicker1.getDayOfMonth();
				month1 = datePicker1.getMonth();
				year1 = datePicker1.getYear() - 1900; // simpledateformat year
														// is relative to 1900

				formatedDate2 = sdf.format(new Date(year2, month2, day2));
				formatedDate1 = sdf.format(new Date(year1, month1, day1));

				diff_date = ((year1 - year2) * 365 * 24 * 60)
						+ ((month1 - month2) * 30 * 24 * 60)
						+ ((day1 - day2) * 24 * 60);

				tp1_hour = timePicker1.getCurrentHour();
				tp1_min = tp1_hour * 60 + timePicker1.getCurrentMinute();

				tp2_hour = timePicker2.getCurrentHour();
				tp2_min = tp2_hour * 60 + timePicker2.getCurrentMinute();

				formatedTime1 = tp1_hour + ":" + timePicker1.getCurrentMinute();
				formatedTime2 = tp2_hour + ":" + timePicker2.getCurrentMinute();

				diff_time = diff_date + (tp2_min - tp1_min);

				// Toast.makeText(getApplicationContext(),""+diff_time,
				// Toast.LENGTH_LONG).show();

				// ************************************************************
				Log.d("bitmap outside", "bitmap=" + bitmap);
				if (task.equals("NO")) {
					if (ck_location.isChecked()&& noti_lat == 0.0 && noti_lng == 0.0) {
						Toast.makeText(getApplicationContext(),
								"No location is set", Toast.LENGTH_LONG).show();

						progressDialog.dismiss();
					} /*else if ((spinner.getSelectedItemPosition() == 0)
							&& (diff_time < 0.0 || diff_time > (24 * 60))) {
						Toast.makeText(getApplicationContext(),
								"You can not choose more 24 hour time window",
								Toast.LENGTH_LONG).show();
								
								progressDialog.dismiss();
					}*/ else {
						Log.d("bitmap", "bitmap=" + bitmap);
						if (bitmap != null) {
							ByteArrayOutputStream stream = new ByteArrayOutputStream();
							bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
							byte[] image = stream.toByteArray();
							img_file = new ParseFile("notify.jpg", image);
							img_file.saveInBackground(new SaveCallback() {

								public void done(ParseException e) {
									// progressDialog.dismiss();
									if (e == null) {
										ByteArrayOutputStream stream = new ByteArrayOutputStream();
										thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, stream);
										byte[] image = stream.toByteArray();
										img_file_thumbnail = new ParseFile("notify_thumbnail.jpg", image);
										img_file_thumbnail.saveInBackground(new SaveCallback() {
											public void done(ParseException e) {
												// progressDialog.dismiss();
												if (e == null) {
										//To get the notification code*************************************************************************
										
											/*ParseQuery<ParseObject> query = ParseQuery.getQuery("Notification");
											query.whereEqualTo("Sender", ParseUser.getCurrentUser().getUsername());

											query.findInBackground(new FindCallback<ParseObject>() {
												@Override
												public void done(List<ParseObject> objects, ParseException e) {
													try {
														for(int i=0;i<objects.size();i++)
														{
															if(objects.get(i).getInt("Notification_Code") > noti_code)
															{
																noti_code = objects.get(i).getInt("Notification_Code");
															}
														}
														noti_code++;
													} catch (Exception ex) {}
													push(noti_code);
												}
											});*/
													push((int) (System.currentTimeMillis()/1000));
										//*****************************************************************************************************
										
									}
								}
							});
									}
								}
							});
						} else {
							Log.d("bitmap", "no image");
							byte[] data = "no_image".getBytes();
							img_file = new ParseFile("no_image", data);
							img_file_thumbnail = new ParseFile("no_image", data);
							//To get the notification code*************************************************************************
							
							/*ParseQuery<ParseObject> query = ParseQuery.getQuery("Notification");
							query.whereEqualTo("Sender", ParseUser.getCurrentUser().getUsername());

							query.findInBackground(new FindCallback<ParseObject>() {
								@Override
								public void done(List<ParseObject> objects, ParseException e) {
									try {
										for(int i=0;i<objects.size();i++)
										{
											if(objects.get(i).getInt("Notification_Code") > noti_code)
											{
												noti_code = objects.get(i).getInt("Notification_Code");
											}
										}
										noti_code++;
									} catch (Exception ex) {}
									push(noti_code);
								}
							});*/
							push((int) (System.currentTimeMillis()/1000));
						//*****************************************************************************************************
						
						}
					}
				} else {
					Log.d("bitmap", "bitmap=" + bitmap);
					if (bitmap != null) {
						ByteArrayOutputStream stream = new ByteArrayOutputStream();
						bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
						byte[] image = stream.toByteArray();
						img_file = new ParseFile("notify.jpg", image);
						img_file.saveInBackground(new SaveCallback() {

							public void done(ParseException e) {
								// progressDialog.dismiss();
								if (e == null) {
									ByteArrayOutputStream stream = new ByteArrayOutputStream();
									thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, stream);
									byte[] image = stream.toByteArray();
									img_file_thumbnail = new ParseFile("notify_thumbnail.jpg", image);
									img_file_thumbnail.saveInBackground(new SaveCallback() {
										public void done(ParseException e) {
											// progressDialog.dismiss();
											if (e == null) {
												push(noti_id);
											}
										}
									});
								}
							}
						});
					} else {
						Log.d("bitmap", "no image");
						byte[] data = "no_image".getBytes();
						img_file = new ParseFile("no_image", data);
						img_file_thumbnail = new ParseFile("no_image", data);
						push(noti_id);
					}
				}

			}

		});

		try {
			// Loading map
			initilizeMap();

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (task.equals("INFO") || task.equals("EDIT")) {
			// behavior with notification type
			if (type.equals("Time and Location")) {
				LL_MAP.setVisibility(View.VISIBLE);
				LL_RADIUS.setVisibility(View.VISIBLE);
				LL_DATE_TIME.setVisibility(View.VISIBLE);

				datePicker2.setVisibility(View.VISIBLE);
				timePicker1.setVisibility(View.VISIBLE);
				toTime.setVisibility(View.VISIBLE);
				datePicker1.setVisibility(View.VISIBLE);
				timePicker2.setVisibility(View.VISIBLE);
			} else if (type.equals("Location")) {
				LL_MAP.setVisibility(View.VISIBLE);
				LL_RADIUS.setVisibility(View.VISIBLE);
				LL_DATE_TIME.setVisibility(View.GONE);

				datePicker2.setVisibility(View.GONE);
				timePicker1.setVisibility(View.GONE);
				toTime.setVisibility(View.GONE);
				datePicker1.setVisibility(View.GONE);
				timePicker2.setVisibility(View.GONE);
			} else if (type.equals("Time")) {
				LL_MAP.setVisibility(View.GONE);
				LL_RADIUS.setVisibility(View.GONE);
				LL_DATE_TIME.setVisibility(View.VISIBLE);

				datePicker2.setVisibility(View.VISIBLE);
				timePicker1.setVisibility(View.VISIBLE);
				toTime.setVisibility(View.GONE);
				datePicker1.setVisibility(View.GONE);
				timePicker2.setVisibility(View.GONE);
			} else if (type.equals("Immediate")) {
				Log.d("type", type);
				LL_MAP.setVisibility(View.GONE);
				LL_RADIUS.setVisibility(View.GONE);
				LL_DATE_TIME.setVisibility(View.GONE);

				datePicker2.setVisibility(View.GONE);
				timePicker1.setVisibility(View.GONE);
				toTime.setVisibility(View.GONE);
				datePicker1.setVisibility(View.GONE);
				timePicker2.setVisibility(View.GONE);
			}
			// *******************************************
			Message.setText(message);
			String[] locationSplit = location.split(",");
			double latitude = Double.parseDouble(locationSplit[0]);
			double longitude = Double.parseDouble(locationSplit[1]);

			if (googleMap == null) {
				Log.d("GoogleMap", "null");
			}
			// create marker
			marker = new MarkerOptions().position(
					new LatLng(latitude, longitude)).title("Hello Maps");
			marker.icon(BitmapDescriptorFactory
					.fromResource(R.drawable.mark_red));
			// adding marker
			prev_marker = googleMap.addMarker(marker);

			CameraPosition cameraPosition = new CameraPosition.Builder()
					.target(new LatLng(latitude, longitude)).zoom(12).build();

			googleMap.animateCamera(CameraUpdateFactory
					.newCameraPosition(cameraPosition));

			circleOptions = new CircleOptions()
					.center(new LatLng(latitude, longitude)).radius(Rad)
					.fillColor(shadeColor).strokeColor(strokeColor)
					.strokeWidth(8);
			prev_circle = googleMap.addCircle(circleOptions);

			Radius.setText("" + Rad);

			String[] stDateSplit = stdt.split("-");
			String stday = stDateSplit[0];
			String stmonth = stDateSplit[1];
			String styear = stDateSplit[2];

			String[] endDateSplit = endt.split("-");
			String enday = endDateSplit[0];
			String enmonth = endDateSplit[1];
			String enyear = endDateSplit[2];

			String[] stTimeSplit = sttm.split(":");
			String sthour = stTimeSplit[0];
			String stmin = stTimeSplit[1];

			String[] endTimeSplit = entm.split(":");
			String enhour = endTimeSplit[0];
			String enmin = endTimeSplit[1];

			datePicker2.init(Integer.parseInt(styear),
					Integer.parseInt(stmonth) - 1, Integer.parseInt(stday),
					null);
			datePicker1.init(Integer.parseInt(enyear),
					Integer.parseInt(enmonth) - 1, Integer.parseInt(enday),
					null);

			timePicker1.setCurrentHour(Integer.parseInt(sthour));
			timePicker1.setCurrentMinute(Integer.parseInt(stmin));
			timePicker2.setCurrentHour(Integer.parseInt(enhour));
			timePicker2.setCurrentMinute(Integer.parseInt(enmin));

			if (img_file_name.equals("no_image")) {
				img.setVisibility(View.GONE);
			} else {

				try {
					Log.d("Image", ParseUser.getCurrentUser().getUsername()
							+ receiver + type + object_id);
					ParseQuery<ParseObject> query = ParseQuery
							.getQuery("Notification");
					query.whereEqualTo("Sender", ParseUser.getCurrentUser()
							.getUsername());
					query.whereEqualTo("Receiver", receiver);
					query.whereEqualTo("Notification_Type", type);
					query.whereEqualTo("objectId", object_id);

					Log.d("Current User", ParseUser.getCurrentUser()
							.getUsername());

					query.getFirstInBackground(new GetCallback<ParseObject>() {
						public void done(ParseObject object, ParseException e) {
							// TODO Auto-generated method stub
							// Locate the column named "ImageName" and set
							// the string
							try {
								ParseFile fileObject = (ParseFile) object
										.get("Notification_Image");
								fileObject
										.getDataInBackground(new GetDataCallback() {
											public void done(byte[] data,
													ParseException e) {
												if (e == null) {
													Log.d("test",
															"We've got data in data.");
													Bitmap bmp = BitmapFactory
															.decodeByteArray(
																	data, 0,
																	data.length);
													img.setImageBitmap(bmp);
													img.setVisibility(View.VISIBLE);
												}
											}
										});
							} catch (Exception ex) {
							}
						}
					});
				} catch (Exception ex) {
				}
			}
		}
		if (task.equals("INFO")) {
			Log.d("TASK", "INFO-->" + type);
			Message.setEnabled(false);

			tv_time.setVisibility(View.GONE);
			time.setVisibility(View.GONE);
			ck_location.setVisibility(View.GONE);
			
			googleMap.getUiSettings().setZoomControlsEnabled(false);
			googleMap.getUiSettings().setScrollGesturesEnabled(false);
			googleMap.getUiSettings().setZoomGesturesEnabled(false);
			googleMap.getUiSettings().setTiltGesturesEnabled(false);

			Radius.setEnabled(false);

			datePicker2.setEnabled(false);
			datePicker1.setEnabled(false);

			timePicker1.setEnabled(false);
			timePicker2.setEnabled(false);

			Select_Image.setVisibility(View.GONE);
			Set_Notification.setVisibility(View.GONE);
		} else if (task.equals("EDIT")) {
			tv_time.setVisibility(View.GONE);
			time.setVisibility(View.GONE);
			ck_location.setVisibility(View.GONE);
		}

	}

	/**
	 * function to load map. If map is not created it will create it for you
	 * */
	private void initilizeMap() {
		SupportMapFragment mMapFragment = ((SupportMapFragment) this
				.getSupportFragmentManager().findFragmentById(R.id.map));
		if (mMapFragment == null) {
			mMapFragment = SupportMapFragment.newInstance();
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.map, mMapFragment).commit();
		}

		googleMap = mMapFragment.getMap();

		/*
		 * googleMap = ((SupportMapFragment)
		 * getSupportFragmentManager().findFragmentById( R.id.map)).getMap();
		 */

		// check if map is created successfully or not
		if (googleMap == null) {
			Toast.makeText(getApplicationContext(),
					"Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
		} else {

			googleMap.setMyLocationEnabled(true);
			googleMap.getUiSettings().setZoomControlsEnabled(true);
			googleMap.getUiSettings().setScrollGesturesEnabled(true);
			googleMap.getUiSettings().setZoomGesturesEnabled(true);
			googleMap.getUiSettings().setTiltGesturesEnabled(true);

			// set map type
			googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

			// Get LocationManager object from System Service
			// LOCATION_SERVICE
			/*LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

			// Create a criteria object to retrieve provider
			Criteria criteria = new Criteria();

			// Get the name of the best provider
			String provider = locationManager.getBestProvider(criteria, true);

			// Get Current Location
			Location myLocation = locationManager
					.getLastKnownLocation(provider);*/
			
			Location net_loc = getLocation(LocationManager.NETWORK_PROVIDER);
			Location gps_loc = getLocation(LocationManager.GPS_PROVIDER);

			if (gps_loc != null) {
				double latitude = gps_loc.getLatitude();
				double longitude = gps_loc.getLongitude();

				// create marker
				MarkerOptions marker = new MarkerOptions().position(
						new LatLng(latitude, longitude)).title("Hello Maps");
				marker.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.noti_marker));
				// adding marker
				googleMap.addMarker(marker);

				CameraPosition cameraPosition = new CameraPosition.Builder()
						.target(new LatLng(latitude, longitude)).zoom(12)
						.build();

				googleMap.animateCamera(CameraUpdateFactory
						.newCameraPosition(cameraPosition));
			} 
			else if (net_loc != null) {
				double latitude = net_loc.getLatitude();
				double longitude = net_loc.getLongitude();
				
				// create marker
				MarkerOptions marker = new MarkerOptions().position(
						new LatLng(latitude, longitude)).title("Hello Maps");
				marker.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.noti_marker));
				// adding marker
				googleMap.addMarker(marker);

				CameraPosition cameraPosition = new CameraPosition.Builder()
						.target(new LatLng(latitude, longitude)).zoom(12)
						.build();

				googleMap.animateCamera(CameraUpdateFactory
						.newCameraPosition(cameraPosition));

			} else {
				Toast.makeText(
						getApplicationContext(),
						"No Location Found! Maparker will be set to (0,0)",
						Toast.LENGTH_SHORT).show();
				// create marker
				MarkerOptions marker = new MarkerOptions()
						.position(
								new LatLng(0.0, 0.0)).title(
								"Hello Maps");

				// adding marker
				googleMap.addMarker(marker);

				CameraPosition cameraPosition = new CameraPosition.Builder()
						.target(new LatLng(0.0, 0.0)).zoom(12).build();

				googleMap.animateCamera(CameraUpdateFactory
						.newCameraPosition(cameraPosition));
			}
			
			
			googleMap.setOnMapClickListener(new OnMapClickListener() {

				@Override
				public void onMapClick(LatLng point) {
					// TODO Auto-generated method stub
					if (task.equals("INFO")) {

					} else {
						if (noti_set == 0) {
							noti_lat = point.latitude;
							noti_lng = point.longitude;

							noti_latLng = new LatLng(noti_lat, noti_lat);

							DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									switch (which) {
									case DialogInterface.BUTTON_POSITIVE:
										Toast.makeText(getApplicationContext(),
												noti_lat + "," + noti_lng,
												Toast.LENGTH_SHORT).show();
										if (task.equals("EDIT")) {
											prev_marker.remove();
											prev_circle.remove();
										}
										notification_marker = new MarkerOptions()
												.position(
														new LatLng(noti_lat,
																noti_lng))
												.title("Hello Maps");
										notification_marker.icon(BitmapDescriptorFactory
												.fromResource(R.drawable.mark_red));
										googleMap
												.addMarker(notification_marker);

										circleOptions = new CircleOptions()
												.center(new LatLng(noti_lat,
														noti_lng)).radius(Rad)
												.fillColor(shadeColor)
												.strokeColor(strokeColor)
												.strokeWidth(8);
										circle = googleMap
												.addCircle(circleOptions);

										noti_set = 1;
										break;

									case DialogInterface.BUTTON_NEGATIVE:
										// No button clicked
										break;
									}
								}
							};

							AlertDialog.Builder builder = new AlertDialog.Builder(
									SetNotificationActivity.this);
							builder.setMessage(
									"Are you sure about the location?")
									.setPositiveButton("Yes",
											dialogClickListener)
									.setNegativeButton("No",
											dialogClickListener).show();
						} else {
							Toast.makeText(
									getApplicationContext(),
									"Location is already set for this Notification",
									Toast.LENGTH_SHORT).show();
						}
					}
				}
			});

			Radius.addTextChangedListener(new TextWatcher() {

				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub

				}

				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
				}

				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					if (!Radius.getText().toString().equals("")) {
						rad = Double.parseDouble(Radius.getText().toString());
						if (circle == null) {
							circleOptions = new CircleOptions()
									.center(new LatLng(noti_lat, noti_lng))
									.radius(rad).fillColor(shadeColor)
									.strokeColor(strokeColor).strokeWidth(8);
							circle = googleMap.addCircle(circleOptions);
						} else {
							circle.remove();
							circleOptions = new CircleOptions()
									.center(new LatLng(noti_lat, noti_lng))
									.radius(rad).fillColor(shadeColor)
									.strokeColor(strokeColor).strokeWidth(8);
							circle = googleMap.addCircle(circleOptions);
						}
					}

				}

			});
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		initilizeMap();
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
				img.setVisibility(View.VISIBLE);

				Select_Image.setText("Remove Image");
				
				thumbnail = getScaledBitmap(filePath,64);
			}
		}
	}

	protected void push(int n_code) {
		
		push_notification_code = n_code;
		Log.d("Notification Code Sent", ""+n_code);
		acl_push = new ParseACL();
		if (task.equals("NO")) {
			if(ck_location.isChecked() && radioButton.getText().equals("Schedule"))
			{
				notification_type = "Time and Location";
			}
			else if(ck_location.isChecked() && radioButton.getText().equals("Now"))
			{
				notification_type = "Location";
			}
			else if(!ck_location.isChecked() && radioButton.getText().equals("Schedule"))
			{
				notification_type = "Time";
			}
			else
			{
				notification_type = "Immediate";
			}
			List<ParseQuery<ParseUser>> queries = new ArrayList<ParseQuery<ParseUser>>();
			for(int i=0;i<receiver.length;i++)
			{
				String[] rec1 = receiver[i].split("<");
				String[] rec2 = rec1[1].split(">");
				Log.d("username",rec2[0]);
				
				ParseQuery<ParseUser> temp_query = ParseUser.getQuery();
				temp_query.whereEqualTo("username", rec2[0]);
				queries.add(temp_query);
				
			}
			
			ParseQuery<ParseUser> mainQuery = ParseQuery.or(queries);
			
			mainQuery.findInBackground(new FindCallback<ParseUser>() {
				@Override
	  			public void done(List<ParseUser> objects, ParseException e) {

					for(int i=0;i<objects.size();i++)
					{

						noti = new ParseObject("Notification");
						noti.put("Sender", ParseUser.getCurrentUser().getUsername());
						noti.put("Receiver", objects.get(i).getString("username"));
						noti.put("Message", Message.getText().toString());
						noti.put("StartDate", formatedDate2);
						noti.put("StartTime", formatedTime1);
						noti.put("EndDate", formatedDate1);
						noti.put("EndTime", formatedTime2);
						noti.put("Location", noti_lat + "," + noti_lng);
						noti.put("Map_Radius", rad);
						noti.put("Notification_Type", notification_type);
						noti.put("Notification_Image", img_file);
						noti.put("Notification_Image_Thumbnail", img_file_thumbnail);
						noti.put("Received", 0);
						noti.put("Read", 0);
						noti.put("Notification_Code", push_notification_code);
						acl_push.setPublicWriteAccess(true);
						acl_push.setPublicReadAccess(true);
						noti.setACL(acl_push);
						noti.put("ReqUserName", objects.get(i).getString("FirstName")+" "+objects.get(i).getString("LastName"));
						noti.put("ReqFromName", ParseUser.getCurrentUser().getString("FirstName")+" "+ParseUser.getCurrentUser().getString("LastName"));
						
						noti.saveInBackground(new SaveCallback() {
							public void done(ParseException e) {
								progressDialog.dismiss();
								if (e == null) {
									// Show a simple Toast message upon successful
									// registration
									Toast.makeText(SetNotificationActivity.this,
											"Notification successfully sent!",
											Toast.LENGTH_SHORT).show();
									List<ParseQuery<ParseUser>> queries = new ArrayList<ParseQuery<ParseUser>>();
									for(int i=0;i<receiver.length;i++)
									{
										String[] rec1 = receiver[i].split("<");
										String[] rec2 = rec1[1].split(">");
										
										ParseQuery<ParseUser> temp_query = ParseUser.getQuery();
										temp_query.whereEqualTo("username", rec2[0]);
										queries.add(temp_query);
									}
									
									ParseQuery<ParseUser> mainQuery = ParseQuery.or(queries);
									
									mainQuery.findInBackground(new FindCallback<ParseUser>() {
							
											@Override
											public void done(List<ParseUser> user_objects, ParseException e) {
												// TODO Auto-generated method stub
												for(int i=0;i<user_objects.size();i++)
												{
													Log.d("userQuery", "" + user_objects.size());
													location_username = user_objects.get(i).getUsername();
													Log.d("username", location_username);
													
													// Find devices associated with these users
													ParsePush parsePush = new ParsePush();
													ParseQuery<ParseInstallation> pQuery = ParseInstallation.getQuery(); 
													pQuery.whereEqualTo("username", location_username); 
								
													JSONObject data = null;
													try {
														data = new JSONObject(
																"{\"action\": \"com.example.parseaccount.UPDATE_STATUS\", \"customdata\": \"silent\" }");
													} catch (JSONException je) {
														je.printStackTrace();
													}
								
													parsePush.sendDataInBackground(data, pQuery);
													
													SetNotificationActivity.this.finish();
												}
											}
									});
								} else {
									Toast.makeText(getApplicationContext(),
											"Notification Setting Error:" + e.toString(),
											Toast.LENGTH_LONG).show();
								}
							}
						});
					}
				}
			});
		}
		if (task.equals("EDIT")) {
			Log.d("set_noti->push", task);
			notification_type = type;
			ParseQuery<ParseObject> query = ParseQuery.getQuery("Notification");
			query.whereEqualTo("objectId", object_id);
			query.whereEqualTo("Notification_Code", n_code);

			query.findInBackground(new FindCallback<ParseObject>() {
				@Override
				public void done(List<ParseObject> objects, ParseException e) {

					if (e == null) {
						try {
							Log.d("Query", "" + objects.size());

							if (objects.size() != 0) {
								objects.get(0).put(
										"Sender",
										ParseUser.getCurrentUser()
												.getUsername());
								objects.get(0).put("Receiver", receiver);
								objects.get(0).put("Message",
										Message.getText().toString());
								objects.get(0).put("StartDate", formatedDate2);
								objects.get(0).put("StartTime", formatedTime1);
								objects.get(0).put("EndDate", formatedDate1);
								objects.get(0).put("EndTime", formatedTime2);
								objects.get(0).put("Location",
										noti_lat + "," + noti_lng);
								objects.get(0).put("Map_Radius", rad);
								objects.get(0).put("Notification_Type",
										notification_type);
								objects.get(0).put("Notification_Image",
										img_file);
								objects.get(0).put("Notification_Image_Thumbnail", img_file_thumbnail);
								objects.get(0).put("Received", 0);
								objects.get(0).put("Read", 0);
								acl_push.setPublicWriteAccess(true);
								acl_push.setPublicReadAccess(true);
								objects.get(0).setACL(acl_push);
								objects.get(0).saveInBackground(
										new SaveCallback() {
											public void done(ParseException e) {
												Log.d("push function",
														"task complete");
												progressDialog.dismiss();
												if (e == null) {
													// Show a simple Toast
													// message upon successful
													// registration
													Toast.makeText(
															SetNotificationActivity.this,
															"Notification successfully sent!",
															Toast.LENGTH_SHORT)
															.show();
													List<ParseQuery<ParseUser>> queries = new ArrayList<ParseQuery<ParseUser>>();
													for(int i=0;i<receiver.length;i++)
													{
														String[] rec1 = receiver[i].split("<");
														String[] rec2 = rec1[1].split(">");
														
														ParseQuery<ParseUser> temp_query = ParseUser.getQuery();
														temp_query.whereEqualTo("username", rec2[0]);
														queries.add(temp_query);
													}
													
													ParseQuery<ParseUser> mainQuery = ParseQuery.or(queries);
													
													mainQuery.findInBackground(new FindCallback<ParseUser>() {
											
															@Override
															public void done(List<ParseUser> user_objects, ParseException e) {
																// TODO Auto-generated method stub
																for(int i=0;i<user_objects.size();i++)
																{
																	Log.d("userQuery", "" + user_objects.size());
																	location_username = user_objects.get(i).getUsername();
																	Log.d("username", location_username);
																	
																	// Find devices associated with these users
																	ParsePush parsePush = new ParsePush();
																	ParseQuery<ParseInstallation> pQuery = ParseInstallation.getQuery(); 
																	pQuery.whereEqualTo("username", location_username); 
												
																	JSONObject data = null;
																	try {
																		data = new JSONObject(
																				"{\"action\": \"com.example.parseaccount.UPDATE_STATUS\", \"customdata\": \"edit\" }");
																	} catch (JSONException je) {
																		je.printStackTrace();
																	}
												
																	parsePush.sendDataInBackground(data, pQuery);
																	
																	SetNotificationActivity.this.finish();
																}
															}
													});
													
												} else {
													Toast.makeText(
															getApplicationContext(),
															"Notification Setting Error:"
																	+ e.toString(),
															Toast.LENGTH_LONG)
															.show();
												}
											}
										});
							}
						} catch (Exception ex) {
						}
					}
				}
			});
		}
	}
	
	public static Bitmap getScaledBitmap(String path, int newSize) {
	    File image = new File(path);

	    BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    options.inInputShareable = true;
	    options.inPurgeable = true;

	    BitmapFactory.decodeFile(image.getPath(), options);
	    if ((options.outWidth == -1) || (options.outHeight == -1))
	        return null;

	    int originalSize = (options.outHeight > options.outWidth) ? options.outHeight
	            : options.outWidth;

	    BitmapFactory.Options opts = new BitmapFactory.Options();
	    opts.inSampleSize = originalSize / newSize;

	    Bitmap scaledBitmap = BitmapFactory.decodeFile(image.getPath(), opts);

	    return scaledBitmap;     
	}
	
	public static void hideSoftKeyboard (Activity activity, View view) 
	{
	    InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
	    imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
	}

	public Location getLocation(String provider) {
		if (locationManager.isProviderEnabled(provider)) {
			locationManager.requestLocationUpdates(provider,
					MIN_TIME_FOR_UPDATE, MIN_DISTANCE_FOR_UPDATE, this);
			if (locationManager != null) {
				loc = locationManager.getLastKnownLocation(provider);
				return loc;
			}
		}
		return null;
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
}