package com.example.talktrainer
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class FirstPage : AppCompatActivity() {
    private lateinit var addBtn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_first_page)

        addBtn = findViewById(R.id.addFlashcard)

        addBtn.setOnClickListener{
            val intent = Intent(applicationContext, AddFlashcard::class.java)
            startActivity(intent)
        }
    }
}
