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

        val gameCounts = getGameCounts()
        val gamesPlayedText = findViewById<TextView>(R.id.gamesPlayedText)
        val stringBuilder = StringBuilder()
        stringBuilder.append("\n")
        for ((flashcardName, gameCount) in gameCounts) {
            if(gameCount == 1){
                stringBuilder.append("$flashcardName: $gameCount game played\n")
            }
            else{
                stringBuilder.append("$flashcardName: $gameCount games played\n")
            }
        }
        gamesPlayedText.text = stringBuilder.toString()
    }


    private fun readGameData(): Cursor {
        val dbHelper = FlashcardDbHelper(this)
        val db = dbHelper.readableDatabase

        val projection = arrayOf(
            BaseColumns._ID,
            FlashcardDbHelper.COLUMN_NAME_FLASHCARD,
            FlashcardDbHelper.COLUMN_NAME_SCORE,
            FlashcardDbHelper.COLUMN_NAME_DATE,
            FlashcardDbHelper.COLUMN_NAME_MAX_SCORE
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

    private fun getGameCounts(): Map<String, Int> {
        val dbHelper = FlashcardDbHelper(this)
        val db = dbHelper.readableDatabase

        val projection = arrayOf(FlashcardDbHelper.COLUMN_NAME_FLASHCARD, "COUNT(*) AS count")
        val groupBy = FlashcardDbHelper.COLUMN_NAME_FLASHCARD

        val cursor = db.query(
            FlashcardDbHelper.TABLE_NAME,
            projection,
            null,
            null,
            groupBy,
            null,
            null
        )

        val gameCounts = mutableMapOf<String, Int>()
        while (cursor.moveToNext()) {
            val flashcardName = cursor.getString(cursor.getColumnIndexOrThrow(FlashcardDbHelper.COLUMN_NAME_FLASHCARD))
            val count = cursor.getInt(cursor.getColumnIndexOrThrow("count"))
            gameCounts[flashcardName] = count
        }
        cursor.close()

        return gameCounts
    }

}

