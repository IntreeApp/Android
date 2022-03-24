package com.intree.development.presentation.home.introduce

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.intree.development.R
import com.intree.development.databinding.FragmentIntroduceBinding

class IntroduceFragment : Fragment(R.layout.fragment_introduce) {

    private lateinit var binding: FragmentIntroduceBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!::binding.isInitialized) {
            binding = FragmentIntroduceBinding.inflate(inflater)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initContent()
    }

    private fun initContent() {
        Glide.with(requireContext())
            .load(R.drawable.ic_no_photo)
            .override(250, 250)
            .into(binding.imgRoundedProfilePhoto)

        Glide.with(requireContext())
            .load(R.drawable.ic_ellipse_84)
            .override(250, 250)
            .into(binding.imgRoundedProfilePhoto)
    }

}