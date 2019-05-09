package com.example.smartbin;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.android.gms.maps.model.LatLng;

import android.os.AsyncTask;


public class DisMatrix
{
	static final int MAX=10;
	String tv1;
	ArrayList<LatLng> routepoints;
	
	DisMatrix(ArrayList<LatLng> list)
	{
		this.routepoints=list;
	}
		
	public int[][] calculateDistance()
	{
		int n=routepoints.size();	 	
		int dismat[][]=new int[n][n];		
	 	//int i=0;
		 	for(int i=0;i<n;i=i+MAX)
		 	{
		 		int rowno=0;
		 		String origin="";
		 		for(int k=i;k<n & k<MAX+i;k++)
		 		{
		 			origin=origin+"|"+routepoints.get(k).latitude+","+routepoints.get(k).longitude;
		 			rowno++;
		 		}
		 		origin=origin.substring(1);
		 		
		 		for(int j=i;j<n;j=j+MAX)
		 		{
		 			int columnno=0;
			 		String destination="";
			 		for(int k=j;k<n & k<MAX+j;k++)
			 		{
			 			destination=destination+"|"+routepoints.get(k).latitude+","+routepoints.get(k).longitude;
			 			columnno++;
			 		}
			 		destination=destination.substring(1);	
			 			
			 		StringBuilder url = new StringBuilder("http://maps.googleapis.com/maps/api/distancematrix/xml?");
		            url.append("&origins="+origin+"&destinations="+destination);
		            	
		            boolean res=false;
		            CalculateDistanceMatrix obj=new CalculateDistanceMatrix(dismat,url.toString(),i,j,rowno,columnno);
		            try {
						
						while(!res)
						{
							obj=new CalculateDistanceMatrix(dismat,url.toString(),i,j,rowno,columnno);
				            res=obj.execute().get(3, TimeUnit.SECONDS);
						}
						if(res)
						{
							dismat=obj.dismat;
							tv1+=("done");
						}
						else
							tv1+=("failed"+i+","+j);
						
					}
		            catch(Exception e)
		            {
						e.printStackTrace();
		            }
	            }
	 		}
		 	for(int i=0;i<n;i++)
		 	{
		 		for(int j=0;j<i;j++)
		 		{
	 				dismat[i][j]=dismat[j][i];
		 		}
		 	}
		return dismat;
	}
}

class CalculateDistanceMatrix extends AsyncTask<Void, Integer,Boolean>
{
	int[][] dismat;
	String url;
	int startr,startc,limitr,limitc;
	//EditText tv1;
	
	
	CalculateDistanceMatrix(int[][] dismat,String url,int startr,int startc, int limitr ,int limitc)
	{
		this.dismat=dismat;
		this.url=url;
		this.startr=startr;
		this.startc=startc;
		this.limitc=limitc;
		this.limitr=limitr;
	}
	
	
	@Override
	protected Boolean doInBackground(Void... params) {
		String error="";
		try
		{
			 InputStream stream = new URL(url.toString()).openStream();
			   
	         DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
	         documentBuilderFactory.setIgnoringComments(true);
	
	         DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
	
	         Document document = documentBuilder.parse(stream);
	         document.getDocumentElement().normalize();
	         
	         error="bl";
	         for(int m=0;m<limitr;m++)
	          {
			        Element elementRow = (Element) document.getElementsByTagName("row").item(m);
			        NodeList nodeListElement = elementRow.getElementsByTagName("element");
			        error+=("in"+m);
			        for(int l=0; l<limitc; l++)
			        {        
			        	Node nodeStep = nodeListElement.item(l);
			        	Element elementStep = (Element) ((Element) nodeStep).getElementsByTagName("duration").item(0);
			            String time=elementStep.getElementsByTagName("value").item(0).getTextContent();
			            dismat[m+startr][l+startc]=Integer.valueOf(time);	
		        	}
	          }
			  return true; 	//in Backgrd operation 
       	}
       catch(Exception E)
       {
    	   //tv1.append("operaton failed"+limitr+","+limitc+error);
    	   return false;
       }
	}
	
}
