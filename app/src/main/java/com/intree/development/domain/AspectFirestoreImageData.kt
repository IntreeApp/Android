package com.intree.development.domain

data class AspectFirestoreImageData(
    val firestoreId: String = "",
    val cd: String = System.currentTimeMillis().toString(),
    var idx: Int = 0,
    val img: String = "",
    val md: String = System.currentTimeMillis().toString(),
    val timg: String = "-"
)