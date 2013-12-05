package it.mmo.wifisher;

import java.util.List;

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
import android.os.IBinder;
import android.util.Log;

public class WiFiShareService extends Service {
	 public class LocalBinder extends Binder {
		 WiFiShareService getService() {
	            return WiFiShareService.this;
	        }
	    }
	 @Override
	    public IBinder onBind(Intent intent) {
	        return mBinder;
	    }

	    // This is the object that receives interactions from clients.  See
	    // RemoteService for a more complete example.
	    private final IBinder mBinder = new LocalBinder();
		private WifiManager wifi;
		public WifiManager getWifiManager(){
			return this.wifi;
		}
		private LocationManager locationManager;
		private String locationProvider = LocationManager.GPS_PROVIDER;
		private Location where_now;
		private WiFiScanReceiver receiver;
	    @Override
	    public int onStartCommand(Intent intent, int flags, int startId) {
	        Log.i("WiFiShareService", "Received start id " + startId + ": " + intent);
	        // We want this service to continue running until it is explicitly
	        // stopped, so return sticky.
	        return START_STICKY;
	    }   
	    
	    @Override
	    public void onCreate() {// Setup WiFi
			wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
			locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
			where_now = locationManager.getLastKnownLocation(locationProvider);
			
			// Get WiFi status
			WifiInfo info = wifi.getConnectionInfo();

			// List available networks
			List<WifiConfiguration> configs = wifi.getConfiguredNetworks();

			// Register Broadcast Receiver
			if (receiver == null)
				receiver = new WiFiScanReceiver(this);

			registerReceiver(receiver, new IntentFilter(
					WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
			
			locationManager.requestLocationUpdates(locationProvider, 0, 0, new LocationReceiver(this));
	    }
}
