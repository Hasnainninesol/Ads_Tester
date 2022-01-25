package com.example.adstester

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adstester.adaptors.RecyclerAdaptor
import com.example.adstester.ads.showInterstitial
import com.example.adstester.databinding.ActivityBBinding
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import java.util.*
import kotlin.collections.ArrayList


class ActivityB : AppCompatActivity() {
    private lateinit var parentBinding: ActivityBBinding
    private val binding get() = parentBinding
    private var itemList: ArrayList<Any> = ArrayList()
    private lateinit var mAdoptor: RecyclerAdaptor
    private var mNativeAds: ArrayList<NativeAd> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentBinding = ActivityBBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showInterstitial()
        loadTitles()
        with(binding)
        {
            mAdoptor = RecyclerAdaptor(itemList, this@ActivityB)
            recycler.apply {
                layoutManager = LinearLayoutManager(this@ActivityB)
                adapter = mAdoptor
            }
            mAdoptor.notifyDataSetChanged()
        }


    }

    private fun loadTitles() {
        var count = 0
        while (count <= 20) {
            itemList.add(count, "Title $count")
            count++
        }
        loadAd()
    }

    private fun insertAdsInMenuItems() {
        if (mNativeAds.size <= 0) {
            return
        }
        val offset: Int = itemList.size / mNativeAds.size + 1
        var index = 0
        for (ad in mNativeAds) {
            itemList.add(index, ad)
            index += offset
        }
    }

    private fun loadAd() {
        val adbuilder = AdLoader.Builder(this, getString(R.string.nativ_id))
        var adloader: AdLoader? = null
        adloader = adbuilder.forNativeAd {
            mNativeAds.add(it)
            if (!adloader!!.isLoading) {
                insertAdsInMenuItems()
            }
        }.withAdListener(object : AdListener() {
            override fun onAdLoaded() {
                super.onAdLoaded()
                if (!adloader!!.isLoading) {
                    insertAdsInMenuItems()
                }

            }
        })
            .withNativeAdOptions(NativeAdOptions.Builder().build())
            .build()
        adloader.loadAds(AdRequest.Builder().build(), 3)
    }
}