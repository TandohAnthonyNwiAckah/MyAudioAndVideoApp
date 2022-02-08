@file:Suppress("NAME_SHADOWING")

package com.bothofus.myaudioandvideoapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bothofus.myaudioandvideoapp.R
import com.bothofus.myaudioandvideoapp.model.AudioModel
import java.util.*


class RecordingAdapter(private val context: Context, items: List<*>) : BaseAdapter() {

    private val items: List<AudioModel> = items as List<AudioModel>

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val viewHolder: ViewHolder
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_records, null)
            viewHolder = ViewHolder()
            viewHolder.iconView = convertView!!.findViewById<View>(R.id.iconView) as ImageView

            viewHolder.nameView = convertView.findViewById<View>(R.id.nameView) as TextView
            viewHolder.modifiedView = convertView.findViewById<View>(R.id.modifiedView) as TextView
            viewHolder.typeView = convertView.findViewById<View>(R.id.typeView) as TextView
            convertView.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
        }
        viewHolder.nameView!!.text = items[position].name
        viewHolder.modifiedView!!.text = items[position].dateModified
        viewHolder.setIcon(items[position].type!!)
        viewHolder.setType(items[position].type!!)
        return convertView
    }

    @SuppressLint("UseSparseArrays")
    private var mSelection = HashMap<Int, Boolean>()

    fun setNewSelection(position: Int, value: Boolean) {
        mSelection[position] = value
        notifyDataSetChanged()
    }

    fun isPositionChecked(position: Int): Boolean {
        val result = mSelection[position]
        return result ?: false
    }

    val currentCheckedPosition: Set<Int>
        get() = mSelection.keys

    fun removeSelection(position: Int) {
        mSelection.remove(position)
        notifyDataSetChanged()
    }

    @SuppressLint("UseSparseArrays")
    fun clearSelection() {
        mSelection = HashMap<Int, Boolean>()
        notifyDataSetChanged()
    }

    internal class ViewHolder {
        var iconView: ImageView? = null
        var nameView: TextView? = null
        var modifiedView: TextView? = null
        var typeView: TextView? = null

        fun setIcon(type: String) {
            if (type.contains("audio"))
                iconView!!.setImageResource(R.drawable.ic_audio)
            else
                iconView!!.setImageResource(R.drawable.ic_unknown)

        }

        fun setType(type: String) {
            val extension: String
            if (type.contains("audio"))
                extension = "mp3"
            else
                extension = "unknown"

            typeView!!.text = "${type.toUpperCase()}:${extension.toUpperCase()}"
        }

    }

}