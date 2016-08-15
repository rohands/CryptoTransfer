package com.example.rohan.securelibrary;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.example.rohan.securelibrary.search.SearchFunctionality;
import com.example.securetransfer.buyer.Buyer;
import com.example.securetransfer.buyer.BuyerIntroduction;


public class Home extends Activity {
	private static final int duration = Toast.LENGTH_LONG;
	Buyer buyer;
	CallbackImplementor callbackObject;
	Context context;
	DBHelperShopkeeper DBHelperShopkeeper;
	DBHelperCustomer DBHelperCustomer;
	Toast toast;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		Button button1 = (Button) findViewById(R.id.button1);
		button1.setOnClickListener(onclickListener1);
		Button button2 = (Button) findViewById(R.id.button2);
		button2.setOnClickListener(onclickListener2);
		Button button3 = (Button) findViewById(R.id.button3);
		button3.setOnClickListener(onclickListener3);

		context = getApplicationContext();
		System.out.println("Before ctor of callbackobject");
		callbackObject = new CallbackImplementor();
		System.out.println("After ctor of callbackobject");
		DBHelperShopkeeper = new DBHelperShopkeeper(context);
		DBHelperCustomer = new DBHelperCustomer(context);
		// buyer = new Buyer(this, getApplicationContext(), callbackObject);
	}

	private OnClickListener onclickListener1 = new OnClickListener() {
		@Override
		public void onClick(final View v) {
			Intent intent = new Intent(Home.this, BuyerIntroduction.class);
			intent.putExtra("callbackObject", callbackObject);
			Customer myself = DBHelperCustomer.getSelf();
			intent.putExtra("data", "name:" + myself.getName() + ",phone:"
					+ myself.getPhoneNumber() + ",amount:0.0,address: Ulsoor");
			startActivityForResult(intent, 1000);
		}
	};

	private OnClickListener onclickListener2 = new OnClickListener() {
		@Override
		public void onClick(final View v) {
			Intent intent = new Intent(Home.this, SearchFunctionality.class);
			intent.putExtra("Transaction", true);
			startActivity(intent);
		}
	};

	private OnClickListener onclickListener3 = new OnClickListener() {
		@Override
		public void onClick(final View v) {
			Intent intent = new Intent(Home.this, SearchFunctionality.class);
			intent.putExtra("Transaction", false);
			startActivity(intent);
		}
	};

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1000) {
			Log.d("DEBUG", "On getting introduction result");
			if (resultCode == Activity.RESULT_OK) {
				String result = data.getStringExtra("dataFromIntroduction");
				System.out.println("Data from introduction:" + result);
				StringTokenizer tokenizer = new StringTokenizer(result, ",");
				List<String> splitData = new ArrayList<String>();
				while (tokenizer.hasMoreElements()) {
					splitData.add((String) tokenizer.nextElement());
				}
				Shopkeeper shopGuy = new Shopkeeper();
				shopGuy.setName(splitData.get(0).split(":")[1]);
				shopGuy.setPhoneno(splitData.get(1).split(":")[1]);
				shopGuy.setAmount(new Double((splitData.get(2).split(":")[1])));
				shopGuy.setAddress(splitData.get(3).split(":")[1]);
				SimpleDateFormat dateFormat = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss", Locale.getDefault());
				Date date = new Date();
				shopGuy.setTimestamp(dateFormat.format(date));
				System.out.println("Shop guy details: " + shopGuy.toString());
				try {
					DBHelperShopkeeper.addShopkeeper(shopGuy);
				} catch (Exception e) {
					String errorMessage = "Error:" + e;
					Log.d("Error_adding_keeper", errorMessage);
					toast = Toast
							.makeText(this.context, errorMessage, duration);
					toast.setGravity(Gravity.BOTTOM | Gravity.LEFT, 400, 300);
					toast.show();
				}
			}

		}
	}
}
