package com.example.talktrainer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class SignIn : AppCompatActivity() {

    private lateinit var uname: EditText
    private lateinit var pword: EditText
    private lateinit var cpword: EditText
    private lateinit var signupBtn: Button
    private lateinit var db: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        uname = findViewById(R.id.username1)
        pword = findViewById(R.id.password1)
        cpword = findViewById(R.id.confirm_password1)
        signupBtn = findViewById(R.id.signin_button)
        db = DBHelper(this)

        val loginTextView = findViewById<TextView>(R.id.login)
        loginTextView.setOnClickListener {
            val intent = Intent(this, LogIn::class.java)
            startActivity(intent)
        }

        signupBtn.setOnClickListener{
            val unametxt = uname.text.toString()
            val pwordtxt = pword.text.toString()
            val cpwordtxt = cpword.text.toString()
            val savedata = db.insertdata(unametxt, pwordtxt)

            if (TextUtils.isEmpty(unametxt) || TextUtils.isEmpty(pwordtxt) || TextUtils.isEmpty(cpwordtxt)){
                Toast.makeText(this,"Add Username, Password & Confirm Password",Toast.LENGTH_SHORT).show()
            }
            else{
                if (pwordtxt.equals(cpwordtxt)){
                    if (savedata==true){
                        Toast.makeText(this,"Success!",Toast.LENGTH_SHORT).show()
                        val intent = Intent(applicationContext, LogIn::class.java)
                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(this,"User already exists",Toast.LENGTH_SHORT).show()
                    }
                }
                else{
                    Toast.makeText(this,"Password not match",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}