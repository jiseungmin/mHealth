package com.example.mhealth_kotlin_ver

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.opencsv.CSVWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.lang.System.exit

class CsvHelper(private val context: Context, private val filePath: String) {
    fun WriteCSVfile(filename: String, datalist: ArrayList<Array<String>>) {
        try {
            val file = FileWriter(File(filePath, filename), true) // 파일을 append mode로 열기
            Log.d("firebase", File(filePath, filename).toString())
            val writer = CSVWriter(file)
            writer.writeAll(datalist) // 데이터 쓰기
            writer.close()
            file.close()
            Log.d("firebase", "저장되었음")
        } catch (e: IOException) {
            Toast.makeText(context, "${e}", Toast.LENGTH_SHORT).show()
        }
    }
}