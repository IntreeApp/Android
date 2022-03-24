package com.intree.development.presentation.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.FragmentActivity
import com.intree.development.R
import com.intree.development.presentation.auth.AuthActivity

class SplashActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(Runnable { // TODO: Your application init goes here.
            val mInHome = Intent(this@SplashActivity, AuthActivity::class.java)
            this@SplashActivity.startActivity(mInHome)
            this@SplashActivity.finish()
        }, 3000)
    }
}