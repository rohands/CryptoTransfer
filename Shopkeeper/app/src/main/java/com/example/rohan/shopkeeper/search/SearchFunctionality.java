package com.example.rohan.shopkeeper.search;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.example.rohan.shopkeeper.Customer;
import com.example.rohan.shopkeeper.DBHelperCustomer;
import com.example.rohan.shopkeeper.R;
import com.example.rohan.shopkeeper.Transaction;

public class SearchFunctionality extends Activity implements OnClickListener,
		OnEditorActionListener, OnItemClickListener {
	DBHelperCustomer db;
	List<Customer> contacts;
	ListView mListView;
	MySimpleSearchAdapter mAdapter;
	Button btnSearch, btnLeft;
	EditText mtxt;
	Toast toast;
	int duration = Toast.LENGTH_LONG;

	// Shopkeeper user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		Log.d("here in oncreate", "hi");
		db = new DBHelperCustomer(getApplicationContext());
		mListView = (ListView) findViewById(R.id.mListView);
		mAdapter = new MySimpleSearchAdapter(this);
		btnSearch = (Button) findViewById(R.id.btnSearch);
		btnLeft = (Button) findViewById(R.id.btnLeft);
		mtxt = (EditText) findViewById(R.id.edSearch);
		mtxt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (0 != mtxt.getText().length()) {
					String spnId = mtxt.getText().toString();
					setSearchResult(spnId);
				} else {
					setData();
				}
			}
		});
		btnLeft.setOnClickListener(this);
		btnSearch.setOnClickListener(this);

		contacts = db.getAllContacts();

		if (contacts != null) {
			Log.d("Getting from contacts", contacts.get(0).toString());
			setData();
		} else {
			String errorMessage = "No contacts founded";
			toast = Toast.makeText(this, errorMessage, duration);
			toast.setGravity(Gravity.BOTTOM | Gravity.LEFT, 400, 300);
			toast.show();
		}

	}

	ArrayList<String> mAllData;

	public void setData() {
		Log.d("Here inside oncreate", "trying");
		// List<String> strList = (List)contacts;

		int contactSize = contacts.size();
		Log.d("Size of list", Integer.toString(contactSize));
		String[] contarr = new String[contactSize];
		/* ArrayList<String> contarr = new ArrayList<String>(); */
		int i = 0;
		for (Customer c : contacts) {
			contarr[i] = c.getName();
			i++;
		}

		// String[] arr = contacts.toArray(new String[contacts.size()]);
		Log.d("before loop", "looping");
		mAllData = new ArrayList<String>();
		mAdapter = new MySimpleSearchAdapter(this);
		for (i = 0; i < contactSize; i++) {
			mAdapter.addItem(contarr[i]);
			mAllData.add(contarr[i]);
		}
		mListView.setOnItemClickListener(this);
		mListView.setAdapter(mAdapter);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btnSearch) {
			mtxt.setText("");
			setData();
			return;
		} else if (v.getId() == R.id.btnLeft) {

			return;
		}
	}

	public void setSearchResult(String str) {
		mAdapter = new MySimpleSearchAdapter(this);
		if (mAllData != null) {
			for (String temp : mAllData) {
				if (temp.toLowerCase().contains(str.toLowerCase())) {
					mAdapter.addItem(temp);
				}
			}
			mListView.setAdapter(mAdapter);
		}
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		return false;
	}

	@Override
	public void onBackPressed() {
		setResult(Activity.RESULT_CANCELED);
		finish();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		String contactNumber, contactToDisplay;
		String str = mAdapter.getItem(position);
		Toast.makeText(this, str, Toast.LENGTH_LONG).show();
		Customer displayContact = db.getCustomerByName(str);
		contactNumber = displayContact.getPhoneNumber();
		contactToDisplay = str + ";" + contactNumber;
		
		Intent intent = new Intent(SearchFunctionality.this, Transaction.class);
		intent.putExtra("CustomerObject", displayContact);
		startActivity(intent);
		

	}
}