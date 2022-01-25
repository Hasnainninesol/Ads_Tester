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
            var count = 0
            while (count <= 20) {
                itemList.add("Title $count")
                count++
            }
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
}