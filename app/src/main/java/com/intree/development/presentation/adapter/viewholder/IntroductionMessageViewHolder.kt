package com.example.intree_initialnoodling.adapter.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.intree.development.R

import com.makeramen.roundedimageview.RoundedImageView
import java.lang.ref.WeakReference

class IntroductionMessageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private var view: WeakReference<View> = WeakReference(itemView)

    internal var avatarView: RoundedImageView? = null
    internal var nameView: TextView? = null
    internal var contentView: TextView? = null
    internal var timestampView: TextView? = null

    init {
        findView()
        setListener()

    }

    private fun findView() {
        avatarView = view.get()?.findViewById(R.id.introductionMessageAvatar)
        nameView = view.get()?.findViewById(R.id.introductionMessageName)
        contentView = view.get()?.findViewById(R.id.introductionMessageContent)
        timestampView = view.get()?.findViewById(R.id.introductionMessageTimestamp)

    }

    private fun setListener() {

    }

    fun updateView() {

    }
}