package it.mmo.wifisher;


import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class WiFiSherActivity extends Activity implements OnClickListener, LocationListener  {
	private static final String TAG = "WiFiDemo";
	WifiManager wifi;
	BroadcastReceiver receiver;

	TextView textStatus;
	Button buttonScan;
	TextView scanStatus;
	LocationManager locationManager;
	
	Location where_now;
	
	
	String locationProvider = LocationManager.GPS_PROVIDER;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Setup UI
		textStatus = (TextView) findViewById(R.id.textStatus);
		scanStatus = (TextView) findViewById(R.id.TV2);
		buttonScan = (Button) findViewById(R.id.buttonScan);
		buttonScan.setOnClickListener(this);

		// Setup WiFi
		wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		where_now = locationManager.getLastKnownLocation(locationProvider);
		
		// Get WiFi status
		WifiInfo info = wifi.getConnectionInfo();
		textStatus.append("\n\nWiFi Status: " + info.toString());

		// List available networks
		List<WifiConfiguration> configs = wifi.getConfiguredNetworks();
		for (WifiConfiguration config : configs) {
			textStatus.append("\n\n" + config.toString());
		}

		// Register Broadcast Receiver
		if (receiver == null)
			receiver = new WiFiScanReceiver(this);

		registerReceiver(receiver, new IntentFilter(
				WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		Log.d(TAG, "onCreate()");
		
		locationManager.requestLocationUpdates(locationProvider, 0, 0, this);
	}

	@Override
	public void onStop() {
		unregisterReceiver(receiver);
		locationManager.removeUpdates(this);
	}

	@Override
	public void onClick(View view) {
		Toast.makeText(this, "On Click Clicked. Toast to that!!!",
				Toast.LENGTH_LONG).show();

		if (view.getId() == R.id.buttonScan) {
			Log.d(TAG, "onClick() wifi.startScan()");
			wifi.startScan();
		}
	}
	
	public void writeScan(String text){
		JSONObject obj = new JSONObject();
		try{
			JSONObject loc = new JSONObject();
			loc.put("lat", where_now.getLatitude());
			loc.put("lon", where_now.getLongitude());
			loc.put("speed", where_now.getSpeed());
			loc.put("accuracy", where_now.getAccuracy());
			loc.put("altitude", where_now.getAltitude());
			loc.put("time", where_now.getTime());
			
			obj.put("location", loc);
			
			
			obj.put("scan_data", text);
		
		} catch (JSONException e) {
			e.printStackTrace();
		}
		textStatus.append(obj.toString());
	}

	@Override
	public void onLocationChanged(Location arg0) {
		where_now = arg0;
		
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