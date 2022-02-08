package com.bothofus.myaudioandvideoapp

import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication


/***
@Developer: Tandoh Anthony Nwi-Ackah
@Email : anthony.tandoh@tanacom.io
@AppName: MyAudioAndVideoApp
@Country: GHANA
 **/


//Todo: Add Google Services Plugin later
class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        MultiDex.install(this)

    }
}