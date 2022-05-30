package com.intree.development.presentation.home.network

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
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.card.MaterialCardView
import com.intree.development.R
import com.intree.development.databinding.FragmentCreateGroupBinding
import com.intree.development.databinding.FragmentNetworkPopupBinding
import com.intree.development.presentation.home.network.vm.NetworkViewModel

class NetworkPopupFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentNetworkPopupBinding

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
        return inflater.inflate(R.layout.fragment_network_popup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNetworkPopupBinding.bind(view)

        addCheckableGroupCard("BJJ", R.drawable.bjj)
        addCheckableGroupCard("Crypto", R.drawable.crypto)
        addCheckableGroupCard("Fashion", R.drawable.fashion)
        addCheckableGroupCard("BJJ", R.drawable.bjj)
        addCheckableGroupCard("Crypto", R.drawable.crypto)
        addCheckableGroupCard("Fashion", R.drawable.fashion)
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

    private fun addCheckableGroupCard(name: String, imageResource: Int) {
        val view = layoutInflater.inflate(R.layout.card_groups_checkable, null)

        val nameView = view.findViewById<TextView>(R.id.nameCardGroupsCheckable)
        val imageView = view.findViewById<ImageView>(R.id.ivCardGroupsCheckable)
        val cardView = view.findViewById<MaterialCardView>(R.id.cardViewCheckable)

        nameView.text = name
        imageView.setImageResource(imageResource)

        cardView.setOnClickListener {
            cardView.isChecked = !cardView.isChecked
        }

        binding.cardContainerAspects.addView(view)
    }
}