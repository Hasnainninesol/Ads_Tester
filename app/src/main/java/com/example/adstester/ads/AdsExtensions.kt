package com.example.adstester.ads

import android.util.Log
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.adstester.R
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback


fun AppCompatActivity.loadBanner(addContainer: RelativeLayout, AdWidth: Int) {
    val adView = AdView(this)
    addContainer.addView(adView)
    adView.adSize =
        AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(applicationContext, AdWidth)
    adView.adUnitId = getString(R.string.banner_id)
    val adRequest: AdRequest = AdRequest.Builder().build()
    adView.loadAd(adRequest)
    adView.adListener = object : AdListener() {
        override fun onAdLoaded() {
            super.onAdLoaded()
            Log.d("ERROR", "Ad loaded")
        }

        override fun onAdFailedToLoad(p0: LoadAdError) {
            super.onAdFailedToLoad(p0)
            Log.d("ERROR", p0.message)
        }
    }
}

var mInterstitialAd: InterstitialAd? = null
var isLoaded = false
fun AppCompatActivity.showInterstitial() {
    if (isLoaded) {
        mInterstitialAd!!.show(this)
        doAfterAd()
    }
}

fun AppCompatActivity.doAfterAd() {
    mInterstitialAd!!.fullScreenContentCallback = object : FullScreenContentCallback() {
        override fun onAdDismissedFullScreenContent() {
            super.onAdDismissedFullScreenContent()
            isLoaded = false
            mInterstitialAd = null
            loadInterstitial()
        }

        override fun onAdFailedToShowFullScreenContent(p0: AdError) {
            super.onAdFailedToShowFullScreenContent(p0)
        }

        override fun onAdShowedFullScreenContent() {
            super.onAdShowedFullScreenContent()
        }
    }
}


fun AppCompatActivity.loadInterstitial() {
    val adRequest: AdRequest = AdRequest.Builder().build()
    InterstitialAd.load(
        this@loadInterstitial,
        getString(R.string.interstitial_id),
        adRequest,
        object : InterstitialAdLoadCallback() {
            override fun onAdLoaded(p0: InterstitialAd) {
                super.onAdLoaded(p0)
                mInterstitialAd = p0
                isLoaded = true
            }

            override fun onAdFailedToLoad(p0: LoadAdError) {
                super.onAdFailedToLoad(p0)
                mInterstitialAd = null
                isLoaded = false
            }
        })
}


