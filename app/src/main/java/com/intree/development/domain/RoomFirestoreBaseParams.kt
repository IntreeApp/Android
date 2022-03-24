package com.intree.development.domain

data class RoomFirestoreBaseParams(
    var fromIdentifier: String = "", //from identifier - created by? | fi
    var title: String = "", //title | ttl
    var gallery: List<RoomFirestoreImageData> = ArrayList() //showroom photos gallery
)