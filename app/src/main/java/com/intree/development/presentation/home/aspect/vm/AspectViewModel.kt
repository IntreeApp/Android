package com.intree.development.presentation.home.aspect.vm

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
class AspectViewModel @Inject constructor() : ViewModel() {

    private val _aspectEntity = MutableLiveData<AspectEntity?>()
    private val _aspectFirestoreMetadata = MutableLiveData<AspectFirestoreMetadata?>()

    lateinit var selectedRoomId: String

    var aspectEntity: LiveData<AspectEntity?> = _aspectEntity
    var aspectFirestoreMetadata: LiveData<AspectFirestoreMetadata?> = _aspectFirestoreMetadata

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
                _aspectEntity.value = it.getValue(AspectEntity::class.java)
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
                        AspectFirestoreAccessData(
                            uidWithAccess = adData.id,
                            permissionToView = adData.get("ptv") as Boolean,
                            timeOpening = adData.get("top") as Long
                        )
                    }
                    baseParamsDoc.addOnCompleteListener { baseParamsTask ->
                        val baseParamsData = baseParamsTask.result!!.data?.let { bpData ->
                            AspectFirestoreBaseParams(
                                fromIdentifier = bpData["fi"] as String,
                                title = bpData["ttl"] as String,
                                gallery = emptyList()
                            )
                        }
                        photosDataCollection.addOnCompleteListener { galleryTask ->
                            val galleryData = galleryTask.result!!.map { photoData ->
                                //photoData.toObject(AspectFirestoreImageData::class.java)
                                AspectFirestoreImageData(
                                    firestoreId = photoData.id,
                                    img = photoData["img"] as String,
                                    idx = (photoData["idx"] as Long).toInt()
                                )
                            }
                            baseParamsData!!.gallery = galleryData

                            _aspectFirestoreMetadata.value = AspectFirestoreMetadata(
                                accessData = accessDataList,
                                baseParams = baseParamsData
                            )
                        }
                    }
                }
            }
        }
    }

    fun updateRoomData(aspectEntity: AspectEntity) {
        FirebaseDatabase.getInstance().reference
            .child("users").child(
                FirebaseAuth.getInstance().uid!!
            )
            .child("data")
            .child("showroom").child(selectedRoomId)
            .setValue(aspectEntity).addOnSuccessListener {
                getRoomData()
            }
    }

    fun createRoom(aspectEntity: AspectEntity) {
        FirebaseFirestore.getInstance().collection("showroom").document().get()
            .addOnCompleteListener {

                val id = it.result!!.id
                selectedRoomId = id

                Log.d("SHOWROOM", "CREATING ROOM WITH ID: $selectedRoomId")

                FirebaseFirestore.getInstance().collection("showroom").document(id)
                    .collection("access")
                    .document(FirebaseAuth.getInstance().uid!!).set(AspectFirestoreAccessInitData())

                FirebaseFirestore.getInstance().collection("showroom").document(id)
                    .collection("data")
                    .document("base-params").set(
                        AspectFirestoreBaseInitParams(
                            fi = FirebaseAuth.getInstance().uid!!,
                            ttl = aspectEntity.title
                        )
                    )

                FirebaseDatabase.getInstance().reference
                    .child("users").child(
                        FirebaseAuth.getInstance().uid!!
                    )
                    .child("data")
                    .child("showroom").child(id)
                    .setValue(aspectEntity)
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