package it.mmo.wifisher;

import it.mmo.wifisher.receivers.LocationReceiver;
import it.mmo.wifisher.receivers.WiFiScanReceiver;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;

import com.androidquery.AQuery;

public class WiFiShareService extends Service{
	private Looper mServiceLooper;
	private WifiManager wifi;
	private LocationManager locationManager;
	private SharedPreferences sp;

	AQuery a = new AQuery(this);

	private final class ServiceHandler extends Handler {
		public ServiceHandler(Looper looper) {
			super(looper);
		}
		@Override
		public void handleMessage(Message msg) {

		}
	}

	private HashMap<String, String> managers = new HashMap<String, String>();
	//private HashMap<String, ReentrantLock> locker = new HashMap<String, ReentrantLock>();

	public WifiManager getWifiManager() {
		return this.wifi;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate(){
		HandlerThread thread = new HandlerThread("ServiceStartArguments",
				Process.THREAD_PRIORITY_BACKGROUND);
		thread.start();
		mServiceLooper = thread.getLooper();
		new ServiceHandler(mServiceLooper);
		managers.put("wifi", "prepareWiFi");
		managers.put("location", "prepareLocation");
		managers.put("time", "prepareTime");

	}

	public int onStartCommand(Intent intent, int flags, int startId) {
		Set<String> parts = new HashSet<String>();
		if (intent.hasExtra("modes")){
			String[] mode = intent.getStringArrayExtra("modes");
			for (String m: mode){
				parts.add(m);
			}
		}
		else
			parts = managers.keySet();
		boolean debug = intent.getBooleanExtra("debug", false);
		prepare(parts, debug);
		return START_STICKY;
	}

	public void prepareWiFi(String variable, SharedPreferences.Editor editor){
		wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		registerReceiver(new WiFiScanReceiver(this, variable, editor), new IntentFilter(
				WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
	}

	public void prepareLocation(String variable, SharedPreferences.Editor editor){
		locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		JSONObject jso = new JSONObject();
		try {
			jso.put("lon", loc.getLongitude());
			jso.put("lat", loc.getLatitude());
			jso.put("alt", loc.getAltitude());
			jso.put("time", loc.getTime());
			jso.put("acc", loc.getAccuracy());
			jso.put("bearing", loc.getBearing());
			jso.put("speed", loc.getSpeed());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		editor.putString(variable, jso.toString());
		editor.commit();

		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
				new LocationReceiver(this, variable, editor));

	}

	public void prepareTime(String variable, SharedPreferences.Editor editor){
		Date date = new Date();
		editor.putString(variable, date.toString());
		editor.commit();
	}

	public void prepare() {
		this.prepare(managers.keySet(), false);
	}
	public void prepare(Set<String> list, boolean debug){
		sp = getSharedPreferences("it.mmo.wifishare.preferences", 0);
		for(Map.Entry<String,String> entry: managers.entrySet()){
			if (list.contains(entry.getKey())){
				try {
					java.lang.reflect.Method method = this.getClass().getMethod(entry.getValue(), String.class, SharedPreferences.Editor.class);
					method.invoke(this, entry.getKey(), sp.edit());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		for (String t:list){
			
		}
		
		if(debug)
			for(String m: list){
				Log.d("SET_"+m, sp.getString(m, "NONE"));
			}
	}

}
