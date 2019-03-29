package com.example.devtalks

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        register_button_register.setOnClickListener {
            performRegister()
        }
        // To open next activity ie login screen if user already has an account
    already_have_account_textView.setOnClickListener{
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}

    private fun performRegister() {
        val email = email_edittext_register.text.toString()
        val password = password_edittext_register.text.toString()
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Enter your credentials to continue", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("MainActivity", "Email is $email")
        Log.d("MainActivity", "Password is :$password")
//    Firebase authentication to create user with email and password
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener
                Log.d("Main", "Sucessfully created user using uid: ${it.result?.user?.uid}")
            }
            .addOnFailureListener {
                Log.d("Main", "Failiure ${it.message}")
                Toast.makeText(this,"Failure!! ${it.message}",Toast.LENGTH_SHORT).show()
            }
    }
}

