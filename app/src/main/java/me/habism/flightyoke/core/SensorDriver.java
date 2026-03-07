package me.habism.flightyoke.core;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class SensorDriver implements SensorEventListener {

    public interface SensorListener {
        void onAccelerometer(float[] values);
    }

    private final SensorManager sensorManager;
    private final Sensor accelerometer;
    private final SensorListener listener;

    public SensorDriver(Context context, SensorListener listener) {
        this.listener = listener;

        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    public void start() {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    public void stop() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        listener.onAccelerometer(event.values.clone());
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}