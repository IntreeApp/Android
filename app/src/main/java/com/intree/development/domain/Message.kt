package com.intree.development.domain

import android.graphics.drawable.Drawable
import com.makeramen.roundedimageview.RoundedImageView

open class Message(
    var avatar: Int,
    var sender: String = "",
    var content: String = "",
    var timestamp: String = "",
    var isIntroduction: Boolean,
    var isRead: Boolean
)
