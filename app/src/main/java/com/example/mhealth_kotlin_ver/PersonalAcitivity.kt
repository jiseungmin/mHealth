package com.example.mhealth_kotlin_ver

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mhealth_kotlin_ver.databinding.ActivityPersonalBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PersonalAcitivity: AppCompatActivity() {
    private lateinit var binding : ActivityPersonalBinding
    private val FirebaseDB = Firebase.firestore
    private val questionFirebase =  FirebaseDB.collection("question").document("Personal Information")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        questionFirebase.get().addOnSuccessListener {
            binding.NameTextview.text  =  it.getString("1")
            binding.AgeTextview.text = it.getString("2")
            binding.HeightTextview.text = it.getString("3")
            binding.WeightTextview.text = it.getString("4")
            binding.GenderTextview.text = it.getString("5")
        }

        binding.NextButton.setOnClickListener {
            val intent = Intent(this, SurveyActivity::class.java)
            startActivity(intent)
        }

    }
}