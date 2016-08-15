package com.example.rohan.securelibrary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelperCustomer extends SQLiteOpenHelper {
	SQLiteDatabase db = this.getWritableDatabase();
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "DevCustDB";
	private static final String TABLE_NAME = "devCustTable";
	private static final String KEY_NAME = "name";
	private static final String KEY_PHNO = "phno";
	private static final String KEY_PASSWORD = "password";
	private static final String[] COLUMNS = { KEY_NAME, KEY_PHNO, KEY_PASSWORD };

	public DBHelperCustomer(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		System.out.println("In constr of devTable");
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CUSTOMER_TABLE = "CREATE TABLE IF NOT EXISTS devCustTable ( "
				+ "name TEXT, " + "phno TEXT PRIMARY KEY, " + "password TEXT)";

		db.execSQL(CREATE_CUSTOMER_TABLE);
		Log.d("table is created on customer side!", CREATE_CUSTOMER_TABLE);
		String CREATE_SHOPKEEPER_TABLE = "CREATE TABLE IF NOT EXISTS devShopkeeperTable ( "
				+ "name TEXT, "
				+ "phno TEXT PRIMARY KEY, "
				+ "amount FLOAT, address TEXT, timestamp DATETIME DEFAULT CURRENT_TIMESTAMP)";

		db.execSQL(CREATE_SHOPKEEPER_TABLE);
		Log.d("table is created on customer side!", CREATE_SHOPKEEPER_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	public long addSelf(Customer user) throws Exception {
		db = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put(KEY_PHNO, user.getPhoneNumber());
		values.put(KEY_NAME, user.getName());
		values.put(KEY_PASSWORD, user.getPassword());

		long row = db.insert(TABLE_NAME, // table
				null, // nullColumnHack
				values); // key/value -> keys = column names/ values = column
		System.out.println("In demo ROW!!!!" + row);
		getSelf();

		return row;
	}

	public Customer getSelf() {
		Customer user = null;
		db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, // a. table
				COLUMNS, // b. column names
				null, // c. selections
				null, // d. selections args
				null, // e. group by
				null, // f. having
				null, // g. order by
				null); // h. limit
		System.out.println("In demo CURSOR" + cursor);
		if (cursor.getCount() == 0) {
			return null;
		} else {
			cursor.moveToFirst();
			user = new Customer();
			System.out.println("In demo CURSOR STUFF " + cursor.getString(0));
			user.setPhoneno(cursor.getString(1));
			user.setName(cursor.getString(0));
			user.setPassword(cursor.getString(2));
			System.out.println("IN DEMO details of user added :"
					+ user.toString());
		}
		System.out.println("In demo" + user.getName());
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		db.close();
		return user;
	}

	public Customer validate(String phno) {
		Customer self = null;

		// 1. get reference to readable DB
		SQLiteDatabase db = this.getWritableDatabase();

		// 2. build query
		Cursor cursor = db.rawQuery("Select * from devCustTable where phno = '"
				+ phno + "'", null);
		System.out.println("In demo Cursor count: " + cursor.getCount());
		if (cursor == null)
			return null;
		// 3. if we got results get the first one
		if (cursor != null) {
			System.out.println("Cursor moveToFirst in demo"
					+ cursor.moveToFirst());
			cursor.moveToFirst();
			Log.d("In demo getOwner(0)", "hello");
			// 4. build book object
			self = new Customer();
			self.setName(cursor.getString(0));
			self.setPhoneno(cursor.getString(1));
			self.setPassword(cursor.getString(2));
			Log.d("In demo getOwner(" + phno + ")", self.toString());
		}
		System.out.println("In demo Name from database: " + self.getName());
		cursor.close();
		db.close();
		return self;
	}
}
