package com.example.talktrainer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    private lateinit var btnLogIn: Button;
    private lateinit var btnSignUp: Button;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        btnLogIn = findViewById(R.id.login_button)
        btnSignUp = findViewById(R.id.signin_button)

        btnLogIn.setOnClickListener{
            val intent = Intent(this, LogIn::class.java)
            startActivity(intent)
        }

        btnSignUp.setOnClickListener{
            val intent = Intent(this, SignIn::class.java)
            startActivity(intent)
        }
    }
}