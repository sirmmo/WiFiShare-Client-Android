package it.mmo.wifisher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import android.os.Messenger;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

public class WiFiShareService extends Service {
	static final int MSG_REGISTER_CLIENT = 1;
	static final int MSG_UNREGISTER_CLIENT = 2;
	static final int MSG_SET_SCANNER = 3;
	static final int MSG_SET_POSITION = 4;
	static final int MSG_SET_WIFI = 5;

	AQuery a = new AQuery(this);

	ArrayList<Messenger> mClients = new ArrayList<Messenger>();
	private Bundle mValue;

	final Messenger mMessenger = new Messenger(new IncomingHandler());

	private final IBinder mBinder = new LocalBinder();

	private WifiManager wifi;

	private LocationManager locationManager;
	private String locationProvider = LocationManager.GPS_PROVIDER;
	private WiFiScanReceiver receiver;

	private Location where_now;
	private JSONArray what_now;

	public class LocalBinder extends Binder {
		WiFiShareService getService() {
			return WiFiShareService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	public WifiManager getWifiManager() {
		return this.wifi;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("WiFiShareService", "Received start id " + startId + ": "
				+ intent);
		registerReceiver(new WiFiScanReceiver(this), new IntentFilter(
				WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

		locationManager.requestLocationUpdates(locationProvider, 0, 0,
				new LocationReceiver(this));
		return START_STICKY;
	}

	@Override
	public void onCreate() {// Setup WiFi
		wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
	}

	class IncomingHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_REGISTER_CLIENT:
				mClients.add(msg.replyTo);
				break;
			case MSG_UNREGISTER_CLIENT:
				mClients.remove(msg.replyTo);
				break;
			case MSG_SET_SCANNER:
				mValue = msg.getData();
				break;
			case MSG_SET_POSITION:
				try {
					what_now = new JSONArray(mValue.getString("location"));
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;
			case MSG_SET_WIFI:
				mValue = msg.getData();
				try {
					what_now = new JSONArray(mValue.getString("wifi"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			default:
				super.handleMessage(msg);
			}
			try {
				JSONObject message = new JSONObject();
				message.put("lc", where_now);
				message.put("wf", what_now);
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("q", message.toString());

				a.ajax("localhost/ciccio", params, JSONObject.class,
						new AjaxCallback<JSONObject>() {
							@Override
							public void callback(String url, JSONObject json,
									AjaxStatus status) {

							}
						});
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
