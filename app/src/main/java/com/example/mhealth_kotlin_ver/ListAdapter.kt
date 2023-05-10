package com.example.mhealth_kotlin_ver

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ListAdapter(context: Context,private val data:List<String>):ArrayAdapter<String>(context,0,data) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_question, parent, false)
        val textView = view.findViewById<TextView>(R.id.Quesiton)
        var editText = view.findViewById<EditText>(R.id.EditText)
        val button = view.findViewById<Button>(R.id.SendButton)

        textView.text = data[position]

        editText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                editText.text.clear()
            }
        }

        button.setOnClickListener {
            val FirebaseDB = Firebase.firestore
            val questionFirebase = FirebaseDB.collection("question").document("Personal Information")
            val newData = hashMapOf<String, Any>()
            newData[(position + 1).toString()] = editText.text.toString()
            questionFirebase.update(newData)
                .addOnSuccessListener {
                    editText.text.clear()
                    questionFirebase.get().addOnSuccessListener { document ->
                        if (document != null) {
                            val updatedData = document.getString((position + 1).toString())
                            textView.text = updatedData
                        }
                    }
                    val snackbar = Snackbar.make(button, "Success", Snackbar.LENGTH_LONG)
                    snackbar.show()
                }
                .addOnFailureListener { e ->
                    Log.e("파이어베이스", "업데이트 실패", e)
                }
        }
        return  view
    }
}