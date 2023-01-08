package com.example.loginapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance();

        Register.setOnClickListener{
            var intent =Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        Login.setOnClickListener{
            if(checking()) {
                var email = Email.text.toString()
                var password = Password.text.toString()
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this){
                        task->
                        if(task.isSuccessful){
                              var intent =Intent(this, LoggedIn::class.java)
                            intent.putExtra("email",email)
                            startActivity(intent)
                            Toast.makeText(this,"Login Successful", Toast.LENGTH_LONG).show()
                            finish()
                        }
                        else{
                            Toast.makeText(this,"Authentication Failed Wrong details", Toast.LENGTH_LONG).show()
                        }
                    }
            }
            else{
                Toast.makeText(this,"Enter the details", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun checking():Boolean{
        if(Email.text.toString().trim{it<=' '}.isNotEmpty() && Password.text.toString().trim{it<=' '}.isNotEmpty()){
            return true;
        }
        return false
    }
}