package com.example.appfifa.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.appfifa.R
import com.google.firebase.auth.FirebaseAuth

class MainActivityLogin : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var signLOutBtn : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_login)
        auth = FirebaseAuth.getInstance()
        signLOutBtn = findViewById(R.id.log)
        signLOutBtn.setOnClickListener(){
            auth.signOut()
            startActivity(Intent(this,MainActivityPhone::class.java))
        }
    }
}