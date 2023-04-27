package com.example.mhealth_kotlin_ver

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener

class Accelerometer: SensorEventListener {
    private val accelerometer = arrayOf("","","")
    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if(event.sensor.type == Sensor.TYPE_ACCELEROMETER){
                accelerometer[0] = event.values[0].toString()
                accelerometer[1] = event.values[1].toString()
                accelerometer[2] = event.values[2].toString()
            }
        }
    }
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
}