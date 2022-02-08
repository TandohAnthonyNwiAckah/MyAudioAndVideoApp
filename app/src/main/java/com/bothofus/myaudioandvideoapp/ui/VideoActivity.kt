package com.bothofus.myaudioandvideoapp.ui

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.bothofus.myaudioandvideoapp.R
import com.bothofus.myaudioandvideoapp.persistence.PrefManager
import com.bothofus.myaudioandvideoapp.persistence.PreferenceConstants
import com.bothofus.myaudioandvideoapp.utils.Constants.VREQUEST
import kotlinx.android.synthetic.main.activity_video_record.*


class VideoActivity : AppCompatActivity() {

    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_record)


        prefManager = PrefManager.getInstance(this)


        vrecord.setOnClickListener()
        {
            val videointent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            if (videointent.resolveActivity(packageManager) != null) {
                startActivityForResult(videointent, VREQUEST)
            }
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == VREQUEST && resultCode == RESULT_OK) {

            prefManager.putString(
                PreferenceConstants.VIDEO_URL,
                data!!.data.toString()
            )


        }
    }

}