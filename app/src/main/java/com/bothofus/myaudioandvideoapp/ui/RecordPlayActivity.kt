package com.bothofus.myaudioandvideoapp.ui

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bothofus.myaudioandvideoapp.R
import com.bothofus.myaudioandvideoapp.utils.Constants.MAX_DURATION
import com.bothofus.myaudioandvideoapp.utils.Util.formatTime
import kotlinx.android.synthetic.main.playe.*


@Suppress("DEPRECATION")
class RecordPlayActivity : AppCompatActivity(), MediaPlayer.OnCompletionListener {

    private var mp: MediaPlayer? = null
    private var timmer: CountDownTimer? = null
    private var timme: CountDownTimer? = null
    private var timePaused = 0
    internal var playing = false
    internal var timePlayed = 0
    internal var dur = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.playe)

        init()

    }

    fun init() {

        title = intent.getStringExtra("name")

        txt!!.visibility = View.GONE


        play!!.setImageDrawable(resources.getDrawable(R.drawable.ic_play))


        play!!.setOnClickListener {
            if (playing) {

            } else {
                play()
            }
        }

        stop!!.setOnClickListener {
            if (mp != null) {
                stop()
            }
        }


    }

    override fun onCompletion(mp: MediaPlayer) {
        play!!.isEnabled = true
        stop!!.isEnabled = true
        sek!!.progress = 100
        timmer?.onFinish()
        timme?.onFinish()

    }

    private fun play() {

        try {
            mp = MediaPlayer.create(this@RecordPlayActivity, Uri.parse(intent.getStringExtra("uri")))

            mp!!.setOnCompletionListener(this)
            mp!!.prepare()

        } catch (t: Throwable) {

        }

        mp!!.start()
        mp!!.seekTo(timePaused)


        playing = true


        if (timePlayed == 0) {
            if (mp != null) {
                dur = mp!!.duration
            }
        } else {
            if (mp != null) {
                dur = mp!!.duration - timePlayed
            }
        }

        timmer = object : CountDownTimer(dur.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                //do update here

                (dur - millisUntilFinished).toInt()

                if (playing) {
                    sek!!.progress = (1000 + dur - millisUntilFinished).toInt() * 100 / dur
                    timePlayed++
                }

            }

            override fun onFinish() {
                timmer?.cancel()
                //    txt.setText("00:00:00");
                txt!!.visibility = View.GONE
            }
        }.start()



        timme = object : CountDownTimer(MAX_DURATION.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val r = (MAX_DURATION - millisUntilFinished).toInt()
                if (playing) {
                    txt!!.visibility = View.VISIBLE
                    txt!!.text = formatTime(r)
                }

            }

            override fun onFinish() {
                timme?.cancel()
                mp!!.reset()
                txt!!.visibility = View.GONE
            }
        }.start()


        if (playing) {

            play!!.isEnabled = true
            stop!!.isEnabled = true
        }

    }

    private fun stop() {

        try {
            txt!!.visibility = View.GONE
            mp!!.stop()
            stop!!.isEnabled = false
            playing = false
            mp!!.seekTo(0)
            timmer?.onFinish()
            timme?.onFinish()
            timePlayed = 0
            play!!.isEnabled = true
            mp!!.release()
            play!!.setImageDrawable(resources.getDrawable(R.drawable.ic_play))
        } catch (t: Throwable) {

        }


    }

    override fun onBackPressed() {
        super.onBackPressed()
        this@RecordPlayActivity.finish()
        if (playing) {
            stop!!.isEnabled = false
            stop()
        } else {

        }


    }

}
