package com.example.devtalks

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*

class LoginActivity:AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
     setContentView(R.layout.activity_login)

        already_have_account_textView.setOnClickListener {
//            val i=Intent(this,MainActivity::class.java)
            val email=email_edittext_login.text.toString()
            val password=password_edittext_login.text.toString()
            finish()
        }
    }
}