package com.intree.development.presentation.auth.validation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.omadahealth.lollipin.lib.managers.AppLock
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.intree.development.R
import com.intree.development.databinding.PhoneValidationFragmentBinding
import com.intree.development.presentation.auth.AuthActivity
import com.intree.development.presentation.auth.PinCodeActivity
import com.intree.development.presentation.auth.forms.SignUpPhoneFormFragmentDirections
import java.util.concurrent.TimeUnit


class PhoneValidationFragment : Fragment() {

    private val args: PhoneValidationFragmentArgs by navArgs()

    private var _binding: PhoneValidationFragmentBinding? = null

    // create instance of firebase auth
    private lateinit var auth: FirebaseAuth

    // we will use this to match the sent otp from firebase
    private lateinit var storedVerificationId: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.phone_validation_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = PhoneValidationFragmentBinding.bind(view)



        auth = FirebaseAuth.getInstance()
        //!for testing period
        //TODO add App Verification
        //auth.firebaseAuthSettings.setAppVerificationDisabledForTesting(true)

        _binding?.toolbar?.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        _binding?.toolbar?.setNavigationOnClickListener { activity?.onBackPressed() }
        _binding?.tvPhoneNumberValue?.text = args.validPhone
        _binding?.otpProgressBar?.isVisible = true
        _binding?.tvWrongCodeIndicator?.isVisible = false

        _binding?.etCode?.doOnTextChanged(fun(
            text: CharSequence?,
            start: Int,
            before: Int,
            count: Int
        ) {
            _binding?.otpProgressBar?.isVisible = false
            _binding?.tvCodeArrivalInProgress?.isVisible = false
            if (text!!.toString().length > 5) {
                signInWithPhoneAuthCredential(text.toString())
            }
        })

        setPhoneAuthCallbacks()
        sendVerificationCode()
    }

    // this method sends the verification code and starts the callback of verification
    // which is implemented above in onCreate
    private fun sendVerificationCode() {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(args.validPhone) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity((activity as AuthActivity)) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        Log.d("GFG" , "Auth started")
    }

    private fun setPhoneAuthCallbacks() {

        // Callback function for Phone Auth
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            // This method is called when the verification is completed
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Log.d("GFG" , "onVerificationCompleted Success")
                Toast.makeText(activity,"Verification completed", Toast.LENGTH_SHORT).show()
            }

            // Called when verification is failed add log statement to see the exception
            override fun onVerificationFailed(e: FirebaseException) {
                Log.d("GFG" , "onVerificationFailed  $e")
                Toast.makeText(activity,"Verification failed!", Toast.LENGTH_SHORT).show()
            }

            // On code is sent by the firebase this method is called
            // in here we start a new activity where user can enter the OTP
            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                Log.d("GFG","onCodeSent: $verificationId")
                storedVerificationId = verificationId
                resendToken = token
            }
        }
    }

    private fun signInWithPhoneAuthCredential(otp: String) {
        if (::storedVerificationId.isInitialized) {
            val credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(
                storedVerificationId, otp)
            auth.signInWithCredential(credential)
                .addOnCompleteListener((activity as AuthActivity)) { task ->
                    if (task.isSuccessful) {
                        _binding?.otpProgressBar?.isVisible = false
                        _binding?.imgCodeIsValidIndicator?.isVisible = true
                        _binding?.imgCodeIsNotValidIndicator?.isVisible = false
                        _binding?.tvWrongCodeIndicator?.isVisible = false
                        _binding?.tvCodeArrivalInProgress?.isVisible = false
                        Log.d("FIREBASE_AUTH", FirebaseAuth.getInstance().uid!!)
                        val action =
                            PhoneValidationFragmentDirections.actionPhoneValidationFragmentToSignUpNameFormFragment()
                        action.phone = args.validPhone
                        findNavController().navigate(action)
                    } else {
                        _binding?.tvWrongCodeIndicator?.isVisible = true
                        _binding?.imgCodeIsValidIndicator?.isVisible = false
                        _binding?.imgCodeIsNotValidIndicator?.isVisible = true
                        _binding?.tvCodeArrivalInProgress?.isVisible = false
                        // Sign in failed, display a message and update the UI
                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                            Toast.makeText(activity,"Invalid verification code", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
        } else {
            Toast.makeText(activity,"SMS shouldn't be retrieved yet. Please wait few seconds", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}