package com.production.auctionapplication.util

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class Application : Application() {

    /**
     * Initialize the timber outside of the main thread
     */
    private val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        delayedInit()
    }

    private fun delayedInit() {
        applicationScope.launch {
            // Initialize the Timber library outside the main thread
            Timber.plant(Timber.DebugTree())
        }
    }
}