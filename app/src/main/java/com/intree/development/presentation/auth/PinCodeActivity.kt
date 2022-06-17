package com.intree.development.presentation.auth

import android.content.Intent
import android.widget.Toast
import com.github.omadahealth.lollipin.lib.managers.AppLock
import com.github.omadahealth.lollipin.lib.managers.AppLockActivity
import com.intree.development.presentation.IntreeApp
import com.intree.development.presentation.home.MainActivity

class PinCodeActivity: AppLockActivity(){


    override fun showForgotDialog() {
        TODO("Not yet implemented")
    }

    override fun onPinFailure(attempts: Int) {
        Toast.makeText(this,"Pin entered is Incorrect and attempts done are $attempts",Toast.LENGTH_LONG).show()
    }

    override fun onPinSuccess(attempts: Int) {
        val intent = Intent(IntreeApp.getContext(), MainActivity::class.java)

        startActivity(intent)
    }
}