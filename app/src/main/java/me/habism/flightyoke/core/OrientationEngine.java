package me.habism.flightyoke.core;

public class OrientationEngine {
    private float[] accelVals = new float[3];

    public void updateAccelerometer(float[] values) {
        accelVals = values.clone();
    }

    public float getRollRad() {
        // rotation left/right (bank)
        return (float) Math.atan2(accelVals[0], accelVals[2]);
    }

    public float getPitchRad() {
        // push/pull (nose up/down)
        return (float) Math.atan2(
                accelVals[1],
                Math.sqrt(accelVals[0] * accelVals[0] +
                        accelVals[2] * accelVals[2])
        );
    }
}
