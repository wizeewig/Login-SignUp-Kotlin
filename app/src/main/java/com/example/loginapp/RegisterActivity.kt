package com.example.loginapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth;
    private lateinit var db: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance()
        Continue.setOnClickListener{
            if(checkin()){
                var email=EmailRegister.text.toString()
                var password=PasswordRegister.text.toString()
                var name=Name.text.toString()
                var phone=Phone.text.toString()
//                auth.createUserWithEmailAndPassword(email,password)
//                    .addOnCompleteListener(this){
//                        task->
//                        if(task.isSuccessful){
//                            Toast.makeText(this,"User Added", Toast.LENGTH_LONG).show()
//                        }
//                        else{
//                            Toast.makeText(this,"User Not Added", Toast.LENGTH_LONG).show()
//                        }
//                    }
                val user = hashMapOf(
                    "Name" to name,
                    "Phone" to phone,
                    "email" to email
                )
                val users= db.collection("USERS")
                val query= users.whereEqualTo("email", email).get()
                    .addOnSuccessListener {
                        it->
                        if(it.isEmpty){
                            auth.createUserWithEmailAndPassword(email,password)
                                .addOnCompleteListener(this){
                                        task->
                                    if(task.isSuccessful){
                                        //Authentication successfull, now adding data in firestore
                                        users.document(email).set(user)
                                        val intent=Intent(this,LoggedIn::class.java)
                                        intent.putExtra("email",email)
                                        startActivity(intent)
                                        finish()
                                    }
                                    else{
                                        Toast.makeText(this,"Authentication Failed", Toast.LENGTH_LONG).show()
                                    }
                                }
                        }
                        else{
                            Toast.makeText(this,"User already registered", Toast.LENGTH_LONG).show()
                            val intent=Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        }
                    }
            }
            else{
                Toast.makeText(this,"Enter the details", Toast.LENGTH_LONG).show()

            }
        }
    }

    private fun checkin():Boolean{
        if(Name.text.toString().trim{it<=' '}.isNotEmpty() &&
            Phone.text.toString().trim{it<=' '}.isNotEmpty() &&
            EmailRegister.text.toString().trim{it<=' '}.isNotEmpty() &&
            PasswordRegister.text.toString().trim{it<=' '}.isNotEmpty()
        ){
            return true;
         }
        return false
    }
}