package com.example.talktrainer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Play : AppCompatActivity() {

    private lateinit var db: AppDatabase
    val translations = HashMap<String, String>()
    private lateinit var polishWord: Button
    private lateinit var englishWord1: Button
    private lateinit var englishWord2: Button
    private lateinit var englishWord3: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_choose)

        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "mydb").build()

        chooseFlashcard()
    }

    private fun chooseFlashcard(){
        val inflater = LayoutInflater.from(this@Play)
        val container = findViewById<LinearLayout>(R.id.container)

        lifecycleScope.launch {
            val flashcardTitles = withContext(Dispatchers.IO){
                db.flashcardDao().getAllFlashcards()
            }

            for (flashcard in flashcardTitles){
                val textViewTitle = inflater.inflate(R.layout.textview, container, false) as TextView
                textViewTitle.text = flashcard.title
                textViewTitle.setBackgroundResource(R.drawable.radius3)
                container.addView(textViewTitle)

                textViewTitle.setOnClickListener{
                    setContentView(R.layout.activity_play)

                    lifecycleScope.launch{
                        val words = withContext(Dispatchers.IO) {
                            db.flashcardDao().getWordsForFlashcard(flashcard.id)
                        }

                        for(word in words){
                            translations[word.word] = word.translation
                        }
                        play()
                    }
                }
            }
        }
    }

    private fun play() {
        polishWord = findViewById(R.id.polishWord)
        englishWord1 = findViewById(R.id.englishWord1)
        englishWord2 = findViewById(R.id.englishWord2)
        englishWord3 = findViewById(R.id.englishWord3)


    }
}