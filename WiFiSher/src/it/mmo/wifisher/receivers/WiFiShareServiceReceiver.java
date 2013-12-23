package it.mmo.wifisher.receivers;

import it.mmo.wifisher.WiFiShareService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
 
public class WiFiShareServiceReceiver extends BroadcastReceiver {
 
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, WiFiShareService.class);
        context.startService(service);
    }
}
