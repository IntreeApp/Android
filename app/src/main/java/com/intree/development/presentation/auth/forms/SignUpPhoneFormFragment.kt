package com.intree.development.presentation.auth.forms

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.telephony.TelephonyManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.omadahealth.lollipin.lib.managers.AppLock
import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.intree.development.R
import com.intree.development.databinding.SignUpPhoneFormFragmentBinding
import com.intree.development.presentation.auth.AuthActivity
import com.intree.development.presentation.auth.PinCodeActivity
import com.intree.development.util.*
import java.util.*


class SignUpPhoneFormFragment : Fragment() {

    private var _binding: SignUpPhoneFormFragmentBinding? = null

    private var fullPhone: String? = null
    private var isPhoneNumberValid: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.sign_up_phone_form_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = SignUpPhoneFormFragmentBinding.bind(view)

        var iso: String? = null

        val telephonyManager =
            activity?.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        // Get network country iso
        if (telephonyManager.networkCountryIso != null
            && telephonyManager.networkCountryIso.toString() != ""
        ) {
            iso = telephonyManager.networkCountryIso.toString()
            Log.d("AUTH", "=================== ISO: $iso") //will be 'ua' for UA network/phone
        }

        setPhoneTypeListeners(iso)

        _binding?.btnContinue?.setOnClickListener {
            if (isPhoneNumberValid) {
                val action =
                    SignUpPhoneFormFragmentDirections.actionSignUpPhoneFromFragmentToPhoneValidationFragment()
                action.validPhone = fullPhone!!
                findNavController().navigate(action)
            } else {
                Toast.makeText(
                    activity,
                    "Please enter valid and full phone number!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        /*
        _binding?.tvBtnTerms?.setOnClickListener {
            val action =
                SignUpPhoneFormFragmentDirections.actionSignUpPhoneFromFragmentToVebViewFragment()
            action.url = TERMS_URL
            findNavController().navigate(action)
        }
        _binding?.tvBtnPrivacyPolicy?.setOnClickListener {
            val action =
                SignUpPhoneFormFragmentDirections.actionSignUpPhoneFromFragmentToVebViewFragment()
            action.url = POLICY_URL
            findNavController().navigate(action)
        }*/
    }

    @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
    private fun setPhoneTypeListeners(iso: String?) {
        if (iso != null) {
            _binding?.etPhoneCode?.setText(
                "+${
                    getPhoneCodeByCountryISO(
                        activity as AuthActivity,
                        iso.toUpperCase(Locale.getDefault())
                    )
                }"
            )
            _binding?.tvCountryFlag?.text = iso.toFlagEmoji()
        }

        _binding?.etPhoneCode?.setOnFocusChangeListener { _, focused ->
            if (focused) {
                _binding?.etPhoneNumber?.text = null
            }
        }

        //set ignore annotation!
        _binding?.etPhoneCode?.setOnTouchListener { _, event ->
            if (_binding?.etPhoneCode?.text != null) {
                _binding?.etPhoneCode?.onTouchEvent(event)
                _binding?.etPhoneCode?.setSelection(_binding?.etPhoneCode?.text!!.length)
            }
            true
        }

        _binding?.etPhoneCode?.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus) {
                _binding?.etPhoneCode?.setSelection(_binding?.etPhoneCode?.text!!.length)
            }
        }

        _binding?.etPhoneCode?.doOnTextChanged(fun(
            text: CharSequence?,
            start: Int,
            before: Int,
            count: Int
        ) {
            Log.d(
                "AUTH",
                "===== SEARCHING COUNTRY CODE BY: ${text.toString().trim('+')}"
            )
            _binding?.tvCountryFlag?.text = getCountryISOByPhoneCode(
                activity as AuthActivity,
                text.toString().trim('+')
            )?.toFlagEmoji()
            if (_binding?.tvCountryFlag?.text != null) {
                if (_binding?.tvCountryFlag?.text.toString().isNotEmpty()) {
                    Log.d(
                        "AUTH",
                        "=========== FOUND COUNTRY FLAG W/UTIL METHOD. SETTING FOCUS on phone.. ||| : ${_binding?.tvCountryFlag?.text}"
                    )
                    _binding?.etPhoneCode?.clearFocus()
                    Handler().post {
                        _binding?.etPhoneNumber?.requestFocus()
                    }
                } else {
                    Log.d(
                        "AUTH",
                        "=========== __NOT_ FOUND COUNTRY FLAG W/UTIL METHOD: ${_binding?.tvCountryFlag?.text}"
                    )
                }
            }
        })

        _binding?.etPhoneNumber?.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus) {
                if (_binding?.tvCountryFlag?.text == null || _binding?.tvCountryFlag?.text.toString()
                        .isEmpty()) {
                    _binding?.etPhoneNumber?.clearFocus()
                    Handler().post {
                        _binding?.etPhoneCode?.requestFocus()
                    }
                }
            }
        }

        _binding?.etPhoneNumber?.doOnTextChanged(fun(
            text: CharSequence?,
            start: Int,
            before: Int,
            count: Int
        ) {
            if (text?.length == 0) {
                _binding?.etPhoneNumber?.clearFocus()
                Handler().post {
                    _binding?.etPhoneCode?.requestFocus()
                }
            } else {
                fullPhone = "${_binding?.etPhoneCode?.text}$text"
                val phoneUtil = PhoneNumberUtil.getInstance()
                isPhoneNumberValid = try {
                    val deviceLocaleCode: String = Locale.getDefault().toString()
                    Log.d("AUTH", "=========== DEVICE LOCALE CODE: $deviceLocaleCode")
                    val numberProto = phoneUtil.parse(fullPhone, deviceLocaleCode)
                    Log.d("AUTH", "=========== NUMBER PROTO: $numberProto")
                    true
                } catch (e: NumberParseException) {
                    System.err.println("NumberParseException was thrown: $e")
                    false
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}