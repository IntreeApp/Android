package com.intree.development.presentation.home.createPost

import androidx.lifecycle.ViewModel
import com.esafirm.imagepicker.model.Image
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreatingPostViewModel @Inject constructor() : ViewModel() {

    val listImages = ArrayList<Image>()
}