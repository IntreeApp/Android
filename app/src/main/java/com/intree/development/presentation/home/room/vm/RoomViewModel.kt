package com.intree.development.presentation.home.room.vm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.intree.development.domain.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RoomViewModel @Inject constructor() : ViewModel() {

    private val _roomEntity = MutableLiveData<RoomEntity?>()
    private val _roomFirestoreMetadata = MutableLiveData<RoomFirestoreMetadata?>()

    lateinit var selectedRoomId: String

    var roomEntity: LiveData<RoomEntity?> = _roomEntity
    var roomFirestoreMetadata: LiveData<RoomFirestoreMetadata?> = _roomFirestoreMetadata

    fun getRoomData(optRoomId: String = "") {
        val roomId = if (optRoomId.isEmpty()) selectedRoomId else optRoomId

        FirebaseDatabase.getInstance().reference
            .child("users").child(
                FirebaseAuth.getInstance().uid!!
            )
            .child("data")
            .child("showroom").child(
                roomId
            )
            .get().addOnSuccessListener {
                //Log.i("firebase", "Got value ${it.value}")
                _roomEntity.value = it.getValue(RoomEntity::class.java)
            }.addOnFailureListener {
                //Log.e("firebase", "Error getting data", it)
                //Stub
            }
    }

    fun getRoomMetadata(optRoomId: String = "") {

        val roomId = if (optRoomId.isEmpty()) selectedRoomId else optRoomId

        Log.d("SHOWROOM", "GETTING SHOWROOM METADATA FOR ID : $roomId")

        val baseRef = FirebaseFirestore.getInstance().collection("showroom").document(roomId)

        baseRef.get().addOnCompleteListener { task ->

            if (task.isComplete && task.isSuccessful) {

                val accessDataCollection = baseRef.collection("access").get()
                val baseParamsDoc = baseRef.collection("data").document("base-params").get()
                val photosDataCollection = baseRef.collection("gallery").get()

                accessDataCollection.addOnCompleteListener { accessTask ->
                    val accessDataList = accessTask.result!!.documents.map { adData ->
                        RoomFirestoreAccessData(
                            uidWithAccess = adData.id,
                            permissionToView = adData.get("ptv") as Boolean,
                            timeOpening = adData.get("top") as Long
                        )
                    }
                    baseParamsDoc.addOnCompleteListener { baseParamsTask ->
                        val baseParamsData = baseParamsTask.result!!.data?.let { bpData ->
                            RoomFirestoreBaseParams(
                                fromIdentifier = bpData["fi"] as String,
                                title = bpData["ttl"] as String,
                                gallery = emptyList()
                            )
                        }
                        photosDataCollection.addOnCompleteListener { galleryTask ->
                            val galleryData = galleryTask.result!!.map { photoData ->
                                //photoData.toObject(RoomFirestoreImageData::class.java)
                                RoomFirestoreImageData(
                                    firestoreId = photoData.id,
                                    img = photoData["img"] as String,
                                    idx = (photoData["idx"] as Long).toInt()
                                )
                            }
                            baseParamsData!!.gallery = galleryData

                            _roomFirestoreMetadata.value = RoomFirestoreMetadata(
                                accessData = accessDataList,
                                baseParams = baseParamsData
                            )
                        }
                    }
                }
            }
        }
    }

    fun updateRoomData(roomEntity: RoomEntity) {
        FirebaseDatabase.getInstance().reference
            .child("users").child(
                FirebaseAuth.getInstance().uid!!
            )
            .child("data")
            .child("showroom").child(selectedRoomId)
            .setValue(roomEntity).addOnSuccessListener {
                getRoomData()
            }
    }

    fun createRoom(roomEntity: RoomEntity) {
        FirebaseFirestore.getInstance().collection("showroom").document().get()
            .addOnCompleteListener {

                val id = it.result!!.id
                selectedRoomId = id

                Log.d("SHOWROOM", "CREATING ROOM WITH ID: $selectedRoomId")

                FirebaseFirestore.getInstance().collection("showroom").document(id)
                    .collection("access")
                    .document(FirebaseAuth.getInstance().uid!!).set(RoomFirestoreAccessInitData())

                FirebaseFirestore.getInstance().collection("showroom").document(id)
                    .collection("data")
                    .document("base-params").set(
                        RoomFirestoreBaseInitParams(
                            fi = FirebaseAuth.getInstance().uid!!,
                            ttl = roomEntity.title
                        )
                    )

                FirebaseDatabase.getInstance().reference
                    .child("users").child(
                        FirebaseAuth.getInstance().uid!!
                    )
                    .child("data")
                    .child("showroom").child(id)
                    .setValue(roomEntity)
            }
    }

    fun deleteRoomPhoto(roomId: String = "", photoId: String) {
        val baseRef = FirebaseFirestore.getInstance().collection("showroom").document(roomId)
        baseRef.collection("gallery").document(photoId).delete()
           .addOnFailureListener {
               Log.d("SHOWROOM", "FAILED TO DELETE ROOM PHOTO: ${it.localizedMessage}")
           }
    }

    //primary purpose of this function is to update room photo index for display
    fun updateRoomPhotoIndex(roomId: String = "", firestoreId: String, index: Int) {
        val baseRef = FirebaseFirestore.getInstance().collection("showroom").document(roomId)
        baseRef.collection("gallery").document(firestoreId)
            .update("idx", index).addOnFailureListener {
                Log.d("SHOWROOM", "FAILED TO UPDATE ROOM IDX: ${it.localizedMessage}")
            }
    }

    fun updateRoomMetadata() {
        //TODO
    }

    fun selectRoom() {
        //TODO ?
    }

}