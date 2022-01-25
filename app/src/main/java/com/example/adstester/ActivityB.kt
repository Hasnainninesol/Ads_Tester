package com.example.adstester

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adstester.adaptors.RecyclerAdaptor
import com.example.adstester.ads.showInterstitial
import com.example.adstester.databinding.ActivityBBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList


class ActivityB : AppCompatActivity() {
    private lateinit var parentBinding: ActivityBBinding
    private val binding get() = parentBinding
    private var itemList: ArrayList<String> = ArrayList()
    private lateinit var mAdoptor: RecyclerAdaptor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentBinding = ActivityBBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showInterstitial()

        CoroutineScope(Dispatchers.IO).launch {
            loadTitles()
        }.invokeOnCompletion {
            with(binding)
            {
                mAdoptor = RecyclerAdaptor(itemList, this@ActivityB)
                recycler.apply {
                    layoutManager = LinearLayoutManager(this@ActivityB)
                    adapter = mAdoptor
                }
            }
        }
    }

    private fun loadTitles() {

        var count = 0
        while (count <= 20) {
            when (count) {
                5 -> {
                    itemList.add(count, "Native")
                }
                10 -> {
                    itemList.add(count, "Native")
                }
                15 -> {
                    itemList.add(count, "Native")
                }
                else -> {
                    itemList.add(count, "Title $count")
                }
            }

            count++
        }

    }
}