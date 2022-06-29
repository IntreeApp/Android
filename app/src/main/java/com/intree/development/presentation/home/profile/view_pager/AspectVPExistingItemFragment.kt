package com.intree.development.presentation.home.profile.view_pager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.intree.development.R
import com.intree.development.databinding.ListItemAspectBinding
import com.intree.development.domain.AspectEntityForPreview
import com.intree.development.presentation.home.profile.ProfilePreviewModeFragmentDirections
import com.squareup.picasso.Picasso

class AspectVPExistingItemFragment(
    private val aspectEntityForPreview: AspectEntityForPreview = AspectEntityForPreview()) : Fragment() {
    private var _binding: ListItemAspectBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.list_item_aspect, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = ListItemAspectBinding.bind(view)

        if (aspectEntityForPreview.backgroundURL.isNotEmpty()) {
            Picasso.get().load(aspectEntityForPreview.backgroundURL)
                .into(_binding?.imgRoomItemLogo)
        }
        if (aspectEntityForPreview.logoURL.isNotEmpty()) {
            Picasso.get().load(aspectEntityForPreview.logoURL)
                .into(_binding?.roomLogoMinified)
        }

        _binding?.tvRoomTitle?.text = aspectEntityForPreview.title

        view.setOnClickListener {
            val action =
                ProfilePreviewModeFragmentDirections.actionProfilePreviewModeFragmentToRoomDetailFragment()
            action.roomId = aspectEntityForPreview.firebaseId
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}