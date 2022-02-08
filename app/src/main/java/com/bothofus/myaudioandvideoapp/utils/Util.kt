package com.bothofus.myaudioandvideoapp.utils

import android.content.Context
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

/***
@Developer: Tandoh Anthony Nwi-Ackah
@Email : anthony.tandoh@tanacom.io
@AppName: MyAudioAndVideoApp
@Country: GHANA
 **/
object Util {

    fun formatTime(time: Int): String {
        val hour =
            if (Integer.toString(time / (1000 * 60 * 60)).length == 1) "0" + Integer.toString(time / (1000 * 60 * 60)) else Integer.toString(
                time / (1000 * 60 * 60)
            )
        val minute =
            if (Integer.toString(time / (1000 * 60) % 60).length == 1) "0" + Integer.toString(time / (1000 * 60) % 60) else Integer.toString(
                time / (1000 * 60) % 60
            )
        val second =
            if (Integer.toString(time / 1000 % 60).length == 1) "0" + Integer.toString(time / 1000 % 60) else Integer.toString(
                time / 1000 % 60
            )
        return "$hour:$minute:$second"
    }

    fun convertDateToString(date: Date): String {
        val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
        return sdf.format(date)
    }

    fun initToast(c: Context, message: String) {
        Toast.makeText(c, message, Toast.LENGTH_SHORT).show()
    }


}
