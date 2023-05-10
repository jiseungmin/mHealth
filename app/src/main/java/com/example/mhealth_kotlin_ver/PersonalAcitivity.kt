package com.example.mhealth_kotlin_ver

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mhealth_kotlin_ver.databinding.ActivityPersonalBinding
import com.google.android.material.snackbar.Snackbar
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
        ChcekBoxControl()
        Adminmeun()
        questionTextview()
        NextButton()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun Adminmeun() {
        var touchcount = 0
        binding.TitleTextview.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                touchcount++
                Log.d("text",touchcount.toString())
                if(binding.NameEditText.text.toString() == "Admin" && touchcount >= 8){
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Admin Page")
                    builder.setMessage("are you going to the admin page?")
                    builder.setPositiveButton("yes",{ dialog, which ->
                        val intent = Intent(this,AdminloginActivity::class.java)
                        startActivity(intent)
                    })
                    builder.setNegativeButton("no",{dialog, which ->})
                    builder.show()
                    touchcount = 0
                }
            }
            true
        }
    }
    private fun questionTextview() {
        questionFirebase.get().addOnSuccessListener {
            binding.NameTextview.text  =  it.getString("1")
            binding.AgeTextview.text = it.getString("2")
            binding.HeightTextview.text = it.getString("3")
            binding.WeightTextview.text = it.getString("4")
            binding.GenderTextview.text = it.getString("5")
        }.addOnFailureListener {}
    }

    private fun NextButton() {
        binding.NextButton.setOnClickListener {
            val isMaleChecked = binding.maleCheckBox.isChecked // "남자" 체크박스의 체크 여부 가져오기
            val isFemaleChecked = binding.femaleCheckBox.isChecked // "여자" 체크박스의 체크 여부 가져오기
            val Username = binding.NameEditText.text.toString()
            val Userage = binding.AgeEditText.text.toString()
            val Userheight = binding.HeightEditText.text.toString()
            val Userweight = binding.WeightEditText.text.toString()
            val Usergender = if(isMaleChecked){ "남자" }else if (isFemaleChecked){ "여자" }else{ "" }

            if (Username.isNotEmpty() && Userage.isNotEmpty() && Userheight.isNotEmpty() && Userweight.isNotEmpty() && (isMaleChecked || isFemaleChecked)) {
                val PersonalIntent = Intent(this, SurveyActivity::class.java)
                PersonalIntent.putExtra("Username", Username)
                PersonalIntent.putExtra("Userage", Userage)
                PersonalIntent.putExtra("Userheight", Userheight)
                PersonalIntent.putExtra("Userweight", Userweight)
                PersonalIntent.putExtra("Usergender", Usergender)
                startActivity(PersonalIntent)
            } else {
                var snackbar = Snackbar.make(it, "Please fill out all required information.", Snackbar.LENGTH_LONG)
                snackbar.show()
            }
        }
    }
    private fun ChcekBoxControl() {
        binding.maleCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.femaleCheckBox.isChecked = false
            }
        }
        binding.femaleCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.maleCheckBox.isChecked = false
            }
        }
    }
}