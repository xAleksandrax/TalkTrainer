package com.example.talktrainer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView

class FirstPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_page)

        // Pobierz Intent z poprzedniego ekranu
        val intent = intent

// Pobierz nazwę użytkownika z Intent
        val username = intent.getStringExtra("username")
        Log.d("FirstPage", "Nazwa użytkownika: $username")
        if (username.isNullOrEmpty()) {
            Log.d("FirstPage", "Nazwa użytkownika jest pusta lub null")
        }


// Znajdź TextView i ustaw jego tekst na nazwę użytkownika
        val tvUsername = findViewById<TextView>(R.id.tv_username)
        tvUsername.text = username



    }
}
