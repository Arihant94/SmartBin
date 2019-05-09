package com.example.smartbin;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.google.android.gms.maps.model.LatLng;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AddBin extends ActionBarActivity{
	
	public static double defaultBinPosition[][]={{12.970192, 79.154092},{12.968038, 79.153116},{12.967902, 79.149082},{12.968038, 79.155905},{12.968670, 79.140735},{12.955348, 79.139626},{12.926312, 79.140238},{12.924242, 79.137202},{12.924794, 79.135387},{12.923929, 79.134834},{12.921506, 79.128023},{12.927521, 79.133396},{12.870000, 79.088869}};
	ArrayList<LatLng> routepoints= new ArrayList<LatLng>();
	ArrayList<Integer> pathlist=new ArrayList<Integer>();
	static final int MAX=10, MaxQueue=8;
	int nBins;
	EditText tv1,tv2,tv;
	LatLng newBin;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_bin);
		
		tv1=(EditText) findViewById(R.id.ad2);
		tv2=(EditText) findViewById(R.id.ad4);
		Button bt=(Button) findViewById(R.id.ad5);
		//tv=(EditText) findViewById(R.id.ad6);
		
		bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {								
				
				SharedPreferences editor=getSharedPreferences("SmartBin",0);
				nBins=editor.getInt("NoOfBins", 13);
				nBins++;
				}
		});
		
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
		SharedPreferences binstate=getSharedPreferences("SmartBin", 0);
		SharedPreferences.Editor editor=binstate.edit();
		if(!(tv1.getText().toString().isEmpty() || tv2.getText().toString().isEmpty()))
		{
			newBin=new LatLng(Double.parseDouble(tv1.getText().toString()),Double.parseDouble(tv2.getText().toString()));
			editor.putString("Bin"+(nBins-1)+"lat", Double.toString(newBin.latitude));
			editor.putString("Bin"+(nBins-1)+"lng", Double.toString(newBin.longitude));
			editor.putInt("NoOfBins", nBins);
		}
		editor.commit();
	}
}
