package com.example.ministudio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.ministudio.databinding.ActivityLoginPageBinding
import com.example.ministudio.Home
import com.example.ministudio.MainActivity2
import com.example.ministudio.UserDataModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var email: String
    private lateinit var password: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    private val binding: ActivityLoginPageBinding by lazy {
        ActivityLoginPageBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //initialize firebase auth
        auth = Firebase.auth
        //initialize firebase database
        database = Firebase.database.reference

        binding.loginButton.setOnClickListener {
            //get info from edittext
            email = binding.loginEmail.text.toString().trim()
            password = binding.loginPassword.text.toString().trim()

            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "All fields are mandatory", Toast.LENGTH_SHORT).show()
            } else {
                createUserAccount(email, password)
            }

        }
    }

    private fun createUserAccount(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val customer = auth.currentUser
                Toast.makeText(this, "Login Successfully", Toast.LENGTH_SHORT).show()

                successLogin(customer)
            } else {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val customer = auth.currentUser
                        saveData()
                        successLogin(customer)

                    } else {
                        Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                        Log.d("Account", "createUserAccount: Authentication failed", task.exception)
                    }
                }
            }
        }
    }

    private fun saveData() {
        //get info from edittext
        email = binding.loginEmail.text.toString().trim()
        password = binding.loginPassword.text.toString().trim()

        val customer = UserDataModel(email, password)
        val customerId = FirebaseAuth.getInstance().currentUser?.uid
        customerId?.let {
            database.child("Customer").child(it).setValue(customer)
        }
    }

    private fun successLogin(customer: FirebaseUser?) {
        startActivity(Intent(this, Home::class.java))
        finish()

    }
}