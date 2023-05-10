package com.example.mhealth_kotlin_ver

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.snackbar.Snackbar

class AdminloginActivity : AppCompatActivity() {

    private val AdminPassword : EditText by lazy {
        findViewById(R.id.AdminPassword)
    }
    private val LoginButton : AppCompatButton by lazy {
        findViewById(R.id.AdminPasswordButton)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adminlogin)

        LoginButton.setOnClickListener {
            if (AdminPassword.text.toString() == "12481632") {
                val intent = Intent(this, AdminPageActivity::class.java)
                startActivity(intent)
            }else{
                var snackbar = Snackbar.make(it, "NO PassWord", Snackbar.LENGTH_LONG)
                snackbar.show()
            }
        }
    }
}