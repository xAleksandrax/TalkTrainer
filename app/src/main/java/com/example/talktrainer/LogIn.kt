package com.example.talktrainer
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LogIn : AppCompatActivity() {

    private lateinit var loginBtn: Button
    private lateinit var editUser: EditText
    private lateinit var editPword: EditText
    private lateinit var db: AppDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_log_in)

        loginBtn = findViewById(R.id.login_button)
        editUser = findViewById(R.id.username)
        editPword = findViewById(R.id.password)
        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "mydb").build()

        val registerTextView = findViewById<TextView>(R.id.register)
        registerTextView.setOnClickListener {
            val intent = Intent(this, SignIn::class.java)
            startActivity(intent)
        }

        loginBtn.setOnClickListener{
            val useredtxt = editUser.text.toString()
            val pwordedtxt = editPword.text.toString()

            if (useredtxt.isEmpty() || pwordedtxt.isEmpty()) {
                Toast.makeText(this,"Add Username & Password", Toast.LENGTH_SHORT).show()
            } else {
                lifecycleScope.launch {
                    val user = withContext(Dispatchers.IO) {
                        db.userDao().getUserByUsername(useredtxt)
                    }
                    if (user != null && user.password == pwordedtxt) {
                        Toast.makeText(this@LogIn, "Login successful!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(applicationContext, FirstPage::class.java)
                        intent.putExtra("username", useredtxt)
                        startActivity(intent)

                    } else {
                        Toast.makeText(this@LogIn, "Wrong Username & Password", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}