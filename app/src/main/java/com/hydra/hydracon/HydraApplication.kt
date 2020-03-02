package com.hydra.hydracon

import android.app.Application
import timber.log.Timber
import timber.log.Timber.DebugTree

@Suppress("unused")
class HydraApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }
}
