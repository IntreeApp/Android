package com.intree.development.presentation.auth.set_up

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.intree.development.R
import com.intree.development.databinding.SetUpUserActivityFragmentBinding

class SetUpUserActivityFragment : Fragment() {

    private var _binding: SetUpUserActivityFragmentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.set_up_user_activity_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = SetUpUserActivityFragmentBinding.bind(view)
        _binding?.cardSetUpStepOne?.setOnClickListener {
            findNavController().navigate(R.id.action_setUpUserActivityFragment_to_profileEditModeFragment)
        }
        _binding?.cardSetUpStepTwo?.setOnClickListener {
            //findNavController().navigate(R.id.action_setUpUserActivityFragment_to_createRoomFragment)
           // val action = SetUpUserActivityFragmentDirections.actionSetUpUserActivityFragmentToCreateRoomFragment()
           // action.existingRoomId = null
            //action.existingRoomToEdit = null
            //findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}