package com.intree.development.presentation.home

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.FragmentActivity
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

        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.menu.getItem(2)?.isEnabled = false
        binding.bottomNavigationView.menu.getItem(4)?.isChecked = true

        binding.fab.setOnClickListener {
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.main_host_fragment) as NavHostFragment
            binding.bottomNavigationView.setupWithNavController(navHostFragment.navController)
            navHostFragment.navController.popBackStack()
            navHostFragment.navController.navigate(R.id.fragmentNetwork)
        }

        //_binding?.bottomNavigationView?.selectedItemId = R.id.bottomTabProfile
        //bottomNavigationView.setOnNavigationItemSelectedListener(myNavigationItemListener)
        //bottomNavigation.setupWithNavController(Navigation.findNavController(this, R.id.my_nav_host_fragment))

    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    private fun initNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_host_fragment) as NavHostFragment
        binding.bottomNavigationView.setupWithNavController(navHostFragment.navController)
    }
}