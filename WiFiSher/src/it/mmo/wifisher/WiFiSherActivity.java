package it.mmo.wifisher;


import com.androidquery.AQuery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class WiFiSherActivity extends Activity   {
	/** Called when the activity is first created. */
	private AQuery a;
	private WiFiSherActivity that = this;
	private Intent intent = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		intent = new Intent(that, WiFiShareService.class);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		a = new AQuery(this);
		a.id(R.id.buttonScan).clicked( new OnClickListener() {

            @Override
            public void onClick(View v) {
            	intent.putExtra("modes", new String[] {"wifi","location", "time"});
            	startService(intent);
            	
            	
            }
		});

	}
}