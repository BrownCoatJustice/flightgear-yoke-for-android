package me.habism.flightyoke.core;

public class OrientationEngine {

    private float[] accelVals = new float[3];

    public void updateAccelerometer(float[] values) {
        accelVals = values.clone();
    }

    public float getPitchRads() {
        float x = accelVals[0];
        float y = accelVals[1];
        float z = accelVals[2];

        return (float) Math.atan2(y, Math.sqrt(x*x + z*z));
    }

    public float getRollRad() {
        float x = accelVals[0];
        float y = accelVals[1];
        float z = accelVals[2];

        return (float) Math.atan2(-z, Math.sqrt(x*x + y*y));
    }
}