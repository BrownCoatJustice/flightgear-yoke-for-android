package me.habism.flightyoke.core;

public class OrientationEngine {

    private float[] accelVals = new float[3];

    public void updateAccelerometer(float[] values) {
        accelVals = values.clone();
        AppLogger.d("ACC X=" + accelVals[0] +
                " Y=" + accelVals[1] +
                " Z=" + accelVals[2]);
    }

    public float getPitchRad() {
        float y = accelVals[1];
        float x = accelVals[0];

        return (float) Math.atan2(y, x);
    }

    public float getRollRad() {
        float z = accelVals[2];
        float x = accelVals[0];

        return (float) Math.atan2(-z, x);
    }
}
