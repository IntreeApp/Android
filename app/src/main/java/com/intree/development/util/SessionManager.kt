package com.intree.development.util

import android.content.Context
import android.content.SharedPreferences
import com.intree.development.presentation.IntreeApp

class SessionManager() {

    private var pref: SharedPreferences
    private var editor: SharedPreferences.Editor? = null
    private val PRIVATE_MODE = 0
    private var _context: Context? = null

    // Sharedpref file name
    private val PREF_NAME = "IntreeApp"
    val KEY_USE_PINCODE_FLAG = "use_pin_code_flag"

    init {
        _context = IntreeApp.getContext()
        pref = _context!!.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }

    fun setUsePinCode(flag: Boolean) {
        editor?.putBoolean(KEY_USE_PINCODE_FLAG, flag)
        editor?.apply()
    }

    fun doesUsePinCode(): Boolean {
        return pref.getBoolean(KEY_USE_PINCODE_FLAG, false)
    }



}