package com.intree.development.presentation.home.aspect

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.intree.development.R
import com.yanzhenjie.album.AlbumFile
import com.yanzhenjie.album.AlbumLoader


class AlbumMediaLoader : AlbumLoader {
    override fun load(imageView: ImageView, albumFile: AlbumFile) {
        load(imageView, albumFile.path)
    }

    override fun load(imageView: ImageView, url: String?) {
        Glide.with(imageView.context)
            .load(url)
            .error(R.drawable.album_ic_eye_white)
            .placeholder(R.drawable.album_ic_eye_white)
            .into(imageView)
    }
}