package com.intree.development.presentation.home.settings

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.omadahealth.lollipin.lib.managers.AppLock
import com.github.omadahealth.lollipin.lib.managers.LockManager
import com.intree.development.R
import com.intree.development.databinding.FragmentSettingsBinding
import com.intree.development.presentation.IntreeApp
import com.intree.development.presentation.auth.PinCodeActivity
import com.intree.development.util.SessionManager

class SettingsFragment: Fragment(R.layout.fragment_settings) {
    
    private lateinit var binding: FragmentSettingsBinding
    val lockManager = LockManager.getInstance()
    val sessionManager =  SessionManager()
    private val TAG = "SettingsFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!::binding.isInitialized) {
            binding = FragmentSettingsBinding.inflate(inflater)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initSwitches()
        initOnclickListeners()
    }

    private fun initSwitches() {
        // set biometrics switch
        if (lockManager?.isAppLockEnabled == true) {
            binding.switchBiometrics.isChecked = lockManager?.appLock?.isFingerprintAuthEnabled ?: false
        }
        // set pin code switch
        binding.switchPinCode.isChecked = lockManager?.isAppLockEnabled ?: true

        binding.switchBiometrics.setOnCheckedChangeListener { _, isChecked ->
            lockManager?.appLock?.isFingerprintAuthEnabled = isChecked
        }

        binding.switchPinCode.setOnCheckedChangeListener { _, isChecked ->

            sessionManager?.setUsePinCode(isChecked)

            if (isChecked) {

                lockManager?.enableAppLock(context, PinCodeActivity::class.java)
                lockManager.appLock.logoId = R.drawable.intree_logo_transparent

                if (lockManager?.appLock?.isPasscodeSet == false) {
                    lockManager.appLock.setShouldShowForgot(false)
                    val intent = Intent(context, PinCodeActivity::class.java)
                    intent.putExtra(AppLock.EXTRA_TYPE, AppLock.ENABLE_PINLOCK)
                    startActivity(intent)
                } else {
                    lockManager.appLock.setShouldShowForgot(true)
                }
                Log.d(TAG, lockManager?.isAppLockEnabled.toString())
            } else {
                lockManager?.disableAppLock()
                binding.switchBiometrics.isChecked = false
                Log.d(TAG, lockManager?.isAppLockEnabled.toString())
            }

        }


    }



    private fun initOnclickListeners() {
        
        binding.btnBack.setOnClickListener() {
            activity?.onBackPressed()
        }
        

    }
}