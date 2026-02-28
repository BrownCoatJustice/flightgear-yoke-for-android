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
    private float deadzone = 0.05f;  // 5% center deadband

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
        value /= Math.toRadians(45);

        value = clamp(value);
        value = applyDeadzone(value);

        return value;
    }

    public float mapPitch(float pitchRad) {
        float value = pitchRad - neutralPitch;
        value /= Math.toRadians(30);

        value = clamp(value);
        value = applyDeadzone(value);

        return value;
    }

    private float clamp(float v) {
        return Math.max(-1f, Math.min(1f, v));
    }

    private float applyDeadzone(float value) {
        if (Math.abs(value) < deadzone) {
            return 0f;
        }
        return value;
    }
}