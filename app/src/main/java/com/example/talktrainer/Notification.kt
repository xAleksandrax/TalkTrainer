    package com.example.talktrainer

import android.R
import android.annotation.TargetApi
import android.app.*
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.talktrainer.databinding.ActivityNotificationBinding
import java.text.DateFormat
import java.util.*


    class Notification : AppCompatActivity() {
    private lateinit var binding : ActivityNotificationBinding
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val timePicker = binding.timePicker
        timePicker.setIs24HourView(true)


        createNotificationChannel()
        binding.addButton.setOnClickListener {
            scheduleNot()
        }
    }

        private fun scheduleNot() {
            val intent = Intent(applicationContext, ScheduleNotification::class.java)
            val title = binding.title.text.toString()
            val message = binding.word.text.toString()
            intent.putExtra(titleExtra,title)
            intent.putExtra(messageExtra,message)

            val pendingIntent = PendingIntent.getBroadcast(
                applicationContext,
                notificationID,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val time = getTime()
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                time,
                pendingIntent
            )
            showAlert(time,title,message)
        }

        private fun showAlert(time: Long, title: String, message: String) {
            val date = Date(time)
            val dateFormat  = android.text.format.DateFormat.getLongDateFormat(applicationContext)
            val timeFormat  = android.text.format.DateFormat.getTimeFormat(applicationContext)

            AlertDialog.Builder(this)
                .setTitle("Your Talk Trainer Reminder")
                .setMessage(
                    "Title: " + title +
                    "\nMessage: " + message +
                    "\nAt: " + dateFormat.format(date) + " " + timeFormat.format(date)
                )
                .setPositiveButton("Thank you"){_,_ ->}
                .show()
        }

        private fun getTime(): Long {
            val minute = binding.timePicker.minute
            val hour = binding.timePicker.hour
            val day = binding.datePicker.dayOfMonth
            val month = binding.datePicker.month
            val year = binding.datePicker.year

            val calendar = Calendar.getInstance()
            calendar.set(year, month, day, hour, minute)
            return calendar.timeInMillis
        }


        @TargetApi(Build.VERSION_CODES.O)
        private fun createNotificationChannel() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = "Notif Channel"
                val desc = "A Description of the Channel"
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(channelID, name, importance)
                channel.description = desc
                val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            } else {
                Log.d(TAG, "Notification channels not supported on API level " + Build.VERSION.SDK_INT)
            }
        }
    }