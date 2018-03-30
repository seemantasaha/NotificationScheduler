package com.example.parseaccount;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String TAG = "JUMLY_DB";
	private static final String DATABASE_NAME = "jumly.db";
	private static final int DATABASE_VERSION = 1;

	private static final String TABLE_CREATE = "CREATE TABLE notification_in (id INTEGER, sender TEXT, message TEXT, image BLOB, in_date TEXT)";
	
	DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
				+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS titles");
		onCreate(db);
	}
}
