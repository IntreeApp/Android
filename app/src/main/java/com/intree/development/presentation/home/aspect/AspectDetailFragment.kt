package com.intree.development.presentation.home.aspect

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.intree.development.R
import com.intree.development.databinding.AspectDetailFragmentBinding
import com.intree.development.domain.AspectFirestoreImageData
import com.intree.development.presentation.home.aspect.vm.AspectViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AspectDetailFragment : Fragment() {

    private val args: AspectDetailFragmentArgs by navArgs()

    private val viewModel by viewModels<AspectViewModel>()

    private var _binding: AspectDetailFragmentBinding? = null

    private var photosForPreviewList: MutableList<AspectFirestoreImageData> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.aspect_detail_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = AspectDetailFragmentBinding.bind(view)

        setupRoomPhotosPreviewGrid()

        viewModel.aspectFirestoreMetadata.observe(viewLifecycleOwner, {
            if (it!!.baseParams.gallery.isNotEmpty()) {
                photosForPreviewList = it.baseParams.gallery.sortedBy { photoItem ->  photoItem.idx }.take(
                    if (it.baseParams.gallery.size > 8) 9 else it.baseParams.gallery.size
                ).toMutableList()
                _binding!!.roomPhotosPreviewGridView.adapter!!.notifyDataSetChanged() //!
            }
        })

        _binding!!.btnEdit.setOnClickListener {
            val action =
                AspectDetailFragmentDirections.actionRoomDetailFragmentToCreateEditRoomFragment(args.roomId)
            action.existingRoomToEdit = viewModel.aspectEntity.value!!
            findNavController().navigate(action)
        }

        viewModel.aspectEntity.observe(viewLifecycleOwner, {
            if (viewModel.aspectEntity.value?.cover?.isNotEmpty() == true) {
                Picasso.get().load(viewModel.aspectEntity.value?.cover!!).into(_binding?.screenImg)
            }
            if (viewModel.aspectEntity.value?.logo?.isNotEmpty() == true) {
                Picasso.get().load(viewModel.aspectEntity.value?.logo!!)
                    .into(_binding?.roomLogoImageView)
            }
            if (viewModel.aspectEntity.value?.title?.isNotEmpty() == true) {
                _binding!!.tvRoomTitle.text = viewModel.aspectEntity.value?.title
                _binding!!.createRoomTitle.text = viewModel.aspectEntity.value?.title
            }
            if (viewModel.aspectEntity.value?.infoLink?.isNotEmpty() == true) {
                _binding!!.tvRoomLinkTitle.text = viewModel.aspectEntity.value?.infoLink
            }
            if (viewModel.aspectEntity.value?.bio?.isNotEmpty() == true) {
                _binding!!.tvAboutRoomDescription.text = viewModel.aspectEntity.value?.bio
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
                            R.layout.list_item_aspect_gallery_simple,
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