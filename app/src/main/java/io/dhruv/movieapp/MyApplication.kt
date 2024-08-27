package io.dhruv.movieapp

import android.app.Application

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppContext.context = this
    }
}