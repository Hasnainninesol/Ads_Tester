package com.example.adstester.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.adstester.R
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView


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

fun Activity.loadNativeAd(newframeLayout: FrameLayout, LayoutId: Int, shimmer: ShimmerFrameLayout) {
    shimmer.startShimmer()
    val adloader = AdLoader.Builder(this, getString(R.string.nativ_id)).forNativeAd {
        val frameLayout = newframeLayout
        val adView = (getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
            LayoutId,
            null
        ) as NativeAdView
        populateUnifiedNativeAdView(it, adView)
        frameLayout.removeAllViews()
        frameLayout.addView(adView)
    }.withAdListener(object : AdListener() {
        override fun onAdLoaded() {
            super.onAdLoaded()
            shimmer.stopShimmer()
            shimmer.visibility = View.GONE
        }

    })
        .withNativeAdOptions(NativeAdOptions.Builder().build())
        .build()
    adloader.loadAds(AdRequest.Builder().build(), 3)
}

fun Activity.loadNativeAd(
    newframeLayout: FrameLayout,
    LayoutId: Int,
    shimmer: ShimmerFrameLayout,
    nativeAd: NativeAd
) {
    shimmer.startShimmer()
    val frameLayout = newframeLayout
    val adView = (getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
        LayoutId,
        null
    ) as NativeAdView
    populateUnifiedNativeAdView(nativeAd, adView)
    frameLayout.removeAllViews()
    frameLayout.addView(adView)
    shimmer.stopShimmer()
    shimmer.visibility = View.GONE
}


fun populateUnifiedNativeAdView(nativeAd: NativeAd, adView: NativeAdView) {
    adView.mediaView = adView.findViewById(R.id.ad_media) as MediaView
    adView.headlineView = adView.findViewById(R.id.ad_headline)
    adView.bodyView = adView.findViewById(R.id.ad_body)
    adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
    adView.iconView = adView.findViewById(R.id.ad_app_icon)
    adView.priceView = adView.findViewById(R.id.ad_price)
    adView.advertiserView = adView.findViewById(R.id.ad_advertiser)
    adView.storeView = adView.findViewById(R.id.ad_store)
    adView.starRatingView = adView.findViewById(R.id.ad_stars)
    (adView.headlineView as TextView).text = nativeAd.headline
    adView.mediaView.setMediaContent(nativeAd.mediaContent)
    if (nativeAd.body == null) {
        adView.bodyView.visibility = View.INVISIBLE
    } else {
        adView.bodyView.visibility = View.VISIBLE
        (adView.bodyView as TextView).text = nativeAd.body
    }
    if (nativeAd.callToAction == null) {
        adView.callToActionView.visibility = View.INVISIBLE
    } else {
        adView.callToActionView.visibility = View.VISIBLE
        (adView.callToActionView as Button).text = nativeAd.callToAction
    }
    if (nativeAd.icon == null) {
        adView.iconView.visibility = View.GONE
    } else {
        (adView.iconView as ImageView).setImageDrawable(nativeAd.icon.drawable)
        adView.iconView.visibility = View.VISIBLE
    }
    if (nativeAd.price == null) {
        adView.priceView.visibility = View.INVISIBLE
    } else {
        adView.priceView.visibility = View.VISIBLE
        (adView.priceView as TextView).text = nativeAd.price
    }
    if (nativeAd.store == null) {
        adView.storeView.visibility = View.INVISIBLE
    } else {
        adView.storeView.visibility = View.VISIBLE
        (adView.storeView as TextView).text = nativeAd.store
    }
    if (nativeAd.starRating == null) {
        adView.starRatingView.visibility = View.INVISIBLE
    } else {
        (adView.starRatingView as RatingBar).rating = nativeAd.starRating.toFloat()
        adView.starRatingView.visibility = View.VISIBLE
    }
    if (nativeAd.advertiser == null) {
        adView.advertiserView.visibility = View.INVISIBLE
    } else {
        (adView.advertiserView as TextView).text = nativeAd.advertiser
        adView.advertiserView.visibility = View.VISIBLE
    }
    adView.setNativeAd(nativeAd)
}



