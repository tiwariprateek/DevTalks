package com.example.devtalks

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*

class LoginActivity:AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
     login_button_login.setOnClickListener {
         val email=email_edittext_login.text.toString()
         val password=password_edittext_login.text.toString()
         Log.d("Login","Login using email:$email , password $password")
         FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
             .addOnCompleteListener {
                 if (!it.isSuccessful)return@addOnCompleteListener
                 Log.d("Login","Loged in using Email and password with uid ${it.result?.user?.uid}")
             }
             .addOnFailureListener {
                 Log.d("Login","Failure with uid: ${it.message}")
             }
     }

//    Takes back to the initial screen when clicked on "Back to register text"
        back_to_register_textview.setOnClickListener {
            finish()
        }
        }
}
