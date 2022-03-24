package com.intree.development.domain

data class RoomFirestoreAccessData(
    var uidWithAccess: String, // - who has access?
    var permissionToView: Boolean, // - permission to view | ptv
    var timeOpening: Long // - time opening | top | timestamp
)