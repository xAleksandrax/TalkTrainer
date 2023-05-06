package com.example.talktrainer
import android.app.Notification
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView

class FirstPage : AppCompatActivity() {
    private lateinit var addBtn: Button
    private lateinit var statsBtn: Button
    private lateinit var myFlashcardBtn: Button
    private lateinit var playButton: Button
    private lateinit var studyButton: Button
    private lateinit var profileImg: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_first_page)

        addBtn = findViewById(R.id.addFlashcard)
        statsBtn = findViewById(R.id.button4)
        myFlashcardBtn = findViewById(R.id.button1)
        playButton = findViewById(R.id.button3)
        studyButton = findViewById(R.id.button2)
        profileImg = findViewById(R.id.imageView3)

        addBtn.setOnClickListener{
            val intent = Intent(applicationContext, AddFlashcard::class.java)
            startActivity(intent)
        }

        myFlashcardBtn.setOnClickListener{
            val intent = Intent(applicationContext, MyFlashcards::class.java)
            startActivity(intent)
        }

        playButton.setOnClickListener{
            val intent = Intent(applicationContext, Play::class.java)
            startActivity(intent)
        }

        studyButton.setOnClickListener{
            val intent = Intent(applicationContext, Study::class.java)
            startActivity(intent)
        }

        profileImg.setOnClickListener{
            val intent = Intent(applicationContext, com.example.talktrainer.Notification::class.java)
            startActivity(intent)
        }

        statsBtn.setOnClickListener{
            val intent = Intent(applicationContext, Statistics::class.java)
            startActivity(intent)
        }
    }
}
