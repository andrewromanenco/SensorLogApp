package com.romanenco.sensorlog;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;

/**
 * Application to log accelerometer data into csv file for analisys in R.
 * App must run in foreground.
 *
 * @author Andrew Romanenco
 *
 */
public class MainActivity extends ActionBarActivity implements SensorEventListener {

    private static final String TAG = "SensorLogAPP";

    private static final String markerWalkStart = "Walk start";
    private static final String markerWalkStop = "Walk stop";

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private PlaceholderFragment fragment;

    private SensorLogger sensorLogger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragment = new PlaceholderFragment();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment).commit();
        }

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private Switch switchOnOff;
        private TextView markerText;
        private TextView statusText;
        private boolean switchIsOff = true;

        public PlaceholderFragment() {
        }

        public boolean isSwitchIsOff() {
            return switchIsOff;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container,
                    false);
            switchOnOff = (Switch)rootView.findViewById(R.id.switch_on_off);
            markerText = (TextView)rootView.findViewById(R.id.marker_text);
            statusText = (TextView)rootView.findViewById(R.id.status);
            Button button = (Button)rootView.findViewById(R.id.mark_it);
            button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    ((MainActivity)getActivity()).writeMarker(markerText.getText());
                    markerText.setText("");
                }
            });
            switchOnOff.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    switchIsOff = !isChecked;
                    ((MainActivity)getActivity()).uiSwitchHandle();
                }
            });

            ((Button)rootView.findViewById(R.id.mark_walk_start)).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    ((MainActivity)getActivity()).writeMarker(markerWalkStart);
                    updateStatus(markerWalkStart);
                }
            });

            ((Button)rootView.findViewById(R.id.mark_walk_stop)).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    ((MainActivity)getActivity()).writeMarker(markerWalkStop);
                    updateStatus(markerWalkStop);
                }
            });

            return rootView;
        }

        public void updateStatus(String text) {
            statusText.setText(text);
        }
    }

    protected void writeMarker(CharSequence text) {
        if (sensorLogger != null) {
            sensorLogger.addToLog(text + "\n");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopListening();
    }

    protected void uiSwitchHandle() {
        if (fragment.isSwitchIsOff()) {
            stopListening();
        } else {
            startListening();
        }
    }

    private void startListening() {
        if (sensorLogger == null) {
            sensorLogger = new SensorLogger();
        }
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        Log.d(TAG, "Event handler registered");
    }

    private void stopListening() {
        mSensorManager.unregisterListener(this);
        if (sensorLogger != null) {
            sensorLogger.flush();
            sensorLogger = null;
        }
        Log.d(TAG, "Event handler unregistered");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // nothing
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorLogger != null) {
            sensorLogger.logSensorEvent(sensorEvent.values);
            fragment.updateStatus(Long.toString(System.currentTimeMillis()));
        }
    }

}
