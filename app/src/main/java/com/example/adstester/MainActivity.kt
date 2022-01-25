package com.example.adstester

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import com.example.adstester.ads.loadBanner
import com.example.adstester.ads.loadInterstitial
import com.example.adstester.ads.loadNativeAd
import com.example.adstester.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var parentBinding: ActivityMainBinding
    private val binding get() = parentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val getAdSize = adSize
        loadBanner(binding.bannarLayout.adContainer, getAdSize)
        loadNativeAd(
            binding.nativeAd.flAdplaceholder,
            R.layout.native_layout,
            binding.nativeAd.shimmer
        )
        loadInterstitial()
        binding.btnInter.setOnClickListener {
            startActivity(Intent(this, ActivityB::class.java))
        }
    }


    private val adSize: Int
        get() {
            val display = windowManager.defaultDisplay
            val outMetrics = DisplayMetrics()
            display.getMetrics(outMetrics)

            val density = outMetrics.density

            var adWidthPixels = binding.bannarLayout.adContainer.width.toFloat()
            if (adWidthPixels == 0f) {
                adWidthPixels = outMetrics.widthPixels.toFloat()
            }

            return (adWidthPixels / density).toInt()
        }

    override fun onStop() {
        super.onStop()
    }
}