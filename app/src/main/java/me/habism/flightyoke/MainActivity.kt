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
    private lateinit var udpSender: UdpSender

    override fun onCreate(savedInstanceState: Bundle?) {
        udpSender = UdpSender("192.168.31.147", 5005)

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        orientationEngine = OrientationEngine()
        controlMapper = ControlMapper(this)

        sensorDriver = SensorDriver(this) { values ->
            orientationEngine.updateAccelerometer(values)

            val rawRollAxis = orientationEngine.getPitchRad()   // left/right
            val rawPitchAxis = orientationEngine.getRollRad()   // push/pull

            val roll = controlMapper.mapRoll(rawRollAxis)
            val pitch = controlMapper.mapPitch(rawPitchAxis)

            udpSender.send(roll, pitch)

            runOnUiThread {
                binding.textView.text =
                    "Roll: %.2f\nPitch: %.2f".format(roll, pitch)
            }

        }

        binding.calibrateButton.setOnClickListener {
            val rawRollAxis = orientationEngine.getPitchRad()
            val rawPitchAxis = orientationEngine.getRollRad()
            controlMapper.calibrate(rawRollAxis, rawPitchAxis)
        }
    }

    override fun onResume() {
        super.onResume()
        sensorDriver.start()
    }

    override fun onPause() {
        super.onPause()
        sensorDriver.stop()
        udpSender.close()
    }
}