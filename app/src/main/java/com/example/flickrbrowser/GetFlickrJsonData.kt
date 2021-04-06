package com.example.flickrbrowser

import android.os.AsyncTask
import android.util.Log
import org.json.JSONException
import org.json.JSONObject

class GetFlickrJsonData(private val listener : OnDataAvailable) : AsyncTask<String , Void , ArrayList<Photo>>() {
    private val TAG = "GetFlickrJsonData"

    interface OnDataAvailable{
        fun onDataAvailable(data : List<Photo>)
        fun onError(exception : Exception)
    }

    override fun onPostExecute(result: ArrayList<Photo>) {
        Log.d(TAG , ".onPostExecute : called")
        listener.onDataAvailable(result)
        Log.d(TAG , ".onPostExecute : done")
    }

    override fun doInBackground(vararg params: String): ArrayList<Photo> {
        Log.d(TAG , ".doInBackground : called")
        val photoList = ArrayList<Photo>()
        try {
            val jsonData = JSONObject(params[0])
            val itemsArray = jsonData.getJSONArray("items")
            for(i in 0 until itemsArray.length()){

                val jsonPhoto = itemsArray.getJSONObject(i)
                val title = jsonPhoto.getString("title")
                val author = jsonPhoto.getString("author")
                val authorID = jsonPhoto.getString("author_id")
                val tags = jsonPhoto.getString("tags")
                val jsonMedia = jsonPhoto.getJSONObject("media")
                val imageURL = jsonMedia.getString("m")
                val link = imageURL.replaceFirst("_m.jpg" , "_b.jpg")
                val photoObject = Photo(title , author , authorID , link , tags , imageURL)

                photoList.add(photoObject)
                Log.d(TAG , ".doInBackground $photoObject")
            }
        }catch (e:JSONException){
            e.printStackTrace()
            Log.e(TAG , ".doInBackground : Error parsing Json data ${e.message}")
            listener.onError(e)
            cancel(true
            )
        }
        return photoList
    }
}