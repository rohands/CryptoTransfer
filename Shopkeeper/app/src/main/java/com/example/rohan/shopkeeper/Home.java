package com.example.rohan.shopkeeper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
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

import com.example.rohan.shopkeeper.search.SearchFunctionality;
import com.example.securetransfer.seller.Seller;
import com.example.securetransfer.seller.SellerIntroduction;

public class Home extends Activity {
	private static final int duration = Toast.LENGTH_LONG;
	Seller seller;
	CallbackImplementor callbackObject;
	Context context;
	DBHelperCustomer DbHelperCustomer;
	DBHelperShopkeeper DbHelperShopkeeper;
	Toast toast;
	String objName = "callbackObject";
	Shopkeeper self;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		this.context =getApplicationContext();
		Button button1 = (Button) findViewById(R.id.button1);
		button1.setOnClickListener(onclickListener1);
		Button button2 = (Button) findViewById(R.id.button2);
		button2.setOnClickListener(onclickListener2);
		Button button3 = (Button) findViewById(R.id.button3);
		button3.setOnClickListener(onclickListener3);
		callbackObject = new CallbackImplementor();
		
	
		DbHelperCustomer = new DBHelperCustomer(context);
		DbHelperShopkeeper = new DBHelperShopkeeper(context);
		self = new Shopkeeper();
		// buyer = new Buyer(this, getApplicationContext(), callbackObject);
	}

	private OnClickListener onclickListener1 = new OnClickListener() {
		@Override
		public void onClick(final View v) {
			Intent intent = new Intent(Home.this, SellerIntroduction.class);
			intent.putExtra("callbackObject", callbackObject);
			self = DbHelperShopkeeper.getSelf();
			
			intent.putExtra("data",
					"name:"+self.getName()+",phone:"+self.getPhoneNumber()+",amount:0,address:"+self.getAddress());
			startActivityForResult(intent, 1000);
		}
	};

	private OnClickListener onclickListener2 = new OnClickListener() {
		@Override
		public void onClick(final View v) {
			Intent intent = new Intent(Home.this, SearchFunctionality.class);
			startActivity(intent);
			
		}
	};
	private OnClickListener onclickListener3 = new OnClickListener() {
		@Override
		public void onClick(final View v) {
			Intent intent = new Intent(Home.this, SearchFunctionality.class);
			startActivity(intent);
		}
	};

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1000) {
			Log.d("DEBUG", "On getting introduction result");
			if (resultCode == Activity.RESULT_OK) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				
				Calendar cal = Calendar.getInstance();
				String now = new String(sdf.format(cal.getTime()));
				String result = data.getStringExtra("dataFromIntroduction");
				StringTokenizer tokenizer = new StringTokenizer(result, ",");
				List<String> splitData = new ArrayList<String>();
				while (tokenizer.hasMoreElements()) {
					splitData.add((String) tokenizer.nextElement());
				}
				Customer customer = new Customer();
				customer.setName(splitData.get(0).split(":")[1]);
				customer.setPhoneno(splitData.get(1).split(":")[1]);
				customer.setAmount(new Double((splitData.get(2).split(":")[1])));
				customer.setAddress(splitData.get(3).split(":")[1]);
				customer.setTime(now);
				try {
					System.out.println("HOME HOME");
					System.out.println(customer.toString());
					DbHelperCustomer.addCustomer(customer);
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
