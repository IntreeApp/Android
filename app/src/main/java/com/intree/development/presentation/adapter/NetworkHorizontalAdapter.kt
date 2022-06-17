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
import com.intree.development.databinding.ItemNetworkCardRecentsBinding

class NetworkHorizontalAdapter:
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val list = ArrayList<NetworkRecentsUIModel>()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemBinding = ItemNetworkCardRecentsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return NetworkViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is NetworkViewHolder) {
            holder.bind(list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class NetworkViewHolder(private val itemBinding: ItemNetworkCardRecentsBinding)
        : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(model: NetworkRecentsUIModel) {
            itemBinding.tvNameSmallcard.text = model.name

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
                        itemBinding.tvNameSmallcard.background = resource
                    }
                })

        }

    }

    fun setData(data: List<NetworkRecentsUIModel>) {
        list.clear()
        if (data.isNullOrEmpty()) {
            list.add(NetworkRecentsUIModel("...", R.drawable.intree_logo))
        } else {
            list.addAll(data)
        }
        notifyDataSetChanged()
    }
}