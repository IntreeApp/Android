package com.intree.development.data.uiModels

data class IdentitiesUIModel(
    val id: Int,
    val tittle: String,
    val imageBackground: Int,
    val imageIcon: Int,
    var isAddCard: Boolean = false
)