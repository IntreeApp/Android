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
import com.intree.development.data.uiModels.AspectsExtendedUIModel
import com.intree.development.databinding.ItemAddAspectsBinding
import com.intree.development.databinding.ItemAspectsBinding

class InviteAspectsAdapter :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val list = ArrayList<AspectsExtendedUIModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ItemType.CONTENT_TYPE.ordinal) {
            val itemBinding = ItemAspectsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            AspectsVH(itemBinding)
        } else {
            val itemBinding = ItemAddAspectsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            AddAspectsVH(itemBinding)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (list[position].isAddCard) {
            ItemType.ADD_TYPE.ordinal
        } else {
            ItemType.CONTENT_TYPE.ordinal
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is AspectsVH) {
            holder.bind(list[position])
        } else if (holder is AddAspectsVH) {
            holder.bind(list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class AddAspectsVH(private val itemBinding: ItemAddAspectsBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(model: AspectsExtendedUIModel) {

        }
    }

    inner class AspectsVH(private val itemBinding: ItemAspectsBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(model: AspectsExtendedUIModel) {
            if (model.id != -1) {
                itemBinding.tvName.text = model.tittle

                Glide.with(itemView.context)
                    .load(
                        model.imageBackground
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
                            itemBinding.ivPhoto.setImageDrawable(null)
                            itemBinding.ivPhoto.background = resource
                        }
                    })


                Glide.with(itemView.context)
                    .load(
                        model.imageIcon
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
                            itemBinding.ivProfile.setImageDrawable(resource)
                        }
                    })

            }
        }
    }

    fun setData(data: ArrayList<AspectsExtendedUIModel>) {
        list.clear()
        if (data.isNullOrEmpty()) {
            list.add(AspectsExtendedUIModel(-1, "", -1, -1))
        } else {
            list.addAll(data)
        }
        notifyDataSetChanged()
    }

    enum class ItemType {
        ADD_TYPE, CONTENT_TYPE
    }

}