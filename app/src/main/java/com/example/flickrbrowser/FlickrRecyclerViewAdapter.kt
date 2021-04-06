package com.example.flickrbrowser

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class FlickrImageViewHolder(v : View) : RecyclerView.ViewHolder(v){
    var thumbnail : ImageView = v.findViewById(R.id.imageView)
    var title : TextView = v.findViewById(R.id.textView)
}

class FlickrRecyclerViewAdapter( private var photoList : List<Photo>) : RecyclerView.Adapter<FlickrImageViewHolder>() {

    private val TAG = "RecyclerViewAdapter"

    override fun getItemCount(): Int {
      //  Log.d(TAG , "getItemCount called")
        return if (photoList.isNotEmpty()) photoList.size else 1
    }

    fun loadNewData(newPhotos : List<Photo>){
        photoList = newPhotos
        notifyDataSetChanged()
    }

    fun getPhoto(position: Int) : Photo?{
        return if(photoList.isNotEmpty()) photoList[position] else null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlickrImageViewHolder {
        //new view requested to create
        Log.d(TAG , "onCreateViewHolder called to request new view")
        val inflater = LayoutInflater.from(parent.context)
        val view  = inflater.inflate(R.layout.browse , parent , false)
        return FlickrImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: FlickrImageViewHolder, position: Int) {
        //new data loaded into existing view
        if(photoList.isEmpty()){
            holder.thumbnail.setImageResource(R.drawable.placeholder)
            holder.title.setText(R.string.no_photo)

        }else{
            val photoItem = photoList[position]
            //Log.d(TAG , "onBindViewHolder : ${photoItem.title} --> at index $position")
            Picasso.with(holder.thumbnail.context).load(photoItem.image)
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(holder.thumbnail)

            holder.title.text = photoItem.title
        }

    }
}