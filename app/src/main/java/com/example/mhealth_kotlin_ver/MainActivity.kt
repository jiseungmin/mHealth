package com.example.mhealth_kotlin_ver

import android.content.Context
import android.content.pm.ActivityInfo
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity(), SensorEventListener {

    private val sensorManager by lazy { getSystemService(Context.SENSOR_SERVICE) as SensorManager }
    private val textview: TextView by lazy { findViewById(R.id.TextView) }
    private val button: Button by lazy { findViewById(R.id.BUTTON) }
    private var exep: Int = 0
    private var isHeaderAdded = false // 헤더 추가 여부를 저장하는 변수
    private val dataArray = arrayOf("","","","","","","","","")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT // 화면 가로 고정
        MeasurementButton() // 측정 버튼
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(p0: SensorEvent?) {

        p0?.let {
            when (it.sensor.type) {
                Sensor.TYPE_ACCELEROMETER -> {
                    textview.text = "Accelerometer : x: ${p0.values[0]}, y: ${p0.values[1]}, z: ${p0.values[2]}"
                    CsvWrite(p0)
                }
                Sensor.TYPE_GYROSCOPE -> {
                    textview.append("\nRotation Vector  : x: ${p0.values[0]}, y: ${p0.values[1]}, z: ${p0.values[2]}")
                    CsvWrite(p0)
                }
                Sensor.TYPE_ROTATION_VECTOR -> {
                    textview.append("\nGyroscope : x: ${p0.values[0]}, y: ${p0.values[1]}, z: ${p0.values[2]}")
                    CsvWrite(p0)
                }
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) = Unit

    private fun MeasurementButton(){
        button.setOnClickListener {
            if (exep == 0) {
                exep = 1
                button.text = "측정종료"
                sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST)
                sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR), SensorManager.SENSOR_DELAY_FASTEST)
                sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_FASTEST)
            } else {
                exep = 0
                sensorManager.unregisterListener(this)
                textview.text = "측정이 종료되었습니다."
                button.text = "측정하기"
            }
        }
    }

    private fun CsvWrite(p0: SensorEvent?) {
        val filePath = filesDir.toString()
        val csvHelper = CsvHelper(this, filePath)
        val datalist = arrayListOf<Array<String>>()
        val headerlist = arrayListOf<Array<String>>()

        // CSV header
        headerlist.add(arrayOf("x_Acc", "y_Acc", "z_Acc", "x_Gyr", "y_Gyr", "z_Gyr", "x_Rot", "y_Rot", "z_Rot"))

        // Add data if SensorEvent is not null
        p0?.let {
            when(it.sensor.type) {
                Sensor.TYPE_ACCELEROMETER -> {
                    dataArray[0] = p0.values[0].toString()
                    dataArray[1] = p0.values[1].toString()
                    dataArray[2] = p0.values[2].toString()
                }
                Sensor.TYPE_GYROSCOPE -> {
                    dataArray[3] = p0.values[0].toString()
                    dataArray[4] = p0.values[1].toString()
                    dataArray[5] = p0.values[2].toString()
                }
                Sensor.TYPE_ROTATION_VECTOR -> {
                    dataArray[6] = p0.values[0].toString()
                    dataArray[7] = p0.values[1].toString()
                    dataArray[8] = p0.values[2].toString()
                }
            }

            // Add data row to the list
            datalist.add(dataArray)
        }

        if (!isHeaderAdded) {
            headerlist.add(arrayOf("x_Acc", "y_Acc", "z_Acc", "x_Gyr", "y_Gyr", "z_Gyr", "x_Rot", "y_Rot", "z_Rot"))
            csvHelper.WriteCSVfile(FILE_NAME, headerlist) // Add header to file
            isHeaderAdded = true // Update header added status
        }
        for (i in 0 until datalist.size) {
            Log.d("debug", "datalist[$i]: ${datalist[i].joinToString()}")
        }

        csvHelper.WriteCSVfile(FILE_NAME, datalist)

    }
    companion object {
        private const val FILE_NAME = "Walk_analysis.csv"
    }
}