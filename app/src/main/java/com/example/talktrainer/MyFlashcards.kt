package com.example.talktrainer

import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
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

        displayAllFlashcards() // wywołanie nowej metody
//        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "mydb").build()
//
//        lifecycleScope.launch {
//            val flashcards = withContext(Dispatchers.IO) {
//                db.flashcardDao().getFlashcardsForUser(1)
//            }
//            var currentTitle = ""
//            val container = findViewById<LinearLayout>(R.id.container)
//            val inflater = LayoutInflater.from(this@MyFlashcards)
//
//            for (flashcard in flashcards) {
//
//                if (flashcard.title != currentTitle) {
//                    // Jeśli tytuł jest inny, dodaj nowy textViewTitle do kontenera
//                    val textViewTitle = inflater.inflate(R.layout.textview, container, false) as TextView
//                    textViewTitle.text = flashcard.title
//                    textViewTitle.setBackgroundResource(R.drawable.radius3)
//                    container.addView(textViewTitle)
//
//                    currentTitle = flashcard.title
//                }
//
//                // Dodaj nowy textViewWords do kontenera
//                val textViewWords = inflater.inflate(R.layout.textview, container, false) as TextView
//                textViewWords.text = "${flashcard.word} - ${flashcard.translation}"
//                textViewWords.textSize = 25f
//                textViewWords.setTextColor(Color.GRAY)
////                textViewWords.setBackgroundResource(R.drawable.radius2)
//                container.addView(textViewWords)
//            }
//        }

    }
    private fun displayAllFlashcards() {
        lifecycleScope.launch {
            val flashcards = withContext(Dispatchers.IO) {
                db.flashcardDao().getAllFlashcards()
            }
            val container = findViewById<LinearLayout>(R.id.container)
            val inflater = LayoutInflater.from(this@MyFlashcards)

            for (flashcard in flashcards) {
                // Jeśli tytuł jest inny, dodaj nowy textViewTitle do kontenera
                val textViewTitle = inflater.inflate(R.layout.textview, container, false) as TextView
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
            }
        }
    }

}

