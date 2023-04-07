package com.example.talktrainer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyFlashcards : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var words: TextView
    private lateinit var title: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_flashcards)

        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "mydb").build()
        words = findViewById(R.id.listFlashcard)
        title = findViewById(R.id.titleFlashcard)

        lifecycleScope.launch {
            val flashcards = withContext(Dispatchers.IO) {
                db.flashcardDao().getFlashcardsForUser(1)
            }
            var allWordsAndTranslations = ""
            var currentTitle = ""

            for (flashcard in flashcards) {
                if (flashcard.title != currentTitle) {
                    allWordsAndTranslations += "\n\n${flashcard.title}\n"
                    currentTitle = flashcard.title
                }
                allWordsAndTranslations += "${flashcard.word} - ${flashcard.translation}\n"
            }

            title.text = currentTitle
            words.text = allWordsAndTranslations
        }
    }
}