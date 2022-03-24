package com.intree.development.presentation.home.invite

import android.content.Context
import android.provider.ContactsContract
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.intree.development.data.model.ReferContactData
import com.intree.development.util.ReferUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InviteViewModel @Inject constructor() : ViewModel() {

    val contactsLive = MutableLiveData<MutableList<ReferContactData>>()

    fun getContacts(context: Context) {
        viewModelScope.launch(Dispatchers.Default) {
            contactsLive.postValue(
                ReferUtils.getContacts(context)
                    ?.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER, { it.name }))
                    ?.toMutableList()
            )
        }
    }


}