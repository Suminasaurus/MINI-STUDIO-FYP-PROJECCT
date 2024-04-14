package com.example.ministudio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.ministudio.databinding.ActivityMain2Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity2 : AppCompatActivity() {

    //initialize all component
    private lateinit var auth: FirebaseAuth
    private lateinit var name: String
    private lateinit var password: String
    private lateinit var email: String
    private lateinit var contact:String
    private lateinit var database: DatabaseReference

    private val binding: ActivityMain2Binding by lazy {
        ActivityMain2Binding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //initialize firebase auth
        auth = Firebase.auth

        //initialize firebase database
        database = Firebase.database.reference

        binding.signButton.setOnClickListener {
            name = binding.signupUsername.text.toString().trim()
            email = binding.signupEmail.text.toString().trim()
            password = binding.signupPassword.text.toString().trim()
            contact = binding.signupContact.text.toString().trim()

            if (email.isBlank()||password.isBlank()||name.isBlank()||contact.isBlank()){
                Toast.makeText(this, "All fields are mandatory", Toast.LENGTH_SHORT).show()

            }else{
                createAccount(email,password)
            }
        }

    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                task ->
            if (task.isSuccessful){
                Toast.makeText(this, "Account Created Successfully",Toast.LENGTH_SHORT).show()
                saveData()
                val i = (Intent(this,LoginActivity::class.java))
                startActivity(i)
                finish()
            }else{
                Toast.makeText(this,"Account Creation Failed, Please Make Sure Password at least 6 characters",Toast.LENGTH_SHORT).show()
                Log.d("Account","createAccount: Failure",task.exception)
            }
        }
    }

    private fun saveData() {
        //retrieve data from input filed
        name = binding.signupUsername.text.toString().trim()
        email = binding.signupEmail.text.toString().trim()
        password = binding.signupPassword.text.toString().trim()
        contact = binding.signupContact.text.toString().trim()


        val customer = UserDataModel(name, email, password, contact)
        val customerId = FirebaseAuth.getInstance().currentUser!!.uid
        //save data to firebase
        database.child("Customer").child(customerId).setValue(customer)
    }
}