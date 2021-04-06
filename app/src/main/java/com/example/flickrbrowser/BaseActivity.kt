package com.example.flickrbrowser

import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

internal const val PHOTO_TRANSFER = "PHOTO_TRANSFER"
internal const val FLICKR_QUERY = "FLICKR_QUERY"

open class BaseActivity : AppCompatActivity() {

    private val TAG = "BaseActivity"
    internal fun activateToolbar(enableHome : Boolean){
        Log.d(TAG , "activateToolbar called")
        var toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(enableHome)
    }
}