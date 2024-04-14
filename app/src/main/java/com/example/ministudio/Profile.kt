package com.example.ministudio

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ministudio.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Profile : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUserData()
        binding.apply {
            tvUsername.isEnabled = false
            tvEmail.isEnabled = false
            tvContact.isEnabled = false
            tvPassword.isEnabled = false

            editButton.setOnClickListener {
                tvUsername.isEnabled = !tvUsername.isEnabled
                tvEmail.isEnabled = !tvEmail.isEnabled
                tvContact.isEnabled = !tvContact.isEnabled
                tvPassword.isEnabled = !tvPassword.isEnabled
            }

            updateButton.setOnClickListener {
                val name = tvUsername.text.toString()
                val email = tvEmail.text.toString()
                val contact = tvContact.text.toString()
                val password = tvPassword.text.toString()

                updateUserData(name, email, contact, password)
            }
        }
    }

    private fun updateUserData(name: String, email: String, contact: String, password: String) {
        val customerId = auth.currentUser?.uid
        if (customerId != null) {
            val customerReference = database.getReference("Customer").child(customerId)

            val customerData = hashMapOf(
                "userName" to name,
                "userEmail" to email,
                "userContact" to contact,
                "userPassword" to password
            )
            customerReference.setValue(customerData).addOnSuccessListener {
                Toast.makeText(this, "Profile Update Successfully", Toast.LENGTH_SHORT).show()
            }
                .addOnFailureListener {
                    Toast.makeText(this, "Profile Update Failed", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun setUserData() {
        val customerId = auth.currentUser?.uid
        if (customerId != null) {
            val customerReference = database.getReference("Customer").child(customerId)

            customerReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val customerProfile = snapshot.getValue(UserDataModel::class.java)
                        if (customerProfile != null) {
                            binding.tvUsername.setText(customerProfile.userName)
                            binding.tvEmail.setText(customerProfile.userEmail)
                            binding.tvContact.setText(customerProfile.userContact)
                            binding.tvPassword.setText(customerProfile.userPassword)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle onCancelled event if needed
                }
            })
        }
    }
}
