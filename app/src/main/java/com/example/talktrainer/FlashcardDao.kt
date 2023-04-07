package com.example.talktrainer

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FlashcardDao {
    @Insert
    fun insertFlashcard(flashcard: Flashcard)

    @Query("SELECT * FROM flashcards WHERE user_id = :userId")
    fun getFlashcardsForUser(userId: Int): List<Flashcard>

}
