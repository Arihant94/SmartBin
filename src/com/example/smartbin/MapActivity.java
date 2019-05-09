package com.example.smartbin;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Stack;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.EditText;
import android.widget.Toast;

public class MapActivity extends FragmentActivity{

	static final int MAX=10, MaxQueue=8;
	EditText tv1;
	GoogleMap gmap;
	LatLng bin[];
	ArrayList<LatLng> routepoints= new ArrayList<LatLng>();
	ArrayList<Integer> pathlist, trackkeeper=new ArrayList<Integer>();
	public static double defaultBinPosition[][]={{12.970192, 79.154092},{12.968038, 79.155905},{12.967902, 79.149082},{12.968038, 79.153116},{12.968670, 79.140735},{12.955348, 79.139626},{12.927521, 79.133396},{12.926312, 79.140238},{12.924242, 79.137202},{12.924794, 79.135387},{12.923929, 79.134834},{12.921506, 79.128023},{12.870000, 79.088869}};
	int trcost=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.maplayout);
		
		SharedPreferences editor=getSharedPreferences("SmartBin",0);
		tv1=(EditText) findViewById(R.id.editText1);
		
		MapFragment mpfg=(MapFragment)getFragmentManager().findFragmentById(R.id.map);
		gmap=mpfg.getMap();
	
	
		int nBins=editor.getInt("NoOfBins", 13);
		bin=new LatLng[nBins];
		if(nBins==13)
			for(int i=0;i<nBins;i++)
			{
				String lat=Double.toString(defaultBinPosition[i][0]),lng=Double.toString(defaultBinPosition[i][1]);
				bin[i]=new LatLng(Double.parseDouble(editor.getString("Bin"+i+"lat",lat)),Double.parseDouble(editor.getString("Bin"+i+"lng",lng)));
			}		
		else
			for(int i=0;i<nBins;i++)
				bin[i]=new LatLng(Double.parseDouble(editor.getString("Bin"+i+"lat","0.0")),Double.parseDouble(editor.getString("Bin"+i+"lng","0.0")));
		
		
		LatLng origin=new LatLng(Double.parseDouble(editor.getString("originlat", "12.977569")),Double.parseDouble(editor.getString("originlng"," 79.156457")));
		gmap.addMarker(new MarkerOptions().position(origin).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).title("origin")); 
		LatLng destination=new LatLng(Double.parseDouble(editor.getString("deslat", "12.875560")),Double.parseDouble(editor.getString("deslng","79.140858")));//new LatLng(12.875560, 79.140858);
		gmap.addMarker(new MarkerOptions().position(destination).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).title("Destination")); 
		
		routepoints.add(origin);
		trackkeeper.add(0);
		boolean states[]=new boolean[nBins];
		
		for(int i=0;i<bin.length;i++)
		{
			states[i]=editor.getBoolean("bin"+i, false);
			MarkerOptions temp=new MarkerOptions().position(bin[i]).title("bin"+(i+1));
			if(!states[i])
				gmap.addMarker(temp.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
			else
			{	
				gmap.addMarker(temp.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));		
				routepoints.add(bin[i]);
				trackkeeper.add(i+1);
			}
		}
		routepoints.add(destination);
		trackkeeper.add(bin.length+1);
		
		CameraUpdate update=CameraUpdateFactory.newLatLngZoom(new LatLng(12.938598,79.136799),14);
		gmap.animateCamera(update);
				
		int n=routepoints.size();
		int dismat[][];
		
		DisMatrix dis=new DisMatrix(routepoints); 
		dismat =dis.calculateDistance();
		//tv1.append(dis.tv1);
		
		String show="";
		for(int i=0;i<n;i++)
		{
			for(int j=0;j<n;j++)
				show=show+dismat[i][j]+":";
			show=show+"; ";
		}
		
		//Zeroing the distance between Origin and Destination
		dismat[n-1][0]=0;
		
		TSP obj=new TSP();
		pathlist=obj.computeTSP(dismat,n);
		
		String str="";
		for(int i=0;i<n;i++)
			str=str+trackkeeper.get(pathlist.get(i))+",";
		
		tv1.append(show+str+":"+pathlist.size()+";"+pathlist.get(n));
		
		String ori=origin.latitude+","+origin.longitude;
		drawMutlipleLines(n,ori,destination); 
		tv1.append("\ntrCost="+trcost);
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
				PlotMap draw=new PlotMap(MapActivity.this,gmap,ori,des,path,tv1);
				boolean result=draw.execute().get();
				if(result)
				{
					ori=des;
					trcost+=draw.cost;
				}
			}
			catch (Exception e) {
				e.printStackTrace();
				tv1.append("had a problem");
			}
		}
	}

}

class PlotMap extends AsyncTask<Void, Integer, Boolean> {
	   
    private static final String TOAST_MSG = "Calculating";
    private static final String TOAST_ERR_MAJ = "Impossible to trace Itinerary";
   
    private Context context;
    private GoogleMap gMap; 
    private String editFrom;
    private String editTo;
    private String stopovers;
    private EditText tv1;
    int cost=0;
    
    private ArrayList<PolylineOptions> lstpolyline = new ArrayList<PolylineOptions>();  
    private PolylineOptions polyline;
   
    public PlotMap(final Context context, final GoogleMap gMap, final String editFrom, final String editTo, String stopovers,EditText tv1) {
        this.context = context;
        this.gMap= gMap;
        this.editFrom = editFrom;
        this.editTo = editTo;
        this.stopovers=stopovers;
        this.tv1=tv1;
    }
   
    private void decodePolylines(final String encodedPoints) {
        int index = 0;
        int lat = 0, lng = 0;
        ArrayList<LatLng> lstLatLng = new ArrayList<LatLng>();
        
        while (index < encodedPoints.length()) {
            int b, shift = 0, result = 0;
   
            do {
                b = encodedPoints.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
   
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;
   
            do {
                b = encodedPoints.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
   
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;
   
            lstLatLng.add(new LatLng((double)lat/1E5, (double)lng/1E5));
        }
        for(LatLng latLng1 : lstLatLng)
        {
            polyline.add(latLng1);
        }
    }
   
    @Override
    protected void onPostExecute(final Boolean result) { 
    	
    	int[] colour={Color.BLACK,Color.BLUE,Color.CYAN,Color.DKGRAY,Color.GREEN,Color.MAGENTA,Color.GRAY,Color.YELLOW,Color.RED};
    	int n=colour.length;
    	int number=lstpolyline.size();	
    	
    	if(!result) {
            Toast.makeText(context, TOAST_ERR_MAJ, Toast.LENGTH_SHORT).show();
        }
        else 
        {
            for(int i=0;i<number;i++) 
            {
	       		PolylineOptions ply=lstpolyline.get(i);
	           	ply.color(colour[i%n]);
	           	gMap.addPolyline(ply);
            }
        }
    }

	@Override
	protected Boolean doInBackground(Void... params) {
		 try {
	            final StringBuilder url = new StringBuilder("http://maps.googleapis.com/maps/api/directions/xml?");
	            url.append("&origin=");
	            url.append(editFrom);
	            url.append("&destination=");
	            url.append(editTo);
	            url.append("&waypoints=");
	            url.append(stopovers);
	           
	            //tv1.setText(tv1.getText().toString()+url.toString());
	            final InputStream stream = new URL(url.toString()).openStream();
	                
	            final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
	            documentBuilderFactory.setIgnoringComments(true);
	   
	            final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
	   
	            final Document document = documentBuilder.parse(stream);
	            document.getDocumentElement().normalize();
	   
	            final NodeList NodeParentList=document.getElementsByTagName("leg");
	            final int limit=NodeParentList.getLength();
	            for(int j=0;j<limit;j++)
	            {	
	   	            final Element elementLeg = (Element) document.getElementsByTagName("leg").item(j);
	   	            
	   	            //NodeList cc=elementLeg.getElementsByTagName("duration");
	   	            //Element elecc=(Element)cc.item(cc.getLength()-1);
	   	            //cost=cost+Integer.parseInt(elecc.getElementsByTagName("value").item(0).getTextContent());
		            
	   	            final NodeList nodeListStep = elementLeg.getElementsByTagName("step");
		            final int length = nodeListStep.getLength();
		            polyline=new PolylineOptions();
		            
		            for(int i=0; i<length; i++)
		            {        
		                final Node nodeStep = nodeListStep.item(i);
		   
		                if(nodeStep.getNodeType() == Node.ELEMENT_NODE) 
		                {
		                    final Element elementStep = (Element) nodeStep;
		                    decodePolylines(elementStep.getElementsByTagName("points").item(0).getTextContent());
		                }
		            }
		            lstpolyline.add(polyline);
	            }
		   
	            return true;          
	        }
	        catch(final Exception e) {
	            return false;
	        }
	}
  }	  
/*
 * 		bin[0]=new LatLng(12.970192, 79.154092);
		bin[1]=new LatLng(12.968038, 79.153116);
		bin[2]=new LatLng(12.967902, 79.149082);
		bin[3]=new LatLng(12.968038, 79.155905);
		bin[4]=new LatLng(12.968670, 79.140735);
		bin[5]=new LatLng(12.955348, 79.139626);
		bin[6]=new LatLng(12.926312, 79.140238);
		bin[7]=new LatLng(12.924242, 79.137202);
		bin[8]=new LatLng(12.924794, 79.135387);
		bin[9]=new LatLng(12.923929, 79.134834);
		bin[10]=new LatLng(12.921506, 79.128023);
		bin[11]=new LatLng(12.927521, 79.133396);
		bin[12]=new LatLng(12.870000, 79.088869);	
 */
