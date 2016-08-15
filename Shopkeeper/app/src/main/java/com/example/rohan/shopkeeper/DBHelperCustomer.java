
package com.example.rohan.shopkeeper;

import java.util.ArrayList;
import java.util.List;

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
	private static final String TABLE_NAME = "devCustomerTable";
	private static final String KEY_NAME = "name";
	private static final String KEY_PHNO = "phno";
	private static final String KEY_AMOUNT = "amount";
	private static final String KEY_ADDRESS = "address";
	private static final String KEY_TIME = "time";
	private static final String[] COLUMNS = { KEY_NAME, KEY_PHNO, KEY_AMOUNT,
			KEY_ADDRESS , KEY_TIME};

	public DBHelperCustomer(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		System.out.println("In constr of devTable");
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	public long addCustomer(Customer customer) throws Exception {
		db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		System.out.println(customer.toString());
		values.put(KEY_PHNO, customer.getPhoneNumber());
		values.put(KEY_NAME, customer.getName());
		values.put(KEY_AMOUNT, customer.getAmount().toString());
		values.put(KEY_ADDRESS, customer.getAddress());
		values.put(KEY_TIME, customer.getTime());

		long row = db.insert(TABLE_NAME, // table
				null, // nullColumnHack
				values); // key/value -> keys = column names/ values = column
		System.out.println("ROW!!!!" + row);
		getCustomer(customer.getPhoneNumber());
		System.out.println("IN demo "+customer.toString());
		return row;
	}

	public Customer getCustomer(String phno) {
		Customer customer = null;
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
			customer = new Customer();
			System.out.println("CURSOR STUFF " + cursor.getString(0));
			customer.setPhoneno(cursor.getString(1));
			customer.setName(cursor.getString(0));
			customer.setAmount(new Double(cursor.getString(2)));
			customer.setAddress(cursor.getString(3));
			customer.setTime(cursor.getString(4));
		}
		System.out.println(customer.getName());
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}

		return customer;
	}

	
	public List<Customer> getAllContacts() {
		List<Customer> contactList = new ArrayList<Customer>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_NAME;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Customer contact = new Customer();
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

	public Customer getCustomerByName(String name) {

		String selectQuery = "SELECT phno FROM " + TABLE_NAME
				+ " where name= '" + name + "'";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.getCount() == 0) {
			db.close();
			return null;
		} else {
			cursor.moveToFirst();
			Customer user = new Customer();
			user.setPhoneno(cursor.getString(0));
			// user.setName(cursor.getString(1));
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
			db.close();
			return user;
		}
	}
	public int updateCustomer(Customer user) throws Exception {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_AMOUNT, user.getAmount());
		values.put(KEY_TIME, user.getTime());
		int i = db.update(TABLE_NAME, // table
				values, // column/value
				KEY_PHNO + " = ?", // selections
				new String[] { String.valueOf(user.getPhoneNumber()) });
		db.close();
		return i;

	}
}
