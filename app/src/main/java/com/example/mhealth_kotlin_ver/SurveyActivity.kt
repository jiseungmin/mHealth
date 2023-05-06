package com.example.mhealth_kotlin_ver

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mhealth_kotlin_ver.databinding.ActivityPersonalBinding
import com.example.mhealth_kotlin_ver.databinding.ActivitySurveyBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SurveyActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySurveyBinding
    private val FirebaseDB = Firebase.firestore
    private val questionFirebase =  FirebaseDB.collection("question").document("Personal Information")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySurveyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        questionFirebase.get().addOnSuccessListener {
            binding.PainQuestionTextview.text  =  it.getString("6")
            binding.StatusQuestionTextview.text = it.getString("7")
            binding.FillingQuestionTextview.text = it.getString("8")
            binding.DiseaseQuestionTextview.text = it.getString("9")
        }

        binding.NextButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


    }
}