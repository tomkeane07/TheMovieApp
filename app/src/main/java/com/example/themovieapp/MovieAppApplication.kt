package com.example.themovieapp

import android.app.Application
import com.example.themovieapp.BuildConfig
import com.facebook.stetho.Stetho
import timber.log.Timber

open class MovieAppApplication : Application() {
    @Override
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
//              Stetho.initializeWithDefaults(this)

    }
}