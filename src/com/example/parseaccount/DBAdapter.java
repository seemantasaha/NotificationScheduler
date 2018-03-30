package com.example.parseaccount;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;



public class DBAdapter {
	public static final String KEY_ID = "id";
	public static final String KEY_SENDER = "sender";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_IMAGE = "image";
	public static final String KEY_IN_DATE = "in_date";
	
	private static final String DATABASE_TABLE = "notification_in";
	
	private final Context context;

	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;

	public DBAdapter(Context ctx) {
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}

	// ---opens the database---
	public DBAdapter open() throws SQLException {
		db = DBHelper.getWritableDatabase();
		return this;
	}

	// ---closes the database---
	public void close() {
		DBHelper.close();
	}
	public void delete_all()
	{
		db.delete(DATABASE_TABLE, null, null);
	}
	// ---insert a employee into the database---
	public long insertemp(int id, String sender, String message, byte[] image, String date) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_ID, id);
		initialValues.put(KEY_SENDER, sender);
		initialValues.put(KEY_MESSAGE, message);
		initialValues.put(KEY_IMAGE, image);
		initialValues.put(KEY_IN_DATE, date);
		return db.insert(DATABASE_TABLE, null, initialValues);
	}
	// ---retrieves all the employee---
	public Cursor getAllNotification() {
		return db.query(DATABASE_TABLE, new String[] { KEY_ID, KEY_SENDER,
				KEY_MESSAGE, KEY_IMAGE, KEY_IN_DATE}, null, null, null, null, null, null);
	}
	/*
	public Cursor getBestRecord() {
		return db.query(DATABASE_TABLE2, new String[] { "MAX("+KEY_no_pushup+")"}, null, null, null, null, null, null);
	}
	public Cursor getTotal() {
		return db.query(DATABASE_TABLE2, new String[] { "SUM("+KEY_no_pushup+")"}, null, null, null, null, null, null);
	}
	public Cursor getAverage() {
		return db.query(DATABASE_TABLE2, new String[] { KEY_no_pushup}, null, null, null, null, null, null);
	}*/
	public Cursor getAllemp2(int noti_id) {
		return db.query(DATABASE_TABLE, new String[] { KEY_SENDER, KEY_MESSAGE,KEY_IMAGE, KEY_IN_DATE}, 
				KEY_ID+ " = "+noti_id, null, null, null, null, null);
	}
	// ---retrieves a particular employee---
	/*public Cursor getemp2(String year, String month, String day) throws SQLException {
		
		Cursor mCursor = db.query(true, DATABASE_TABLE2, new String[] {
				KEY_no_pushup, KEY_calorie}, KEY_year+ " = "+year+ " and "+KEY_month+" = "+month+" and "+KEY_dateofmonth+ "=" + day,null, null, null, null, null);

		return mCursor;
	}

	// ---updates a employee---
	public boolean updatepushup(String year, String month,
			String date, String pushup, String calorie) {
		ContentValues args = new ContentValues();
		args.put(KEY_no_pushup, pushup);
		args.put(KEY_calorie, calorie);
		String whereup = KEY_year+ " ="+ year+" and " + KEY_month + " ="+ month +"and " + KEY_dateofmonth + " = "+date;
		return db.update(DATABASE_TABLE2, args, whereup, null) > 0;
	}*/
	
	public boolean deleteRow(int noti_id) 
	{
	    return db.delete(DATABASE_TABLE, KEY_ID + "=" + noti_id, null) > 0;
	}
}

