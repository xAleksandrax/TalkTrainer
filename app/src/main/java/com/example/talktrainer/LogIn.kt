package com.example.talktrainer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class LogIn : AppCompatActivity() {

    private lateinit var loginBtn: Button
    private lateinit var editUser: EditText
    private lateinit var editPword: EditText
    private lateinit var dbh: DBHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        loginBtn = findViewById(R.id.login_button)
        editUser = findViewById(R.id.username)
        editPword = findViewById(R.id.password)
        dbh = DBHelper(this)

        loginBtn.setOnClickListener{
            val useredtxt = editUser.text.toString()
            val pwordedtxt = editPword.text.toString()

            if (TextUtils.isEmpty(useredtxt) || TextUtils.isEmpty(pwordedtxt)){
                Toast.makeText(this,"Add Username & Password", Toast.LENGTH_SHORT).show()
            }
            else{
                val checkUser = dbh.checkUserPassword(useredtxt, pwordedtxt)
                if (checkUser==true){
                    Toast.makeText(this,"Login successful!",Toast.LENGTH_SHORT).show()
                    val intent = Intent(applicationContext, FirstPage::class.java)
                    startActivity(intent)
                }
                else{
                    Toast.makeText(this,"Wrong Username & Password",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}