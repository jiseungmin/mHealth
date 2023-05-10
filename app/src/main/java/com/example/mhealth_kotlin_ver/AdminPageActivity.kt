package com.example.mhealth_kotlin_ver

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AdminPageActivity:AppCompatActivity() {

    private val FirebaseDB = Firebase.firestore
    private val questionFirebase =  FirebaseDB.collection("question").document("Personal Information")
    private val DataList = ArrayList<String>(9)
    private val itemlist: ListView by lazy {
        findViewById(R.id.QuesitonList)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adminpage)
        val returntextView = findViewById<TextView>(R.id.returnTextView)
        returntextView.setOnClickListener {
            val intent = Intent(this, PersonalAcitivity::class.java)
            startActivity(intent)
        }

        val adapter = ListAdapter(this, DataList)
        itemlist.adapter = adapter

        questionFirebase.get().addOnSuccessListener {
            for (i in 1..9) {
                var data = it.getString(i.toString())
                data?.let { question ->
                    Log.d("파이어 베이스", question)
                    DataList.add(question)
                }
            }
            adapter.notifyDataSetChanged()
        }
    }
}