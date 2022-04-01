package com.intree.development.presentation.home

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.intree.development.R
import com.intree.development.databinding.ActivityMainBinding
import com.intree.development.presentation.home.inbox.InboxFragment
import com.intree.development.presentation.home.introduce.IntroductionsFragment
import com.intree.development.presentation.home.network.NetworkFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)

        val context = applicationContext

        val networkFragment = NetworkFragment()
        val introductionsFragment = IntroductionsFragment()
        val inboxFragment = InboxFragment()

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        val bottomNavCenterIcon = findViewById<FloatingActionButton>(R.id.fab)

        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.bottomTabNetwork -> {
                    setCurrentFragment(networkFragment)
                    bottomNavCenterIcon.setImageResource(R.drawable.ic_btn_nav_bar_center)
                }
                R.id.bottomTabIntroduce -> {
                    setCurrentFragment(introductionsFragment)
                    bottomNavCenterIcon.setImageResource(R.drawable.ic_btn_nav_bar_center)
                }
                R.id.bottomTabProfile -> {
                    bottomNavCenterIcon.setImageResource(R.drawable.ic_btn_nav_bar_center)
                }
                R.id.bottomTabInbox -> {
                    setCurrentFragment(inboxFragment)
                    bottomNavCenterIcon.setImageResource(R.drawable.ic_btn_nav_bar_center)
                }

            }
            true
        }


        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val scaledBottomNavIconPressed = context.scaledDrawableResources(R.drawable.ic_btn_nav_bar_center_pressed, R.dimen.design_fab_image_size, R.dimen.design_fab_image_size)

        bottomNavCenterIcon.setOnClickListener {
            bottomNavigationView.selectedItemId = R.id.bottomTabExplore
            bottomNavCenterIcon.setImageDrawable(scaledBottomNavIconPressed)
        }

        // Makes the status bar white TODO: Doesn't work
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    private fun setCurrentFragment(fragment: Fragment) {
        if (fragment != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.main_host_fragment, fragment)
            transaction.commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val bottomNavCenterIcon = findViewById<FloatingActionButton>(R.id.bottomNavigationView)
        when (item.itemId) {
            R.id.bottomTabNetwork -> bottomNavCenterIcon.setImageResource(R.drawable.ic_btn_nav_bar_center)
            R.id.bottomTabIntroduce -> bottomNavCenterIcon.setImageResource(R.drawable.ic_btn_nav_bar_center)
            R.id.bottomTabProfile -> bottomNavCenterIcon.setImageResource(R.drawable.ic_btn_nav_bar_center)
            R.id.bottomTabInbox -> bottomNavCenterIcon.setImageResource(R.drawable.ic_btn_nav_bar_center)

        }
        return super.onOptionsItemSelected(item)
    }


    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {

        if (event!!.action == MotionEvent.ACTION_DOWN) {
            val view = currentFocus
            if (view is EditText) {
                val outRect = Rect()
                view.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event?.rawX.toInt(), event.rawY.toInt())) {
                    view.clearFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    fun Context.scaledDrawableResources(@DrawableRes id: Int, @DimenRes width: Int, @DimenRes height: Int): Drawable {
        val w = resources.getDimension(width).toInt()
        val h = resources.getDimension(height).toInt()
        return scaledDrawable(id, w, h)
    }

    fun Context.scaledDrawable(@DrawableRes id: Int, width: Int, height: Int): Drawable {
        val bmp = BitmapFactory.decodeResource(resources, id)
        val bmpScaled = Bitmap.createScaledBitmap(bmp, width, height, false)
        return BitmapDrawable(resources, bmpScaled)
    }
}