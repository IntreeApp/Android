package com.intree.development.presentation.home.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.intree.development.R
import com.intree.development.databinding.ProfilePreviewModeFragmentBinding
import com.intree.development.domain.UserProfileEntity
import com.intree.development.presentation.home.profile.view_pager.RoomVPAdapter
import com.intree.development.presentation.home.profile.vm.ProfileViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfilePreviewModeFragment : Fragment() {
    private val viewModel by viewModels<ProfileViewModel>()

    private var _binding: ProfilePreviewModeFragmentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Disable onBack click
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            //Stub: we don't want user to return to sign up flow
        }
        return inflater.inflate(R.layout.profile_preview_mode_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = ProfilePreviewModeFragmentBinding.bind(view)

        val viewpager: ViewPager2 = _binding!!.vpRooms
        viewpager.adapter = RoomVPAdapter(childFragmentManager, lifecycle)
        //mAdapter.registerAdapterDataObserver(indicator.adapterDataObserver)

        viewpager.clipToPadding = false
        viewpager.offscreenPageLimit = 2
        //viewpager.setPadding(20, 40, 20, 0)

        _binding?.vpRoomsIndicator?.setViewPager(viewpager)

        _binding?.btnEditProfile?.setOnClickListener {
            findNavController().navigate(R.id.action_profilePreviewModeFragment_to_profileEditModeFragment2)
        }

        viewModel.ownRooms.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                it.forEach { roomForPreview ->
                    (viewpager.adapter as RoomVPAdapter).addExistingRoomItem(roomEntityForPreview = roomForPreview)
                }
            }
            (viewpager.adapter as RoomVPAdapter).addCreateRoomItem(it.isEmpty())
            (viewpager.adapter as RoomVPAdapter).notifyDataSetChanged()

            _binding!!.vpRoomsIndicator.setViewPager(viewpager)
        })

        viewModel.userEntity.observe(viewLifecycleOwner, {
            val profile = it ?: UserProfileEntity()

            _binding?.tvUserFullName?.text = profile.data.fullName

            if (profile.data.photo.photoLight.isNotEmpty()) {
                Picasso.get().load(profile.data.photo.photoLight).into(_binding?.imgProfilePhotoPreview)
            }
            if (profile.data.photo.photo.isNotEmpty()) {
                Picasso.get().load(profile.data.photo.photo).into(_binding?.profileCoverImg)
            }
        })

        viewModel.getProfile()
        viewModel.getOwnRooms()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}