package com.example.talktrainer

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FlashcardDao {
    @Insert
    fun insertFlashcard(flashcard: Flashcard): Long

    @Query("SELECT * FROM flashcards WHERE user_id = :userId")
    fun getFlashcardsForUser(userId: Int): List<Flashcard>

    @Query("SELECT * FROM flashcards WHERE  title = :title")
    fun getFlashcardByTitle(title: String): Flashcard?

    @Query("SELECT * FROM flashcards")
    fun getAllFlashcards(): List<Flashcard>

    @Insert
    fun insertWord(word: Word): Long

    @Query("SELECT * FROM words WHERE flashcard_id = :flashcardId")
    fun getWordsForFlashcard(flashcardId: Int): List<Word>

    @Query("SELECT * FROM words WHERE flashcard_id = :flashcardId AND word = :word")
    fun getWordForFlashcard(flashcardId: Int, word: String): Word?

}

