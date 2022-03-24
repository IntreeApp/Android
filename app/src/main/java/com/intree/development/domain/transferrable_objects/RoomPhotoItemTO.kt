package com.intree.development.domain.transferrable_objects

import com.intree.development.domain.RoomFirestoreImageData

data class RoomPhotoItemTO(
    var isSelected: Boolean = false,
    //var isIndexUpdated: Boolean = false,
    var idx: Int = 0,
    val item: RoomFirestoreImageData
)