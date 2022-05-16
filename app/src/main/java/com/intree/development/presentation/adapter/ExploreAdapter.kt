package com.intree.development.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.intree.development.R
import com.intree.development.data.uiModels.ExploreUIModel
import com.intree.development.databinding.ItemExploreBinding
import com.intree.development.presentation.dialogs.AddedToFavouriteDialogFragment
import com.intree.development.presentation.dialogs.ExploreBottomSheetDialogFragment
import com.intree.development.presentation.home.explore.IExplore
import com.intree.development.presentation.home.explore.IExploreViewContent

class ExploreAdapter(
    private val listener: IExplore,
    private val supportFragmentManager: FragmentManager
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), IExploreViewContent {

    private val list = ArrayList<ExploreUIModel>()
    private val viewPagerAdapter = ExploreViewContentAdapter(this)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemBinding = ItemExploreBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ExploreVH(itemBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ExploreVH) {
            holder.bind(list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ExploreVH(private val itemBinding: ItemExploreBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(exploreUIModel: ExploreUIModel) {
            val context = itemView.context

            if (exploreUIModel.content.isNotEmpty()) {
                itemBinding.viewPager.adapter = viewPagerAdapter
                viewPagerAdapter.setData(exploreUIModel.content)
                itemBinding.tabLayout.setViewPager(itemBinding.viewPager)
            } else {
                itemBinding.viewPager.visibility = View.GONE
                itemBinding.tabLayout.visibility = View.INVISIBLE
            }

            itemBinding.tvTime.text = exploreUIModel.lastActivityInMinutes + " min ago"

            itemBinding.tvName.text = exploreUIModel.nameOfUser
            itemBinding.tvFrom.text =
                context.resources.getString(R.string.from) + " " + exploreUIModel.from

            itemBinding.tvDescription.text = exploreUIModel.description

            Glide.with(context)
                .load(exploreUIModel.userPhoto)
                .centerCrop()
                .override(125, 125)
                .into(itemBinding.imgRoundedProfilePhoto)

            Glide.with(context)
                .load(R.drawable.ic_sortiing_expr)
                .override(85, 85)
                .into(itemBinding.ivPrefferedPosts)

            itemBinding.ivPrefferedPosts.setOnClickListener {
                itemBinding.ivPrefferedPosts.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_vector_prefered_posts
                    )
                )

                AddedToFavouriteDialogFragment.newInstance(exploreUIModel).show(
                    supportFragmentManager,
                    this.javaClass.simpleName
                )
            }

            itemBinding.ivSettings.setOnClickListener {
                ExploreBottomSheetDialogFragment.newInstance()
                    .show(supportFragmentManager, ExploreBottomSheetDialogFragment.TAG)
            }

        }
    }

    fun setData(data: ArrayList<ExploreUIModel>) {
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }
}