package com.example.themovieapp.utils

import android.app.Application
import com.example.themovieapp.BuildConfig
import com.facebook.stetho.Stetho
import timber.log.Timber

open class MovieAppApplication: Application() {
    @Override
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        if (BuildConfig.DEBUG) {
            if (!isUnitTesting()) {
                Stetho.initializeWithDefaults(this)
            }
        }
    }

    protected open fun  isUnitTesting(): Boolean{
        //return false;
        //until testApp class is ready
        return true
    }
}