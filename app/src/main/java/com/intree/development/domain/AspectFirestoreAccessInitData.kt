package com.intree.development.domain

data class AspectFirestoreAccessInitData(
    val ptv: Boolean = true,
    val top: Long = System.currentTimeMillis()
)