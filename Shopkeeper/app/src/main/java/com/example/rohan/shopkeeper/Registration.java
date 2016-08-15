package com.example.rohan.shopkeeper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.securetransfer.seller.Seller;


public class Registration extends Activity  {
	Seller sellerObj;
	DBHelperShopkeeper dbHelperShopkeeper;
	Shopkeeper self;
	int duration = Toast.LENGTH_SHORT;
	Activity activity;
	String regex = "[0-9]+";
	boolean flag = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		Button buttonnext = (Button) findViewById(R.id.button1);
		//buttonnext.setOnClickListener(onclickListener);
		dbHelperShopkeeper = new DBHelperShopkeeper(getApplicationContext());
		System.out.println("IN REGISTRATION");
		sellerObj = new Seller(this, getApplicationContext());
		activity = this;
		
		buttonnext.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
            	EditText name1 = (EditText) findViewById(R.id.editText1);
    			EditText phno = (EditText) findViewById(R.id.editText2);
    			EditText password = (EditText) findViewById(R.id.editText3);
    			EditText address = (EditText) findViewById(R.id.editText4);
    			Context context = getApplicationContext();
    			
    			Toast toast;
    			if ((password.getText().length() < 5)) {
    				CharSequence text = "Password should be atleast 5 characters long!";
    				toast = Toast.makeText(context, text, duration);
    				toast.setGravity(Gravity.BOTTOM | Gravity.LEFT, 400, 300);
    				toast.show();
    				flag = false;
    				finish();
    				startActivity(getIntent());
    				
    			}
    			String name = name1.getText().toString();
    			if (name.length() < 6 || name.length() >= 20 || !name.matches("^[a-zA-Z][A-Za-z0-9 ]+")) {
    				
    						String errorMessage = "Name should begin with an alphabet and have only 6 to 15 characters.";
    						 toast = Toast.makeText(getApplicationContext(), errorMessage,duration);
    						toast.setGravity(Gravity.BOTTOM | Gravity.LEFT, 400, 300);
    						toast.show();
    						flag = false;
    						Intent intent = new Intent(Registration.this,Registration.class);
    						startActivity(intent);
    			}
    			if((phno.getText().length()<10)||!((phno.getText().toString()).matches(regex)))
    			  {
    				  CharSequence text = "Invalid Phone number!";
    				  toast = Toast.makeText(context, text, duration);
    				  toast.setGravity(Gravity.TOP|Gravity.LEFT, 50, 100);
    				  toast.show();
    					flag = false;
    				  Intent i=new Intent(Registration.this,Registration.class);
    				  startActivity(i);
    			  }
    			if(flag){
    			System.out.println(name);
    			sellerObj.register(name, phno.getText()
    					.toString());
    			self = new Shopkeeper(name,
    					(phno.getText()).toString(),
    					(password.getText()).toString(),(address.getText()).toString());
    			Log.d("Added self",self.toString());
    			try {
    				dbHelperShopkeeper.addSelf(self);
    				SharedPreferences pref = getSharedPreferences("ShopkeeperPREF",
    						Context.MODE_PRIVATE);
    				Editor ed = pref.edit();
    				ed.putBoolean("activity_executed", true);
    				ed.commit();
    			

    				Intent i = new Intent(view.getContext(), Home.class);
    	
    				view.getContext().startActivity(i);
    			} catch (Exception e) {
    				String errorMessage = "Cannot update the database with registration details.";
    				toast = Toast.makeText(getApplicationContext(), errorMessage,
    						duration);
    				toast.setGravity(Gravity.BOTTOM | Gravity.LEFT, 400, 300);
    				toast.show();
    			}
            }
            }
        });
		
	}

	private OnClickListener onclickListener = new OnClickListener() {
		@Override
		public void onClick(final View view) {
			
			
		}
	};


}