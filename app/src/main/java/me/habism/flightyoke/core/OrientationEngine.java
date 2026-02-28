package me.habism.flightyoke.core;

public class OrientationEngine {
    private float[] accelVals = new float[3];

    public void updateAccelerometer(float[] values) {
        accelVals = values.clone();
    }

    public float getRollRad() {
        return (float) Math.atan2(accelVals[1], accelVals[2]);
    }

    public float getPitchRad() {
        return (float) Math.atan2(
                -accelVals[0],
                Math.sqrt(accelVals[1] * accelVals[1] +
                        accelVals[2] * accelVals[2])
        );
    }
}
