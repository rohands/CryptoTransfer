package com.example.rohan.securelibrary;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.securetransfer.buyer.BuyerTransaction;


public class RenewBalance extends Activity {

	String phoneNumber = "";
	CallbackImplementor callbackObject;
	DBHelperShopkeeper dbhShop;
	Toast toast;
	int duration = Toast.LENGTH_LONG;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.display);
		Intent intent = getIntent();
		Shopkeeper shopGuy = (Shopkeeper) intent
				.getSerializableExtra("ShopkeeperObject");
		callbackObject = new CallbackImplementor();
		System.out.println("In renewBalance: " + shopGuy.toString());
		phoneNumber = shopGuy.getPhoneNumber();
		TableLayout tblLayout = (TableLayout) findViewById(R.id.tableLayout1);
		TableRow rowOne = (TableRow) tblLayout.getChildAt(1);
		TextView columnName = (TextView) rowOne.getChildAt(1);
		columnName.setText(shopGuy.getName());
		TableRow rowTwo = (TableRow) tblLayout.getChildAt(2);
		TextView columnPhone = (TextView) rowTwo.getChildAt(1);
		columnPhone.setText(shopGuy.getPhoneNumber());
		TableRow rowThree = (TableRow) tblLayout.getChildAt(3);
		TextView columnAmount = (TextView) rowThree.getChildAt(1);
		columnAmount.setText(shopGuy.getAmount().toString());
		TableRow rowFour = (TableRow) tblLayout.getChildAt(4);
		TextView columnAddress = (TextView) rowFour.getChildAt(1);
		columnAddress.setText(shopGuy.getAddress());
		TableRow rowFive = (TableRow) tblLayout.getChildAt(5);
		TextView columnTime = (TextView) rowFive.getChildAt(1);
		columnTime.setText(shopGuy.getTimestamp());
		TextView textview = (TextView) findViewById(R.id.DisplayField);
		textview.setText("Enter Amount:");
		Button button1 = (Button) findViewById(R.id.button1);
		button1.setText("Renew");
		button1.setOnClickListener(onclickListener1);
	}

	private OnClickListener onclickListener1 = new OnClickListener() {
		@Override
		public void onClick(final View v) {
			EditText edit = (EditText) findViewById(R.id.editText1);
			String data = edit.getText().toString();
			System.out.println("DATA ENTERED: " + data);
			dbhShop = new DBHelperShopkeeper(getApplicationContext());
			Shopkeeper shopGuy = dbhShop.getShopkeeper(phoneNumber);
			Double balance = shopGuy.getAmount();
			Double amount = new Double(data);
			if (amount < 0) {
				String errorMessage = "Invalid Amount! Retry.";
				toast = Toast.makeText(getApplicationContext(), errorMessage,
						duration);
				toast.setGravity(Gravity.BOTTOM | Gravity.LEFT, 400, 300);
				toast.show();
				finish();
				startActivity(getIntent());
			}
			balance = (balance + amount) >= 0 ? (balance + amount) : 0;
			data = "balance=" + balance.toString();
			Intent intent = new Intent(RenewBalance.this,
					BuyerTransaction.class);
			intent.putExtra("callbackObject", callbackObject);
			intent.putExtra("phoneNumber", phoneNumber);
			intent.putExtra("data", data);
			startActivityForResult(intent, 1002);
		}
	};

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1002) {
			Log.d("DEBUG", "On getting introduction result");
			if (resultCode == Activity.RESULT_OK) {
				String returnString = data
						.getStringExtra("dataFromTransaction");
				Log.d("DEBUG", "Transaction complete!");
				Shopkeeper shopGuy = dbhShop.getShopkeeper(phoneNumber);
				String splitString[] = returnString.split("=");
				if (splitString[0].equals("balance")) {
					shopGuy.setAmount(new Double(splitString[1]));
					SimpleDateFormat dateFormat = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss", Locale.getDefault());
					Date date = new Date();
					shopGuy.setTimestamp(dateFormat.format(date));
					dbhShop.updateBalance(shopGuy);
				}
				Intent intent = new Intent(RenewBalance.this, Home.class);
				finishActivity(requestCode);
				finish();
				startActivity(intent);

			}

		}
	}
}
