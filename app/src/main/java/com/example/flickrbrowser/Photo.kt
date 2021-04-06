package com.example.flickrbrowser

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Photo(var title : String , var author : String , var authorID : String , var link : String , var tags : String ,
            var image : String) : Parcelable{

    private val TAG = "Photo"

    override fun toString(): String {
        return "Photo(title='$title', author='$author', authorID='$authorID', link='$link', tags='$tags', image='$image')"
    }
}