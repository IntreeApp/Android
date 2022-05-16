package com.intree.development.presentation.home.create_group

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.intree.development.databinding.FragmentCreateGroupBinding
import com.google.android.material.card.MaterialCardView
import com.intree.development.R
import com.intree.development.presentation.adapter.CreateGroupAdapter
import com.intree.development.presentation.home.create_group.vm.CreateGroupViewModel


class CreateGroupFragment: Fragment(R.layout.fragment_create_group) {

    private lateinit var binding: FragmentCreateGroupBinding
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: CreateGroupAdapter
    private lateinit var mockDB: MutableList<CreateGroupViewModel>
    private lateinit var checkedState: BooleanArray



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding = FragmentCreateGroupBinding.bind(view)
        mockDB = createMockDb()
        initContactList()
        /*
        checkedState = BooleanArray(mockDB.size)

        println("checkedState: ${checkedState.size}")*/


        // Add New Group button as groups-card
        addCheckableGroupCard("BJJ", R.drawable.bjj)
        addCheckableGroupCard("Crypto", R.drawable.crypto)
        addCheckableGroupCard("Fashion", R.drawable.fashion)


        binding.btnBack.setOnClickListener{
            activity?.onBackPressed()
        }

        binding.btnCreate.setOnClickListener{
            val action = CreateGroupFragmentDirections.actionCreateGroupFragmentToCreateGroupDoneFragment()
            findNavController().navigate(action)
        }

    }

    private fun initContactList() {
        layoutManager = LinearLayoutManager(context)
        adapter = CreateGroupAdapter(mockDB
        ) { checkedItemsCount, contactListSize -> updateCountTextView(checkedItemsCount) }

        binding.contactsRecyclerView.layoutManager = layoutManager
        binding.contactsRecyclerView.adapter = adapter

    }



    private fun updateCountTextView(checkedItemsCount: Int) {
        if (checkedItemsCount < 1) {
            binding.checkedItemsCountTextView.visibility = View.INVISIBLE
        } else {
            binding.checkedItemsCountTextView.visibility = View.VISIBLE
        }
        binding.checkedItemsCountTextView.text = checkedItemsCount.toString()
    }

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

        binding.cardContainerGroups.addView(view)
    }

    private fun createMockDb(): MutableList<CreateGroupViewModel> {
        return mutableListOf(
            CreateGroupViewModel(R.drawable.amir, "Amir Khan"),
            CreateGroupViewModel(R.drawable.amir, "Amir Khan"),
            CreateGroupViewModel(R.drawable.amir, "Amir Khan"),
            CreateGroupViewModel(R.drawable.amir, "Amir Khan"),

            CreateGroupViewModel(R.drawable.seth_doyle, "Seth Doyle"),
            CreateGroupViewModel(R.drawable.seth_doyle, "Seth Doyle"),
            CreateGroupViewModel(R.drawable.seth_doyle, "Seth Doyle"),

            CreateGroupViewModel(R.drawable.janko, "Janko Pascal"),
            CreateGroupViewModel(R.drawable.janko, "Janko Pascal"),
            CreateGroupViewModel(R.drawable.janko, "Janko Pascal"),

            CreateGroupViewModel(R.drawable.amir, "Amir Khan"),
            CreateGroupViewModel(R.drawable.amir, "Amir Khan"),
            CreateGroupViewModel(R.drawable.amir, "Amir Khan"),
            CreateGroupViewModel(R.drawable.amir, "Amir Khan"),

            CreateGroupViewModel(R.drawable.seth_doyle, "Seth Doyle"),
            CreateGroupViewModel(R.drawable.seth_doyle, "Seth Doyle"),
            CreateGroupViewModel(R.drawable.seth_doyle, "Seth Doyle"),

            CreateGroupViewModel(R.drawable.janko, "Janko Pascal"),
            CreateGroupViewModel(R.drawable.janko, "Janko Pascal"),
            CreateGroupViewModel(R.drawable.janko, "Janko Pascal")


        )

    }
}