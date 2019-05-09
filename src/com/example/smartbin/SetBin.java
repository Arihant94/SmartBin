package com.example.smartbin;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

public class SetBin extends ActionBarActivity{
	
	ToggleButton bt[]=new ToggleButton[15];
	TextView tv[]=new TextView[3];
	int nBins;
	String arg[]={"237237","267822","267827"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setbin_layout);
				
		//bt[1]=(ToggleButton) findViewById(R.id.toggleButton2);
		//bt[2]=(ToggleButton) findViewById(R.id.toggleButton3);
		bt[3]=(ToggleButton) findViewById(R.id.toggleButton4);
		bt[4]=(ToggleButton) findViewById(R.id.toggleButton5);
		bt[5]=(ToggleButton) findViewById(R.id.toggleButton6);
		bt[6]=(ToggleButton) findViewById(R.id.toggleButton7);
		bt[7]=(ToggleButton) findViewById(R.id.toggleButton8);
		bt[8]=(ToggleButton) findViewById(R.id.toggleButton9);
		bt[9]=(ToggleButton) findViewById(R.id.toggleButton10);
		bt[10]=(ToggleButton) findViewById(R.id.toggleButton11);
		bt[11]=(ToggleButton) findViewById(R.id.toggleButton12);
		bt[12]=(ToggleButton) findViewById(R.id.toggleButton13);
		bt[13]=(ToggleButton) findViewById(R.id.toggleButton14);
		bt[14]=(ToggleButton) findViewById(R.id.toggleButton15);
		LinearLayout ll[]=new LinearLayout[2];
		ll[0]=(LinearLayout) findViewById(R.id.ll14);
		ll[1]=(LinearLayout) findViewById(R.id.ll15);
		
		SharedPreferences editor=getSharedPreferences("SmartBin",0);
		nBins=editor.getInt("NoOfBins", 13);
		
		for(int i=13;i<nBins;i++)
		{
			ll[i-13].setVisibility(android.view.View.VISIBLE);
		}
		
		
		tv[0]=(TextView) findViewById(R.id.textviews1); 
		tv[1]=(TextView) findViewById(R.id.textviews2);
		tv[2]=(TextView) findViewById(R.id.textviews3);
		
		for(int i=3;i<nBins;i++)
		{
			bt[i].setChecked((editor.getBoolean("bin"+i, true))?true:false);
		}
		for(int i=0;i<3;i++)
		{
			ThingSpeak thingSpeak=new ThingSpeak(arg[i]);
			Boolean res;
			try
			{
				res = thingSpeak.execute().get();
				if(res)
				{
					String display="...1";
					if(thingSpeak.status==0)
						display="Full";
					else if(thingSpeak.status==1)
						display="Empty";
					
					tv[i].setText(display);
				}
				else
					tv[i].setText("failed");
			}
			catch (InterruptedException e) {
				tv[i].setText("failed1");
				e.printStackTrace();
			}
			catch (ExecutionException e) {
				tv[i].setText("failed2");
				e.printStackTrace();
			}
		}
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
		SharedPreferences binstate=getSharedPreferences("SmartBin", 0);
		SharedPreferences.Editor editor=binstate.edit();
		
		for(int i=0;i<3;i++)
		{
			String temp=tv[i].getText().toString();
			if(temp.equals("Full"))
				editor.putBoolean("bin"+i,true);
			else if(temp.equals("Empty"))
				editor.putBoolean("bin"+i,false);
		}
		for(int i=3;i<nBins;i++)
		{
			editor.putBoolean("bin"+i, bt[i].isChecked());
		}
		editor.commit();
	}	
}

class ThingSpeak extends AsyncTask<Void, Integer,Boolean>
{

	int status;
	String chId;
	
	ThingSpeak(String No)
	{
		chId=No;
	}
	@Override
	protected Boolean doInBackground(Void... params) {
		
			String s="http://api.thingspeak.com/channels/"+chId+"/feeds.xml?results=1";
						
			try
			{
			InputStream stream = new URL(s).openStream();
			
			   
	        DocumentBuilderFactory dBF = DocumentBuilderFactory.newInstance();
	        dBF.setIgnoringComments(true);
	
	        DocumentBuilder documentBuilder = dBF.newDocumentBuilder();
				
	        Document document = documentBuilder.parse(stream);
	        document.getDocumentElement().normalize();
	
	            
	        NodeList nl1=document.getElementsByTagName("feeds");
	        Element temp1 = (Element) nl1.item(0);
	        Element temp2=(Element)((Element) temp1).getElementsByTagName("field1").item(0);            
	        String text=temp2.getTextContent();
	        status=text.charAt(0)-'0';
	        return true;
		}
		catch (ParserConfigurationException e) {
			e.printStackTrace();
			status=-1;
			return false;
		}
		catch (MalformedURLException e1) {
			e1.printStackTrace();
			status=-2;
			return false;
		} catch (IOException e2) {
			e2.printStackTrace();
			status=-3;
			return false;
		} catch (SAXException e) {
			e.printStackTrace();
			status=-4;
			return false;
		}
    }	
}

