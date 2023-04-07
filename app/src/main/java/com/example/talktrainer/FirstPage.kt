package com.example.talktrainer
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class FirstPage : AppCompatActivity() {
    private lateinit var addBtn: Button
    private lateinit var myFlashcardBtn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_first_page)

        addBtn = findViewById(R.id.addFlashcard)
        myFlashcardBtn = findViewById(R.id.button1)

        addBtn.setOnClickListener{
            val intent = Intent(applicationContext, AddFlashcard::class.java)
            startActivity(intent)
        }

        myFlashcardBtn.setOnClickListener{
            val intent = Intent(applicationContext, MyFlashcards::class.java)
            startActivity(intent)
        }
    }
}
