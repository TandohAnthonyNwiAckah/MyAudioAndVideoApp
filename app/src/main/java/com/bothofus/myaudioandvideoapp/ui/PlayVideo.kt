package com.bothofus.myaudioandvideoapp.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.bothofus.myaudioandvideoapp.R
import com.bothofus.myaudioandvideoapp.persistence.PrefManager
import com.bothofus.myaudioandvideoapp.persistence.PreferenceConstants
import com.bothofus.myaudioandvideoapp.utils.Util.initToast
import kotlinx.android.synthetic.main.activity_play_video.*

class PlayVideo : AppCompatActivity() {

    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_play_video)

        init()

    }

    private fun init() {

        prefManager = PrefManager.getInstance(this)

        val videoUrl = prefManager.getString(PreferenceConstants.VIDEO_URL)

        if (videoUrl != "") {
            video.setVideoURI(videoUrl?.toUri())
            video.start()
            lott!!.visibility = View.GONE
        } else {
            lott!!.visibility = View.VISIBLE
            initToast(this, "No video available")
        }
    }

}
