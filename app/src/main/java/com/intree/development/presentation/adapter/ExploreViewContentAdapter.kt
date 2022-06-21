package com.intree.development.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.intree.development.data.uiModels.ExploreImages
import com.intree.development.databinding.ItemExploreViewContentBinding
import com.intree.development.presentation.home.explore.IExploreViewContent

class ExploreViewContentAdapter(private val listener: IExploreViewContent) :
    RecyclerView.Adapter<ExploreViewContentAdapter.ExploreViewContentVH>() {

    private val list = ArrayList<ExploreImages>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExploreViewContentAdapter.ExploreViewContentVH {
        val binding = ItemExploreViewContentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ExploreViewContentVH(binding)
    }

    override fun onBindViewHolder(
        holder: ExploreViewContentAdapter.ExploreViewContentVH,
        position: Int
    ) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ExploreViewContentVH(private val binding: ItemExploreViewContentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(exploreImages: ExploreImages) {
            val context = itemView.context

            binding.ivContent.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    exploreImages.imageUrl
                )
            )
//            Glide.with(context)
//                .load(exploreImages.imageUrl)
//                //.override(200, 200)
//                .into(binding.ivContent)
        }
    }

    fun setData(content: ArrayList<ExploreImages>) {
        list.clear()
        list.addAll(content)
        notifyDataSetChanged()
    }
}