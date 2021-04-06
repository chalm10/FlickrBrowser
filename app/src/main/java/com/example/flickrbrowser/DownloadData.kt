package com.example.flickrbrowser

import android.os.AsyncTask
import android.util.Log
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL


enum class DownloadStatus{
    OK , IDLE , NOT_INITIALISED , FAILED_OR_EMPTY , PERMISSIONS_ERROR , ERROR
}

private const val TAG = "DownloadData"

class DownloadData(private val listener : OnDownloadComplete) : AsyncTask<String , Void , String>(){
    private var downloadStatus = DownloadStatus.IDLE
//    private var listener : MainActivity? = null
//
//    fun setDownloadCompletelistener( callbackObject : MainActivity){
//        listener = callbackObject
//    }
    interface OnDownloadComplete{
        fun onDownloadComplete(data : String , status : DownloadStatus)
    }

    override fun onPostExecute(result: String) {
//        Log.d(TAG , "onPostExecute called with parameter as $result ")
        listener.onDownloadComplete(result , downloadStatus)
    }

    override fun doInBackground(vararg url: String?): String {
        if(url[0]==null){
            downloadStatus = DownloadStatus.NOT_INITIALISED
            return "No URL Specified"
        }
        try {
            downloadStatus = DownloadStatus.OK
            return URL(url[0]).readText()
        }catch (e : Exception){
            val errorMessage: String
            when(e){
                is MalformedURLException -> {
                    downloadStatus = DownloadStatus.NOT_INITIALISED
                    errorMessage = "doInBackground : Invalid URL ${e.message}"
                }
                is IOException -> {
                    downloadStatus = DownloadStatus.FAILED_OR_EMPTY
                    errorMessage = "doInBackground : IO Exception reading data ${e.message}"
                }
                is SecurityException -> {
                    downloadStatus = DownloadStatus.PERMISSIONS_ERROR
                    errorMessage = "doInBackground : Security Exception : needs permission ? ${e.message}"
                }
                else -> {
                    downloadStatus = DownloadStatus.ERROR
                    errorMessage = "doInBackground : Unknown Error ${e.message}"
                }
            }
            Log.e(TAG , errorMessage)
            return errorMessage
        }
    }

}