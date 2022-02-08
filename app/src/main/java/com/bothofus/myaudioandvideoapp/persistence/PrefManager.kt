package com.bothofus.myaudioandvideoapp.persistence

import android.content.Context
import android.content.SharedPreferences


/***
@Developer: Tandoh Anthony Nwi-Ackah
@Email : anthony.tandoh@tanacom.io
@AppName: MyAudioAndVideoApp
@Country: GHANA
 **/
class PrefManager private constructor() {

    fun putString(key: String, value: String) {
        val edit = sharedPreferences!!.edit()
        edit.putString(key, value)
        edit.apply()
    }

    fun getString(key: String): String? {

        return sharedPreferences!!.getString(key, "")
    }


    companion object {

        private var sharedPreferences: SharedPreferences? = null
        private val PREF_NAME = "PREFS"
        private var prefManager: PrefManager? = null

        fun getInstance(context: Context): PrefManager {
            if (prefManager == null) {
                prefManager = PrefManager()
                sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            }
            return prefManager as PrefManager
        }

    }
}
