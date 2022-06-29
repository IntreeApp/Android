package com.intree.development.presentation.home.network

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.intree.development.R
import com.intree.development.data.uiModels.TreetsUIModel
import com.intree.development.data.uiModels.AspectsExtendedUIModel
import com.intree.development.databinding.FragmentNetworkPopupBinding
import com.intree.development.presentation.adapter.TreetsAdapter
import com.intree.development.presentation.adapter.InviteAspectsAdapter
import com.intree.development.presentation.home.network.vm.NetworkViewModel

class NetworkPopupFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentNetworkPopupBinding
    private val adapterAspects = InviteAspectsAdapter()
    private val adapterTreets = TreetsAdapter()

    /*companion object {
        const val TAG = "SNetworkPopupFragmentDialog"

        private const val KEY_TITLE = "KEY_TITLE"
        private const val KEY_SUBTITLE = "KEY_SUBTITLE"

        fun newInstance(title: String, subTitle: String): NetworkPopupFragment {
            val args = Bundle()
            args.putString(KEY_TITLE, title)
            args.putString(KEY_SUBTITLE, subTitle)
            val fragment = NetworkPopupFragment()
            fragment.arguments = args
            return fragment
        }
    }*/

    private lateinit var viewModel: NetworkViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (!::binding.isInitialized) {
            binding = FragmentNetworkPopupBinding.inflate(inflater)
        }
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initContent()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    private fun setupClickListeners(view: View) {
        /*view.btnPositive.setOnClickListener {
            // TODO: Do some task here
            dismiss()
        }
        view.btnNegative.setOnClickListener {
            // TODO: Do some task here
            dismiss()
        }*/
    }

    /*override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NetworkViewModel::class.java)
        // TODO: Use the ViewModel
    }*/

    private fun initContent() {
        //initOnClickListeners()
        initTreetsAdapter()
        initAspectsAdapter()


    }

    private fun initOnClickListeners() {
        TODO("Not yet implemented")
    }

    private fun initTreetsAdapter() {
        binding.rvTreets.adapter = adapterTreets
        binding.rvTreets.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        binding.rvTreets.layoutManager = layoutManager

        adapterTreets.setData(treetsMockData())
    }

    private fun initAspectsAdapter() {
        binding.rvAspects.adapter = adapterAspects
        binding.rvAspects.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        binding.rvAspects.layoutManager = layoutManager

        adapterAspects.setData(aspectsMockData())
    }

    private fun aspectsMockData(): ArrayList<AspectsExtendedUIModel> {
        val list = ArrayList<AspectsExtendedUIModel>()
        list.add(
            AspectsExtendedUIModel(
                1,
                "Design Intern",
                R.drawable.unsplash_mock_1,
                R.drawable.julian
            )
        )
        list.add(
            AspectsExtendedUIModel(
                2,
                "Crypto",
                R.drawable.crypto,
                R.drawable.janko
            )
        )
        return list
    }

    private fun treetsMockData(): ArrayList<TreetsUIModel> {
        val list = ArrayList<TreetsUIModel>()
        list.add(
            TreetsUIModel(
                1,
                "Fashion 2022",
                R.drawable.fashion
            )
        )

        return list
    }



}