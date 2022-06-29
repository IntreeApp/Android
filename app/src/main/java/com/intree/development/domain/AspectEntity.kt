package com.intree.development.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class AspectEntity(
    var draft: String = "YES",
    var avatar: String = "",
    var bio: String = "",
    var cover: String = "",
    var headTitle: String = "",
    var infoEmail: String = "",
    var infoLink: String = "",
    var infoPhone: String = "",
    var infoRelation: String = "",
    var isActive: String = "YES",
    var logo: String = "",
    var modified: String = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault()).format(Date()),
    var shareLink: String = "",
    var tg: String = "",
    var title: String = "",
    //var photos: List<String> = ArrayList(),
    //var tags: List<String> = ArrayList(),
    //val created:
) : Parcelable