package com.intree.development.presentation.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.intree.development.R
import com.intree.development.databinding.ExploreBottomSheetBinding

class ExploreBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private lateinit var binding: ExploreBottomSheetBinding

    companion object {
        const val TAG = "ExploreBottomSheetDialogFragment"
        fun newInstance(): ExploreBottomSheetDialogFragment {
            val args = Bundle()
            val fragment = ExploreBottomSheetDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    //    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.explore_bottom_sheet, container, false)
//    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!::binding.isInitialized) {
            binding = ExploreBottomSheetBinding.inflate(inflater)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun initOnClickListeners() {

    }
}