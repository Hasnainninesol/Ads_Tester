package com.example.adstester

import android.app.Application
import com.example.adstester.ads.AdOpenHelper
import com.google.android.gms.ads.MobileAds

class AppClass : Application() {
    private var appOpenManager: AdOpenHelper? = null
    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this) {}
        appOpenManager = AdOpenHelper(this)
    }
}