package com.intree.development.presentation.home.room

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.intree.development.R
import com.intree.development.databinding.RoomDetailFragmentBinding
import com.intree.development.domain.RoomFirestoreImageData
import com.intree.development.presentation.auth.forms.SignUpPhoneFormFragmentDirections
import com.intree.development.presentation.home.room.vm.RoomViewModel
import com.intree.development.util.TERMS_URL
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoomDetailFragment : Fragment() {

    private val args: RoomDetailFragmentArgs by navArgs()

    private val viewModel by viewModels<RoomViewModel>()

    private var _binding: RoomDetailFragmentBinding? = null

    private var photosForPreviewList: MutableList<RoomFirestoreImageData> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.room_detail_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = RoomDetailFragmentBinding.bind(view)

        setupRoomPhotosPreviewGrid()

        viewModel.roomFirestoreMetadata.observe(viewLifecycleOwner, {
            if (it!!.baseParams.gallery.isNotEmpty()) {
                photosForPreviewList = it.baseParams.gallery.sortedBy { photoItem ->  photoItem.idx }.take(
                    if (it.baseParams.gallery.size > 8) 9 else it.baseParams.gallery.size
                ).toMutableList()
                _binding!!.roomPhotosPreviewGridView.adapter!!.notifyDataSetChanged() //!
            }
        })

        _binding!!.btnEdit.setOnClickListener {
            val action =
                RoomDetailFragmentDirections.actionRoomDetailFragmentToCreateEditRoomFragment(args.roomId)
            action.existingRoomToEdit = viewModel.roomEntity.value!!
            findNavController().navigate(action)
        }

        viewModel.roomEntity.observe(viewLifecycleOwner, {
            if (viewModel.roomEntity.value?.cover?.isNotEmpty() == true) {
                Picasso.get().load(viewModel.roomEntity.value?.cover!!).into(_binding?.screenImg)
            }
            if (viewModel.roomEntity.value?.logo?.isNotEmpty() == true) {
                Picasso.get().load(viewModel.roomEntity.value?.logo!!)
                    .into(_binding?.roomLogoImageView)
            }
            if (viewModel.roomEntity.value?.title?.isNotEmpty() == true) {
                _binding!!.tvRoomTitle.text = viewModel.roomEntity.value?.title
                _binding!!.createRoomTitle.text = viewModel.roomEntity.value?.title
            }
            if (viewModel.roomEntity.value?.infoLink?.isNotEmpty() == true) {
                _binding!!.tvRoomLinkTitle.text = viewModel.roomEntity.value?.infoLink
            }
            if (viewModel.roomEntity.value?.bio?.isNotEmpty() == true) {
                _binding!!.tvAboutRoomDescription.text = viewModel.roomEntity.value?.bio
            }
        })

        viewModel.getRoomMetadata(args.roomId)
        viewModel.getRoomData(args.roomId)
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
}