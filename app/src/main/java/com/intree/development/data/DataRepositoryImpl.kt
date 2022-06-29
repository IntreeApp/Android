package com.intree.development.data

import com.intree.development.domain.AspectEntity
import com.intree.development.domain.UserProfileEntity
import javax.inject.Inject


class DataRepositoryImpl @Inject constructor() : DataRepository {
    override fun getProfile(): UserProfileEntity {
        TODO("Not yet implemented")
    }
    override fun getRoom(id: String): AspectEntity {
        TODO("Not yet implemented")
    }
    override fun writeProfile(user: UserProfileEntity) {
        TODO("Not yet implemented")
    }
    override fun writeRoom(room: AspectEntity) {
        TODO("Not yet implemented")
    }

}