package com.example.talktrainer

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

class Play : AppCompatActivity() {

    private val translations = HashMap<String, String>()
    private lateinit var db: AppDatabase
    private lateinit var polishWord: Button
    private lateinit var englishWord1: Button
    private lateinit var englishWord2: Button
    private lateinit var englishWord3: Button
    private lateinit var btnTryAgain: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_choose)

        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "mydb").build()

        chooseFlashcard()
    }

    private var lastFlashcard: String = "N/A"
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
                    lastFlashcard = textViewTitle.text.toString()
                    setContentView(R.layout.activity_play)

                    lifecycleScope.launch {
                        val words = withContext(Dispatchers.IO) {
                            db.flashcardDao().getWordsForFlashcard(flashcard.id)
                        }

                        for (word in words) {
                            translations[word.word] = word.translation
                        }
                        setContentView(R.layout.activity_play)
                        play()
                    }
                }
            }
        }
    }

    private var currentPolishWordIndex = 0
    private var maxScore = 0
    private var score = 0
    private fun play() {
        polishWord = findViewById(R.id.polishWord)
        englishWord1 = findViewById(R.id.englishWord1)
        englishWord2 = findViewById(R.id.englishWord2)
        englishWord3 = findViewById(R.id.englishWord3)

        val polishWords = translations.keys.toList()
        val englishWords = translations.values.toMutableList()
        val colorStateList = ContextCompat.getColorStateList(this, R.color.postMain)
        val colorStateList1 = ContextCompat.getColorStateList(this, R.color.rightAnswer)
        val colorStateList2 = ContextCompat.getColorStateList(this, R.color.wrongAnswer)

        englishWord1.backgroundTintList = colorStateList
        englishWord2.backgroundTintList = colorStateList
        englishWord3.backgroundTintList = colorStateList

        if(englishWords.size >= 3){
            if ((currentPolishWordIndex < englishWords.size) && translations.size >= 3)
            {
                polishWord.text = polishWords[currentPolishWordIndex]

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
                        englishWord1.backgroundTintList = colorStateList1
                        englishWord2.backgroundTintList = colorStateList2
                        englishWord3.backgroundTintList = colorStateList2

                        score++
                        currentPolishWordIndex++
                        Handler().postDelayed({
                            setContentView(R.layout.activity_play)
                            play()
                        }, 2000)
                    }
                    else{
                        englishWord1.backgroundTintList = colorStateList2

                        if (englishWord2.text == correctTranslation) {
                            englishWord2.backgroundTintList = colorStateList1
                            englishWord3.backgroundTintList = colorStateList2
                        } else {
                            englishWord2.backgroundTintList = colorStateList2
                            englishWord3.backgroundTintList = colorStateList1
                        }

                        currentPolishWordIndex++
                        Handler().postDelayed({
                            setContentView(R.layout.activity_play)
                            play()
                        }, 2000)
                    }
                }

                englishWord2.setOnClickListener {
                    if (englishWord2.text == correctTranslation) {
                        englishWord1.backgroundTintList = colorStateList2
                        englishWord2.backgroundTintList = colorStateList1
                        englishWord3.backgroundTintList = colorStateList2

                        score++
                        currentPolishWordIndex++
                        Handler().postDelayed({
                            setContentView(R.layout.activity_play)
                            play()
                        }, 2000)
                    }
                    else{
                        englishWord2.backgroundTintList = colorStateList2

                        if (englishWord3.text == correctTranslation) {
                            englishWord3.backgroundTintList = colorStateList1
                            englishWord1.backgroundTintList = colorStateList2
                        } else {
                            englishWord3.backgroundTintList = colorStateList2
                            englishWord1.backgroundTintList = colorStateList1
                        }

                        currentPolishWordIndex++
                        Handler().postDelayed({
                            setContentView(R.layout.activity_play)
                            play()
                        }, 2000)
                    }
                }

                englishWord3.setOnClickListener {
                    if (englishWord3.text == correctTranslation) {
                        englishWord1.backgroundTintList = colorStateList2
                        englishWord2.backgroundTintList = colorStateList2
                        englishWord3.backgroundTintList = colorStateList1

                        score++
                        currentPolishWordIndex++
                        Handler().postDelayed({
                            setContentView(R.layout.activity_play)
                            play()
                        }, 2000)
                    }
                    else{
                        englishWord3.backgroundTintList = colorStateList2

                        if (englishWord2.text == correctTranslation) {
                            englishWord2.backgroundTintList = colorStateList1
                            englishWord1.backgroundTintList = colorStateList2
                        } else {
                            englishWord2.backgroundTintList = colorStateList2
                            englishWord1.backgroundTintList = colorStateList1
                        }

                        currentPolishWordIndex++
                        Handler().postDelayed({
                            setContentView(R.layout.activity_play)
                            play()
                        }, 2000)
                    }
                }
            }
            else {
                setContentView(R.layout.activity_score)
                val container = findViewById<LinearLayout>(R.id.container_score)
                val inflater = LayoutInflater.from(this@Play)
                val textViewScore = inflater.inflate(R.layout.scoretextview, container, false) as TextView

                textViewScore.text = "$score/${englishWords.size}"

                maxScore = englishWords.size

                container.addView(textViewScore)

                insertGameData()

                btnTryAgain = findViewById(R.id.tryAgain)

                btnTryAgain.setOnClickListener {
                    resetGame()
                }
            }
        }else {
            englishWord1.visibility = View.INVISIBLE
            englishWord2.visibility = View.INVISIBLE
            englishWord3.visibility = View.INVISIBLE
            polishWord.text = "Add at least 3 words to flashcard"
        }

    }
    private fun resetGame() {
        setContentView(R.layout.activity_play)
        currentPolishWordIndex = 0
        score = 0
        play()
    }

    private fun insertGameData() {
        val dbHelper = FlashcardDbHelper(this)
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(FlashcardDbHelper.COLUMN_NAME_FLASHCARD, lastFlashcard)
            put(FlashcardDbHelper.COLUMN_NAME_SCORE, score)
            put(FlashcardDbHelper.COLUMN_NAME_DATE, System.currentTimeMillis())
            put(FlashcardDbHelper.COLUMN_NAME_MAX_SCORE, maxScore)
        }

        db.insert(FlashcardDbHelper.TABLE_NAME, null, values)
        db.close()
    }
}