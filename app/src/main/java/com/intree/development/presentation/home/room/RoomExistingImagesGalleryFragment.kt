package com.intree.development.presentation.home.room

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.intree.development.databinding.RoomExistingImagesGalleryFragmentBinding
import com.squareup.picasso.Picasso
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.intree.development.R
import com.intree.development.domain.transferrable_objects.RoomPhotoItemTO
import com.intree.development.presentation.home.room.vm.RoomViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoomExistingImagesGalleryFragment : Fragment() {

    private val args: RoomExistingImagesGalleryFragmentArgs by navArgs()

    private var _binding: RoomExistingImagesGalleryFragmentBinding? = null

    private val viewModel by viewModels<RoomViewModel>()

    private var photoDataList: MutableList<RoomPhotoItemTO> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.room_existing_images_gallery_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = RoomExistingImagesGalleryFragmentBinding.bind(view)

        viewModel.getRoomData(args.roomId)

        _binding!!.btnDeleteSelectedItems.setOnClickListener {
            onDeleteSelectedPhotosClick()
        }

        _binding!!.btnSaveRoomGallery.setOnClickListener {
            updateRoomPhotoIndexesForResult()
        }

        _binding!!.recyclerView.adapter =
            object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int
                ): RecyclerView.ViewHolder {
                    return object : RecyclerView.ViewHolder(
                        LayoutInflater.from(parent.context).inflate(
                            R.layout.list_item_room_gallery_draggable,
                            parent,
                            false
                        )
                    ) {
                        //Stub
                    }
                }

                override fun getItemCount() = photoDataList.size

                override fun onBindViewHolder(
                    holder: RecyclerView.ViewHolder,
                    position: Int
                ) {
                    val data = photoDataList[position]
                    val imageView =
                        holder.itemView.findViewById(R.id.imgRoomPhoto) as ImageView
                    val selectedIndicator =
                        holder.itemView.findViewById(R.id.selectedIndicator) as View
                    selectedIndicator.isVisible = false

                    val circularProgressDrawable =
                        CircularProgressDrawable(activity as FragmentActivity)
                    circularProgressDrawable.strokeWidth = 5f
                    circularProgressDrawable.centerRadius = 30f
                    circularProgressDrawable.start()
                    Picasso.get().load(data.item.img)
                        .placeholder(circularProgressDrawable)
                        .error(R.drawable.ic_delete_or_error)
                        .into(imageView)
                }
            }

        _binding!!.recyclerView.addOnItemTouchListener(
            RecyclerItemClickListener(
                context,
                _binding!!.recyclerView,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View?, position: Int) {
                        val selectedIndicator =
                            _binding!!.recyclerView.findContainingViewHolder(view!!)?.itemView?.findViewById(
                                R.id.selectedIndicator
                            ) as View
                        if (!photoDataList[position].isSelected) {
                            selectedIndicator.isVisible = true
                            photoDataList[position].isSelected = true
                        } else {
                            selectedIndicator.isVisible = false
                            photoDataList[position].isSelected = false
                        }
                    }

                    override fun onLongItemClick(view: View?, position: Int) {
                        // Stub
                    }
                })
        )

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun isLongPressDragEnabled() = true
            override fun isItemViewSwipeEnabled() = false
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                val dragFlags =
                    ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                val swipeFlags =
                    if (isItemViewSwipeEnabled) ItemTouchHelper.START or ItemTouchHelper.END else 0
                return makeMovementFlags(dragFlags, swipeFlags)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                if (viewHolder.itemViewType != target.itemViewType)
                    return false
                val fromPosition = viewHolder.adapterPosition
                val toPosition = target.adapterPosition

                val itemReplacing = photoDataList.removeAt(fromPosition)

                photoDataList.add(toPosition, itemReplacing)

                var counter = 0
                photoDataList.forEach {
                    counter += 1
                    it.idx = counter
                    it.item.idx = counter
                }

                recyclerView.adapter!!.notifyItemMoved(fromPosition, toPosition)
                return true
            }
        })
        itemTouchHelper.attachToRecyclerView(_binding!!.recyclerView)

        viewModel.roomFirestoreMetadata.observe(viewLifecycleOwner, {
            if (it!!.baseParams.gallery.isNotEmpty()) {
                photoDataList = it.baseParams.gallery.sortedBy { photoItem -> photoItem.idx }
                    .map { item -> RoomPhotoItemTO(isSelected = false, idx = item.idx, item) }.toMutableList()
                _binding!!.recyclerView.adapter!!.notifyDataSetChanged() //!
            }
        })

        viewModel.getRoomMetadata(optRoomId = args.roomId)
    }

    private fun updateRoomPhotoIndexesForResult() {
        var counter = 0
        photoDataList.forEach {
            counter += 1
            viewModel.updateRoomPhotoIndex(args.roomId, it.item.firestoreId, it.item.idx)
            if (counter == photoDataList.size) {
                Handler(Looper.myLooper()!!).postDelayed({
                    val action = RoomExistingImagesGalleryFragmentDirections.actionRoomExistingImagesGalleryFragment2ToCreateRoomFragment2(args.roomId)
                    action.existingRoomToEdit = viewModel.roomEntity.value!!
                    findNavController().navigate(action)
                }, (500).toLong())
            }
        }
    }

    private fun onDeleteSelectedPhotosClick() {
        var counter = 0
        val selectedItems =
            photoDataList.filter { transferrableObj -> transferrableObj.isSelected }.toList()
        val unselectedItems =
            photoDataList.filter { transferrableObj -> !transferrableObj.isSelected }.toList()
        selectedItems.forEach { to ->
            counter += 1
            viewModel.deleteRoomPhoto(args.roomId, to.item.firestoreId)
            if (counter == selectedItems.size) {
                unselectedItems.sortedBy { un -> un.item.idx }.forEach {
                    viewModel.updateRoomPhotoIndex(args.roomId, it.item.firestoreId, it.item.idx)
                }
                Handler(Looper.myLooper()!!).postDelayed({
                    viewModel.getRoomMetadata(args.roomId)
                }, (500).toLong())
            }
        }
    }

}