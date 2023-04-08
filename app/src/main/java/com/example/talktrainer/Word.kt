package com.example.talktrainer

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "words", foreignKeys = [ForeignKey(entity = Flashcard::class, parentColumns = ["id"], childColumns = ["flashcard_id"], onDelete = ForeignKey.CASCADE)])
data class Word(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val word: String,
    val translation: String,
    @ColumnInfo(name = "flashcard_id") val flashcardId: Int
)
