package com.intree.development.domain

data class UserDataEntity(
    var email: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var fullName: String = "",
    //var latestLaunch: String = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault()).format(Date()), - deprecated?
    var phone: String = "",
    var photo: UserPhotoVariants = UserPhotoVariants(),
    var showroom: Map<String, AspectEntity> = HashMap()
)