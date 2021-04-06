package com.example.flickrbrowser

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.content_main.*

private const val TAG = "MainActivity"
class MainActivity : BaseActivity() , DownloadData.OnDownloadComplete ,
    GetFlickrJsonData.OnDataAvailable , RecyclerItemClickListener.OnRecyclerClickListener {

    private val flickrRecyclerViewAdapter = FlickrRecyclerViewAdapter(ArrayList())

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG , "onCreate : Called")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        activateToolbar(false)

//        downloadData.setDownloadCompletelistener(this)


        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addOnItemTouchListener(RecyclerItemClickListener(this , recyclerView , this))
        recyclerView.adapter = flickrRecyclerViewAdapter

        Log.d(TAG , "onCreate : Done")
    }

    override fun onResume() {
        super.onResume()
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val queryResult = sharedPref.getString(FLICKR_QUERY , "")
        if (queryResult != null) {
            if(queryResult.isNotEmpty() ){
                val url = createUri("https://www.flickr.com/services/feeds/photos_public.gne" , queryResult , "en-us" , true)
                val downloadData = DownloadData(this)
                downloadData.execute(url)
            }
        }

    }

    override fun onItemClick(view: View, position: Int) {
       Log.d(TAG , "onItemClick : Called")
        Toast.makeText(this , "Single tap at position ${position+1}" , Toast.LENGTH_SHORT).show()
    }

    override fun onItemLongClick(view: View, position: Int) {
        Log.d(TAG , "onItemLongClick : Called")
        val photo = flickrRecyclerViewAdapter.getPhoto(position)
        if(photo!=null){
            val intent = Intent(this , PhotoDetailsActivity::class.java)
            intent.putExtra(PHOTO_TRANSFER , photo)
            startActivity(intent)
        }

    }

    private fun createUri(baseUrl:String, searchTags:String, lang : String, matchAll : Boolean):String {
        Log.d(TAG, "createUri : Called")
        return Uri.parse(baseUrl).buildUpon()
            .appendQueryParameter("tags", searchTags)
            .appendQueryParameter("tagmode", if (matchAll) "ALL" else "ANY")
            .appendQueryParameter("lang", lang)
            .appendQueryParameter("format", "json")
            .appendQueryParameter("nojsoncallback", "1")
            .build().toString()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        Log.d(TAG , "onCreateOptionsMenu : Called")

        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d(TAG , "onOptionsItemSelected : Created")

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_search -> {
                startActivity(Intent(this , SearchActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
//    companion object{
//        private const val TAG = "MainActivity"
//    }
    override fun onDownloadComplete( data : String , status : DownloadStatus){
        if(status == DownloadStatus.OK){
            //Log.d(TAG , "onDownloadComplete called with data being $data")
            val getFlickrJsonData = GetFlickrJsonData(this)
            getFlickrJsonData.execute(data)
        }else{
            //download failed
            Log.d(TAG , "onDownloadComplete failed with status $status and error as $data")
        }
    }

    override fun onDataAvailable(data: List<Photo>) {
        Log.d(TAG , ".onDataAvailable : called")
        flickrRecyclerViewAdapter.loadNewData(data)

        Log.d(TAG , ".onDataAvailable done")

    }

    override fun onError(exception: Exception) {
        Log.e(TAG , ".onError called with error ${exception.message}")
    }
}
