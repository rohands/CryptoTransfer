package com.example.rohan.shopkeeper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.securetransfer.callback.SellerCallback;

public class CallbackImplementor extends Activity implements SellerCallback,
		Serializable {

	Context context;
	private String bill = "";
	String customerPhno;
	int duration =  Toast.LENGTH_SHORT;
	Activity activity;
	private double amount;
	private double balance;
	private String data;

	public CallbackImplementor() {

		Log.d("CallbackIMplementor", "came here");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.transaction);
		this.activity = this;
		this.context = getApplicationContext();

		Intent intent = getIntent();
		String data = intent.getStringExtra("data");
		int flag = intent.getIntExtra("flag", 0);
		if (flag == 1) {
			transactionCallback(data);
		}
		if (flag == 2) {
			transactionConflictCallback(data);
		}
		if (flag == 3) {
			transactionResolveConflict(data);
		}

	}

	@Override
	public void transactionCallback(String data1) {

		data = data1;
		String amount = data1.substring(8);
		Log.d("In transaction callback", amount);

		AlertDialog.Builder builder = new AlertDialog.Builder(this.activity);
		builder.setMessage(
				"Do you agree after this transaction your balance will be "
						+ amount).setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Intent i = getIntent();
						i.putExtra("Clickno", false);
						i.putExtra("data", data);
						setResult(Activity.RESULT_OK, i);
						finish();
					}
				});
		builder.setNegativeButton(android.R.string.no,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// do nothing
						Intent i = getIntent();
						i.putExtra("Clickno", true);
						setResult(Activity.RESULT_OK, i);
						finish();
					}
				});
		AlertDialog alert = builder.create();
		alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		alert.show();
		System.out.println(data);

	}

	@Override
	public void transactionConflictCallback(String data) {

		StringTokenizer tokenizer = new StringTokenizer(data, ";");
		List<String> packet = new ArrayList<String>();
		while (tokenizer.hasMoreElements()) {
			packet.add((String) tokenizer.nextElement());
		}
		Log.d("In transaction callback", "returning same data");
		AlertDialog.Builder builder = new AlertDialog.Builder(this.activity);
		builder.setTitle("Mismatch in balance!");
		builder.setMessage(
				"Your balance was " + parse(packet.get(0)) + " Now it will be updated to "
						+ parse(packet.get(1))).setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// do things
						finish();
					}
				});
		AlertDialog alert = builder.create();
		alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		alert.show();
		return;
	}

	public String parse(String parseString) {
		return parseString.substring(8);
	}

	@Override
	public void transactionResolveConflict(String data1) {
		data = data1;
		final EditText input = new EditText(activity);

		SharedPreferences pref = context.getSharedPreferences("ShopkeeperPREF",
				Context.MODE_PRIVATE);

		customerPhno = pref.getString("CustomerPhno", null);
		DBHelperCustomer dbCustomer = new DBHelperCustomer(context);
		Customer customer = dbCustomer.getCustomer(customerPhno);
		balance = customer.getAmount();
		System.out.println(balance);

		AlertDialog.Builder builder = new AlertDialog.Builder(this.activity);
		builder.setView(input);
		builder.setTitle("Conflict arised!");
		builder.setMessage(
				"You have the latest data!!!Your balance is " + balance
						+ ". Please enter bill amount").setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						amount = Double.parseDouble(input.getText().toString());
						if(amount> balance)
						{
							String text = "your bill amount is more than balance!";
		    				Toast toast = Toast.makeText(context, text, duration);
		    				toast.setGravity(Gravity.BOTTOM | Gravity.LEFT, 400, 300);
		    				toast.show();
		    				Intent myIntent = new Intent(activity,CallbackImplementor.class);
						      myIntent.putExtra("data", data);
						      myIntent.putExtra("flag", 3);
						      startActivity(myIntent);
						}
						String returnString = "balance=" + (balance - amount);
						System.out.println(returnString);
						System.out.println(returnString);
						Intent i = getIntent();
						i.putExtra("data", returnString);
						setResult(Activity.RESULT_OK, i);
						finish();
					}
				});
		builder.setNegativeButton(android.R.string.no,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// do nothing
						finish();
					}
				});

		AlertDialog alert = builder.create();
		alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		alert.show();

	}
}
