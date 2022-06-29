package com.intree.development.data

import com.intree.development.domain.AspectEntity
import com.intree.development.domain.UserProfileEntity

//TODO may decline repo pattern usage for profile(at least) if all logic will be in VMs
interface DataRepository {
    fun getProfile(): UserProfileEntity

    fun getRoom(id: String): AspectEntity

    fun writeProfile(user: UserProfileEntity)

    fun writeRoom(room: AspectEntity)
}