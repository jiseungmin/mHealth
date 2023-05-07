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
    private val questionFirebase =
        FirebaseDB.collection("question").document("Personal Information")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySurveyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ChcekBoxControl()
        questionTextview()
        NextButton()
    }
    private fun questionTextview() {
        questionFirebase.get().addOnSuccessListener {
            binding.PainQuestionTextview.text = it.getString("6")
            binding.StatusQuestionTextview.text = it.getString("7")
            binding.FillingQuestionTextview.text = it.getString("8")
            binding.DiseaseQuestionTextview.text = it.getString("9")
        }
    }

    private fun NextButton() {
        binding.NextButton.setOnClickListener {
            val yespain = binding.PainYesCheckbox.isChecked
            val nopain = binding.PainNoCheckbox.isChecked
            val Goodfilling = binding.FillingGoodCheckbox.isChecked
            val Commonfilling = binding.FillingCommonlyCheckbox.isChecked
            val Badisfilling = binding.FillingBadCheckbox.isChecked

            val SurveyIntent = Intent(this, MainActivity::class.java)
            val CurrentPain = if (yespain) {
                "Yes"
            } else if (nopain) {
                "No"
            } else {
                ""
            }
            val CurrentFilling = binding.StatusEditText.text.toString()
            val CurrentFilling2 = if (Goodfilling) {
                "Good"
            } else if (Commonfilling) {
                "Common"
            } else if (Badisfilling) {
                "Bad"
            } else {
                ""
            }
            val Currentdisease = binding.DiseaseEditText.text.toString()

            val Username = intent.getStringExtra("Username")
            val Userage = intent.getStringExtra("Userage")
            val Userheight = intent.getStringExtra("Userheight")
            val Userweight = intent.getStringExtra("Userweight")
            val Usergender = intent.getStringExtra("Usergender")

            SurveyIntent.putExtra("Username", Username)
            SurveyIntent.putExtra("Userage", Userage)
            SurveyIntent.putExtra("Userheight", Userheight)
            SurveyIntent.putExtra("Userweight", Userweight)
            SurveyIntent.putExtra("Usergender", Usergender)
            SurveyIntent.putExtra("CurrentPain", CurrentPain)
            SurveyIntent.putExtra("CurrentFilling", CurrentFilling)
            SurveyIntent.putExtra("CurrentFilling2", CurrentFilling2)
            SurveyIntent.putExtra("Currentdisease", Currentdisease)

            startActivity(SurveyIntent)
        }
    }
    private fun ChcekBoxControl() {
        binding.PainYesCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.PainNoCheckbox.isChecked = false
            }
        }
        binding.PainNoCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.PainYesCheckbox.isChecked = false
            }
        }

        binding.FillingGoodCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.FillingCommonlyCheckbox.isChecked = false
                binding.FillingBadCheckbox.isChecked = false
            }
        }
        binding.FillingCommonlyCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.FillingGoodCheckbox.isChecked = false
                binding.FillingBadCheckbox.isChecked = false
            }
        }
        binding.FillingBadCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.FillingGoodCheckbox.isChecked = false
                binding.FillingCommonlyCheckbox.isChecked = false
            }
        }
    }
}