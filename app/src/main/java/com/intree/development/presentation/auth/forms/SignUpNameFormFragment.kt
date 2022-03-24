package com.intree.development.presentation.auth.forms

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.intree.development.R
import com.intree.development.databinding.SignUpNameFormFragmentBinding
import com.intree.development.domain.UserDataEntity
import com.intree.development.domain.UserProfileEntity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpNameFormFragment : Fragment() {

    private var _binding: SignUpNameFormFragmentBinding? = null

    private val args: SignUpNameFormFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Disable onBack click
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            //Stub: we don't want user to return to sign up flow
        }
        return inflater.inflate(R.layout.sign_up_name_form_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = SignUpNameFormFragmentBinding.bind(view)
        _binding?.btnContinue?.setOnClickListener {

            val firstName = _binding?.etFirstName?.text.toString()
            val lastName = _binding?.etLastName?.text.toString()
            val fullName = "${firstName.trim()} ${lastName.trim()}"

            val initialUserData = UserProfileEntity(
                UserDataEntity(firstName = firstName, lastName = lastName,
                fullName = fullName, phone = args.phone)
            )

            FirebaseDatabase.getInstance().reference.child("users").child(
                FirebaseAuth.getInstance().uid!!).setValue(initialUserData).addOnCompleteListener {
                findNavController().navigate(R.id.action_signUpNameFormFragment_to_setUpUserActivityFragment)
            }.addOnFailureListener {
                Toast.makeText(activity, "Profile setup data upload failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}