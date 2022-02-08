package com.bothofus.myaudioandvideoapp.ui

import android.R.attr.value
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.bothofus.myaudioandvideoapp.R
import com.bothofus.myaudioandvideoapp.utils.Constants
import com.bothofus.myaudioandvideoapp.utils.Constants.DURATION


@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    override fun onCreate(args: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(args)

        Log.d(Constants.TAG, "onCreate: ")

        setContentView(R.layout.activity_splash)


        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController!!.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }


        if (!PrefM(this@SplashActivity).isFirstTimeLaunch()) {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("value", value)
            startActivity(intent)
            this@SplashActivity.finish()
        } else {
            metho()
        }


    }

    /*** Set Duration Method**/
    private fun metho() {
        PrefM(this@SplashActivity).setFirstTimeLaunch(false)
        Handler().postDelayed({
            this@SplashActivity.finish()
            val mainIntent = Intent(this@SplashActivity, MainActivity::class.java)
            this@SplashActivity.startActivity(mainIntent)
        }, DURATION.toLong()) //


    }

    /*** Preference Manager **/
    inner class PrefM(_context: Context) {

        val pref: SharedPreferences
        private val editor: SharedPreferences.Editor
        private val PRIVATE_MODE = 0


        init {
            pref = _context.getSharedPreferences("PREFS", PRIVATE_MODE)
            editor = pref.edit()
        }

        fun setFirstTimeLaunch(isFirstTime: Boolean) {
            editor.putBoolean("Fi", isFirstTime)
            editor.commit()
        }

        fun isFirstTimeLaunch(): Boolean {
            return pref.getBoolean("Fi", true)
        }

    }


}
