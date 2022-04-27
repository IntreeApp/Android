package com.intree.development.presentation.home.profile.vm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.intree.development.domain.RoomEntity
import com.intree.development.domain.RoomEntityForPreview
import com.intree.development.domain.UserProfileEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {
    private val _userEntity = MutableLiveData<UserProfileEntity?>()

    private val _ownRooms = MutableLiveData<List<RoomEntityForPreview>>()

    var userEntity: LiveData<UserProfileEntity?> = _userEntity

    var ownRooms: LiveData<List<RoomEntityForPreview>> = _ownRooms

    var isSignUpFlowFinished: Boolean = false

    var photoCode = -1

    fun getProfile() {
        FirebaseDatabase.getInstance().reference.child("users").child(
            FirebaseAuth.getInstance().uid!!
        )
            .get().addOnSuccessListener {
                //Log.i("firebase", "Got value ${it.value}")
                _userEntity.value = it.getValue(UserProfileEntity::class.java)
            }.addOnFailureListener {
                Log.e("PROFILE", "Error getting PROFIlE", it)
            }
    }

    fun updateProfile(user: UserProfileEntity) {
        FirebaseDatabase.getInstance().reference.child("users").child(
            FirebaseAuth.getInstance().uid!!
        ).setValue(user).addOnSuccessListener {
            getProfile()
        }
    }

    fun getOwnRooms() {
        FirebaseDatabase.getInstance().reference.child("users").child(
            FirebaseAuth.getInstance().uid!!
        )
            .child("data")
            .child("showroom")
            .get().addOnSuccessListener { dataSnap ->
                //Log.i("firebase", "Got value ${it.value}")
                _ownRooms.value =
                    dataSnap.children.filter { it.getValue(RoomEntity::class.java)!!.draft == "NO" }
                        .map {
                            RoomEntityForPreview(
                                firebaseId = it.key as String,
                                title = it.getValue(RoomEntity::class.java)!!.title,
                                backgroundURL = it.getValue(RoomEntity::class.java)!!.cover,
                                logoURL = it.getValue(RoomEntity::class.java)!!.logo
                            )
                        }
            }.addOnFailureListener {
                Log.e("ROOM", "Error getting OWN ROOM DATA", it)
            }
    }
}