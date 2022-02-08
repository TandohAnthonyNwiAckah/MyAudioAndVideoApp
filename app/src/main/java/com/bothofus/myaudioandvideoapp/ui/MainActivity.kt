@file:Suppress("DEPRECATION")

package com.bothofus.myaudioandvideoapp.ui


import android.Manifest
import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bothofus.myaudioandvideoapp.R
import com.bothofus.myaudioandvideoapp.adapter.MainAdapter
import com.bothofus.myaudioandvideoapp.utils.Constants.PERMISSION
import com.bothofus.myaudioandvideoapp.utils.Constants.TAG
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.app_bar.*


//todo: Add Splash Intro Screen before MainActivity
class MainActivity : AppCompatActivity() {

    private var myLayoutManager: RecyclerView.LayoutManager? = null
    private var myAdapter: RecyclerView.Adapter<*>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "onCreate: ")

        setContentView(R.layout.activity_main)

        perm()

        collapse()

        init()

    }

    private fun init() {
        Log.d(TAG, "init: ")

        recycler_view.setHasFixedSize(true)

        myLayoutManager = LinearLayoutManager(this)

        recycler_view.addItemDecoration(Decor(1, dpToPx(5), true))

        recycler_view.itemAnimator = DefaultItemAnimator()

        recycler_view.layoutManager = myLayoutManager

        myAdapter = MainAdapter()

        recycler_view.adapter = myAdapter

    }


    override fun onBackPressed() {
        Log.d(TAG, "onBackPressed: ")

        this@MainActivity.finish()
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun perm() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val hasStoragePermission =
                checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            val hasAudioPermission = checkSelfPermission(Manifest.permission.RECORD_AUDIO)
            val hasExternalPermission =
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            val permissions = ArrayList<String>()

            if (hasStoragePermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }

            if (hasExternalPermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }

            if (hasAudioPermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.RECORD_AUDIO)
            }

            if (permissions.isNotEmpty()) {
                requestPermissions(permissions.toTypedArray(), PERMISSION)
            }

        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION -> for (i in permissions.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED)
                    Toast.makeText(
                        this, "Permission denied: " + permissions[i] + ". This may cause " +
                                "the app to behave abnormally", Toast.LENGTH_SHORT
                    ).show()
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private class Decor(
        private val spanCount: Int,
        private val spacing: Int,
        private val includeEdge: Boolean
    ) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view) // item position
            val column = position % spanCount // item column

            if (includeEdge) {
                outRect.left =
                    spacing - column * spacing / spanCount // spacing - column * ((1f / spanCount) * spacing)
                outRect.right =
                    (column + 1) * spacing / spanCount // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing
                }
                outRect.bottom = spacing // item bottom
            } else {
                outRect.left = column * spacing / spanCount // column * ((1f / spanCount) * spacing)
                outRect.right =
                    spacing - (column + 1) * spacing / spanCount // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing // item top
                }
            }
        }
    }

    private fun dpToPx(dp: Int): Int {
        val r = resources
        return Math.round(
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp.toFloat(),
                r.displayMetrics
            )
        )
    }

    private fun collapse() {

        collapsing_toolbar.title = " "

        appbar.setExpanded(true)

        // hiding & showing the title when toolbar expanded & collapsed
        appbar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            internal var isShow = false
            internal var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsing_toolbar.title = getString(R.string.app_name)
                    isShow = true
                } else if (isShow) {
                    collapsing_toolbar.title = " "
                    isShow = false
                }
            }
        })
    }

}




