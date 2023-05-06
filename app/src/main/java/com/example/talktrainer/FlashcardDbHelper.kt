package com.example.talktrainer

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class FlashcardDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "flashcards.db"
        const val TABLE_NAME = "game_scores"
        const val COLUMN_NAME_FLASHCARD = "flashcard_name"
        const val COLUMN_NAME_SCORE = "score"
        const val COLUMN_NAME_DATE = "date"
        const val COLUMN_NAME_MAX_SCORE = "max_score"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = "CREATE TABLE $TABLE_NAME (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_NAME_FLASHCARD TEXT," +
                "$COLUMN_NAME_SCORE INTEGER," +
                "$COLUMN_NAME_DATE TEXT," +
                "$COLUMN_NAME_MAX_SCORE INTEGER" +
                ")"
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}
