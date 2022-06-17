package com.intree.development.presentation.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.intree.development.R
import com.intree.development.data.uiModels.NetworkRecentsUIModel
import com.intree.development.data.uiModels.NetworkWideCardUIModel
import com.intree.development.databinding.ItemNetworkCardWideBinding

class NetworkVerticalAdapter:
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val list = ArrayList<NetworkWideCardUIModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemBinding = ItemNetworkCardWideBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(private val itemBinding: ItemNetworkCardWideBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
            fun bind(model: NetworkWideCardUIModel){
                itemBinding.nameTextView.text = model.name
                itemBinding.connectedStringTextView.text = model.connection

                Glide.with(itemView.context)
                    .load(
                        model.image
                    )
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .error(
                        ContextCompat.getDrawable(
                            itemView.context,
                            android.R.drawable.ic_menu_report_image
                        )
                    )
                    .into(object : CustomTarget<Drawable?>() {
                        override fun onLoadCleared(placeholder: Drawable?) {
                        }

                        override fun onResourceReady(
                            resource: Drawable,
                            transition: Transition<in Drawable?>?
                        ) {
                            itemBinding.avatarImageView.background = resource
                        }
                    })

            }

    }

    fun setData(data: List<NetworkWideCardUIModel>) {
        list.clear()
        if (data.isNullOrEmpty()) {
            list.add(NetworkWideCardUIModel("...", R.drawable.intree_logo, "..."))
        } else {
            list.addAll(data)
        }
        notifyDataSetChanged()
    }
}

