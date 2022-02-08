package com.bothofus.myaudioandvideoapp

import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication


//Todo: Add Google Services Plugin later
class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        MultiDex.install(this)

    }
}