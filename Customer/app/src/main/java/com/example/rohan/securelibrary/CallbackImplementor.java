package com.example.rohan.securelibrary;

import java.io.Serializable;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.example.securetransfer.callback.BuyerCallback;

public class CallbackImplementor extends Activity implements BuyerCallback,
		Serializable {

	Context context;
	Boolean flag;
	Activity activity;
	String data;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.callback);
		this.activity = this;
		this.context = getApplicationContext();
		Intent intent = getIntent();
		data = intent.getStringExtra("dataForCallback");
		int flag = intent.getIntExtra("flag", 0);
		if (flag == 1) {
			transactionCallback(data);
		} else {
			transactionNotification(data);
		}
	}

	public CallbackImplementor() {
		Log.d("In callback ctor", "HERE!");
	}

	@Override
	public void transactionCallback(String dummyData) {
		this.flag = true;
		String[] splitString = data.split("=");
		String amount = splitString[1];
		DBHelperShopkeeper dbHelperShop = new DBHelperShopkeeper(this.context);
		SharedPreferences pref = context.getSharedPreferences("DemoCustomer",
				context.MODE_PRIVATE);
		String phno = pref.getString("ShopkeeperPhoneNumber", "none");
		System.out.println("phone: " + phno);
		Shopkeeper shopGuy = dbHelperShop.getShopkeeper(phno);

		AlertDialog.Builder builder = new AlertDialog.Builder(this.activity);
		builder.setTitle("Conflict Arised!But was resolved!");
		builder.setMessage(
				"Your balance was: " + shopGuy.getAmount()
						+ "\nBalance will be updated to: " + amount
						+ "\nDo you agree?").setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = getIntent();
						intent.putExtra("data", data);
						setResult(Activity.RESULT_OK, intent);
						finish();
					}
				});
		builder.setNegativeButton(android.R.string.no,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = getIntent();
						intent.putExtra("data", "false");
						setResult(Activity.RESULT_OK, intent);
						finish();
					}
				});
		System.out.println("In callback" + builder);
		AlertDialog alert = builder.create();
		alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		alert.show();

	}

	@Override
	public void transactionNotification(String dummyData) {
		Log.d("In transaction callback", "returning same data");
		String[] amount = data.split("=");
		AlertDialog.Builder builder = new AlertDialog.Builder(this.activity);

		builder.setMessage("Balance will be now be: " + amount[1])
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						finish();
					}
				});
		AlertDialog alert = builder.create();
		alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		alert.show();
		return;
	}
}
