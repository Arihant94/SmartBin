package com.example.smartbin;

import com.google.android.gms.maps.model.LatLng;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button bt0=(Button) findViewById(R.id.button0);
		Button bt1=(Button) findViewById(R.id.button1);
		Button bt2=(Button) findViewById(R.id.button2);
		Button bt3=(Button) findViewById(R.id.button3);
		Button bt4=(Button) findViewById(R.id.button4);
		Button bt5=(Button) findViewById(R.id.button5);
		
		bt0.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent=new Intent(MainActivity.this,Intialize.class);
				startActivity(intent);	
			}			
		});
		
		bt1.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent=new Intent(MainActivity.this,MapActivity.class);
				startActivity(intent);	
			}			
		});
		
		bt2.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent=new Intent(MainActivity.this,SetBin.class);
				startActivity(intent);
			}			
		});
		
		bt3.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent=new Intent(MainActivity.this,NormalRoute.class);
				startActivity(intent);
			}			
		});
		bt4.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent=new Intent(MainActivity.this,AddBin.class);
				startActivity(intent);
			}			
		});
		bt5.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent=new Intent(MainActivity.this,BackendOp.class);
				startActivity(intent);
			}			
		});		
	
	}
}
