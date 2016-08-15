package com.example.rohan.shopkeeper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelperShopkeeper extends SQLiteOpenHelper {
	SQLiteDatabase db = this.getWritableDatabase();
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "DevCustDB";
	private static final String TABLE_NAME = "devShopkeeperTable";
	private static final String KEY_NAME = "name";
	private static final String KEY_PHNO = "phno";
	private static final String KEY_PASSWORD = "password";
	private static final String KEY_ADDRESS = "address";
	private static final String[] COLUMNS = { KEY_NAME, KEY_PHNO, KEY_PASSWORD ,KEY_ADDRESS};

	public DBHelperShopkeeper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		System.out.println("In constr of devTable");
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_DEV_TABLE = "CREATE TABLE IF NOT EXISTS devShopkeeperTable ( "
				+ "name TEXT, " + "phno TEXT PRIMARY KEY, " + "password TEXT, address TEXT)";

		db.execSQL(CREATE_DEV_TABLE);
		Log.d("table is created on Shopkeeper side!", CREATE_DEV_TABLE);
		String CREATE_CUST_TABLE = "CREATE TABLE IF NOT EXISTS devCustomerTable ( "
				+ "name TEXT, "
				+ "phno TEXT PRIMARY KEY, "
				+ "amount FLOAT, address TEXT,time TEXT)";

		db.execSQL(CREATE_CUST_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	public long addSelf(Shopkeeper user) throws Exception {
		db = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put(KEY_PHNO, user.getPhoneNumber());
		values.put(KEY_NAME, user.getName());
		values.put(KEY_PASSWORD, user.getPassword());
		values.put(KEY_ADDRESS, user.getAddress());

		long row = db.insert(TABLE_NAME, // table
				null, // nullColumnHack
				values); // key/value -> keys = column names/ values = column
		System.out.println("ROW!!!!" + row);
		getSelf();

		return row;
	}

	public Shopkeeper getSelf() {
		Shopkeeper user = null;
		db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, // a. table
				COLUMNS, // b. column names
				null, // c. selections
				null, // d. selections args
				null, // e. group by
				null, // f. having
				null, // g. order by
				null); // h. limit
		System.out.println("CURSOR" + cursor);
		if (cursor.getCount() == 0) {
			return null;
		} else {
			cursor.moveToFirst();
			user = new Shopkeeper();
			System.out.println("CURSOR STUFF " + cursor.getString(0));
			user.setPhoneno(cursor.getString(1));
			user.setName(cursor.getString(0));
			user.setPassword(cursor.getString(2));
			user.setAddress(cursor.getString(3));
		}
		System.out.println(user.getName());
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}

		return user;
	}

	public Shopkeeper validate(String phno) {
		Shopkeeper self = null;

		// 1. get reference to readable DB
		SQLiteDatabase db = this.getReadableDatabase();

		// 2. build query
		Cursor cursor = db.query(TABLE_NAME, // a. table
				COLUMNS, // b. column names
				"phno=?", // c. selections
				new String[] { phno }, // d. selections args
				null, // e. group by
				null, // f. having
				null, // g. order by
				null); // h. limit

		if (cursor == null)
			return null;
		// 3. if we got results get the first one
		if (cursor != null && cursor.moveToFirst()) {
			Log.d("getOwner(0)", "hello");
			// 4. build book object
			self = new Shopkeeper();
			self.setName(cursor.getString(0));
			self.setPhoneno(cursor.getString(1));
			self.setPassword(cursor.getString(2));
			self.setAddress(cursor.getString(3));
			Log.d("getOwner(" + phno + ")", self.toString());
		}
		cursor.close();
		db.close();
		return self;
	}
}
