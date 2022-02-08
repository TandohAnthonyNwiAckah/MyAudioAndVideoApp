package com.bothofus.myaudioandvideoapp.ui


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.bothofus.myaudioandvideoapp.R
import com.bothofus.myaudioandvideoapp.adapter.RecordingListAdapter
import com.bothofus.myaudioandvideoapp.ui.fragment.RecordListFragment
import kotlinx.android.synthetic.main.activity_record_list.*

class RecordListActivity : AppCompatActivity(), RecordListFragment.OnListFragmentInteractionListener {

    override fun onCreate(args: Bundle?) {
        super.onCreate(args)
        setContentView(R.layout.activity_record_list)

        setSupportActionBar(toolbar)

        setupViewPager(vp)


    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = RecordingListAdapter(supportFragmentManager)
        adapter.addFrag(RecordListFragment(), "Audio".toUpperCase())
        viewPager.adapter = adapter
    }


}
