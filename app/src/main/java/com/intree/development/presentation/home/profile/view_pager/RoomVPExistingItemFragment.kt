package com.intree.development.presentation.home.profile.view_pager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.intree.development.R
import com.intree.development.databinding.ListItemRoomBinding
import com.intree.development.domain.RoomEntityForPreview
import com.intree.development.presentation.home.profile.ProfilePreviewModeFragmentDirections
import com.squareup.picasso.Picasso

class RoomVPExistingItemFragment(
    private val roomEntityForPreview: RoomEntityForPreview) : Fragment() {
    private var _binding: ListItemRoomBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.list_item_room, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = ListItemRoomBinding.bind(view)

        if (roomEntityForPreview.backgroundURL.isNotEmpty()) {
            Picasso.get().load(roomEntityForPreview.backgroundURL)
                .into(_binding?.imgRoomItemLogo)
        }
        if (roomEntityForPreview.logoURL.isNotEmpty()) {
            Picasso.get().load(roomEntityForPreview.logoURL)
                .into(_binding?.roomLogoMinified)
        }

        _binding?.tvRoomTitle?.text = roomEntityForPreview.title

        view.setOnClickListener {
            val action =
                ProfilePreviewModeFragmentDirections.actionProfilePreviewModeFragmentToRoomDetailFragment()
            action.roomId = roomEntityForPreview.firebaseId
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}