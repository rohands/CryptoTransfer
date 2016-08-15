package com.example.rohan.securelibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.securetransfer.buyer.Buyer;


public class Registration extends Activity {
	Buyer buyerObj;
	DBHelperCustomer dbHelperCustomer;
	Customer self;
	Boolean flag;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		flag = true;
		dbHelperCustomer = new DBHelperCustomer(getApplicationContext());
		System.out.println("In the on create method of registration");
		buyerObj = new Buyer(this, getApplicationContext());
		Button buttonnext = (Button) findViewById(R.id.button1);
		buttonnext.setOnClickListener(onclickListener);
	}

	private OnClickListener onclickListener = new OnClickListener() {
		@Override
		public void onClick(final View v) {
			EditText name1 = (EditText) findViewById(R.id.editText1);
			EditText phno = (EditText) findViewById(R.id.editText2);
			EditText password = (EditText) findViewById(R.id.editText3);
			Context context = getApplicationContext();
			int duration = Toast.LENGTH_SHORT;
			Toast toast;
			if ((password.getText().length() < 5)) {
				flag = false;
				CharSequence text = "Password should be atleast 5 characters long!";
				toast = Toast.makeText(context, text, duration);
				toast.setGravity(Gravity.BOTTOM | Gravity.LEFT, 400, 300);
				toast.show();
				finish();
				Intent intent = new Intent(Registration.this,
						Registration.class);
				startActivity(intent);
			}
			String name = name1.getText().toString();
			if (name.length() < 6 || name.length() >= 20
					|| !name.matches("^[a-zA-Z][A-Za-z0-9 ]+")) {
				flag = false;
				String errorMessage = "Name should begin with an alphabet and have only 6 to 15 characters.";
				toast = Toast.makeText(getApplicationContext(), errorMessage,
						duration);
				toast.setGravity(Gravity.BOTTOM | Gravity.LEFT, 400, 300);
				toast.show();

				Intent intent = new Intent(Registration.this,
						Registration.class);
				startActivity(intent);
			}
			String regex = "[0-9]+";
			if ((phno.getText().length() < 10)
					|| !((phno.getText().toString()).matches(regex))) {
				flag = false;
				CharSequence text = "Invalid Phone number!";
				toast = Toast.makeText(context, text, duration);
				toast.setGravity(Gravity.TOP | Gravity.LEFT, 50, 100);
				toast.show();
				Intent i = new Intent(Registration.this, Registration.class);
				startActivity(i);
			}
			if (flag) {
				buyerObj.register(name, phno.getText().toString());
				self = new Customer(name, (phno.getText()).toString(),
						(password.getText()).toString());
				try {
					dbHelperCustomer.addSelf(self);
					SharedPreferences pref = getSharedPreferences(
							"DemoCustomer", Context.MODE_PRIVATE);
					Editor ed = pref.edit();
					ed.putBoolean("activity_executed", true);
					ed.commit();
					Intent i = new Intent(Registration.this, Home.class);
					startActivity(i);
				} catch (Exception e) {
					String errorMessage = "Cannot update the database with registration details.";
					toast = Toast.makeText(getApplicationContext(),
							errorMessage, duration);
					toast.setGravity(Gravity.BOTTOM | Gravity.LEFT, 400, 300);
					toast.show();
				}
			} else {
				Intent i = new Intent(Registration.this, Registration.class);
				startActivity(i);
			}
		}
	};
}
