package com.example.mhealth_kotlin_ver

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener

class RotationVector : SensorEventListener {
    private val rotationVector = arrayOf("", "", "")
    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {
                rotationVector[0] = event.values[0].toString()
                rotationVector[1] = event.values[1].toString()
                rotationVector[2] = event.values[2].toString()
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
}