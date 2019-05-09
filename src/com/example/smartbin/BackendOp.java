package com.example.smartbin;

import com.google.android.gms.maps.model.LatLng;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.EditText;

public class BackendOp extends ActionBarActivity{
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.backend_op);
		
		EditText tv=(EditText) findViewById(R.id.editText1);
		SharedPreferences editor=getSharedPreferences("SmartBin",0);
		int nBins=editor.getInt("NoOfBins", 1);

		LatLng bin[]=new LatLng[nBins];
		String str="";
		str="ori:"+editor.getString("originlat", "0.0")+","+editor.getString("originlng", "0.0")+";\n";
		str+="des:"+editor.getString("deslat", "0.0")+","+editor.getString("deslng", "0.0")+";\n";
				
		for(int i=0;i<nBins;i++)
		{
			bin[i]=new LatLng(Double.parseDouble(editor.getString("Bin"+i+"lat","0.0")),Double.parseDouble(editor.getString("Bin"+i+"lng","0.0")));tv.append(str);
			str+=(i+":"+bin[i].latitude+":"+bin[i].longitude+":"+editor.getBoolean("bin"+i, false)+":"+";\n");
		}
		tv.setText(nBins+"\n"+str);
	}

}
