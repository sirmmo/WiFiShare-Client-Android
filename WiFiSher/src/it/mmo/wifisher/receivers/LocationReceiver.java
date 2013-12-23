package it.mmo.wifisher.receivers;

import it.mmo.wifisher.WiFiShareService;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Messenger;

public class LocationReceiver implements LocationListener{

	WiFiShareService  wifiDemo;
	String varname;
	SharedPreferences.Editor editor;
	Messenger service;
	
	
	
	public LocationReceiver(WiFiShareService wiFiShareService, String variable, SharedPreferences.Editor editor) {
		this.wifiDemo = wiFiShareService;
		this.varname = variable;
		this.editor = editor;
	}

	@Override
	public void onLocationChanged(Location arg0) {
		JSONObject jso = new JSONObject();
		try {
			jso.put("lon", arg0.getLongitude());
			jso.put("lat", arg0.getLatitude());
			jso.put("alt", arg0.getAltitude());
			jso.put("time", arg0.getTime());
			jso.put("acc", arg0.getAccuracy());
			jso.put("bearing", arg0.getBearing());
			jso.put("speed", arg0.getSpeed());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		editor.putString(varname, jso.toString());
		editor.commit();
	}

	@Override
	public void onProviderDisabled(String arg0) {

	}

	@Override
	public void onProviderEnabled(String arg0) {

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {

	}

}
