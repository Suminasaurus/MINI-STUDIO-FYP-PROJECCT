package com.example.ministudio

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class Faq : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private var mList = ArrayList<LanguageData>()
    private lateinit var adapter: LanguageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faq)

        recyclerView = findViewById(R.id.recyclerView)
        searchView = findViewById(R.id.searchView)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        addDataToList()
        adapter = LanguageAdapter(mList)
        recyclerView.adapter = adapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }

        })
    }

    private fun filterList(query: String?) {

        if (query != null) {
            val filteredList = ArrayList<LanguageData>()
            for (i in mList) {
                if (i.title.lowercase(Locale.ROOT).contains(query)) {
                    filteredList.add(i)
                }
            }

            if (filteredList.isEmpty()) {
                Toast.makeText(this, "No Data found", Toast.LENGTH_SHORT).show()
            } else {
                adapter.setFilteredList(filteredList)
            }
        }
    }

    private fun addDataToList() {
        mList.add(
            LanguageData(
                "How to make online booking?",
                R.drawable.baseline_book_online_24,
                "You can use the Mini Studio app to make online reservation by simply clicking the online booking button and fill in the form"
            )
        )
        mList.add(
            LanguageData(
                "Is walk-in available as well?",
                R.drawable.sharp_directions_walk_24,
                "Yes Walk-in is available as well, but for convenience do check out the online reservation."
            )
        )
        mList.add(
            LanguageData(
                "Can I request for More Microphone?",
                R.drawable.baseline_mic_external_on_24,
                "The quantity of microphones in room is fixed. Any request for additional microphone shall not be entertained."
            )
        )
        mList.add(
            LanguageData(
                "Can you Smoke in Mini Studio?",
                R.drawable.baseline_smoke_free_24,
                "No, as a family-friendly karaoke establishment, we strictly enforce a no-smoking policy."
            )
        )
        mList.add(
            LanguageData(
                "Is there alcohol beverage? Or can I bring my own Alcohol?",
                R.drawable.baseline_no_drinks_24,
                "No, alcohol is strictly prohibited at all our premises, and customers are not allowed to bring their own."
            )
        )
        mList.add(
            LanguageData(
                "Can I bring My Food & Drinks to Mini Studio?",
                R.drawable.baseline_no_food_24,
               "No, outside food is prohibited within Mini Studio premises. Customers must obtain food and beverages exclusively from our Food Counter."
            )
        )
        mList.add(
            LanguageData(
                "Can i walk-in with my School Uniform?",
                R.drawable.baseline_school_24,
                "School Uniform is strictly not allowed within Mini Studio Premises."

            )
        )
        mList.add(
            LanguageData(
                "Can I change my Mini Studio Session time?",
                R.drawable.baseline_access_time_filled_24,
                "After payment has been completed, no further changes can be made."
            )
        )
    }
}