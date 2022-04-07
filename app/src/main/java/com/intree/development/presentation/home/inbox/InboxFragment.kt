package com.intree.development.presentation.home.inbox

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransitionImpl
import com.intree.development.databinding.FragmentInboxBinding
import com.intree.development.R

class InboxFragment : Fragment(R.layout.fragment_inbox)  {

    lateinit var _binding: FragmentInboxBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentInboxBinding.bind(view)

        // Add New Group button as groups-card
        addGroupCard("BJJ", R.drawable.bjj)
        addGroupCard("Crypto", R.drawable.crypto)
        addGroupCard("Fashion", R.drawable.fashion)
    }

    private fun addGroupCard(name: String, imageResource: Int) {
        val view = layoutInflater.inflate(R.layout.inbox_card_groups, null)

        val nameView = view.findViewById<TextView>(R.id.nameCardGroups)
        val imageView = view.findViewById<ImageView>(R.id.ivCardGroups)

        nameView.text = name
        imageView.setImageResource(imageResource)

        _binding.cardContainerGroups.addView(view)
    }
}