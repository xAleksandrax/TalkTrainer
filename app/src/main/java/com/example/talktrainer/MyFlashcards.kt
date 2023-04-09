package com.example.talktrainer

import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyFlashcards : AppCompatActivity() {

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_my_flashcards)

        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "mydb").build()

        displayAllFlashcards()
    }
    private fun displayAllFlashcards() {
        lifecycleScope.launch {
            val container = findViewById<LinearLayout>(R.id.container)

            container.removeAllViews()

            val flashcards = withContext(Dispatchers.IO) {
                db.flashcardDao().getAllFlashcards()
            }

            val inflater = LayoutInflater.from(this@MyFlashcards)

            if(flashcards.isEmpty()) {
                val noFlashcardsTextView = TextView(this@MyFlashcards)
                noFlashcardsTextView.text = "No flashcards yet"
                noFlashcardsTextView.textSize = 20f
                noFlashcardsTextView.gravity = Gravity.CENTER
                container.addView(noFlashcardsTextView)
            }

            for (flashcard in flashcards) {
                // Jeśli tytuł jest inny, dodaj nowy textViewTitle do kontenera
                val textViewTitle = inflater.inflate(R.layout.textview, container, false) as TextView
                val btnDelete = inflater.inflate(R.layout.buttondelete, container, false) as Button
                textViewTitle.text = flashcard.title
                textViewTitle.setBackgroundResource(R.drawable.radius3)
                container.addView(textViewTitle)

                // Pobierz wszystkie słowa dla danej fiszki
                val words = withContext(Dispatchers.IO) {
                    db.flashcardDao().getWordsForFlashcard(flashcard.id)
                }

                for (word in words) {
                    // Dodaj nowy textViewWords do kontenera
                    val textViewWords = inflater.inflate(R.layout.textview, container, false) as TextView
                    textViewWords.text = "${word.word} - ${word.translation}"
                    textViewWords.textSize = 25f
                    textViewWords.setTextColor(Color.GRAY)
                    container.addView(textViewWords)
                }
                container.addView(btnDelete)

                btnDelete.setOnClickListener {
                    lifecycleScope.launch{
                        withContext(Dispatchers.IO){
                            db.flashcardDao().deleteByFlashcardTitle(flashcard.title)
                        }
                        displayAllFlashcards()
                    }
                }
            }
        }
    }
}

