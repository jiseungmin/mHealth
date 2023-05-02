package com.example.mhealth_kotlin_ver

import android.Manifest
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class MainActivity : AppCompatActivity(), SensorEventListener {

    private val sensorManager by lazy { getSystemService(Context.SENSOR_SERVICE) as SensorManager }
    private val textview: TextView by lazy { findViewById(R.id.TextView) }
    private val button: AppCompatButton by lazy { findViewById(R.id.BUTTON) }
    private val SaveButton: AppCompatButton by lazy { findViewById(R.id.SaveButton) }
    private val ResetButton: AppCompatButton by lazy { findViewById(R.id.ResetButton) }
    private val pathToExternalStorage = Environment.getExternalStorageDirectory().toString()
    private val exportDir = File(pathToExternalStorage, "/mHealth_kotlin")
    private val dataArray = arrayOf("0:0", "", "", "", "", "", "", "", "", "")
    private var datalist = arrayListOf<Array<String>>()
    private var Filename: String = ""
    private var exep: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT // 화면 가로 고정
        MeasurementButton() // 측정 버튼
        SaveCsvfileButton() // 저장 버튼
        ResetButton() // 초기화 버튼
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        p0?.let {
            when (it.sensor.type) {
                Sensor.TYPE_ACCELEROMETER -> {
                    textview.text =
                        "Accelerometer : x: ${p0.values[0]}, y: ${p0.values[1]}, z: ${p0.values[2]}"
                    dataArray[1] = p0.values[0].toString()
                    dataArray[2] = p0.values[1].toString()
                    dataArray[3] = p0.values[2].toString()
                    CsvWrite()
                }

                Sensor.TYPE_GYROSCOPE -> {
                    textview.append("\nRotation Vector  : x: ${p0.values[0]}, y: ${p0.values[1]}, z: ${p0.values[2]}")
                    dataArray[4] = p0.values[0].toString()
                    dataArray[5] = p0.values[1].toString()
                    dataArray[6] = p0.values[2].toString()
                    CsvWrite()
                }

                Sensor.TYPE_ROTATION_VECTOR -> {
                    textview.append("\nGyroscope : x: ${p0.values[0]}, y: ${p0.values[1]}, z: ${p0.values[2]}")
                    dataArray[7] = p0.values[0].toString()
                    dataArray[8] = p0.values[1].toString()
                    dataArray[9] = p0.values[2].toString()
                    CsvWrite()
                }
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) = Unit

    private fun MeasurementButton() {
        button.setOnClickListener {
            if (exep == 0) {
                exep = 1
                button.text = "Stop"
                SaveButton.setEnabled(false)
                ResetButton.setEnabled(false)
                Filename = UUID.randomUUID().toString() + ".csv"
                sensorManager.registerListener(
                    this,
                    sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_FASTEST
                )
                sensorManager.registerListener(
                    this,
                    sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
                    SensorManager.SENSOR_DELAY_FASTEST
                )
                sensorManager.registerListener(
                    this,
                    sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                    SensorManager.SENSOR_DELAY_FASTEST
                )
            } else {
                exep = 0
                sensorManager.unregisterListener(this)
                textview.text = "Measurement has ended."
                button.text = "RESUME"
                SaveButton.setEnabled(true)
                ResetButton.setEnabled(true)
            }
        }
    }

    private fun SaveCsvfileButton() {
        val filePath = exportDir.toString()
        Log.d("filepath", "${exportDir}")
        val csvHelper = CsvHelper(this, filePath)
        val headerlist = arrayListOf<Array<String>>()
        if (!exportDir.exists()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 2000)
            }
        }
        SaveButton.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Save CSV File")
            builder.setMessage("Would you like to save the csv file?")
            builder.setPositiveButton("Yes") { dialog, which ->
                headerlist.add(arrayOf("Evnet_Time", "x_Acc", "y_Acc", "z_Acc", "x_Gyr", "y_Gyr", "z_Gyr", "x_Rot", "y_Rot","z_Rot"))
                csvHelper.WriteCSVfile(Filename, headerlist)
                csvHelper.WriteCSVfile(Filename, datalist)
                var snackbar = Snackbar.make(it, "The CSV file has been saved.", Snackbar.LENGTH_LONG)
                snackbar.show()
                MeasureAgain()
            }
            builder.setNegativeButton("No") { dialog, which -> dialog.dismiss() }
            val dialog = builder.create()
            dialog.show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            2000 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val pathToExternalStorage = Environment.getExternalStorageDirectory().toString()
                    val exportDir = File(pathToExternalStorage, "/mHealth_kotlin")
                    if (!exportDir.exists()) {
                        exportDir.mkdirs()
                    }
                } else {
                    Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun ResetButton() {
        ResetButton.setOnClickListener {
            MeasureAgain()
        }
    }

    private fun MeasureAgain() {
        exep = 0
        datalist = arrayListOf()
        Log.d("1231", "${datalist}")
        SaveButton.setEnabled(false)
        ResetButton.setEnabled(false)
        button.text = "Start"
        textview.text = "Please start measuring."
    }

    private fun CsvWrite() {
        if (dataArray.any { it.isEmpty() }) {
            Log.d("debug", "Empty elements found in dataArray")
            return
        } else {
            val eventTime = System.currentTimeMillis()
            val formatter = SimpleDateFormat("MM-dd HH:mm:ss:SSS", Locale.getDefault())
            val formattedTime = formatter.format(Date(eventTime))

            dataArray[0] = formattedTime.toString()
            datalist.add(dataArray.copyOf())
            dataArray.fill("", 1, 9)
        }
    }

    companion object {
        private const val FILE_NAME = "Walk_analysis&uuid.csv"
    }
}