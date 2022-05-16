package com.intree.development.presentation.home

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.intree.development.R
import com.intree.development.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initNavController()
        onNavigationListener()

        val context = applicationContext

        //Can't get it to work with binding
        val scaledBottomNavIconPressed = context.scaledDrawableResources(
            R.drawable.ic_btn_nav_bar_center_pressed,
            R.dimen.design_fab_image_size,
            R.dimen.design_fab_image_size
        )

        binding.fab.setOnClickListener {
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.main_host_fragment) as NavHostFragment
            binding.bottomNavigationView.setupWithNavController(navHostFragment.navController)
            navHostFragment.navController.popBackStack()
            navHostFragment.navController.navigate(R.id.exploreFragment)
            binding.fab.setImageDrawable(scaledBottomNavIconPressed)
        }

        // Check if user has just created a group. In that case, navigate to inbox
        val intent = intent
        if (intent.hasExtra("groupCreated")) {
            val hasCreatedGroup = intent.getBooleanExtra("groupCreated", false)
            if (hasCreatedGroup) {
                binding.bottomNavigationView.selectedItemId = R.id.bottomTabInbox
            }

        }

        // Makes the status bar white TODO: Only works when sign up flow is done
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            val w: Window = window
//            w.setFlags(
//                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//            )
//        }


    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {

        if (event!!.action == MotionEvent.ACTION_DOWN) {
            val view = currentFocus
            if (view is EditText) {
                val outRect = Rect()
                view.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    view.clearFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    fun Context.scaledDrawableResources(
        @DrawableRes id: Int,
        @DimenRes width: Int,
        @DimenRes height: Int
    ): Drawable {
        val w = resources.getDimension(width).toInt()
        val h = resources.getDimension(height).toInt()
        return scaledDrawable(id, w, h)
    }

    fun Context.scaledDrawable(@DrawableRes id: Int, width: Int, height: Int): Drawable {
        val bmp = BitmapFactory.decodeResource(resources, id)
        val bmpScaled = Bitmap.createScaledBitmap(bmp, width, height, false)
        return BitmapDrawable(resources, bmpScaled)
    }

    private fun initNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_host_fragment) as NavHostFragment
        binding.bottomNavigationView.setupWithNavController(navHostFragment.navController)
    }

    private fun onNavigationListener() {
        val listener =
            NavController.OnDestinationChangedListener { controller, destination, arguments ->
                if (destination.id != R.id.exploreFragment) {
                    binding.fab.setImageDrawable(
                        ContextCompat.getDrawable(
                            applicationContext,
                            R.drawable.ic_btn_nav_bar_center
                        )
                    )
                }
            }
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_host_fragment) as NavHostFragment
        navHostFragment.navController.addOnDestinationChangedListener(listener)
    }
}