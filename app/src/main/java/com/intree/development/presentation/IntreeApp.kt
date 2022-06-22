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
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class IntreeApp : Application() {

    private val lockManager = LockManager.getInstance()

    override fun onCreate() {
        super.onCreate()

        //registerActivityLifecycleCallbacks(ActivityLifecycleListener)

        instance = this

        // Enables custom Pin code activity
        lockManager.enableAppLock(this,PinCodeActivity::class.java)



        // Setting logo for  Pin code activity
        lockManager.appLock.logoId = R.drawable.intree_logo_transparent

        // removes forgot-pin-option
        if (lockManager.appLock.isPasscodeSet) {
            lockManager.appLock.setShouldShowForgot(false)
        } else {
            lockManager.appLock.setShouldShowForgot(true)

        }

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

    object ActivityLifecycleListener : ActivityLifecycleCallbacks {
        private const val TAG = "LifecycleCallbacks"
        override fun onActivityPaused(p0: Activity) {
            Log.d(TAG,"onActivityPaused at ${p0.localClassName}")
            // LockManager.getInstance().appLock.setLastActiveMillis()
        }

        override fun onActivityStarted(p0: Activity) {
            Log.d(TAG,"onActivityStarted at ${p0.localClassName}")
        }

        override fun onActivityDestroyed(p0: Activity) {
            Log.d(TAG,"onActivityDestroyed at ${p0.localClassName}")
        }

        override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
            Log.d(TAG,"onActivitySaveInstanceState at ${p0.localClassName}")
        }

        override fun onActivityStopped(p0: Activity) {
            Log.d(TAG,"onActivityStopped at ${p0.localClassName}")
        }

        override fun onActivityCreated(p0: Activity, p1: Bundle?) {
            Log.d(TAG,"onActivityCreated at ${p0.localClassName}")
        }

        override fun onActivityResumed(p0: Activity) {
            Log.d(TAG,"onActivityResumed at ${p0.localClassName}")
        }
    }



}