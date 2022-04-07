package com.intree.development.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.intree.development.data.model.IntroduceHistoryUIModel
import com.intree.development.databinding.ItemIntroduceHistoryBinding
import com.intree.development.presentation.interfaces.IintroduceHistory

class IntroduceHistoryAdapter(private val listener: IintroduceHistory) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val list = ArrayList<IntroduceHistoryUIModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemBinding = ItemIntroduceHistoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ContactsVH(itemBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ContactsVH) {
            holder.bind(list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ContactsVH(private val itemBinding: ItemIntroduceHistoryBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(model: IntroduceHistoryUIModel) {
            val context = itemView.context

            Glide.with(context)
                .load(model.myImage)
                .override(150, 150)
                .into(itemBinding.imgRoundedProfilePhoto)

            Glide.with(context)
                .load(model.friendImage)
                .override(150, 150)
                .into(itemBinding.imgRoundedFriend)

            itemBinding.tvFriendName.text = model.friendName
            itemBinding.tvName.text = model.myName


        }
    }

    fun setData(data: ArrayList<IntroduceHistoryUIModel>) {
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }
}