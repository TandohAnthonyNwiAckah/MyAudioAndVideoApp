package com.bothofus.myaudioandvideoapp.ui


import android.annotation.SuppressLint
import android.content.DialogInterface
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.view.Window
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bothofus.myaudioandvideoapp.R
import com.bothofus.myaudioandvideoapp.model.AudioModel
import com.bothofus.myaudioandvideoapp.persistence.Provider
import com.bothofus.myaudioandvideoapp.utils.Constants
import com.bothofus.myaudioandvideoapp.utils.Constants.FOLDER
import com.bothofus.myaudioandvideoapp.utils.Constants.MAX_DURATION
import kotlinx.android.synthetic.main.activity_record.*
import java.io.File

class RecordActivity : AppCompatActivity(), MediaRecorder.OnErrorListener,
    MediaRecorder.OnInfoListener {

    private lateinit var recorder: MediaRecorder
    private lateinit var output: File
    private lateinit var fil: File
    lateinit var timer: CountDownTimer
    private val BASENAME = "recording.mp3"

    var AUDIO = "";

    public override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)

        Log.d(Constants.TAG, "onCreate: ")

        setContentView(R.layout.activity_record)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            AUDIO = getExternalFilesDir(null).toString() + File.separator + FOLDER;

        } else {
            AUDIO =
                Environment.getExternalStorageDirectory().absolutePath + File.separator + FOLDER;

        }

        init()


    }

    fun init() {

        stop!!.setOnClickListener(object : OnClickListener {
            override fun onClick(view: View) {
                try {
                    timer.onFinish()

                    txt!!.visibility = View.GONE

                    stop!!.visibility = View.GONE
                    record!!.visibility = View.VISIBLE
                } catch (e: Exception) {
                    Log.w(javaClass.simpleName, "Exception in stopping recorder", e)
                }

            }
        })

        record!!.setBackgroundResource(R.drawable.ic_record)

        record!!.setOnClickListener(object : OnClickListener {
            @SuppressLint("ObsoleteSdkInt")
            override fun onClick(view: View) {
                run {
                    stop!!.visibility = View.VISIBLE
                    record!!.visibility = View.GONE


                    timer = object : CountDownTimer(MAX_DURATION.toLong(), 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                            val r = (MAX_DURATION - millisUntilFinished).toInt()
                            txt!!.text = formatTime(r)
                        }

                        override fun onFinish() {
                            try {
                                recorder.stop()
                                stop!!.visibility = View.GONE
                                record!!.visibility = View.VISIBLE
                            } catch (e: Exception) {
                                Log.w(javaClass.simpleName, "Exception in stopping recorder", e)
                            }

                            timer.cancel()
                            recorder.reset()
                            recorder.release()


                            save()


                        }
                    }.start()





                    if (!File(AUDIO).exists()) {

                        if (!File(AUDIO).mkdirs()) {
                            Toast.makeText(
                                this@RecordActivity,
                                "Failed to create folder",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            return
                        }
                    }

                    Log.d("DEFAULT", "onClick: $AUDIO")


                    output = File(AUDIO, BASENAME)



                    recorder.setMaxDuration(MAX_DURATION)
                    recorder.setAudioSource(MediaRecorder.AudioSource.MIC)
                    recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)


                    if (Build.VERSION.SDK_INT < 26) {
                        recorder.setOutputFile(output.absolutePath)
                    } else {
                        recorder.setOutputFile(output)
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1) {
                        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                        recorder.setAudioEncodingBitRate(160 * 1024)
                    } else {
                        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                    }
                    recorder.setAudioChannels(2)

                    try {
                        recorder.prepare()

                        recorder.start()


                    } catch (e: Exception) {
                        Log.e(javaClass.simpleName, "Exception in preparing recorder", e)
                        Toast.makeText(this@RecordActivity, e.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        })

    }

    @SuppressLint("NewApi")
    public override fun onResume() {
        super.onResume()
        recorder = MediaRecorder()
        recorder.setOnErrorListener(this)
        recorder.setOnInfoListener(this)
    }

    public override fun onPause() {
        recorder.release()
//        recorder = ""
        super.onPause()
    }

    override fun onInfo(mr: MediaRecorder, what: Int, extra: Int) {
        var msg = ""

        when (what) {
            MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED -> msg =
                getString(R.string.max_duration)

            MediaRecorder.MEDIA_RECORDER_INFO_MAX_FILESIZE_REACHED -> msg =
                getString(R.string.max_size)
            MediaRecorder.MEDIA_RECORDER_INFO_UNKNOWN -> msg = getString(R.string.strange) + what
        }
        if (msg.trim { it <= ' ' } != "")
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
        else
            Toast.makeText(this, getString(R.string.strange) + what, Toast.LENGTH_LONG).show()
    }

    override fun onError(mr: MediaRecorder, what: Int, extra: Int) {
        Toast.makeText(this, R.string.strange, Toast.LENGTH_LONG).show()
    }

    fun save() {


        val edi = EditText(this@RecordActivity)
        edi.hint = "audioname"

        AlertDialog.Builder(this@RecordActivity)
            .setIcon(R.mipmap.ic_launcher)
            .setTitle(getString(R.string.app_name))
            .setMessage("")
            .setView(edi)
            .setNegativeButton("Cancel") { _, _ -> }
            .setPositiveButton("Save", DialogInterface.OnClickListener { _, _ ->

                fil = File(AUDIO)


                if (!fil.exists()) {
                    if (!fil.mkdirs()) {
                        Toast.makeText(
                            this@RecordActivity,
                            "Failed to save file",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@OnClickListener
                    }
                }

                var fn = edi.text.toString()
                if (fn.split(".".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray().size > 1) {

                    if (fn.split(".".toRegex()).dropLastWhile { it.isEmpty() }
                            .toTypedArray()[fn.split(".".toRegex()).dropLastWhile { it.isEmpty() }
                            .toTypedArray().size - 1] !== "mp3")
                        fn += ".mp3"
                } else
                    fn += ".mp3"

                val f = File(fil, fn)


                if (!output.renameTo(f)) {
                    Toast.makeText(
                        this@RecordActivity,
                        "File could not be saved to: " + f.path,
                        Toast.LENGTH_SHORT
                    ).show()
                    return@OnClickListener
                }

                val aud = AudioModel("audio", fn, Uri.fromFile(f).toString())
                val value = aud.contentValues

                val uri = contentResolver.insert(Provider.CONTENT_URI, value)
                contentResolver.notifyChange(uri!!, null)
                this@RecordActivity.finish()
            })
            .show()
    }

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

}
