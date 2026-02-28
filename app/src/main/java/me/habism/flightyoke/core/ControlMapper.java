package me.habism.flightyoke.core;

import android.content.Context;
import android.content.SharedPreferences;

public class ControlMapper {

    private static final String PREFS = "flight_yoke_prefs";
    private static final String KEY_ROLL = "neutral_roll";
    private static final String KEY_PITCH = "neutral_pitch";

    private float neutralRoll = 0f;
    private float neutralPitch = 0f;

    private final SharedPreferences prefs;

    public ControlMapper(Context context) {
        prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        neutralRoll = prefs.getFloat(KEY_ROLL, 0f);
        neutralPitch = prefs.getFloat(KEY_PITCH, 0f);
    }

    public void calibrate(float rollRad, float pitchRad) {
        neutralRoll = rollRad;
        neutralPitch = pitchRad;

        prefs.edit()
                .putFloat(KEY_ROLL, neutralRoll)
                .putFloat(KEY_PITCH, neutralPitch)
                .apply();
    }

    public float mapRoll(float rollRad) {
        float value = rollRad - neutralRoll;
        value /= Math.toRadians(45);  // 45° full deflection
        return clamp(value);
    }

    public float mapPitch(float pitchRad) {
        float value = pitchRad - neutralPitch;
        value /= Math.toRadians(30);  // 30° full deflection
        return clamp(value);
    }

    private float clamp(float v) {
        return Math.max(-1f, Math.min(1f, v));
    }
}