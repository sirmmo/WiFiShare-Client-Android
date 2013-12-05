package it.mmo.wifisher;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

public class WiFiScanReceiver extends BroadcastReceiver {
	private static final String TAG = "WiFiScanReceiver";
	WiFiShareService  wifiDemo;
	WiFiShareServiceConnector  wifiConnDemo;

	public WiFiScanReceiver(WiFiShareService  wifiDemo) {
		super();
		this.wifiConnDemo = new WiFiShareServiceConnector(wifiDemo);
		this.wifiDemo = wifiConnDemo.getBoundService();
	}

	@Override
	public void onReceive(Context c, Intent intent) {
		List<ScanResult> results = wifiDemo.getWifiManager().getScanResults();
		ScanResult bestSignal = null;
		JSONArray jsa = new JSONArray();

		for (ScanResult result : results) {
			JSONObject obj = new JSONObject();
			try {
				obj.put("frequency", result.frequency);
				obj.put("level", result.level);
				obj.put("SSID", result.SSID);
				obj.put("BSSID", result.BSSID);
				obj.put("capabilities", result.capabilities);
				jsa.put(obj);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.d(TAG, result.toString());
			if (bestSignal == null
					|| WifiManager.compareSignalLevel(bestSignal.level,
							result.level) < 0)
				bestSignal = result;
		}
		// Give it some value as an example.
					Message msg = Message.obtain(null,
		            		WiFiShareService.MSG_SET_WIFI);
					Bundle b = new Bundle();
					b.putString("wifi", jsa.toString());
					msg.setData(b); 
					try {
						this.wifiConnDemo.mService.send(msg);
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	}

}
