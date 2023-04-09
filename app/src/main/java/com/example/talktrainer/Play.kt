package com.example.talktrainer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

class Play : AppCompatActivity() {

    val translations = HashMap<String, String>()
    private lateinit var db: AppDatabase
    private lateinit var polishWord: Button
    private lateinit var englishWord1: Button
    private lateinit var englishWord2: Button
    private lateinit var englishWord3: Button
//    private lateinit var btnBack: Button
//    private lateinit var btnTryAgain: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_choose)

        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "mydb").build()

        chooseFlashcard()
    }

    private fun chooseFlashcard() {
        val inflater = LayoutInflater.from(this@Play)
        val container = findViewById<LinearLayout>(R.id.container)

        lifecycleScope.launch {
            val flashcardTitles = withContext(Dispatchers.IO) {
                db.flashcardDao().getAllFlashcards()
            }

            for (flashcard in flashcardTitles) {
                val textViewTitle =
                    inflater.inflate(R.layout.textview, container, false) as TextView
                textViewTitle.text = flashcard.title
                textViewTitle.setBackgroundResource(R.drawable.radius3)
                container.addView(textViewTitle)

                textViewTitle.setOnClickListener {
                    setContentView(R.layout.activity_play)

                    lifecycleScope.launch {
                        val words = withContext(Dispatchers.IO) {
                            db.flashcardDao().getWordsForFlashcard(flashcard.id)
                        }

                        for (word in words) {
                            translations[word.word] = word.translation
                        }
                        play()
                    }
                }
            }
        }
    }

    private var currentPolishWordIndex = 0
    private var score = 0
    private fun play() {
        polishWord = findViewById(R.id.polishWord)
        englishWord1 = findViewById(R.id.englishWord1)
        englishWord2 = findViewById(R.id.englishWord2)
        englishWord3 = findViewById(R.id.englishWord3)

        val polishWords = translations.keys.toList()

        polishWord.text = polishWords[currentPolishWordIndex]

        val englishWords = translations.values.toMutableList()
        val correctTranslation = translations[polishWords[currentPolishWordIndex]]
        val randomIndex = (0..2).random()

        englishWords.remove(correctTranslation)
        englishWords.shuffle()

        if (englishWords.size >= 3) {
            when (randomIndex) {
                0 -> {
                    englishWord1.text = correctTranslation
                    englishWord2.text = englishWords[0]
                    englishWord3.text = englishWords[1]
                }
                1 -> {
                    englishWord1.text = englishWords[0]
                    englishWord2.text = correctTranslation
                    englishWord3.text = englishWords[1]
                }
                2 -> {
                    englishWord1.text = englishWords[0]
                    englishWord2.text = englishWords[1]
                    englishWord3.text = correctTranslation
                }
            }
        } else {
            englishWord1.visibility = View.INVISIBLE
            englishWord2.visibility = View.INVISIBLE
            englishWord3.visibility = View.INVISIBLE
            polishWord.text = "Add at least 3 words to flashcard"
        }

        englishWord1.setOnClickListener {
            if (englishWord1.text == correctTranslation) {
                score++
                currentPolishWordIndex++
                play()
            }
            else{
                currentPolishWordIndex++
                play()
            }
        }

        englishWord2.setOnClickListener {
            if (englishWord2.text == correctTranslation) {
                score++
                currentPolishWordIndex++
                play()
            }
            else{
                currentPolishWordIndex++
                play()
            }
        }

        englishWord3.setOnClickListener {
            if (englishWord3.text == correctTranslation) {
                score++
                currentPolishWordIndex++
                play()
            }
            else{
                currentPolishWordIndex++
                play()
            }
        }
        if ((currentPolishWordIndex >= englishWords.size) && englishWords.size >= 3){
            setContentView(R.layout.activity_score)
            val container = findViewById<LinearLayout>(R.id.container_score)
            val inflater = LayoutInflater.from(this@Play)
            val textViewScore = inflater.inflate(R.layout.scoretextview, container, false) as TextView

            textViewScore.text = "$score/${englishWords.size}"

            container.addView(textViewScore)

//            btnBack = findViewById(R.id.back)
//            btnTryAgain = findViewById(R.id.tryAgain)
//
//            btnBack.setOnClickListener {
//                setContentView(R.layout.activity_choose)
//                chooseFlashcard()
//            }
//
//            btnTryAgain.setOnClickListener {
//                currentPolishWordIndex = 0
//                score = 0
//                play()
//            }
        }

    }
}