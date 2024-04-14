package com.example.ministudio

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel

class Home : AppCompatActivity() {

    private lateinit var btnprofile: ImageButton
    private lateinit var btnlocation: ImageButton
    private lateinit var btnfaq: ImageButton
    private lateinit var btnbooking: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        btnprofile = findViewById(R.id.profilebtn)
        btnlocation = findViewById(R.id.locationbtn)
        btnfaq = findViewById(R.id.faqbtn)
        btnbooking = findViewById(R.id.bookingbtn)


        btnprofile.setOnClickListener {
            val i = Intent(this, Profile::class.java)
            startActivity(i)
        }

        btnfaq.setOnClickListener {
            val i = Intent(this, Faq::class.java)
            startActivity(i)
        }

        btnlocation.setOnClickListener {
            val i = Intent(this, Recyleviewlocation::class.java)
            startActivity(i)
        }

        btnbooking.setOnClickListener {
            val i =Intent(this, BookingActivity::class.java )
            startActivity(i)
        }



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val imageList = ArrayList<SlideModel>() // Create image list

// imageList.add(SlideModel("String Url" or R.drawable)
// imageList.add(SlideModel("String Url" or R.drawable, "title") You can add title

        imageList.add(SlideModel(R.drawable.slide1,"Promotion FnB"))
        imageList.add(SlideModel(R.drawable.slide2,"Promotion Grand Opening"))
        imageList.add(SlideModel(R.drawable.slide3,"New experience!"))
        imageList.add(SlideModel(R.drawable.highlight1, "Dpulze Shopping Center Grand Opening"))
        imageList.add(SlideModel(R.drawable.highlight2,"Ramadan Offer!"))

        val imageSlider = findViewById<ImageSlider>(R.id.image_slider)
        imageSlider.setImageList(imageList)
        imageSlider.setImageList(imageList, ScaleTypes.CENTER_INSIDE)
    }


}