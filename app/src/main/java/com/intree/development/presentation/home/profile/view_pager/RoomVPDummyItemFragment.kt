package com.intree.development.presentation.home.profile.view_pager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.intree.development.R
import com.intree.development.databinding.ListItemCreateRoomBinding
import com.intree.development.presentation.home.profile.ProfilePreviewModeFragment
import com.intree.development.presentation.home.profile.ProfilePreviewModeFragmentDirections

class RoomVPDummyItemFragment(val isFirstRoom: Boolean) : Fragment() {
    private var _binding: ListItemCreateRoomBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.list_item_create_room, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = ListItemCreateRoomBinding.bind(view)

        _binding?.tvRoomDummyTitle?.text = if (isFirstRoom) getString(R.string.create_your_first_room)
            else getString(R.string.create_your_next_room)

        _binding?.createRoomCardPlaceholder?.setOnClickListener {
            val action = ProfilePreviewModeFragmentDirections.actionProfilePreviewModeFragmentToCreateRoomFragment2(null)
            action.existingRoomToEdit = null
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}