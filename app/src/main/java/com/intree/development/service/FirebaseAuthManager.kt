package com.intree.development.service

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class FirebaseAuthManager {

    private val TAG = "FirebaseAuthManager"

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun getAuthInstance() = auth

    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    fun isUserPresent() = getCurrentUser() != null

    fun signOut(): Unit {
        if (getCurrentUser() != null) {
            auth.signOut()
            Log.d(TAG, "DeviceUser signed out")
        }
        else { Log.d(TAG, "No Firebase user is active to sign out!") }
    }

    fun isUserAuthenticatedWithCreds(): Boolean {
        if(!isUserPresent()) return false
        for (user in getCurrentUser()!!.providerData) {
            if (user.providerId == "password") {
                return true
            }
        }
        return false
    }

    fun isUserAnonymous(): Boolean {
        return if(!isUserPresent()) false
        else getCurrentUser()?.isAnonymous!!
    }

}
