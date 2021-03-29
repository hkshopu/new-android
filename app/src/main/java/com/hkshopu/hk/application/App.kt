package com.hkshopu.hk.application

import android.app.Application
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.mallotec.reb.localeplugin.LocaleConstant
import com.mallotec.reb.localeplugin.LocalePlugin
import com.tencent.mmkv.MMKV


class App : Application(), LifecycleOwner {


    override fun onCreate() {
        super.onCreate()
        instance = this
        LocalePlugin.init(this, LocaleConstant.RECREATE_CURRENT_ACTIVITY)
        FacebookSdk.sdkInitialize(this);
        AppEventsLogger.activateApp(this);
//        initStrict()
        initMMKV()

    }

    companion object {
        lateinit var instance: App
            private set
    }

    val lifecycleRegistry: LifecycleRegistry = LifecycleRegistry(this)
    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }
    private fun initStrict() {
//        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()
                    .detectAll()
                    .penaltyLog()
                    .build()
            )
        StrictMode.setVmPolicy(
            VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build()
        )
//        }
    }
    private fun initMMKV() {
        MMKV.initialize(this)
    }
}