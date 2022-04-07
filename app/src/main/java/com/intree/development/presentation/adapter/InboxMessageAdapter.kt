package com.intree.development.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.intree_initialnoodling.adapter.viewholder.IntroductionMessageViewHolder
import com.example.intree_initialnoodling.adapter.viewholder.ReadMessageViewHolder
import com.example.intree_initialnoodling.adapter.viewholder.UnreadMessageViewHolder
import com.intree.development.R
import com.intree.development.domain.IntroductionMessage
import com.intree.development.domain.Message
import com.intree.development.domain.ReadMessage
import com.intree.development.domain.UnreadMessage

class InboxMessageAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    enum class ViewHolderType {
        READ, UNREAD, INTRODUCTION
    }

    //Mock DB goes here
    private var messageList: MutableList<Message> = mutableListOf()

    var onLoadMore: (() -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ViewHolderType.READ.ordinal -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_inbox_message_read, parent, false)
                return ReadMessageViewHolder(view)
            }
            ViewHolderType.UNREAD.ordinal -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_inbox_message_unread, parent, false)
                return UnreadMessageViewHolder(view)
            }
            ViewHolderType.INTRODUCTION.ordinal -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_inbox_message_introduction, parent, false)
                return IntroductionMessageViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_inbox_message_unread, parent, false)
                return UnreadMessageViewHolder(view)
            }
        }
    }
    // Manage onItemClick in this function: https://www.youtube.com/watch?v=q-MVlCI9l5k 18.30
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ReadMessageViewHolder -> {
                holder.avatarView?.setImageResource(messageList[position].avatar)
                holder.contentView?.text = messageList[position].content
                holder.nameView?.text = messageList[position].sender
                holder.timestampView?.text = messageList[position].timestamp
                holder.updateView()
            }
            is UnreadMessageViewHolder -> {
                holder.avatarView?.setImageResource(messageList[position].avatar)
                holder.contentView?.text = messageList[position].content
                holder.nameView?.text = messageList[position].sender
                holder.timestampView?.text = messageList[position].timestamp
                holder.updateView()
            }
            is IntroductionMessageViewHolder -> {
                holder.avatarView?.setImageResource(messageList[position].avatar)
                holder.contentView?.text = messageList[position].content
                holder.nameView?.text = messageList[position].sender
                holder.timestampView?.text = messageList[position].timestamp
                holder.updateView()
            }
        }

        if (position == messageList.size -1) {
            onLoadMore?.let {
                it()
            }
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (messageList[position]) {
            is ReadMessage -> ViewHolderType.READ.ordinal
            is UnreadMessage -> ViewHolderType.UNREAD.ordinal
            is IntroductionMessage -> ViewHolderType.INTRODUCTION.ordinal
            else -> ViewHolderType.UNREAD.ordinal 
        }
    }

    fun reload(list: MutableList<Message>) {
        this.messageList.clear()
        this.messageList.addAll(list)
        notifyDataSetChanged()
    }

    fun loadMore(list: MutableList<Message>) {
        this.messageList.addAll(list)
        notifyItemRangeChanged(this.messageList.size - messageList.size + 1, messageList.size)

    }



}