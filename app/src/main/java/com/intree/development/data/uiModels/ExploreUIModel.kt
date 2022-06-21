package com.intree.development.data.uiModels

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExploreUIModel(
    val id: Int,
    val nameOfUser: String,
    val userPhoto: Int,
    val from: String,
    val description: String,
    val content: ArrayList<ExploreImages>,
    val lastActivityInMinutes: String
) : Parcelable

@Parcelize
data class ExploreImages(
    val id: Int,
    val imageUrl: Int
) : Parcelable