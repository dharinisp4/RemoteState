package com.example.assignment.appModules


import android.app.Application
import com.example.assignment.utils.NetworkManager
import dagger.hilt.android.HiltAndroidApp
@HiltAndroidApp
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        NetworkManager.registerNetworkConnections(this, null)

    }
}

// register observer
