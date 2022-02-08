@file:Suppress("DEPRECATION")

package com.bothofus.myaudioandvideoapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bothofus.myaudioandvideoapp.R
import com.bothofus.myaudioandvideoapp.model.MainModel
import com.bothofus.myaudioandvideoapp.ui.RecordActivity
import com.bothofus.myaudioandvideoapp.ui.RecordListActivity
import java.util.*


// TODO: Add pre loaded fonts

class MainAdapter : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    private val myList: MutableList<MainModel>

    init {
        myList = ArrayList()

        var mod = MainModel()
        mod.name = R.string.recordaudio
        mod.logo = R.drawable.ic_recorder
        myList.add(mod)

        mod = MainModel()
        mod.name = R.string.playdaudio
        mod.logo = R.drawable.ic_playlist
        myList.add(mod)


    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_main, viewGroup, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {

        val mod = myList[i]

        viewHolder.tvName.setText(mod.name)

        viewHolder.imLogo.setImageResource(mod.logo)

    }

    override fun getItemCount(): Int {
        return myList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imLogo: ImageView = itemView.findViewById(R.id.iv_logo)

        val tvName: TextView = itemView.findViewById(R.id.tv_name)

        private val context: Context = itemView.context

        init {
            itemView.setOnClickListener {
                var intent: Intent? = null

                when (position) {

                    0 -> intent = Intent(context, RecordActivity::class.java)
                    1 -> intent = Intent(context, RecordListActivity::class.java)


                }
                context.startActivity(intent)
            }


        }

    }


}