package com.example.talktrainer

import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.BaseColumns
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class Statistics : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_statistics)
        displayGameData()
    }
    private fun displayGameData() {
        val cursor = readGameData()

        val recyclerViewData = findViewById<RecyclerView>(R.id.recyclerViewData)
        recyclerViewData.layoutManager = LinearLayoutManager(this)

        val adapter = FlashcardAdapter(cursor)
        recyclerViewData.adapter = adapter
    }

    private fun readGameData(): Cursor {
        val dbHelper = FlashcardDbHelper(this)
        val db = dbHelper.readableDatabase

        val projection = arrayOf(
            BaseColumns._ID,
            FlashcardDbHelper.COLUMN_NAME_FLASHCARD,
            FlashcardDbHelper.COLUMN_NAME_SCORE,
            FlashcardDbHelper.COLUMN_NAME_DATE
        )

        val sortOrder = "${FlashcardDbHelper.COLUMN_NAME_SCORE} DESC"

        return db.query(
            FlashcardDbHelper.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            sortOrder
        )
    }
}

