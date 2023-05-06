package com.example.talktrainer

import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class FlashcardAdapter(private val cursor: Cursor) :
    RecyclerView.Adapter<FlashcardAdapter.FlashcardViewHolder>() {
    private val dateFormatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

    class FlashcardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewFlashcard: TextView = itemView.findViewById(R.id.textViewFlashcard)
        val textViewScore: TextView = itemView.findViewById(R.id.textViewScore)
        val textViewDate: TextView = itemView.findViewById(R.id.textViewDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlashcardViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_flashcard, parent, false)
        return FlashcardViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FlashcardViewHolder, position: Int) {
        cursor.moveToPosition(position)
        val flashcard = cursor.getString(cursor.getColumnIndexOrThrow(FlashcardDbHelper.COLUMN_NAME_FLASHCARD))
        val score = cursor.getInt(cursor.getColumnIndexOrThrow(FlashcardDbHelper.COLUMN_NAME_SCORE))
        val dateMillis = cursor.getLong(cursor.getColumnIndexOrThrow(FlashcardDbHelper.COLUMN_NAME_DATE))
        val date = dateFormatter.format(Date(dateMillis))

        holder.textViewFlashcard.text = flashcard
        holder.textViewScore.text = score.toString()
        holder.textViewDate.text = date

        // Set different background colors for rows based on the position of the row
        if (position % 2 == 0) {
            holder.itemView.setBackgroundResource(R.drawable.radius2)
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.white))
        }
    }


    override fun getItemCount(): Int {
        return cursor.count
    }
}
