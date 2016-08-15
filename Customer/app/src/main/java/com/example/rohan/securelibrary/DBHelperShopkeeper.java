package com.example.rohan.securelibrary;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelperShopkeeper extends SQLiteOpenHelper {
	SQLiteDatabase db = this.getWritableDatabase();
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "DevCustDB";
	private static final String TABLE_NAME = "devShopkeeperTable";
	private static final String KEY_NAME = "name";
	private static final String KEY_PHNO = "phno";
	private static final String KEY_AMOUNT = "amount";
	private static final String KEY_ADDRESS = "address";
	private static final String KEY_TIMESTAMP = "timestamp";
	private static final String[] COLUMNS = { KEY_NAME, KEY_PHNO, KEY_AMOUNT,
			KEY_ADDRESS, KEY_TIMESTAMP };

	public DBHelperShopkeeper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		System.out.println("In constr of devTable");
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	public long addShopkeeper(Shopkeeper shopGuy) throws Exception {
		db = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put(KEY_PHNO, shopGuy.getPhoneNumber());
		values.put(KEY_NAME, shopGuy.getName());
		values.put(KEY_AMOUNT, shopGuy.getAmount().toString());
		values.put(KEY_ADDRESS, shopGuy.getAddress());
		values.put(KEY_TIMESTAMP, shopGuy.getTimestamp());

		long row = db.insert(TABLE_NAME, // table
				null, // nullColumnHack
				values); // key/value -> keys = column names/ values = column
		System.out.println("ROW!!!!" + row);
		getShopkeeper(shopGuy.getPhoneNumber());

		return row;
	}

	public Shopkeeper getShopkeeper(String phno) {
		Shopkeeper shopGuy = null;
		db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, // a. table
				COLUMNS, // b. column names
				"phno=?", // c. selections
				new String[] { phno }, // d. selections args
				null, // e. group by
				null, // f. having
				null, // g. order by
				null); // h. limit
		System.out.println("CURSOR" + cursor);
		if (cursor.getCount() == 0) {
			return null;
		} else {
			cursor.moveToFirst();
			shopGuy = new Shopkeeper();
			System.out.println("CURSOR STUFF " + cursor.getString(0));
			shopGuy.setPhoneno(cursor.getString(1));
			shopGuy.setName(cursor.getString(0));
			shopGuy.setAmount(new Double(cursor.getString(2)));
			shopGuy.setAddress(cursor.getString(3));
			shopGuy.setTimestamp(cursor.getString(4));
		}
		System.out.println(shopGuy.getName());
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}

		return shopGuy;
	}

	public List<Shopkeeper> getAllContacts() {
		List<Shopkeeper> contactList = new ArrayList<Shopkeeper>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_NAME;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Shopkeeper contact = new Shopkeeper();
				// contact.setID(Integer.parseInt(cursor.getString(0)));
				contact.setName(cursor.getString(0));
				contact.setPhoneno(cursor.getString(1));
				// Adding contact to list
				contactList.add(contact);
			} while (cursor.moveToNext());
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		} else {
			db.close();
			return null;
		}
		db.close();
		return contactList;
	}

	public Shopkeeper getShopkeeperByName(String name) {

		String selectQuery = "SELECT * FROM " + TABLE_NAME + " where name = '"
				+ name + "'";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.getCount() == 0) {
			db.close();
			return null;
		} else {
			cursor.moveToFirst();
			Shopkeeper user = new Shopkeeper(cursor.getString(0),
					cursor.getString(1), new Double(cursor.getString(2)),
					cursor.getString(3), cursor.getString(4));
			System.out.println("Get shopkeeper by name: " + user.toString());
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
			db.close();
			return user;
		}
	}

	public long updateBalance(Shopkeeper shopGuy) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_AMOUNT, shopGuy.getAmount());
		values.put(KEY_TIMESTAMP, shopGuy.getTimestamp());
		int i = db.update(TABLE_NAME, // table
				values, // column/value
				KEY_PHNO + " = ?", // selections
				new String[] { String.valueOf(shopGuy.getPhoneNumber()) });
		db.close();
		return i;
	}
}
