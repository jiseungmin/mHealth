package com.example.mhealth_kotlin_ver

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener

class GyroScope : SensorEventListener {
    private val gyroScope = arrayOf("", "", "")
    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_GYROSCOPE) {
                gyroScope[0] = event.values[0].toString()
                gyroScope[1] = event.values[1].toString()
                gyroScope[2] = event.values[2].toString()
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
}