package com.echiou.represent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.view.GridViewPager;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends Activity implements SensorEventListener{

    private TextView mTextView;
    private SensorManager mSensorManager;
    private Sensor mSensor;

    private static final int SHAKE_THRESHOLD = 800;
    long lastUpdate; //Debouncing
    float x;
    float y;
    float z;
    float last_x;
    float last_y;
    float last_z;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final GridViewPager pager = (GridViewPager) findViewById(R.id.pager);
        Intent intent = getIntent();

        String[] names = {"Welcome"};
        String[] parties = {"to Represent!"};
        String[] ovr = {};
        if(intent.getStringArrayExtra(WatchListenerService.REP_NAME_MESSAGE) != null){
            names = intent.getStringArrayExtra(WatchListenerService.REP_NAME_MESSAGE);
            parties = intent.getStringArrayExtra(WatchListenerService.REP_PARTY_MESSAGE);
            ovr = intent.getStringArrayExtra(WatchListenerService.OBAMA_V_ROMNEY_MESSAGE);
        }
        RepGridPagerAdapter rgpa = new RepGridPagerAdapter(this, getFragmentManager(), names, parties, ovr);
        pager.setAdapter(rgpa);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null){
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            lastUpdate = 0;
        }
        else{
            // Sorry, there are no accelerometers on your device.
            // You can't play this game.
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public final void onSensorChanged(SensorEvent event)
    {
        long curTime = System.currentTimeMillis();
        // only allow one update every 1s.
        if ((curTime - lastUpdate) > 1000) {
            long diffTime = (curTime - lastUpdate);
            lastUpdate = curTime;

            x = event.values[0];
            y = event.values[1];
            z = event.values[2];

            float speed = Math.abs(x+y+z - last_x - last_y - last_z) / diffTime * 10000;

            if (speed > SHAKE_THRESHOLD) {
                Log.d("sensor", "shake detected w/ speed: " + speed);
                Toast.makeText(this, "Finding random district...", Toast.LENGTH_SHORT).show();

                Intent sendIntent = new Intent(this, WatchToPhoneService.class);
                sendIntent.putExtra("ACTIVITY_TYPE", "/cong");
                startService(sendIntent);
            }
            last_x = x;
            last_y = y;
            last_z = z;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not a concern right now.
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
}
