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
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions

class AddFlashcard : AppCompatActivity() {

    private lateinit var wordToTranslate: EditText
    private lateinit var translatedWord: TextView
    private lateinit var addBtn: Button
    val translations = HashMap<String, String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_add_flashcard)

        wordToTranslate = findViewById(R.id.word)
        translatedWord = findViewById(R.id.translatedWord)
        addBtn = findViewById(R.id.add_button)

        addBtn.setOnClickListener{
            validateData()
        }
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
                    .addOnSuccessListener {translatedText ->
                        translatedWord.text = translatedText
                        translations[sourceText] = translatedText

                        var mapContent = ""
                        for (entry in translations) {
                            mapContent += "${entry.key} -> ${entry.value}\n"
                        }
                        translatedWord.text = mapContent
                    }
            }
    }
}

