package it.mmo.wifisher;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

public class WiFiShareServiceConnector implements ServiceConnection{

	private WiFiShareService mBoundService;
	
	public WiFiShareServiceConnector(WiFiShareService service){
		this.setBoundService(service);
	}
	Messenger mService = null;
	
	@Override
    public void onServiceConnected(ComponentName className, IBinder service) {
		mService = new Messenger(service);
		
	}

	@Override
    public void onServiceDisconnected(ComponentName className) {
		setBoundService(null);
		
	}

	public WiFiShareService getBoundService() {
		return mBoundService;
	}

	private void setBoundService(WiFiShareService mBoundService) {
		this.mBoundService = mBoundService;
	}
}
