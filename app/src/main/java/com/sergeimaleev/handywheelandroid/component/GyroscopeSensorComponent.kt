package com.sergeimaleev.handywheelandroid.component

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class GyroscopeSensorComponent(
    sensorManager: SensorManager,
    private val onSensorValue: (gValues: FloatArray) -> Unit
) {
    private val gyroscopeSensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            if (event?.sensor?.type == Sensor.TYPE_ROTATION_VECTOR) {
                onSensorValue(event.values)
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            //Nothing
        }
    }

    var maxRange: Float = 0f
        private set

    init {
        sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)?.also {
            maxRange = it.maximumRange
            sensorManager.registerListener(
                gyroscopeSensorEventListener,
                it,
                SensorManager.SENSOR_DELAY_NORMAL,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }

    }
}