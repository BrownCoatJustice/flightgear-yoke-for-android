package me.habism.flightyoke

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import me.habism.flightyoke.databinding.ActivityMainBinding
import me.habism.flightyoke.core.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sensorDriver: SensorDriver
    private lateinit var orientationEngine: OrientationEngine
    private lateinit var controlMapper: ControlMapper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        orientationEngine = OrientationEngine()
        controlMapper = ControlMapper(this)

        sensorDriver = SensorDriver(this) { values ->
            orientationEngine.updateAccelerometer(values)

            val rollRad = orientationEngine.getRollRad()
            val pitchRad = orientationEngine.getPitchRad()

            val roll = controlMapper.mapRoll(rollRad)
            val pitch = controlMapper.mapPitch(pitchRad)

            runOnUiThread {
                binding.textView.text =
                    "Roll: %.2f\nPitch: %.2f".format(roll, pitch)
            }
        }

        binding.calibrateButton.setOnClickListener {
            val rollRad = orientationEngine.getRollRad()
            val pitchRad = orientationEngine.getPitchRad()
            controlMapper.calibrate(rollRad, pitchRad)
        }
    }

    override fun onResume() {
        super.onResume()
        sensorDriver.start()
    }

    override fun onPause() {
        super.onPause()
        sensorDriver.stop()
    }
}