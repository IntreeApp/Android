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
import com.intree.development.data.uiModels.BenefitsUIModel
import com.intree.development.databinding.ItemBenefitsBinding

class BenefitsAdapter :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val list = ArrayList<BenefitsUIModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemBinding = ItemBenefitsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BenefitsVH(itemBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is BenefitsVH) {
            holder.bind(list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class BenefitsVH(private val itemBinding: ItemBenefitsBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(model: BenefitsUIModel) {
            if (model.id != EMPTY_ID) {
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
                            itemBinding.ivPhoto.background = resource
                        }
                    })
            }
        }
    }

    fun setData(data: List<BenefitsUIModel>) {
        list.clear()
        if (data.isNullOrEmpty()) {
            list.add(BenefitsUIModel(EMPTY_ID, "", -1))
        } else {
            list.addAll(data)
        }
        notifyDataSetChanged()
    }

    companion object {
        const val EMPTY_ID = -1
    }
}