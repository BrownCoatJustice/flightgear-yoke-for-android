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

    /*
    * Exponential Moving Average (EMA)
    *   new_output =
    *       prev_output + smoothing_factor *
    *       (raw_input - prev_output)
    *   Eg: Suppose, previous stable output = 0.2 and then next output = 0.6
    *       With smoothing: 0.2 + 0.2 * (0.6 - 0.2) = 0.28 => Smoother
    *   Less jitter at the cost of slightly higher latency.
    *
    * Below are the fields for the same
    * */

    private float smoothedRoll = 0f;
    private float smoothedPitch = 0f;
    private static final float SMOOTHING = 0.2f;

    public ControlMapper(Context context) {
        prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        neutralRoll = prefs.getFloat(KEY_ROLL, 0f);
        neutralPitch = prefs.getFloat(KEY_PITCH, 0f);
    }

    public void calibrate(float rollRad, float pitchRad) {
        neutralRoll = rollRad;
        neutralPitch = pitchRad;

        smoothedRoll = 0f;
        smoothedPitch = 0f;

        prefs.edit()
                .putFloat(KEY_ROLL, neutralRoll)
                .putFloat(KEY_PITCH, neutralPitch)
                .apply();
    }

    public float mapRoll(float rollRad) {
        float value = rollRad - neutralRoll;
        value /= Math.toRadians(45);

        value = applyDeadzone(value);
        value = clamp(value);
        AppLogger.d("Unsmoothed, clamped and \"deadzoned\" values for ROLL: " + value);

        smoothedRoll = smooth(value, smoothedRoll);
        if (Math.abs(smoothedRoll) < 0.001f) {
            smoothedRoll = 0f;
        }

        return smoothedRoll;
    }

    public float mapPitch(float pitchRad) {
        float value = pitchRad - neutralPitch;
        value /= Math.toRadians(30);

        value = -(applyDeadzone(value)); // -ve to mimic real yoke
        value = clamp(value);
        AppLogger.d("Unsmoothed, clamped and \"deadzoned\" values for PITCH: " + value);

        smoothedPitch = smooth(value, smoothedPitch);
        if (Math.abs(smoothedPitch) < 0.001f) {
            smoothedPitch = 0f;
        }

        return smoothedPitch;
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

    private float smooth(float current, float previous) { return previous + SMOOTHING * (current - previous); }
}