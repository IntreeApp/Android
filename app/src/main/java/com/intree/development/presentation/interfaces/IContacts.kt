package com.intree.development.presentation.interfaces

import com.intree.development.data.model.ReferContactData

interface IContacts {
    fun onItemClicked(model: ReferContactData)
    fun onItemReady()
}