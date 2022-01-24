package com.example.adstester

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.adstester.ads.showInterstitial

class ActivityB : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b)
        showInterstitial()
    }
}