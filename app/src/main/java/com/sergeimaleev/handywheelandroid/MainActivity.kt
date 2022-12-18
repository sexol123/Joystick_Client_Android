package com.sergeimaleev.handywheelandroid

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Vibrator
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.sergeimaleev.handywheelandroid.databinding.ActivityMainBinding
import com.sergeimaleev.handywheelandroid.network.NetworkComponent
import java.util.concurrent.Executors

private const val HOST = "192.168.137.1"
private const val PORT = 4999

const val KEY_LEFT_ARROW = 37;
const val KEY_UP_ARROW = 38;
const val KEY_RIGHT_ARROW = 39;
const val KEY_DOWN_ARROW = 40;
const val KEY_Z_FIRE = 90;
const val KEY_START_ENTER = 13;
const val KEY_SELECT_SHIFT = 16;

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    //private lateinit var gyroscopeSensorComponent: GyroscopeSensorComponent
    private lateinit var networkComponent: NetworkComponent
    private val vibrator by lazy { setupVibrator() }

    private val executors = Executors.newCachedThreadPool()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupComponents()
    }

    private fun setupComponents() {
//        setupGyroscopeComponent()
        binding.etReadHostValue.setText(HOST)
        binding.etReadPortValue.setText(PORT.toString())
        setupStartButton()
        binding.btnRestartApp.setOnClickListener {
            it.setOnClickListener(null)
            networkComponent.terminate()
            this.finish()
            startActivity(intent)
        }
    }


    private fun setupStartButton() {
        binding.btnStart.setOnClickListener {
            binding.groupConfigure.visibility = View.GONE
            binding.groupJoystick.visibility = View.VISIBLE

            setupNetworkComponent()
            setupJoystickButtons()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupJoystickButtons() {
        with(binding) {

            btnFire.apply {
                setImageBitmap(Utils.textAsBitmap(Char(KEY_Z_FIRE).toString(), 70f, Color.BLACK))
                setOnTouchListener { _, event ->
                    onTouchButton(KEY_Z_FIRE, event.action)
                    true
                }
            }
            btnLeft.apply {
                setImageBitmap(
                    Utils.textAsBitmap(
                        Char(KEY_LEFT_ARROW).toString(),
                        70f,
                        Color.BLACK
                    )
                )
                setOnTouchListener { _, event ->
                    onTouchButton(KEY_LEFT_ARROW, event.action)
                    true
                }
            }
            btnUp.apply {
                setImageBitmap(Utils.textAsBitmap(Char(KEY_UP_ARROW).toString(), 70f, Color.BLACK))
                setOnTouchListener { _, event ->
                    onTouchButton(KEY_UP_ARROW, event.action)
                    true
                }
            }
            btnRight.apply {
                setImageBitmap(
                    Utils.textAsBitmap(
                        Char(KEY_RIGHT_ARROW).toString(),
                        70f,
                        Color.BLACK
                    )
                )
                setOnTouchListener { _, event ->
                    onTouchButton(KEY_RIGHT_ARROW, event.action)
                    true
                }
            }
            btnDown.apply {
                setImageBitmap(
                    Utils.textAsBitmap(
                        Char(KEY_DOWN_ARROW).toString(),
                        70f,
                        Color.BLACK
                    )
                )
                setOnTouchListener { _, event ->
                    onTouchButton(KEY_DOWN_ARROW, event.action)
                    true
                }
            }
            btnSelect.setOnTouchListener { _, event ->
                onTouchButton(KEY_SELECT_SHIFT, event.action)
                true
            }
            btnStartGame.setOnTouchListener { _, event ->
                onTouchButton(KEY_START_ENTER, event.action)
                true
            }
        }
    }

    private fun onTouchButton(keyCode: Int, action: Int) {
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                executors.submit {
                    networkComponent.sendMsg("$keyCode-$action")
                }
                vibrator!!.vibrate(1)
            }
            MotionEvent.ACTION_UP -> {
                executors.submit {
                    networkComponent.sendMsg("$keyCode-${action}")
                }
            }
        }
    }

    private fun setupVibrator() = getSystemService(VIBRATOR_SERVICE) as? Vibrator


    /* private fun setupGyroscopeComponent() {
         var maxValueZ = 10

         val sm = getSystemService(SENSOR_SERVICE) as SensorManager
         gyroscopeSensorComponent = GyroscopeSensorComponent(sm) {

             if (!isStarted) return@GyroscopeSensorComponent

             val x = Math.toDegrees(it[0].toDouble()).roundToInt()
             val y = Math.toDegrees(it[1].toDouble()).roundToInt()
             val z = Math.toDegrees(it[2].toDouble()).roundToInt()

             val m = "$x\n$y\n$z"

             if (z > maxValueZ) {
                 maxValueZ = z
                 binding.linearProgressIndicator.max = maxValueZ
             }

             binding.tvDegressValue.text = m

             binding.linearProgressIndicator.post {
                 binding.linearProgressIndicator.progress = z
             }

             Thread {
                 networkComponent.sendMsg(z)
             }.start()
         }
     }*/

    private fun setupNetworkComponent() {
        networkComponent = NetworkComponent()
        val host = binding.etReadHostValue.text?.trim()?.toString() ?: HOST
        val port = binding.etReadPortValue.text?.trim()?.toString()?.toIntOrNull() ?: PORT
        networkComponent.setupBackground(host, port)
    }
}