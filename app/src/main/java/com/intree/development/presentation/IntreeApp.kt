package com.intree.development.presentation

import android.app.Activity
import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import com.github.omadahealth.lollipin.lib.managers.LockManager
import com.google.firebase.FirebaseApp
import com.google.firebase.storage.internal.ActivityLifecycleListener
import com.intree.development.R
import com.intree.development.presentation.auth.PinCodeActivity
import com.intree.development.util.SessionManager
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class IntreeApp : Application() {

    private val lockManager = LockManager.getInstance()
    private val TAG = "IntreeApp"
    private lateinit var sessionManager: SessionManager


    override fun onCreate() {
        super.onCreate()

        instance = this

        sessionManager = SessionManager()

        Log.d(TAG, LockManager.getInstance().toString())

        // Enables custom Pin code activity

        if (sessionManager.doesUsePinCode()) {
            lockManager.enableAppLock(this,PinCodeActivity::class.java)
            // Setting logo for  Pin code activity
                lockManager.appLock.logoId = R.drawable.intree_logo_transparent
            Log.d(TAG, "Pincode enabled")
        } else {
            lockManager.disableAppLock()
            Log.d(TAG, "Pincode disabled")
        }



        // removes forgot-pin-option
        /*
            Log.d(TAG, lockManager.appLock.isPasscodeSet.toString())
            if (lockManager.appLock.isPasscodeSet) {
                lockManager.appLock.setShouldShowForgot(true)
            } else {
                lockManager.appLock.setShouldShowForgot(false)
            }

         */

        // timeout set in milliseconds
        //lockManager.appLock.timeout = 1000


        //FirebaseApp.initializeApp(this)
    }

    companion object {
        lateinit var instance: IntreeApp
        fun getContext(): Context? {
            return instance.applicationContext
        }
    }
}