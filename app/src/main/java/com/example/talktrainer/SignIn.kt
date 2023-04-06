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

class SignIn : AppCompatActivity() {

    private lateinit var uname: EditText
    private lateinit var pword: EditText
    private lateinit var cpword: EditText
    private lateinit var signupBtn: Button
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_sign_in)
        uname = findViewById(R.id.username1)
        pword = findViewById(R.id.password1)
        cpword = findViewById(R.id.confirm_password1)
        signupBtn = findViewById(R.id.signin_button)
        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "mydb").build()

        val loginTextView = findViewById<TextView>(R.id.login)
        loginTextView.setOnClickListener {
            val intent = Intent(this, LogIn::class.java)
            startActivity(intent)
        }

        signupBtn.setOnClickListener {
            val unametxt = uname.text.toString()
            val pwordtxt = pword.text.toString()
            val cpwordtxt = cpword.text.toString()

            if (TextUtils.isEmpty(unametxt) || TextUtils.isEmpty(pwordtxt) || TextUtils.isEmpty(cpwordtxt)){
                Toast.makeText(this,"Add Username, Password & Confirm Password",Toast.LENGTH_SHORT).show()
            } else {
                if (pwordtxt.equals(cpwordtxt)){
                    lifecycleScope.launch{
                        val user = withContext(Dispatchers.IO) {
                            db.userDao().getUserByUsername(unametxt)
                        }
                        if (user == null) {
                            withContext(Dispatchers.IO) {
                                db.userDao().insert(User(username = unametxt, password = pwordtxt))
                            }
                            Toast.makeText(this@SignIn, "Success!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(applicationContext, LogIn::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this@SignIn, "User already exists", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this,"Password not match",Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}