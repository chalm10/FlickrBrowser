package com.example.flickrbrowser

import android.os.Bundle
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.content_photo_detail.*

class PhotoDetailsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_detail)

        activateToolbar(true)

        val photo = intent.getParcelableExtra(PHOTO_TRANSFER) as Photo
//        photo_author.text = photo.author
//        photo_title.text = photo.title
//        photo_tags.text = photo.tags

        Picasso.with(this).load(photo.link)
            .error(R.drawable.placeholder)
            .placeholder(R.drawable.placeholder)
            .into(photo_image)

        photo_image.setOnClickListener { Toast.makeText(this@PhotoDetailsActivity , "${photo.title}", Toast.LENGTH_LONG).show() }


    }

}
