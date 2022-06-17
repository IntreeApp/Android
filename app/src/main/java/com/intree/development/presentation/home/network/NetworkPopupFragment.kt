package com.intree.development.presentation.home.network

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.card.MaterialCardView
import com.intree.development.R
import com.intree.development.data.uiModels.BenefitsUIModel
import com.intree.development.data.uiModels.IdentitiesUIModel
import com.intree.development.databinding.FragmentCreateGroupBinding
import com.intree.development.databinding.FragmentNetworkPopupBinding
import com.intree.development.databinding.ProfilePreviewModeFragmentBinding
import com.intree.development.domain.UserProfileEntity
import com.intree.development.presentation.adapter.BenefitsAdapter
import com.intree.development.presentation.adapter.IdentitiesAdapter
import com.intree.development.presentation.home.network.vm.NetworkViewModel
import com.squareup.picasso.Picasso

class NetworkPopupFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentNetworkPopupBinding
    private val adapterIdentities = IdentitiesAdapter()
    private val adapterBenefits = BenefitsAdapter()

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
        initBenefitsAdapter()
        initAspectsAdapter()


    }

    private fun initOnClickListeners() {
        TODO("Not yet implemented")
    }

    private fun initBenefitsAdapter() {
        binding.rvBenefits.adapter = adapterBenefits
        binding.rvBenefits.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        binding.rvBenefits.layoutManager = layoutManager

        adapterBenefits.setData(benefitsMockData())
    }

    private fun initAspectsAdapter() {
        binding.rvAspects.adapter = adapterIdentities
        binding.rvAspects.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        binding.rvAspects.layoutManager = layoutManager

        adapterIdentities.setData(aspectsMockData())
    }

    private fun aspectsMockData(): ArrayList<IdentitiesUIModel> {
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