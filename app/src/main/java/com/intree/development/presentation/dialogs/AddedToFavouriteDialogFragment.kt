package com.intree.development.presentation.dialogs

import android.Manifest
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.intree.development.R
import com.intree.development.data.uiModels.ExploreUIModel
import com.intree.development.databinding.DialogFragmentAddedToFavouriteBinding

class AddedToFavouriteDialogFragment : DialogFragment() {

    private lateinit var binding: DialogFragmentAddedToFavouriteBinding

    companion object {
        private const val exploreUIModelKey = "exploreUIModelKey"
        fun newInstance(exploreUIModel: ExploreUIModel): AddedToFavouriteDialogFragment {
            val fragment = AddedToFavouriteDialogFragment()
            val args = Bundle()
            args.putParcelable(exploreUIModelKey, exploreUIModel)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!::binding.isInitialized) {
            binding = DialogFragmentAddedToFavouriteBinding.inflate(inflater)
        }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setContent()
        initOnClickListeners()
    }

    private fun setContent() {
        val data = exploreUIModel()

        binding.tv2.text = data!!.nameOfUser
            .plus("'s ")
            .plus(resources.getString(R.string.posts_will_now_be_displayed_at_the_top))

        Glide.with(requireContext())
            .load(data.userPhoto)
            .override(300, 300)
            .error(R.drawable.ic_no_photo)
            .into(binding.imgRoundedProfilePhoto)
    }

    private fun initOnClickListeners() {
        binding.btnContinue.setOnClickListener {
            dialog?.cancel()
        }

        binding.cbDoNotShow.setOnCheckedChangeListener { _, b ->

        }
    }

    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.WRAP_CONTENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window?.setLayout(width, height)
            dialog.window?.setBackgroundDrawableResource(
                R.color.transparent
            )
            dialog.setCancelable(false)
        }
    }

    private fun exploreUIModel(): ExploreUIModel? {
        return requireArguments().getParcelable(exploreUIModelKey)
    }
}