package com.intree.development.presentation.home.createPost

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.esafirm.imagepicker.features.registerImagePicker
import com.esafirm.imagepicker.model.Image
import com.intree.development.R
import com.intree.development.data.uiModels.AspectsUIModel
import com.intree.development.databinding.FragmentCreatingPostBinding
import com.intree.development.presentation.adapter.AspectsAdapter

class CreatingPostFragment : Fragment(R.layout.fragment_creating_post) {

    private lateinit var binding: FragmentCreatingPostBinding
    private val adapter = AspectsAdapter()
    private val vm by viewModels<CreatingPostViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!::binding.isInitialized) {
            binding = FragmentCreatingPostBinding.inflate(inflater)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUserPhoto()
        initOnClickListeners()
        initAdapter()

        val launcher = registerImagePicker {
            setResult(it)
        }
        binding.ivImagePicker.setOnClickListener {
            launcher.launch()
        }
    }


    private fun setResult(list: List<Image>) {
        vm.listImages.addAll(list)
        if (vm.listImages.isNotEmpty()) {
            Glide.with(requireContext())
                .load(vm.listImages[0].uri)
                .centerCrop()
                .override(250, 250)
                .into(binding.ivPickedImages)
            binding.tvImageCount.text = "+" + vm.listImages.size.toString()
        }
    }

    private fun setUserPhoto() {
        //mock data
        Glide.with(requireContext())
            .load(R.drawable.julian)
            .centerCrop()
            .override(100, 100)
            .into(binding.imgRoundedProfilePhoto)
    }

    private fun initAdapter() {
        binding.rvAspects.adapter = adapter
        binding.rvAspects.setHasFixedSize(true)

        val data = ArrayList<AspectsUIModel>()
        data.add(
            AspectsUIModel(
                1,
                R.drawable.unsplash_mock_1,
                "My A life"
            )
        )
        data.add(
            AspectsUIModel(
                2,
                R.drawable.crypto,
                "Andersson Productions"
            )
        )
        adapter.setMockData(data)
    }

    private fun initOnClickListeners() {
        binding.ivBack.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.tvShare.setOnClickListener {
            share()
        }



        binding.edtMessage.hint = getString(R.string.write_a_caption)
        binding.edtMessage.setOnFocusChangeListener { _, b ->
            if (b) {
                binding.edtMessage.hint = ""
            } else {
                binding.edtMessage.hint = getString(R.string.write_a_caption)
            }
        }

    }

    private fun share() {}
}
