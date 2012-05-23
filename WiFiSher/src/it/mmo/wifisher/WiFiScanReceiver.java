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
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class WiFiScanReceiver extends BroadcastReceiver {
	private static final String TAG = "WiFiScanReceiver";
	WiFiSherActivity wifiDemo;

	public WiFiScanReceiver(WiFiSherActivity wifiDemo) {
		super();
		this.wifiDemo = wifiDemo;
	}

	@Override
	public void onReceive(Context c, Intent intent) {
		List<ScanResult> results = wifiDemo.wifi.getScanResults();
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

		// tv.setText(sb.toString());
		String message = String.format(
				"%s networks found. %s is the strongest.", results.size(),
				bestSignal.SSID);
		Toast.makeText(wifiDemo, message, Toast.LENGTH_LONG).show();

		wifiDemo.writeScan(jsa);
		Log.d(TAG, "onReceive() message: " + message);
	}

}
