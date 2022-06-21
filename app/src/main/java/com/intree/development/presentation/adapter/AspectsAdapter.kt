package com.intree.development.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.intree.development.data.uiModels.AspectsUIModel
import com.intree.development.databinding.ItemAspectsContentBinding

class AspectsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val list = ArrayList<AspectsUIModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemBinding = ItemAspectsContentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AspectsVH(itemBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is AspectsVH) {
            holder.bind(list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class AspectsVH(private val itemBinding: ItemAspectsContentBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(aspectsUIModel: AspectsUIModel) {
            val context = itemView.context
            Glide.with(context)
                .load(aspectsUIModel.imagePath)
                .centerCrop()
                .override(550,350)
                .into(itemBinding.ivContent)
            itemBinding.tvName.text = aspectsUIModel.name
        }
    }

    fun setMockData(mockData: ArrayList<AspectsUIModel>) {
        list.clear()
        list.addAll(mockData)
        notifyDataSetChanged()
    }
}