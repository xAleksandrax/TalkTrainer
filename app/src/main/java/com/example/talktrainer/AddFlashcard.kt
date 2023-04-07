package com.example.talktrainer

import android.app.ProgressDialog
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddFlashcard : AppCompatActivity() {

    private lateinit var wordToTranslate: EditText
    private lateinit var translatedWord: TextView
    private lateinit var addBtn: Button
    private lateinit var titleFlashcard: EditText
    private lateinit var db: AppDatabase
    val translations = HashMap<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_add_flashcard)

        wordToTranslate = findViewById(R.id.word)
        translatedWord = findViewById(R.id.translatedWord)
        titleFlashcard = findViewById(R.id.title)
        addBtn = findViewById(R.id.add_button)
        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "mydb").build()

        addBtn.setOnClickListener{
            validateData()
        }
    }

    private fun getFlashcards(): List<Flashcard> {
        return db.flashcardDao().getFlashcardsForUser(1)
    }

    private var sourceText = ""
    private fun validateData() {
        sourceText = wordToTranslate.text.toString().trim()

        if(sourceText.isEmpty()){
            Toast.makeText(this,"Add word", Toast.LENGTH_SHORT).show()
        }
        else{
            startTranslation()
        }
    }

    private fun startTranslation() {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.POLISH)
            .setTargetLanguage(TranslateLanguage.ENGLISH)
            .build()
        val polishEnglishTranslator = Translation.getClient(options)

        var conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()
        polishEnglishTranslator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                polishEnglishTranslator.translate(sourceText)
//                        sprawdzac czy slowko juz jest
                    .addOnSuccessListener { translatedText ->
                        val flashcard = Flashcard(
                            title = titleFlashcard.text.toString(),
                            word = sourceText,
                            translation = translatedText,
                            userId = 1 // id użytkownika, którego dotyczy fiszka
                        )
                        lifecycleScope.launch {
                            val flashcardDao = withContext(Dispatchers.IO) {
                                db.flashcardDao().insertFlashcard(flashcard)
                            }
                        }

                        lifecycleScope.launch {
                            val flashcards = withContext(Dispatchers.IO) {
                                db.flashcardDao().getFlashcardsForUser(1)
                            }
                            var allWordsAndTranslations = ""
                            for (flashcard in flashcards) {
                                if (flashcard.title == titleFlashcard.text.toString()) {
                                    allWordsAndTranslations += "${flashcard.word} - ${flashcard.translation}\n"
                                }
                            }
                            translatedWord.text = allWordsAndTranslations
                        }
                    }

            }
    }
}

