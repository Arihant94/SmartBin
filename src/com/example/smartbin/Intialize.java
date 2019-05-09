package com.example.smartbin;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.EditText;
import android.widget.LinearLayout;

public class Intialize extends ActionBarActivity {
	
	int nBins=13;
	EditText tv[],ori[],des[];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.intialize_layout);
		
		SharedPreferences editor=getSharedPreferences("SmartBin",0);
		nBins=editor.getInt("NoOfBins", 13);
		ori=new EditText[2];
		ori[0]=(EditText) findViewById(R.id.e0lat);
		ori[1]=(EditText) findViewById(R.id.e0lng);
		
		des=new EditText[2];
		des[0]=(EditText) findViewById(R.id.edlat);
		des[1]=(EditText) findViewById(R.id.edlng);
		
		tv=new EditText[30];
		tv[0]=(EditText) findViewById(R.id.e1lat);
		tv[1]=(EditText) findViewById(R.id.e1lng);
		tv[2]=(EditText) findViewById(R.id.e2lat);
		tv[3]=(EditText) findViewById(R.id.e2lng);
		tv[4]=(EditText) findViewById(R.id.e3lat);
		tv[5]=(EditText) findViewById(R.id.e3lng);
		tv[6]=(EditText) findViewById(R.id.e4lat);
		tv[7]=(EditText) findViewById(R.id.e4lng);
		tv[8]=(EditText) findViewById(R.id.e5lat);
		tv[9]=(EditText) findViewById(R.id.e5lng);
		tv[10]=(EditText) findViewById(R.id.e6lat);
		tv[11]=(EditText) findViewById(R.id.e6lng);
		tv[12]=(EditText) findViewById(R.id.e7lat);
		tv[13]=(EditText) findViewById(R.id.e7lng);
		tv[14]=(EditText) findViewById(R.id.e8lat);
		tv[15]=(EditText) findViewById(R.id.e8lng);
		tv[16]=(EditText) findViewById(R.id.e9lat);
		tv[17]=(EditText) findViewById(R.id.e9lng);
		tv[18]=(EditText) findViewById(R.id.e10lat);
		tv[19]=(EditText) findViewById(R.id.e10lng);
		tv[20]=(EditText) findViewById(R.id.e11lat);
		tv[21]=(EditText) findViewById(R.id.e11lng);
		tv[22]=(EditText) findViewById(R.id.e12lat);
		tv[23]=(EditText) findViewById(R.id.e12lng);
		tv[24]=(EditText) findViewById(R.id.e13lat);
		tv[25]=(EditText) findViewById(R.id.e13lng);
		tv[26]=(EditText) findViewById(R.id.e14lat);
		tv[27]=(EditText) findViewById(R.id.e14lng);
		tv[28]=(EditText) findViewById(R.id.e15lat);
		tv[29]=(EditText) findViewById(R.id.e15lng);
		
		LinearLayout ll[]=new LinearLayout[4];
		ll[0]=(LinearLayout) findViewById(R.id.ll14lat);
		ll[1]=(LinearLayout) findViewById(R.id.ll14lng);
		ll[2]=(LinearLayout) findViewById(R.id.ll15lat);
		ll[3]=(LinearLayout) findViewById(R.id.ll15lng);
		
		for(int i=13;i<nBins;i++)
		{
			ll[2*(i-13)].setVisibility(android.view.View.VISIBLE);
			ll[2*(i-13)+1].setVisibility(android.view.View.VISIBLE);
		}
		
		ori[0].setText(editor.getString("originlat", ori[0].getText().toString()));
		ori[1].setText(editor.getString("originlng", ori[1].getText().toString()));
		des[0].setText(editor.getString("deslat", des[0].getText().toString()));
		des[1].setText(editor.getString("deslng", des[1].getText().toString()));
		
		for(int i=0;i<nBins;i++)
		{
			tv[(2*i)].setText(editor.getString("Bin"+i+"lat", tv[(2*i)].getText().toString()));
			tv[((2*i)+1)].setText(editor.getString("Bin"+i+"lng", tv[((2*i)+1)].getText().toString()));
		}
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
		SharedPreferences binstate=getSharedPreferences("SmartBin", 0);
		SharedPreferences.Editor editor=binstate.edit();
		editor.putInt("NoOfBins", nBins);
		
		editor.putString("originlat",ori[0].getText().toString());
		editor.putString("originlng",ori[1].getText().toString());
		
		editor.putString("deslat",des[0].getText().toString());
		editor.putString("deslng",des[1].getText().toString());
		
		for(int i=0;i<nBins;i++)
		{
			editor.putString("Bin"+i+"lat",tv[(2*i)].getText().toString());
			editor.putString("Bin"+i+"lng", tv[((2*i)+1)].getText().toString());
		}
		editor.commit();
	}
}
