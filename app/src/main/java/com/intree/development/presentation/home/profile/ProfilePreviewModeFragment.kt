package com.intree.development.presentation.home.profile

import android.Manifest
import android.R.attr.left
import android.R.attr.right
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.marginStart
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.intree.development.R
import com.intree.development.data.uiModels.BenefitsUIModel
import com.intree.development.data.uiModels.IdentitiesUIModel
import com.intree.development.databinding.ProfilePreviewModeFragmentBinding
import com.intree.development.domain.UserProfileEntity
import com.intree.development.presentation.adapter.BenefitsAdapter
import com.intree.development.presentation.adapter.IdentitiesAdapter
import com.intree.development.presentation.home.profile.vm.ProfileViewModel
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProfilePreviewModeFragment : Fragment(R.layout.profile_preview_mode_fragment) {
    private val viewModel by viewModels<ProfileViewModel>()

    private lateinit var binding: ProfilePreviewModeFragmentBinding
    private val adapterIdentities = IdentitiesAdapter()
    private val adapterBenefits = BenefitsAdapter()

    companion object {
        private const val requestContactCode = 1
        private val permissionArray = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!::binding.isInitialized) {
            binding = ProfilePreviewModeFragmentBinding.inflate(inflater)
        }
        //viewModel.getProfile()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initContent()
        requestPermissions()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == requestContactCode) {
            allPermissionsGranted()
        }
    }

    private fun allPermissionsGranted() = permissionArray.all {
        ContextCompat.checkSelfPermission(
            requireContext(),
            it
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun initContent() {
        initOnClickListeners()
        initBenefitsAdapter()
        initIdentitiesAdapter()

        viewModel.userEntity.observe(viewLifecycleOwner) {
            val profile = it ?: UserProfileEntity()

            binding.tvUserFullName.text = profile.data.fullName

            if (profile.data.photo.photoLight.isNotEmpty()) {
                Picasso.get().load(profile.data.photo.photoLight)
                    .into(binding.imgProfilePhotoPreview)
            }

            if (profile.data.photo.photo.isNotEmpty()) {
                Picasso.get().load(profile.data.photo.photo).into(binding.profileCoverImg)
            }

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri = result.uri
                handleCameraImage(resultUri)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }

    private fun initOnClickListeners() {
        binding.addUserPhoto.setOnClickListener {
            if (allPermissionsGranted()) {
                viewModel.photoCode = 0
                CropImage.activity()
                    .start(requireContext(), this)
            } else {
                requestPermissions()
            }
        }

        binding.addBackgroundPhoto.setOnClickListener {
            if (allPermissionsGranted()) {
                viewModel.photoCode = 1
                CropImage.activity()
                    .start(requireContext(), this)
            } else {
                requestPermissions()
            }
        }
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            permissionArray,
            requestContactCode
        )
    }

    private fun handleCameraImage(uri: Uri) {
        when (viewModel.photoCode) {
            0 -> {
                binding.imgProfilePhotoPreview.setImageDrawable(null)
                binding.addUserPhoto.visibility = View.GONE
                Glide.with(requireContext())
                    .load(
                        uri
                    )
                    .override(200, 200)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .error(
                        ContextCompat.getDrawable(
                            requireContext(),
                            android.R.drawable.ic_menu_report_image
                        )
                    )
                    .into(object : CustomTarget<Drawable?>() {
                        override fun onLoadCleared(placeholder: Drawable?) {
                        }

                        override fun onResourceReady(
                            resource: Drawable,
                            transition: Transition<in Drawable?>?
                        ) {
                            binding.imgProfilePhotoPreview.setImageDrawable(resource)
                            binding.imgProfilePhotoPreview.borderColor = Color.WHITE
                            binding.imgProfilePhotoPreview.borderWidth = 2F
                        }
                    })
            }
            1 -> {
                binding.profileCoverImg.background = null
                binding.addBackgroundPhoto.visibility = View.GONE

                Glide.with(requireContext())
                    .load(
                        uri
                    )
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .error(
                        ContextCompat.getDrawable(
                            requireContext(),
                            android.R.drawable.ic_menu_report_image
                        )
                    )
                    .into(object : CustomTarget<Drawable?>() {
                        override fun onLoadCleared(placeholder: Drawable?) {
                        }

                        override fun onResourceReady(
                            resource: Drawable,
                            transition: Transition<in Drawable?>?
                        ) {
                            binding.profileCoverImg.background = resource
                        }
                    })
            }
        }
    }

    private fun initBenefitsAdapter() {
        binding.rvBenefits.adapter = adapterBenefits
        binding.rvBenefits.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        binding.rvBenefits.layoutManager = layoutManager

        adapterBenefits.setData(benefitsMockData())
    }

    private fun initIdentitiesAdapter() {
        binding.rvIdentities.adapter = adapterIdentities
        binding.rvIdentities.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        binding.rvIdentities.layoutManager = layoutManager

        adapterIdentities.setData(identitiesMockData())
    }

    private fun identitiesMockData(): ArrayList<IdentitiesUIModel> {
        val list = ArrayList<IdentitiesUIModel>()
        list.add(
            IdentitiesUIModel(
                1,
                "Design Intern",
                R.drawable.unsplash_mock_1,
                R.drawable.julian
            )
        )
        list.add(
            IdentitiesUIModel(
                2,
                "Crypto",
                R.drawable.crypto,
                R.drawable.janko
            )
        )
        return list
    }

    private fun benefitsMockData(): ArrayList<BenefitsUIModel> {
        val list = ArrayList<BenefitsUIModel>()
        list.add(
            BenefitsUIModel(
                1,
                "Fashion 2022",
                R.drawable.fashion
            )
        )

        return list
    }
}