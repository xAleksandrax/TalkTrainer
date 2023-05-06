package com.example.talktrainer

import android.app.ProgressDialog
import android.nfc.Tag
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date


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

        addBtn.setOnClickListener {
            validateData()
        }
    }

    private fun getFlashcards(): List<Flashcard> {
        return db.flashcardDao().getFlashcardsForUser(1)
    }

    private var sourceText = ""
    private fun validateData() {
        sourceText = wordToTranslate.text.toString().trim()

        if (sourceText.isEmpty()) {
            Toast.makeText(this, "Add word", Toast.LENGTH_SHORT).show()
        } else {
            addWordsToFlashcard()
        }
    }

    private fun addWordsToFlashcard() {
        val flashcardTitle = titleFlashcard.text.toString()
        lifecycleScope.launch {
            val existingFlashcard = withContext(Dispatchers.IO) {
                db.flashcardDao().getFlashcardByTitle(flashcardTitle)
            }
            if (existingFlashcard != null) {
                // Flashcard o danej nazwie już istnieje - dodaj słowa do istniejącej fiszki
                val flashcardId = existingFlashcard.id
                startTranslation(flashcardId)
            } else {
                // Tworzenie nowej fiszki i dodanie do niej słów
                val flashcard = Flashcard(
                    title = flashcardTitle, userId = 1 // id użytkownika, którego dotyczy fiszka
                )
                lifecycleScope.launch {
                    val flashcardId = withContext(Dispatchers.IO) {
                        db.flashcardDao().insertFlashcard(flashcard).toInt()
                    }
                    startTranslation(flashcardId)
                }
            }

        }
    }

    private fun startTranslation(flashcardId: Int) {
        val sourceText = wordToTranslate.text.toString().trim()
        val options = TranslatorOptions.Builder().setSourceLanguage(TranslateLanguage.POLISH)
            .setTargetLanguage(TranslateLanguage.ENGLISH).build()
        val polishEnglishTranslator = Translation.getClient(options)

        var conditions = DownloadConditions.Builder().requireWifi().build()
        polishEnglishTranslator.downloadModelIfNeeded(conditions).addOnSuccessListener {
                polishEnglishTranslator.translate(sourceText)
                    .addOnSuccessListener { translatedText ->
                        // Sprawdzenie, czy słowo już istnieje w bazie danych dla danej fiszki
                        lifecycleScope.launch {
                            val existingWord = withContext(Dispatchers.IO) {
                                db.flashcardDao().getWordForFlashcard(flashcardId, sourceText)
                            }
                            if (existingWord == null) {
                                val word = Word(
                                    word = sourceText,
                                    translation = translatedText,
                                    flashcardId = flashcardId
                                )
                                lifecycleScope.launch {
                                    withContext(Dispatchers.IO) {
                                        db.flashcardDao().insertWord(word)
                                    }
                                    Toast.makeText(
                                        this@AddFlashcard,
                                        "New word added to the flashcard",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    lifecycleScope.launch {
                                        val flashcards = withContext(Dispatchers.IO) {
                                            db.flashcardDao().getFlashcardsForUser(1)
                                        }
                                        var allWordsAndTranslations = ""
                                        for (flashcard in flashcards) {
                                            if (flashcard.id == flashcardId) {
                                                val words = withContext(Dispatchers.IO) {
                                                    db.flashcardDao().getWordsForFlashcard(flashcard.id)
                                                }
                                                allWordsAndTranslations += "\n"
                                                for (word in words) {
                                                    allWordsAndTranslations += "${word.word} - ${word.translation}\n"
                                                }
                                            }
                                        }
                                        translatedWord.text = allWordsAndTranslations
                                        translatedWord.setBackgroundResource(R.drawable.radius3)
                                    }
                                }
                            } else {
                                titleFlashcard.text.clear()
                                wordToTranslate.text.clear()
                                translatedWord.text = ""
                                Toast.makeText(
                                    this@AddFlashcard,
                                    "This word already exists in the flashcard",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
            }
    }
}

