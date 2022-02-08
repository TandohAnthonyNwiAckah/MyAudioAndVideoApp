package com.bothofus.myaudioandvideoapp.utils

import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*


object Util {

    fun getAppName(ctx: Context, pkgName: String): String {
        try {
            val pm = ctx.packageManager
            val appInfo = pm.getApplicationInfo(pkgName, 0)
            val label = pm.getApplicationLabel(appInfo).toString()
            return label
        } catch (e: PackageManager.NameNotFoundException) {
            return ""
        }

    }

    fun getAppVersionName(ctx: Context, pkgName: String): String {
        try {
            val pm = ctx.packageManager
            val pkgInfo = pm.getPackageInfo(pkgName, 0)
            val ver = pkgInfo.versionName
            return ver
        } catch (e: PackageManager.NameNotFoundException) {
            return "0"
        }

    }

    fun convertDateToString(date: Date): String {
        val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
        return sdf.format(date)
    }

    fun connect(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val n = cm.activeNetwork
            if (n != null) {
                val nc = cm.getNetworkCapabilities(n)
                //It will check for both wifi and cellular network
                return nc!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(
                    NetworkCapabilities.TRANSPORT_WIFI
                )
            }
            return false
        } else {
            val netInfo = cm.activeNetworkInfo
            return netInfo != null && netInfo.isConnectedOrConnecting
        }
    }

    fun initToast(c: Context, message: String) {
        Toast.makeText(c, message, Toast.LENGTH_SHORT).show()
    }


}
