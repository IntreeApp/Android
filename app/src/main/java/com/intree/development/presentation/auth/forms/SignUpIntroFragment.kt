package com.intree.development.presentation.auth.forms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.intree.development.R
import com.intree.development.databinding.SignUpIntroFragmentBinding
import com.intree.development.util.POLICY_URL
import com.intree.development.util.TERMS_URL

class SignUpIntroFragment : Fragment() {

    private var _binding: SignUpIntroFragmentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.sign_up_intro_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = SignUpIntroFragmentBinding.bind(view)

        _binding?.btnSignUp?.setOnClickListener {
            findNavController().navigate(R.id.action_signUpIntroFragment_to_signUpPhoneFromFragment)
        }
        _binding?.tvBtnTerms?.setOnClickListener {
            val action = SignUpIntroFragmentDirections.actionSignUpIntroFragmentToVebViewFragment()
            action.url = TERMS_URL
            findNavController().navigate(action)
        }
        _binding?.tvBtnPrivacyPolicy?.setOnClickListener {
            val action = SignUpIntroFragmentDirections.actionSignUpIntroFragmentToVebViewFragment()
            action.url = POLICY_URL
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}