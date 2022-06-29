package com.intree.development.presentation.home.profile

import com.intree.development.util.ProfilePhotoPickCase
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.intree.development.R
import com.intree.development.databinding.ProfileEditModeFragmentBinding
import com.intree.development.domain.UserProfileEntity
import com.intree.development.presentation.home.MainActivity
import com.intree.development.presentation.home.profile.vm.ProfileViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

// THIS FRAGMENT IS NOT IN USE


@AndroidEntryPoint
class ProfileEditModeFragment : Fragment() {
    private val viewModel by viewModels<ProfileViewModel>()

    private var _binding: ProfileEditModeFragmentBinding? = null

    private val _avatarPhotoPickerActivityResultLauncher =
        getPhotoPickerActivityResultLauncher(ProfilePhotoPickCase.AVATAR)

    private val _coverPhotoPickerActivityResultLauncher =
        getPhotoPickerActivityResultLauncher(ProfilePhotoPickCase.COVER)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.profile_edit_mode_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var profile = UserProfileEntity() //stub initial value

        _binding = ProfileEditModeFragmentBinding.bind(view)

        _binding?.ciProfileAvatar?.isVisible = false
        _binding?.ciProfileCover?.isVisible = false

        viewModel.userEntity.observe(viewLifecycleOwner) {

            Log.d("SHOWROOM", "------ PROFILE CHECK 111 : ${profile.data.toString()}")

            profile = it ?: UserProfileEntity()
            _binding?.etFirstName?.setText(profile.data.firstName)
            _binding?.etLastName?.setText(profile.data.lastName)

            if (profile.data.photo.photo.isNotEmpty()) {
                Picasso.get().load(profile.data.photo.photo).into(_binding?.profileChangeCoverImg)
            }
            if (profile.data.photo.photoLight.isNotEmpty()) {
                Picasso.get().load(profile.data.photo.photoLight)
                    .into(_binding?.imgRoundedProfilePhoto)
            }
        }

        viewModel.getProfile()

        _binding?.btnPseudoChangeCoverPhoto?.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(1024)  //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                    1080,
                    1080
                )  //Final image resolution will be less than 1080 x 1080(Optional)
                .createIntent { intent ->
                    _coverPhotoPickerActivityResultLauncher.launch(intent)
                }
        }
        _binding?.imgRoundedProfilePhoto?.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(1024)  //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                    1080,
                    1080
                )  //Final image resolution will be less than 1080 x 1080(Optional)
                .createIntent { intent ->
                    _avatarPhotoPickerActivityResultLauncher.launch(intent)
                }
        }

        _binding?.createRoomCardPlaceholder?.setOnClickListener {
            val action = ProfileEditModeFragmentDirections.actionProfileEditModeFragmentToCreateRoomFragment()
            action.existingRoomId = null
            action.existingRoomToEdit = null
            findNavController().navigate(action)
        }

        _binding?.btnEdit?.setOnClickListener {

            val actualProfile = viewModel.userEntity.value!!.data

            Log.d("SHOWROOM", "------ PROFILE SHOWROOM CHECK 222 : ${profile.data.showroom.map { it.key }}")

            profile.data.firstName = _binding?.etFirstName?.text.toString()
            profile.data.lastName = _binding?.etLastName?.text.toString()
            profile.data.fullName = _binding?.etFirstName?.text.toString() + " " + _binding?.etLastName?.text.toString()
            profile.data.showroom = actualProfile.showroom

            viewModel.updateProfile(profile)

            if (!viewModel.isSignUpFlowFinished) {
                viewModel.isSignUpFlowFinished = true
                Handler(Looper.getMainLooper()).postDelayed({
                    val intent = Intent(activity, MainActivity::class.java)
                    startActivity(intent)
                }, 300)
            } else {
                //TODO test
                findNavController().popBackStack()
            }
        }
    }

    private fun getPhotoPickerActivityResultLauncher(photoPickCase: ProfilePhotoPickCase)
            : ActivityResultLauncher<Intent> {
        return registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data
            when (resultCode) {
                Activity.RESULT_OK -> {
                    if (photoPickCase == ProfilePhotoPickCase.AVATAR) {
                        _binding?.ciProfileAvatar?.isVisible = true
                    }
                    if (photoPickCase == ProfilePhotoPickCase.COVER) {
                        _binding?.ciProfileCover?.isVisible = true
                    }
                    onPhotoPicked(photoPickCase, data!!)
                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(activity, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(activity, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //TODO deal with different resolutions for light/regular/avatar cases
    private fun onPhotoPicked(profilePhotoPickCase: ProfilePhotoPickCase, resultData: Intent) {
        //Image Uri will not be null for RESULT_OK
        val fileUri = resultData.data!!

        Log.d("PROFILE", "UPDATING IMAGE FOR PROFILE [${FirebaseAuth.getInstance().uid!!}]")

        val profile = viewModel.userEntity.value ?: UserProfileEntity()

        val imagesRef: StorageReference =
            FirebaseStorage.getInstance().reference.child(
                "users"
            )
                .child(FirebaseAuth.getInstance().uid!!)

        if (profilePhotoPickCase == ProfilePhotoPickCase.AVATAR) {
            val photoRef = imagesRef.child("avatar.jpg")
            val photoLightRef = imagesRef.child("avatarLight.jpg")

            val photoRefUploadTask: UploadTask = photoRef.putFile(fileUri)
            val photoLightRefUploadTask: UploadTask = photoLightRef.putFile(fileUri)

            photoRefUploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                photoRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    profile.data.photo.photoThumb = downloadUri.toString()
                    viewModel.updateProfile(profile)
                    _binding?.ciProfileAvatar?.isVisible = false
                } else {
                    Toast.makeText(activity, "Photo Upload Failed", Toast.LENGTH_SHORT).show()
                }
            }
            photoLightRefUploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                photoRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    profile.data.photo.photoLight = downloadUri.toString()
                    viewModel.updateProfile(profile)
                    _binding?.ciProfileAvatar?.isVisible = false
                } else {
                    Toast.makeText(activity, "Photo Upload Failed", Toast.LENGTH_SHORT).show()
                }
            }

        } else {
            val backgroundRef = imagesRef.child("background.jpg")
            val photoBackgroundRefUploadTask: UploadTask = backgroundRef.putFile(fileUri)

            photoBackgroundRefUploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                backgroundRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    profile.data.photo.photo = downloadUri.toString()
                    viewModel.updateProfile(profile)
                    _binding?.ciProfileCover?.isVisible = false
                } else {
                    Toast.makeText(activity, "Photo Upload Failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}