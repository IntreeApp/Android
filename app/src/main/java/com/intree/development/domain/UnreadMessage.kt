package com.intree.development.domain

import android.graphics.drawable.Drawable

class UnreadMessage(
    avatar: Int,
    sender: String = "",
    content: String = "",
    timestamp: String = "",
    isIntroduction: Boolean,
    isRead: Boolean
) : Message (avatar, sender, content, timestamp, isIntroduction, isRead) {
}