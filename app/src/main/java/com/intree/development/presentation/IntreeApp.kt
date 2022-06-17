package com.intree.development.presentation

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat
import com.github.omadahealth.lollipin.lib.managers.LockManager
import com.google.firebase.FirebaseApp
import com.intree.development.R
import com.intree.development.presentation.auth.PinCodeActivity
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class IntreeApp : Application() {

    private val lockManager = LockManager.getInstance()

    override fun onCreate() {
        super.onCreate()

        instance = this

        // Enables custom Pin code activity
        lockManager.enableAppLock(this,PinCodeActivity::class.java)

        // Setting logo for  Pin code activity
        lockManager.appLock.logoId = R.drawable.ic_btn_nav_bar_center

        lockManager.appLock.setShouldShowForgot(false)


        //FirebaseApp.initializeApp(this)
    }

    companion object {
        lateinit var instance: IntreeApp
        fun getContext(): Context? {
            return instance.applicationContext
        }
    }
}