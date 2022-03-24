package com.intree.development.domain

data class RoomFirestoreAccessInitData(
    val ptv: Boolean = true,
    val top: Long = System.currentTimeMillis()
)