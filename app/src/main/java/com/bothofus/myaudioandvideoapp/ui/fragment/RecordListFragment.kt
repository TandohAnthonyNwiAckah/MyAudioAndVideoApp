package com.bothofus.myaudioandvideoapp.ui.fragment

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.*
import android.widget.AbsListView.MultiChoiceModeListener
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.ListView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.bothofus.myaudioandvideoapp.R
import com.bothofus.myaudioandvideoapp.adapter.RecordingAdapter
import com.bothofus.myaudioandvideoapp.model.AudioModel
import com.bothofus.myaudioandvideoapp.persistence.Provider
import com.bothofus.myaudioandvideoapp.ui.RecordPlayActivity
import kotlinx.android.synthetic.main.record_frag.*
import java.io.File
import java.util.*

class RecordListFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor>, OnItemClickListener {
    private val ITEMS: MutableList<AudioModel?> = ArrayList()
    private var adapter: RecordingAdapter? = null

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = RecordingAdapter(requireContext(), ITEMS)


        loaderManager.initLoader(0, null, this)


    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) loaderManager.restartLoader(0, null, this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.record_frag, container, false)

        val lis = view.findViewById<ListView>(R.id.list)

        lis.onItemClickListener = this
        lis.adapter = adapter

        lis.onItemLongClickListener = OnItemLongClickListener { arg0, arg1, position, arg3 ->
            lis.setItemChecked(position, !adapter!!.isPositionChecked(position))
            false
        }
        lis.choiceMode = ListView.CHOICE_MODE_MULTIPLE_MODAL
        lis.setMultiChoiceModeListener(object : MultiChoiceModeListener {
            private var nr = 0
            override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
                return false
            }

            override fun onDestroyActionMode(mode: ActionMode) {
                adapter!!.clearSelection()
            }

            override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
                nr = 0
                val inflater = activity!!.menuInflater
                inflater.inflate(R.menu.audio_frag, menu)
                return true
            }

            override fun onActionItemClicked(mode: ActionMode, mitem: MenuItem): Boolean {

                when (mitem.itemId) {
                    R.id.action_delete -> {
                        nr = 0
                        val sp = PreferenceManager.getDefaultSharedPreferences(activity)
                        var del = 1
                        if (sp.getString(PREF_DELETE, "1") == "2") del = 2
                        var count = 0
                        for (a in adapter!!.currentCheckedPosition) {
                            val item = adapter!!.getItem(a) as AudioModel
                            val where = Provider.KEY_NAME + " = ?;"
                            val array = arrayOf(item.name)
                            if (del == 2) {
                                File(item.link).delete()
                            }
                            context!!.contentResolver.delete(Provider.CONTENT_URI, where, array)
                            Log.d("COUNT ", Integer.toString(count++))
                            adapter!!.notifyDataSetChanged()
                        }
                        adapter!!.clearSelection()
                        loaderManager.restartLoader(0, null, this@RecordListFragment)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            mode.finish()
                        }
                    }
                    R.id.action_share -> {
                        val audioUris = ArrayList<Uri>()
                        for (a in adapter!!.currentCheckedPosition) {
                            val i = adapter!!.getItem(a) as AudioModel
                            audioUris.add(Uri.parse(i.link))
                        }
                        val shareIntent = Intent(Intent.ACTION_SEND_MULTIPLE)
                        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, audioUris)
                        shareIntent.type = "audio/*"
                        startActivity(Intent.createChooser(shareIntent, "Share via"))
                    }
                }
                return true
            }

            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            override fun onItemCheckedStateChanged(
                mode: ActionMode,
                position: Int,
                id: Long,
                checked: Boolean
            ) {
                // TODO Auto-generated method stub
                if (checked) {
                    nr++
                    adapter!!.setNewSelection(position, checked)
                } else {
                    nr--
                    adapter!!.removeSelection(position)
                }
                mode.title = "$nr selected"
            }
        })



        return view
    }


    override fun onResume() {
        super.onResume()
        loaderManager.restartLoader(0, null, this)


        if (ITEMS.isEmpty()) {
            lott!!.visibility = View.VISIBLE
        } else {
            lott!!.visibility = View.GONE
        }

        adapter!!.notifyDataSetChanged()

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
        } else {
            throw RuntimeException("$context must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return CursorLoader(requireContext(), Provider.CONTENT_URI, null, null, null, null)
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor) {
        ITEMS.clear()
        while (data.moveToNext()) {
            val newItem = AudioModel(data)
            if (newItem.type!!.trim { it <= ' ' }.contains("audio")) ITEMS.add(newItem)
        }
        adapter!!.notifyDataSetChanged()
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {}


    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        val item = parent.adapter.getItem(position) as AudioModel
        val intent = Intent(context, RecordPlayActivity::class.java)
        intent.putExtra("uri", item.link)
        intent.putExtra("name", item.name)
        startActivity(intent)
    }

    interface OnListFragmentInteractionListener

    companion object {
        private const val ARG_COLUMN_COUNT = "column-count"
        private const val PREF_DELETE = "del"
    }


}