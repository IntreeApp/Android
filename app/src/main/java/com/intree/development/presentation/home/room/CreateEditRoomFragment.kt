package com.intree.development.presentation.home.room

import com.intree.development.util.ShowroomPhotoPickCase
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
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.intree.development.R
import com.intree.development.databinding.CreateRoomFragmentBinding
import com.intree.development.domain.RoomEntity
import com.intree.development.domain.RoomFirestoreImageData
import com.intree.development.presentation.home.MainActivity
import com.intree.development.presentation.home.profile.vm.ProfileViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import android.app.Activity.RESULT_OK
import android.net.Uri
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.intree.development.presentation.auth.AuthActivity
import com.intree.development.presentation.home.room.vm.RoomViewModel
import com.yanzhenjie.album.AlbumFile
import com.yanzhenjie.album.Album
import com.yanzhenjie.album.AlbumConfig
import java.io.File


@AndroidEntryPoint
class CreateEditRoomFragment : Fragment() {

    private var _binding: CreateRoomFragmentBinding? = null

    private val args: CreateEditRoomFragmentArgs by navArgs()

    private val viewModel by viewModels<RoomViewModel>()
    private val profileViewModel by viewModels<ProfileViewModel>()

    private val logoPhotoPickerActivityResultLauncher =
        getPhotoPickerActivityResultLauncher(ShowroomPhotoPickCase.LOGO)
    private val coverPhotoPickerActivityResultLauncher =
        getPhotoPickerActivityResultLauncher(ShowroomPhotoPickCase.COVER)
    private val albumList = ArrayList<AlbumFile>()

    private var photosForPreviewList: MutableList<RoomFirestoreImageData> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.create_room_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = CreateRoomFragmentBinding.bind(view)

        Album.initialize(
            AlbumConfig.newBuilder(activity)
                .setAlbumLoader(AlbumMediaLoader())
                .build()
        )

        _binding?.ciRoomCover?.isVisible = false
        _binding?.ciRoomLogo?.isVisible = false
        _binding?.ciRoomPhotosActionInProgress?.isVisible = false
        _binding?.photoActionBtnsHolder?.isVisible = true
        _binding?.roomPhotosNestedScrollView?.isVisible = true

        val room: RoomEntity

        if (args.existingRoomToEdit != null && args.existingRoomId != null) {
            room = args.existingRoomToEdit!!
            viewModel.selectedRoomId = args.existingRoomId as String
            if (room.cover.isNotEmpty()) {
                Picasso.get().load(room.cover).into(_binding?.screenImg)
            }
            if (room.logo.isNotEmpty()) {
                Picasso.get().load(room.logo)
                    .into(_binding?.roomLogoImageView)
            }
            _binding?.createRoomTitle?.text = getString(R.string.edit_room_title)
        } else {
            room = RoomEntity()
            viewModel.createRoom(room)
        }

        setupTextInputFields(room)
        setupRoomSingleImagePickActions()
        setupRoomPhotosPreviewGrid()

        val tagList = ArrayList<String>()
        if (room.tg.isNotEmpty()) {
            tagList.addAll(room.tg.split(", "))
        } else {
            tagList.add("#")
            tagList.add("#")
            tagList.add("#")
        }
        val tagAdapter = TagAdapter(tagList)
        setupTagList(tagAdapter)

        viewModel.roomFirestoreMetadata.observe(viewLifecycleOwner, {
            if (it!!.baseParams.gallery.isNotEmpty()) {
                photosForPreviewList = it.baseParams.gallery.sortedBy { photoItem ->  photoItem.idx }.take(
                    if (it.baseParams.gallery.size > 5) 6 else it.baseParams.gallery.size
                ).toMutableList()
                _binding!!.roomPhotosPreviewGridView.adapter!!.notifyDataSetChanged() //!
                _binding!!.roomPhotosPreviewGridView.post {
                    _binding!!.roomPhotosPreviewGridView.smoothScrollToPosition(tagAdapter.itemCount - 1)
                }
            }
        })

        _binding?.btnAddMultiplePhotos?.setOnClickListener {
            _binding?.ciRoomPhotosActionInProgress?.isVisible = true
            _binding?.photoActionBtnsHolder?.isVisible = false
            _binding?.roomPhotosNestedScrollView?.isVisible = false
            startRoomMultiplePhotosPicking()
        }
        _binding?.btnEditDeleteMultiplePhotos?.setOnClickListener {

                val action =
                    CreateEditRoomFragmentDirections.actionCreateRoomFragment2ToRoomExistingImagesGalleryFragment2()
                action.roomId = viewModel.selectedRoomId
                findNavController().navigate(action)
        }

        _binding?.btnEdit?.setOnClickListener {

            val existingRoomData = viewModel.roomEntity.value

            val logoURL = existingRoomData?.logo ?: ""
            val coverURL = existingRoomData?.cover ?: ""

            room.tg = tagAdapter.tagList.joinToString(", ") //need more testing on tags
            room.logo = if (logoURL.isEmpty() && args.existingRoomToEdit != null) args.existingRoomToEdit!!.logo else logoURL
            room.cover = if (coverURL.isEmpty() && args.existingRoomToEdit != null) args.existingRoomToEdit!!.cover else logoURL
            room.draft = "NO"
            viewModel.updateRoomData(room)

            Handler(Looper.getMainLooper()).postDelayed({

                if (validateRoomData(room)) {
                    if (!profileViewModel.isSignUpFlowFinished) {
                        val intent = Intent(activity, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Log.d("SHOWROOM", "NAVIGATING TO PROFILE PREVIEW...")
                        //findNavController().popBackStack()
                        findNavController().navigate(R.id.action_createRoomFragment2_to_roomExistingImagesGalleryFragment2)
                    }
                } else {
                    Log.d("SHOWROOM", "ROOM DATA IS MISSING: ${room.toString()}")
                    Toast.makeText(
                        activity,
                        "Please provide all required room data [TEST : cover, logo, headTitle, title]",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }, 300)
        }

        if (args.existingRoomId != null) {
            viewModel.getRoomMetadata(optRoomId = args.existingRoomId as String)
        }
    }

    private fun validateRoomData(room: RoomEntity): Boolean {
        Log.d("SHOWROOM", "ROOM DATA IS MISSING: ${room.cover} | ${room.avatar} | ${room.title} | ${room.headTitle}")
        return room.cover.isNotEmpty() && room.logo.isNotEmpty()
                && room.title.isNotEmpty() && room.headTitle.isNotEmpty()

    }

    private fun setupTagList(tagAdapter: TagAdapter) {
        _binding?.tagsRecyclerView?.adapter = tagAdapter
        _binding?.tagsRecyclerView?.layoutManager = LinearLayoutManager(activity)

        _binding?.btnAddRoomTag?.setOnClickListener {
            tagAdapter.addTag()
        }

        _binding?.btnRemoveRoomTag?.setOnClickListener {
            tagAdapter.removeLastTag()
        }

        viewModel.roomEntity.observe(viewLifecycleOwner, {
            if (viewModel.roomEntity.value?.cover!!.isNotEmpty()) {
                Picasso.get().load(viewModel.roomEntity.value?.cover!!).into(_binding?.screenImg)
            }
            if (viewModel.roomEntity.value?.logo!!.isNotEmpty()) {
                Picasso.get().load(viewModel.roomEntity.value?.logo!!)
                    .into(_binding?.roomLogoImageView)
            }
        })
    }

    private fun setupRoomSingleImagePickActions() {
        _binding?.btnChangeRoomCover?.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(1024)  //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                    1080,
                    1080
                )  //Final image resolution will be less than 1080 x 1080(Optional)
                .createIntent { intent ->
                    coverPhotoPickerActivityResultLauncher.launch(intent)
                }
        }
        _binding?.btnPseudoChangeAvatar?.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(1024)  //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                    1080,
                    1080
                )  //Final image resolution will be less than 1080 x 1080(Optional)
                .createIntent { intent ->
                    logoPhotoPickerActivityResultLauncher.launch(intent)
                }
        }
    }

    private fun setupTextInputFields(room: RoomEntity) {
        _binding?.etRoomName?.setText(room.headTitle)
        _binding?.etTitleForRoom?.setText(room.title)
        _binding?.etLinkForRoom?.setText(room.infoLink)
        _binding?.etEmailForRoom?.setText(room.infoEmail)
        _binding?.etRoomPhone?.setText(room.infoPhone)
        _binding?.etRoomDescription?.setText(room.bio)

        _binding?.etRoomName?.addTextChangedListener {
            room.headTitle = it.toString()
        }
        _binding?.etTitleForRoom?.addTextChangedListener {
            room.title = it.toString()
        }
        _binding?.etLinkForRoom?.addTextChangedListener {
            room.infoLink = it.toString()
            room.shareLink = it.toString()
        }
        _binding?.etEmailForRoom?.addTextChangedListener {
            room.infoEmail = it.toString()
        }
        _binding?.etRoomPhone?.addTextChangedListener {
            room.infoPhone = it.toString()
        }
        _binding?.etRoomDescription?.addTextChangedListener {
            room.bio = it.toString()
        }
    }

    private fun setupRoomPhotosPreviewGrid() {
        _binding!!.roomPhotosPreviewGridView.adapter =
            object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int
                ): RecyclerView.ViewHolder {
                    return object : RecyclerView.ViewHolder(
                        LayoutInflater.from(parent.context).inflate(
                            R.layout.list_item_room_gallery_simple,
                            parent,
                            false
                        )
                    ) {
                        //Stub
                    }
                }

                override fun getItemCount() = photosForPreviewList.size

                override fun onBindViewHolder(
                    holder: RecyclerView.ViewHolder,
                    position: Int
                ) {
                    val data = photosForPreviewList[position]
                    val imageView =
                        holder.itemView.findViewById(R.id.imgRoomPhotoSimplified) as ImageView

                    val circularProgressDrawable = CircularProgressDrawable(activity as FragmentActivity)
                    circularProgressDrawable.strokeWidth = 5f
                    circularProgressDrawable.centerRadius = 30f
                    circularProgressDrawable.start()
                    Picasso.get().load(data.img)
                        .placeholder(circularProgressDrawable)
                        .into(imageView)
                }
            }
    }

    private fun startRoomMultiplePhotosPicking() {
        Album.image(this) // Image selection.
            .multipleChoice()
            .camera(true)
            .columnCount(2)
            .selectCount(20) //TODO change to 50+ (up to 100) after testing and PoC
            .checkedList(albumList)
            //.filterSize() // Filter the file size.
            //.filterMimeType() // Filter file format.
            //.afterFilterVisibility(true) // Show the filtered files, but they are not available.
            .onResult { pickedAlbumFiles ->
                val imagesRef: StorageReference =
                    FirebaseStorage.getInstance().reference
                        .child("users")
                        .child(FirebaseAuth.getInstance().uid!!)
                        .child("showroom")
                        .child(viewModel.selectedRoomId)
                var counter = 0
                pickedAlbumFiles.forEach {
                    //TODO add executor w/background thread for multiple images upload optimization
                    counter += 1
                    onRoomPhotoPickReady(
                        imagesRef,
                        fileUri = Uri.fromFile(File(it.path)),
                        index = counter,
                        isLast = counter == pickedAlbumFiles.size
                    )
                }
            }
            .onCancel {
                //Stub
            }
            .start()
    }

    private fun onRoomPhotoPickReady(
        imagesRef: StorageReference,
        fileUri: Uri,
        index: Int,
        isLast: Boolean
    ) {
        Log.d("SHOWROOM", "ADDING PHOTO FOR ROOM WITH ID: ${viewModel.selectedRoomId}")

        FirebaseFirestore.getInstance().collection("showroom")
            .document(viewModel.selectedRoomId).collection("gallery").get()
            .addOnSuccessListener {
                val upcomingIdx = it.size() + index

                Log.d("SHOWROOM", "ADDING PHOTO WITH INDEX: $upcomingIdx")

                FirebaseFirestore.getInstance().collection("showroom")
                    .document(viewModel.selectedRoomId)
                    .collection("gallery")
                    .add(
                        RoomFirestoreImageData(
                            idx = upcomingIdx,
                            img = "-"
                        )
                    ).addOnCompleteListener { photoTask ->

                        val newPhotoId = photoTask.result!!.id

                        val photoRef = imagesRef.child("photos").child("$newPhotoId.jpg")

                        val photoBackgroundRefUploadTask: UploadTask =
                            photoRef.putFile(fileUri)

                        photoBackgroundRefUploadTask.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                imagesRef.child("photos").child("$newPhotoId.jpg").downloadUrl
                                    .addOnSuccessListener { downloadUri ->
                                        Log.d("ROOM_PHOTOS", "UPLOADED ROOM PHOTO: $downloadUri")

                                        FirebaseFirestore.getInstance().collection("showroom")
                                            .document(viewModel.selectedRoomId)
                                            .collection("gallery")
                                            .document(newPhotoId)
                                            .update("img", downloadUri.toString())
                                            //.set(RoomFirestoreImageData(idx = size, img = downloadUri))
                                            .addOnCompleteListener { metadataTask ->
                                                if (isLast) {
                                                    _binding?.ciRoomPhotosActionInProgress?.isVisible =
                                                        false
                                                    _binding?.photoActionBtnsHolder?.isVisible =
                                                        true
                                                    _binding?.roomPhotosNestedScrollView?.isVisible =
                                                        true
                                                    viewModel.getRoomMetadata()
                                                    Toast.makeText(
                                                        activity,
                                                        "Selected photos are successfully uploaded",
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                }
                                                Log.d(
                                                    "ROOM_PHOTOS",
                                                    "ROOM METADATA UPDATE TASK COMPLETE. SUCCESS?: ${metadataTask.isSuccessful}"
                                                )
                                            }.addOnFailureListener { ex ->
                                                Log.d(
                                                    "ROOM_PHOTOS",
                                                    "ROOM METADATA UPDATE ERROR! ${ex.localizedMessage}"
                                                )
                                            }
                                    }
                            } else {
                                Toast.makeText(
                                    activity,
                                    "Photo Upload Failed",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
            }
    }

    private fun getPhotoPickerActivityResultLauncher(photoPickCase: ShowroomPhotoPickCase)
            : ActivityResultLauncher<Intent> {
        return registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data
            when (resultCode) {
                RESULT_OK -> {
                    if (photoPickCase == ShowroomPhotoPickCase.COVER) {
                        _binding?.ciRoomCover?.isVisible = true
                    }
                    if (photoPickCase == ShowroomPhotoPickCase.LOGO) {
                        _binding?.ciRoomLogo?.isVisible = true
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

    private fun onPhotoPicked(photoPickCase: ShowroomPhotoPickCase, resultData: Intent) {
        //Image Uri will not be null for RESULT_OK
        val fileUri = resultData.data!!

        val room = viewModel.roomEntity.value ?: RoomEntity()

        val imagesRef: StorageReference =
            FirebaseStorage.getInstance().reference
                .child("users")
                .child(FirebaseAuth.getInstance().uid!!)
                .child("showroom")
                .child(viewModel.selectedRoomId)

        if (photoPickCase == ShowroomPhotoPickCase.LOGO) {
            Log.d("SHOWROOM", "UPDATING LOGO FOR ROOM WITH ID: ${viewModel.selectedRoomId}")

            val avatarRef = imagesRef.child("logo.jpg")
            val avatarRefUploadTask: UploadTask = avatarRef.putFile(fileUri)
            avatarRefUploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                avatarRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    room.logo = downloadUri.toString()
                    viewModel.updateRoomData(room)
                    _binding?.ciRoomLogo?.isVisible = false
                } else {
                    Toast.makeText(activity, "Photo Upload Failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
        if (photoPickCase == ShowroomPhotoPickCase.COVER) {
            Log.d("SHOWROOM", "UPDATING COVER FOR ROOM WITH ID: ${viewModel.selectedRoomId}")

            val coverRef = imagesRef.child("cover.jpg")
            val coverRefUploadTask: UploadTask = coverRef.putFile(fileUri)
            coverRefUploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                coverRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    room.cover = downloadUri.toString()
                    viewModel.updateRoomData(room)
                    _binding?.ciRoomCover?.isVisible = false
                } else {
                    Toast.makeText(activity, "Photo Upload Failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
        //! for multiple photos selection/addition case, we use another mechanism
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}