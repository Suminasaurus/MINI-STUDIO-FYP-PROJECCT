package com.example.ministudio

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BookingActivity : AppCompatActivity() {

    private lateinit var spinnerOutlets: Spinner
    private lateinit var tvDate: TextView
    private lateinit var tvTime: TextView
    private lateinit var btnShowDatePicker: Button
    private lateinit var spinnerRoomTypes: Spinner
    private lateinit var timePicker: TimePicker
    private lateinit var buttonSubmit: Button
    private lateinit var eTDuration: EditText
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_booking)

        // Initialize views
        spinnerOutlets = findViewById(R.id.spinnerOutlets)
        tvDate = findViewById(R.id.tvSelectDate)
        tvTime = findViewById(R.id.tvTime)
        btnShowDatePicker = findViewById(R.id.btnShowDatePicker)
        spinnerRoomTypes = findViewById(R.id.spinnerRoomTypes)
        timePicker = findViewById(R.id.timePicker)
        buttonSubmit = findViewById(R.id.buttonSubmit)
        eTDuration = findViewById(R.id.etDuration)

        // Set up date picker button click listener
        btnShowDatePicker.setOnClickListener {
            showDatePicker()
        }

        // Initialize Firebase database
        database = FirebaseDatabase.getInstance().reference

        // Initialize spinners with array adapters
        ArrayAdapter.createFromResource(
            this,
            R.array.outlets_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerOutlets.adapter = adapter
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.room_types_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerRoomTypes.adapter = adapter
        }

        // Set up time picker
        timePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
            val format = if (hourOfDay >= 12) "PM" else "AM"
            val displayHour = if (hourOfDay > 12) hourOfDay - 12 else if (hourOfDay == 0) 12 else hourOfDay
            tvTime.text = String.format(Locale.getDefault(), "%02d:%02d %s", displayHour, minute, format)
        }

        // Call insertRoomTypes function to insert room types into Firebase
        insertRoomTypes()

        // Set up button click listener
        buttonSubmit.setOnClickListener {
            val outlet = spinnerOutlets.selectedItem.toString()
            val date = tvDate.text.toString()
            val roomType = spinnerRoomTypes.selectedItem.toString()
            val hour = timePicker.hour
            val minute = timePicker.minute
            val duration = eTDuration.text.toString()

            // Calculate total price based on room type and duration
            val roomPricesRef = database.child("roomtypes").child(roomType)
            roomPricesRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val pricePerHour = snapshot.child("pricePerHour").getValue(Int::class.java) ?: 25
                    val durationHours = duration.toInt()
                    val totalPrice = pricePerHour * durationHours

                    // Convert totalPrice to String
                    val totalPriceString = totalPrice.toString()

                    // Create an intent to start PayActivity and pass booking details
                    val intent = Intent(this@BookingActivity, PayActivity::class.java)
                    intent.putExtra("outlet", outlet)
                    intent.putExtra("date", date)
                    intent.putExtra("roomType", roomType)
                    intent.putExtra("hour", hour)
                    intent.putExtra("minute", minute)
                    intent.putExtra("durationHours", durationHours)
                    intent.putExtra("totalPrice", totalPriceString) // Pass the total price as String
                    startActivity(intent)
                }

                override fun onCancelled(error: DatabaseError) {
                    showToast("Failed to retrieve room price: ${error.message}")
                }
            })
        }
    }

    private fun showDatePicker() {
        val today = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, monthOfYear, dayOfMonth)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)
                tvDate.text = formattedDate
            },
            today.get(Calendar.YEAR),
            today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)
        )

        // Set the minimum date to today to restrict past dates
        datePickerDialog.datePicker.minDate = today.timeInMillis
        datePickerDialog.show()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun insertRoomTypes() {
        val roomTypesRef = database.child("roomtypes")

        // Create RoomPrice objects for each room type

        val bandStudio = RoomPrice("band_studio", 25, "Band Studio for 3-4 people")

        // Push room types data to Firebase
        roomTypesRef.child("band_studio").setValue(bandStudio)
    }
}
