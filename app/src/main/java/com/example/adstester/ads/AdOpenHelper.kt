package com.example.adstester.ads

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.adstester.AppClass
import com.example.adstester.R
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd


class AdOpenHelper(private var myApplication: AppClass) : Application.ActivityLifecycleCallbacks,
    LifecycleObserver {
    private var loadCallback: AppOpenAd.AppOpenAdLoadCallback? = null
    var appOpenAd: AppOpenAd? = null
    private var currentActivity: Activity? = null
    private var isShowingAd = false

    init {
        myApplication.registerActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().lifecycle.addObserver(this);
    }

    fun fetchAd() {
        if (isAdAvailable()) {
            return
        }
        loadCallback =
            object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(p0: AppOpenAd) {
                    super.onAdLoaded(p0)
                    appOpenAd = p0
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                }
            }

        val request = getAdRequest()
        AppOpenAd.load(
            myApplication, myApplication.getString(R.string.open_id), request,
            AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback
        )
    }

    fun showAdIfAvailable() {
        if (!isShowingAd && isAdAvailable()) {
            Log.d("mTAG", "Will show ad.")
            val fullScreenContentCallback: FullScreenContentCallback =
                object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        appOpenAd = null
                        isShowingAd = false
                        fetchAd()
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {}
                    override fun onAdShowedFullScreenContent() {
                        isShowingAd = true
                    }
                }
            appOpenAd!!.fullScreenContentCallback = fullScreenContentCallback
            appOpenAd!!.show(currentActivity)
        } else {
            Log.d("mTAG", "Can not show ad.")
            fetchAd()
        }
    }

    fun getAdRequest(): AdRequest? {
        return AdRequest.Builder().build()
    }

    fun isAdAvailable(): Boolean {
        return appOpenAd != null
    }

    override fun onActivityCreated(p0: Activity, p1: Bundle?) {
        currentActivity = p0

    }

    override fun onActivityStarted(p0: Activity) {

            currentActivity = p0

    }

    override fun onActivityResumed(p0: Activity) {
            currentActivity = p0
            if (!isLoaded) {
                onStart()
            }

    }

    override fun onActivityPaused(p0: Activity) {
        currentActivity = p0
    }

    override fun onActivityStopped(p0: Activity) {
        currentActivity = p0
    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
        currentActivity = p0
    }

    override fun onActivityDestroyed(p0: Activity) {
        currentActivity = null
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        showAdIfAvailable()
        Log.d("mTAG", "onStart")
    }

}