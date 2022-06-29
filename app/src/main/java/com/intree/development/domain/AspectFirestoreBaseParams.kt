package com.intree.development.domain

data class AspectFirestoreBaseParams(
    var fromIdentifier: String = "", //from identifier - created by? | fi
    var title: String = "", //title | ttl
    var gallery: List<AspectFirestoreImageData> = ArrayList() //showroom photos gallery
)