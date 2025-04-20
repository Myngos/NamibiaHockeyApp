package com.example.hockeyapp


import android.app.Application
import com.google.firebase.FirebaseApp

class NamibiaHockeyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}