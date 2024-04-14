package com.example.ministudio

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Recyleviewlocation : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var locationList: ArrayList<Location>
    lateinit var imageList: Array<Int>
    lateinit var titleList: Array<String>
    lateinit var descList: Array<String>
    lateinit var detailImageList: Array<Int>
    private lateinit var searchList: ArrayList<Location>
    private lateinit var locationAdapter: LocationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recyleviewlocation)

        imageList = arrayOf(
            R.drawable.aeonmaluri,
            R.drawable.dpulzecyberjaya,
            R.drawable.quaysidemall,
            R.drawable.quietplacekl,
            R.drawable.toppenjb
        )

        titleList = arrayOf(
            " AEON MALURI ",
            " DPULZE CYBERJAYA ",
            " QUAYSIDE MALL ",
            " QUIET PLACE KL ",
            " TOPPEN MALL JB "
        )

        descList = arrayOf(
            getString(R.string.AeonMaluri),
            getString(R.string.DpulzeMall),
            getString(R.string.QuaysideMall),
            getString(R.string.QuietPlace),
            getString((R.string.Toppen))
        )

        detailImageList = arrayOf(
            R.drawable.aeonmaluri,
            R.drawable.dpulzecyberjaya,
            R.drawable.quaysidemall,
            R.drawable.quietplacekl,
            R.drawable.toppenjb
        )

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        locationList = arrayListOf<Location>()
        searchList = arrayListOf<Location>()
        getLocation()

        locationAdapter = LocationAdapter(searchList)
        recyclerView.adapter = locationAdapter

        locationAdapter.onItemClick = {
            val intent = Intent(this, DetailedLocation::class.java)
            val bundle = Bundle()
            bundle.putParcelable("Location", it)
            intent.putExtras(bundle)
            startActivity(intent)
        }
    }

    private fun getLocation() {
        for (i in imageList.indices) {
            val location = Location(
                imageList[i],
                titleList[i],
                descList[i],
                detailImageList[i]
            )
            locationList.add(location)
        }
        searchList.addAll(locationList)
        recyclerView.adapter = LocationAdapter(searchList)
    }
}
