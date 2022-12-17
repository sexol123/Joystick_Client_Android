package com.sergeimaleev.handywheelandroid

import android.hardware.SensorManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sergeimaleev.handywheelandroid.component.GyroscopeSensorComponent
import com.sergeimaleev.handywheelandroid.databinding.ActivityMainBinding
import com.sergeimaleev.handywheelandroid.network.NetworkComponent
import kotlin.math.roundToInt

private const val HOST = "192.168.137.1"
private const val PORT = 4999

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var gyroscopeSensorComponent: GyroscopeSensorComponent
    private lateinit var networkComponent: NetworkComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupComponents()
    }

    private fun setupComponents() {
        setupGyroscopeComponent()
        setupNetworkComponent()
        setupProgressBar()
    }

    private fun setupProgressBar() {
        binding.linearProgressIndicator.max = 42
    }

    private fun setupGyroscopeComponent() {
        val sm = getSystemService(SENSOR_SERVICE) as SensorManager
        gyroscopeSensorComponent = GyroscopeSensorComponent(sm) {

            val x = Math.toDegrees(it[0].toDouble()).roundToInt()
            val y = Math.toDegrees(it[1].toDouble()).roundToInt()
            val z = Math.toDegrees(it[2].toDouble()).roundToInt()

            val m = "$x\n$y\n$z"

            binding.tvDegressValue.text = m

            binding.linearProgressIndicator.progress = it[2].roundToInt()

            Thread{
                networkComponent.sendMsg(m)
            }.start()
        }
    }

    private fun setupNetworkComponent(){
        networkComponent = NetworkComponent()
        networkComponent.setupBackground(HOST, PORT)
    }
}