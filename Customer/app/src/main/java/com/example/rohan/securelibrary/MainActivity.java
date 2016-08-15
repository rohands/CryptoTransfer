package com.example.rohan.securelibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class MainActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences pref = getSharedPreferences("DemoCustomer",
				Context.MODE_PRIVATE);
		if (pref.getBoolean("activity_executed", false)) {
			Intent intent = new Intent(this, Login.class);
			startActivity(intent);
			finish();
		} else {
			Intent intent = new Intent(this, Registration.class);
			startActivity(intent);
			finish();
		}
	}

}
