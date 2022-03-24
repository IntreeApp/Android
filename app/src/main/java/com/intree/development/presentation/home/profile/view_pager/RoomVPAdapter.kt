package com.intree.development.presentation.home.profile.view_pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import com.intree.development.domain.RoomEntityForPreview
import androidx.viewpager2.adapter.FragmentStateAdapter

class RoomVPAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private val fragments: ArrayList<Fragment> = ArrayList()

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    fun addCreateRoomItem(isFirstRoom: Boolean) {
        fragments.add(RoomVPDummyItemFragment(isFirstRoom))
        notifyDataSetChanged()
    }

    fun addExistingRoomItem(roomEntityForPreview: RoomEntityForPreview) {
        fragments.add(RoomVPExistingItemFragment(roomEntityForPreview))
    }

}