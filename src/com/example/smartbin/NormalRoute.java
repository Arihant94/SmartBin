package com.example.smartbin;

import java.util.ArrayList;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.EditText;

public class NormalRoute extends FragmentActivity{

	static int MaxQueue=8;
	EditText tv1;
	GoogleMap gmap;
	ArrayList<LatLng> routepoints= new ArrayList<LatLng>();
	ArrayList<Integer>pathlist,trackkeeper=new ArrayList<Integer>();
	public static double defaultBinPosition[][]={{12.970192, 79.154092},{12.968038, 79.155905},{12.967902, 79.149082},{12.968038, 79.153116},{12.968670, 79.140735},{12.955348, 79.139626},{12.927521, 79.133396},{12.926312, 79.140238},{12.924242, 79.137202},{12.924794, 79.135387},{12.923929, 79.134834},{12.921506, 79.128023},{12.870000, 79.088869}};
	int cost=0;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.maplayout);
		
		SharedPreferences editor=getSharedPreferences("SmartBin",0);
		tv1=(EditText) findViewById(R.id.editText1);
		
		MapFragment mpfg=(MapFragment)getFragmentManager().findFragmentById(R.id.map);
		gmap=mpfg.getMap();
		
		int nBins=editor.getInt("NoOfBins", 13);
		LatLng bin[]=new LatLng[nBins];
		
		LatLng origin=new LatLng(Double.parseDouble(editor.getString("originlat", "12.977569")),Double.parseDouble(editor.getString("originlng"," 79.156457")));
		gmap.addMarker(new MarkerOptions().position(origin).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).title("origin")); 
		LatLng destination=new LatLng(Double.parseDouble(editor.getString("deslat", "12.875560")),Double.parseDouble(editor.getString("deslng","79.140858")));//new LatLng(12.875560, 79.140858);
		gmap.addMarker(new MarkerOptions().position(destination).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).title("Destination")); 
		
		routepoints.add(origin);
		trackkeeper.add(0);
		if(nBins==13)
			for(int i=0;i<nBins;i++)
			{
				String lat=Double.toString(defaultBinPosition[i][0]),lng=Double.toString(defaultBinPosition[i][1]);
				bin[i]=new LatLng(Double.parseDouble(editor.getString("Bin"+i+"lat",lat)),Double.parseDouble(editor.getString("Bin"+i+"lng",lng)));
				routepoints.add(bin[i]);
				trackkeeper.add(i+1);
			}		
		else
			for(int i=0;i<nBins;i++)
			{	
				bin[i]=new LatLng(Double.parseDouble(editor.getString("Bin"+i+"lat","0.0")),Double.parseDouble(editor.getString("Bin"+i+"lng","0.0")));
				routepoints.add(bin[i]);
				trackkeeper.add(i+1);
			}
		routepoints.add(destination);	
		trackkeeper.add(bin.length+1);
		
		int n=routepoints.size();
		int dismat[][];
		
		DisMatrix dis=new DisMatrix(routepoints); 
		dismat =dis.calculateDistance();
		//tv1.append("ok"+dis.tv1);
		dismat[n-1][0]=0;
		
		TSP obj=new TSP();
		pathlist=obj.computeTSP(dismat,n);
		
		for(int i=0;i<bin.length;i++)
		{
			boolean states=editor.getBoolean("bin"+i, false);
			MarkerOptions temp=new MarkerOptions().position(bin[i]).title("bin"+(i+1));
			if(!states)
			{	
				gmap.addMarker(temp.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
				//routepoints.remove(i+1-deleteCount);
				//trackkeeper.remove(i+1-deleteCount);
				int p=pathlist.indexOf(i+1);
				pathlist.remove(p);
			}			
			else
			{	
				gmap.addMarker(temp.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));		
			}
		}
		

		CameraUpdate update=CameraUpdateFactory.newLatLngZoom(new LatLng(12.938598,79.136799),14);
		gmap.animateCamera(update);
		
		n=pathlist.size();
		String ori=origin.latitude+","+origin.longitude;
		
		//for(int i=0;i<n;i++)
		//tv1.append(routepoints.get(i).latitude+":"+routepoints.get(i).longitude+"\n");
		String str="";
		for(int i=0;i<n;i++)
			str=str+pathlist.get(i)+",";
		
		int cost=0;
		for(int i=0;i<n-2;i++)
			cost+=dismat[trackkeeper.get(pathlist.get(i))][trackkeeper.get(pathlist.get(i+1))];
		
		tv1.append(str+"\nCost="+cost);
		drawMutlipleLines(n,ori,destination);
		//tv1.append("\nTrCost="+cost);
	}
	
private void drawMutlipleLines(int n,String ori,LatLng destination) {
		
		int j=1;
		while(j<n)
		{
			String path="";
			String str="",des="";

			for(int i=j;i<n-1 & i<j+MaxQueue;i++)
			{
				path=path+"|"+routepoints.get(pathlist.get(i)).latitude+","+routepoints.get(pathlist.get(i)).longitude;
				str=str+(trackkeeper.get(pathlist.get(i)))+",";
			}
			j+=MaxQueue;
			
			if(j<n-1)	
				des=routepoints.get(pathlist.get(j)).latitude+","+routepoints.get(pathlist.get(j)).longitude;
			else
				des=destination.latitude+","+destination.longitude;
			j++;
			
			if(!path.isEmpty())
				path.substring(1);
			
			try {
				//tv1.append(str);
				PlotMap draw=new PlotMap(NormalRoute.this,gmap,ori,des,path,tv1);
				boolean result=draw.execute().get();
				if(result)
				{
					ori=des;
					//cost+=draw.cost;
				}
			}
			catch (Exception e) {
				e.printStackTrace();
				tv1.append("had a problem");
			}
		}
	}
}
