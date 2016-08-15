package com.example.rohan.shopkeeper;

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


public class Login extends Activity {
	Shopkeeper self;
	DBHelperShopkeeper dbh;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		Button buttonnext = (Button) findViewById(R.id.button1);
		buttonnext.setOnClickListener(onclickListener);
		self = new Shopkeeper();
		dbh = new DBHelperShopkeeper(getApplicationContext());
	}

	private OnClickListener onclickListener = new OnClickListener() {
		@Override
		public void onClick(final View v) {
			EditText phno1 = (EditText) findViewById(R.id.editText1);
			EditText password1 = (EditText) findViewById(R.id.editText2);
			String phno = phno1.getText().toString();
			String password = password1.getText().toString();

			try {
				self = dbh.validate(phno);
				if (self == null) {
					Context context = getApplicationContext();
					CharSequence text = "Invalid Phone Number!Retry";
					int duration = Toast.LENGTH_SHORT;
					Toast toast = Toast.makeText(context, text, duration);
					toast.setGravity(Gravity.BOTTOM | Gravity.LEFT, 400, 300);
					toast.show();
					Intent i = new Intent(Login.this, Login.class);
					startActivity(i);
				} else {
					if (!password.equals(self.getPassword())) {
						Context context = getApplicationContext();
						CharSequence text = "Invalid Password!Retry";
						int duration = Toast.LENGTH_SHORT;
						Toast toast = Toast.makeText(context, text, duration);
						toast.setGravity(Gravity.BOTTOM | Gravity.LEFT, 400, 300);
						toast.show();
						Intent i = new Intent(Login.this, Login.class);
						startActivity(i);
					} else {
						Context context = getApplicationContext();
						CharSequence text = "Logged In!";
						int duration = Toast.LENGTH_SHORT;
						Toast toast = Toast.makeText(context, text, duration);
						toast.setGravity(Gravity.BOTTOM | Gravity.LEFT, 400, 300);
						toast.show();
						SharedPreferences pref = getSharedPreferences(
								"CustActivityPREF", Context.MODE_PRIVATE);
						Editor ed = pref.edit();
						ed.putString("customer_phno", self.getPhoneNumber());
						Intent i = new Intent(Login.this, Home.class);
						startActivity(i);
					}

				}
			} catch (Exception e) {
			}
		}
	};

}
