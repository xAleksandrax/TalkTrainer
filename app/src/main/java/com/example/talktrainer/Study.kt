package com.example.talktrainer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.google.mlkit.nl.translate.Translation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Study : AppCompatActivity() {

    val translations = HashMap<String, String>()
    private lateinit var db: AppDatabase
    private lateinit var englishWord: Button
    private lateinit var checkButton: Button
    private lateinit var translation: EditText
    private lateinit var btnTryAgain: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_choose_to_learn)

        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "mydb").build()

        chooseFlashcard()
    }

    private fun chooseFlashcard() {
        val inflater = LayoutInflater.from(this@Study)
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
                        setContentView(R.layout.activity_study)
                        learn()
                    }
                }
            }
        }
    }

    private var currentEnglishWordIndex = 0
    private var score = 0
    private var sourceText = ""

    private fun learn() {
        englishWord = findViewById(R.id.englishWord)
        translation = findViewById(R.id.translation)
        checkButton = findViewById(R.id.checkBtn)

        val englishWords = translations.values.toList()
        val colorStateList = ContextCompat.getColorStateList(this, R.color.postMain)
        val colorStateList1 = ContextCompat.getColorStateList(this, R.color.rightAnswer)
        val colorStateList2 = ContextCompat.getColorStateList(this, R.color.wrongAnswer)

        translation.backgroundTintList = colorStateList

        if (currentEnglishWordIndex < englishWords.size) { // check if currentEnglishWordIndex is within range
            englishWord.text = englishWords[currentEnglishWordIndex]
            val correctTranslation = translations.filterValues { it == englishWord.text.toString() }.keys.firstOrNull()

            checkButton.setOnClickListener {
                sourceText = translation.text.toString().trim()

                if (sourceText.isEmpty()) {
                    Toast.makeText(this, "Add translation", Toast.LENGTH_SHORT).show()
                } else {
                    if (correctTranslation != null) {
                        if (sourceText.lowercase() == correctTranslation.lowercase()) {
                            translation.backgroundTintList = colorStateList1

                            score++
                            currentEnglishWordIndex++
                            Handler().postDelayed({
                                setContentView(R.layout.activity_study)
                                learn()
                            }, 2000)
                        } else {
                            translation.backgroundTintList = colorStateList2
                            currentEnglishWordIndex++
                            Handler().postDelayed({
                                setContentView(R.layout.activity_study)
                                learn()
                            }, 2000)
                        }
                    }
                }
            }
        } else {
            setContentView(R.layout.activity_score_learn)
            currentEnglishWordIndex = 0
            val container = findViewById<LinearLayout>(R.id.container_score)
            val inflater = LayoutInflater.from(this@Study)
            val textViewScore =
                inflater.inflate(R.layout.scoretextview, container, false) as TextView

            textViewScore.text = "$score/${englishWords.size}"

            container.addView(textViewScore)

            btnTryAgain = findViewById(R.id.tryAgain)

            btnTryAgain.setOnClickListener {
                resetGame()
            }
        }
    }

    private fun resetGame() {
        setContentView(R.layout.activity_play)
        currentEnglishWordIndex = 0
        score = 0
        learn()
    }
}
