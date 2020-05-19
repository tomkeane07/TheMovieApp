package com.example.themovieapp

import android.app.Application
import com.facebook.stetho.Stetho
import timber.log.Timber

class MovieAppApplication: Application() {
    @Override
    public override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
        Timber.plant(Timber.DebugTree())
    }
}