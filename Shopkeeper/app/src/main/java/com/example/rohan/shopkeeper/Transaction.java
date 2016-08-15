package com.example.rohan.shopkeeper;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.securetransfer.seller.SellerTransaction;


public class Transaction extends Activity {

	String phoneNumber = "";
	CallbackImplementor callbackObject;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.display);
		Intent intent = getIntent();
		DBHelperCustomer dbHelperCustomer = new DBHelperCustomer(getApplicationContext());
		Customer customer = (Customer) intent
				.getSerializableExtra("CustomerObject");
		callbackObject = new CallbackImplementor();
		System.out.println("In transaction: " + customer.toString());
		phoneNumber = customer.getPhoneNumber();
		customer = dbHelperCustomer.getCustomer(phoneNumber);

		SharedPreferences pref = getSharedPreferences("ShopkeeperPREF",
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString("CustomerPhno", phoneNumber);
		editor.commit();

		TableLayout tblLayout = (TableLayout) findViewById(R.id.tableLayout1);
		TableRow rowOne = (TableRow) tblLayout.getChildAt(1);
		TextView columnName = (TextView) rowOne.getChildAt(1);
		columnName.setText(customer.getName());
		TableRow rowTwo = (TableRow) tblLayout.getChildAt(2);
		System.out.println("Row two: " + rowTwo);
		TextView columnPhone = (TextView) rowTwo.getChildAt(1);
		System.out.println("Columnphone: " + columnPhone);
		columnPhone.setText(customer.getPhoneNumber());
		TableRow rowThree = (TableRow) tblLayout.getChildAt(3);
		TextView columnAmount = (TextView) rowThree.getChildAt(1);
		columnAmount.setText(customer.getAmount().toString());
		TableRow rowFour = (TableRow) tblLayout.getChildAt(4);
		TextView columnAddress = (TextView) rowFour.getChildAt(1);
		columnAddress.setText(customer.getAddress());
		TableRow rowFive = (TableRow) tblLayout.getChildAt(5);
		TextView columnTime = (TextView) rowFive.getChildAt(1);
		columnTime.setText(customer.getTime());
		
		Button button1 = (Button) findViewById(R.id.button1);
		button1.setOnClickListener(onclickListener1);
	}

	private OnClickListener onclickListener1 = new OnClickListener() {
		@Override
		public void onClick(final View v) {

			Intent intent = new Intent(Transaction.this,
					SellerTransaction.class);
			intent.putExtra("callbackObject", callbackObject);
			intent.putExtra("phoneNumber", phoneNumber);
			startActivityForResult(intent, 1001);
		}
	};

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1001) {
			Log.d("DEBUG", "On getting introduction result");
			if (resultCode == Activity.RESULT_OK) {
				Log.d("DEBUG", "Transaction complete!");
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd hh:mm:ss");

				Calendar cal = Calendar.getInstance();
				String now = new String(sdf.format(cal.getTime()));
				SharedPreferences pref = getSharedPreferences("ShopkeeperPREF",
						Context.MODE_PRIVATE);
			
				String customerPhno = pref.getString("CustomerPhno", null);
				System.out.println("Customer Number In Tran demo"+customerPhno);
				
				String result = data.getStringExtra("dataFromTransaction");
				System.out.println("Customer result In Tran demo"+result);
				DBHelperCustomer dbCustomer = new DBHelperCustomer(
						getApplicationContext());
				Customer customer = dbCustomer.getCustomer(customerPhno);
				customer.setAmount(Double.parseDouble(result.substring(8)));
				customer.setTime(now);
				try{
				dbCustomer.updateCustomer(customer);
				}
				catch(Exception ex)
				{
					System.out.println(ex);
				}
			}

		}
	}
}
