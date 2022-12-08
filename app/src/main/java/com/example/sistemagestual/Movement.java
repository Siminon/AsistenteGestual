package com.example.sistemagestual;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.Toast;

import java.util.HashSet;

public class Movement implements SensorEventListener {

    private SensorManager sensorMan;
    private Sensor gyroscope;
    private Sensor accelerometer;

    private Movement() {
    }

    private static Movement mInstance;

    public static Movement getInstance() {
        if (mInstance == null) {
            mInstance = new Movement();
            mInstance.init();
        }
        return mInstance;
    }

    private HashSet<Listener> mListeners = new HashSet<Listener>();

    private void init() {
        sensorMan = (SensorManager) GlobalData.getInstance().getContext().getSystemService(Context.SENSOR_SERVICE);
        gyroscope = sensorMan.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        accelerometer = sensorMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    public void start() {
        sensorMan.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        sensorMan.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stop() {
        sensorMan.unregisterListener(this);
    }

    public void addListener(Listener listener) {
        mListeners.add(listener);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {

            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            float alpha = 1f;
            float diff = (float) Math.sqrt((y * y)) * alpha;
            for (Listener listener : mListeners) {
                listener.onMotionDetected(event, diff);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public interface Listener {
        void onMotionDetected(SensorEvent event, float acceleration);
    }

}
