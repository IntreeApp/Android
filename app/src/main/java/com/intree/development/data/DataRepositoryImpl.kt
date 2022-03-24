package com.intree.development.data

import com.intree.development.domain.RoomEntity
import com.intree.development.domain.UserProfileEntity
import javax.inject.Inject


class DataRepositoryImpl @Inject constructor() : DataRepository {
    override fun getProfile(): UserProfileEntity {
        TODO("Not yet implemented")
    }
    override fun getRoom(id: String): RoomEntity {
        TODO("Not yet implemented")
    }
    override fun writeProfile(user: UserProfileEntity) {
        TODO("Not yet implemented")
    }
    override fun writeRoom(room: RoomEntity) {
        TODO("Not yet implemented")
    }

}