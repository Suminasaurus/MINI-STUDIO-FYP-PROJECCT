package com.example.ministudio

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Locale

class PayActivity : AppCompatActivity() {
    private val auth = FirebaseAuth.getInstance()

    private lateinit var tvUserName: TextView
    private lateinit var tvUserEmail: TextView
    private lateinit var tvOutlet: TextView
    private lateinit var tvRoomType: TextView
    private lateinit var tvBookingTime: TextView
    private lateinit var tvBookingDate: TextView
    private lateinit var tvDuration: TextView
    private lateinit var tvTotalPayment: TextView
    private lateinit var btnPay: Button
    private lateinit var btnHome: Button
    private lateinit var mDialog :Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay)

        tvUserName = findViewById(R.id.tvUsername)
        tvUserEmail = findViewById(R.id.tvUserEmail)
        tvOutlet = findViewById(R.id.tvOutlet)
        tvRoomType = findViewById(R.id.tvRoomType)
        tvBookingTime = findViewById(R.id.tvBookingTime)
        tvBookingDate = findViewById(R.id.tvBookingDate)
        tvDuration = findViewById(R.id.tvDuration)
        tvTotalPayment = findViewById(R.id.tvTotalPrice)
        btnPay = findViewById(R.id.btnPay)
        btnHome = findViewById(R.id.btnhome)

        // Retrieve booking details from intent extras
        val outlet = intent.getStringExtra("outlet")
        val date = intent.getStringExtra("date")
        val roomType = intent.getStringExtra("roomType")
        val hour = intent.getIntExtra("hour", 0)
        val minute = intent.getIntExtra("minute", 0)
        val totalPrice = intent.getStringExtra("totalPrice")
        val durationHours = intent.getIntExtra("durationHours", 0)
        val userId = auth.currentUser?.uid

        userId?.let { uid ->
            val userRef = FirebaseDatabase.getInstance().reference.child("Customer").child(uid)
            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userData = snapshot.getValue(UserDataModel::class.java)
                    userData?.let {
                        tvUserName.text = "User Name: ${it.userName}"
                        tvUserEmail.text = "User Email: ${it.userEmail}"
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle onCancelled event if needed
                }
            })
        }

        tvRoomType.text = "Room Type: $roomType"
        tvBookingTime.text = "Booking Time: ${formatTime(hour, minute)}"
        tvBookingDate.text = "Booking Date: ${formatDate(date)}"
        tvTotalPayment.text = "Total Payment: RM $totalPrice"
        tvOutlet.text = "Outlet: $outlet"
        tvDuration.text = "Booking Duration: $durationHours Hour"

        // Set up Pay button click listener
        btnPay.setOnClickListener {
            showPopup()
        }

        btnHome.setOnClickListener {
            val i = Intent(this, Home::class.java)
            startActivity(i)
        }
    }

    private fun formatTime(hour: Int, minute: Int): String {
        val format = if (hour >= 12) "PM" else "AM"
        val displayHour = if (hour > 12) hour - 12 else if (hour == 0) 12 else hour
        return String.format(Locale.getDefault(), "%02d:%02d %s", displayHour, minute, format)
    }

    private fun formatDate(date: String?): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return date?.let {
            val parsedDate = dateFormat.parse(it)
            dateFormat.format(parsedDate)
        } ?: ""
    }

    private fun showPopup() {
        mDialog = Dialog(this)
        mDialog.setContentView(R.layout.qrpopup)

        // Find views in the popup layout
        val qrImageView = mDialog.findViewById<ImageView>(R.id.qrIV)
        val notesTextView = mDialog.findViewById<TextView>(R.id.notesTV)
        val thanksTextView = mDialog.findViewById<TextView>(R.id.thanksTV)

        // Show the dialog
        mDialog.show()
    }
}
