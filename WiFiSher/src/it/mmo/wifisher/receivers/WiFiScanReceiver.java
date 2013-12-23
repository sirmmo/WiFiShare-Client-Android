package it.mmo.wifisher.receivers;

import it.mmo.wifisher.WiFiShareService;

import java.util.List;
import java.util.concurrent.locks.Condition;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

public class WiFiScanReceiver extends BroadcastReceiver {
	private static final String TAG = "WiFiScanReceiver";
	WiFiShareService  wifiDemo;
	String varname;
	SharedPreferences.Editor editor;
	Condition condition;

	public WiFiScanReceiver(WiFiShareService  wifiDemo, String variable, SharedPreferences.Editor editor) {
		super();
		this.wifiDemo = wifiDemo;
		this.varname = variable;
		this.editor = editor;
		this.condition = condition;
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
		editor.putString(varname, jsa.toString());
		editor.commit();
		condition.signal();
		//intent = new Intent(wifiDemo, WiFiShareService.class);
		

	}

}
