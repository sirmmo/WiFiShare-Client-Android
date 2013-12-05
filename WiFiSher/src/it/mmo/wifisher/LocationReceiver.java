package it.mmo.wifisher;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class LocationReceiver implements LocationListener{

	WiFiShareService service;
	
	public LocationReceiver(WiFiShareService wiFiShareService) {
		service = new WiFiShareServiceConnector(wiFiShareService).getBoundService();
	}

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}

}
