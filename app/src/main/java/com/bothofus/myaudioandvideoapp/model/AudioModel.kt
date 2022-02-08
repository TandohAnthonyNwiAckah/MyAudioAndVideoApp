package com.bothofus.myaudioandvideoapp.model

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import com.bothofus.myaudioandvideoapp.persistence.Provider
import com.bothofus.myaudioandvideoapp.utils.Util
import java.util.*


class AudioModel {
    var type: String? = null
        private set
    private var dateTaken: String? = null
    var link: String? = null
        private set
    var dateModified: String? = null
        private set
    var name: String? = null

    constructor(type: String, name: String, link: String) {
        this.type = type
        this.link = link
        this.name = name
        this.dateTaken = Util.convertDateToString(Date(System.currentTimeMillis()))
        this.dateModified = Util.convertDateToString(Date(System.currentTimeMillis()))
    }

    @SuppressLint("Range")
    constructor(data: Cursor) {
        this.type = data.getString(data.getColumnIndex(Provider.KEY_TYPE))
        this.name = data.getString(data.getColumnIndex(Provider.KEY_NAME))
        this.link = data.getString(data.getColumnIndex(Provider.KEY_LINK))
        this.dateTaken = data.getString(data.getColumnIndex(Provider.KEY_DATE_TAKEN))
        this.dateModified = data.getString(data.getColumnIndex(Provider.KEY_DATE_MODIFIED))
    }

    val contentValues: ContentValues
        get() {
            val c = ContentValues()
            c.put(Provider.KEY_TYPE, this.type)
            c.put(Provider.KEY_DATE_MODIFIED, this.dateModified)
            c.put(Provider.KEY_DATE_TAKEN, this.dateTaken)
            c.put(Provider.KEY_NAME, this.name)
            c.put(Provider.KEY_LINK, this.link)
            return c
        }

}
