package com.intree.development.presentation.home.inbox

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.intree.development.databinding.FragmentInboxBinding
import com.intree.development.domain.IntroductionMessage
import com.intree.development.domain.Message
import com.intree.development.domain.ReadMessage
import com.intree.development.domain.UnreadMessage
import com.intree.development.R
import com.intree.development.presentation.adapter.InboxMessageAdapter
import com.intree.development.presentation.home.create_group.CreateGroupActivity

class InboxFragment : Fragment(R.layout.fragment_inbox)  {

    private lateinit var binding: FragmentInboxBinding
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: InboxMessageAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentInboxBinding.bind(view)

        // Add New Group button as groups-card
        addGroupCard("BJJ", R.drawable.bjj)
        addGroupCard("Crypto", R.drawable.crypto)
        addGroupCard("Fashion", R.drawable.fashion)

        binding.btnNewGroup.setOnClickListener{
            val intent = Intent(requireContext(), CreateGroupActivity::class.java)
            startActivity(intent)
        }

        findView()
        initMessageList()
        reload()
    }

    private fun findView() {
        swipeRefreshLayout = view?.findViewById(R.id.swipeRefreshLayout)!!
        recyclerView = view?.findViewById(R.id.messagesRecyclerView)!!
    }

    private fun initMessageList() {
        layoutManager = LinearLayoutManager(context)
        adapter = InboxMessageAdapter()

        adapter.onLoadMore = {
            loadMore()
        }

        swipeRefreshLayout.setOnRefreshListener {
            reload()
            swipeRefreshLayout.isRefreshing = false
        }

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    private fun reload() {
        recyclerView.post {
            adapter.reload(createMockDb())
        }
    }

    private fun loadMore() {
        println("InboxFragment loadMore() ran")
    }

    private fun createMockDb(): MutableList<Message> {
        return mutableListOf(
            ReadMessage(R.drawable.amir, "Amir Khan", "Sup?", "19:42", false, true),
            UnreadMessage(R.drawable.amir, "Amir Khan", "Hey man, just wonderingg if you're coming over tonight", "Wed", false, true),
            IntroductionMessage(R.drawable.amir, "Amir Khan", "Hey man, just wonderingg if you're coming over tonight", "Wed", false, true),
            ReadMessage(R.drawable.amir, "Amir Khan", "Hey man, just wonderingg if you're coming over tonight", "Wed", false, true),
            UnreadMessage(R.drawable.amir, "Amir Khan", "Hey man, just wonderingg if you're coming over tonight", "Wed", false, true),
            IntroductionMessage(R.drawable.amir, "Amir Khan", "Hey man, just wonderingg if you're coming over tonight", "Wed", false, true),
            ReadMessage(R.drawable.amir, "Amir Khan", "Hey man, just wonderingg if you're coming over tonight", "Wed", false, true),
            UnreadMessage(R.drawable.amir, "Amir Khan", "Hey man, just wonderingg if you're coming over tonight", "Wed", false, true),
            IntroductionMessage(R.drawable.amir, "Amir Khan", "Hey man, just wonderingg if you're coming over tonight", "Wed", false, true),
            ReadMessage(R.drawable.amir, "Amir Khan", "Hey man, just wonderingg if you're coming over tonight", "Wed", false, true),
            UnreadMessage(R.drawable.amir, "Amir Khan", "Hey man, just wonderingg if you're coming over tonight", "Wed", false, true),
            IntroductionMessage(R.drawable.amir, "Amir Khan", "Hey man, just wonderingg if you're coming over tonight", "Wed", false, true),
            ReadMessage(R.drawable.amir, "Amir Khan", "Hey man, just wonderingg if you're coming over tonight", "Wed", false, true),
            UnreadMessage(R.drawable.amir, "Amir Khan", "Hey man, just wonderingg if you're coming over tonight", "Wed", false, true),
            IntroductionMessage(R.drawable.amir, "Amir Khan", "Hey man, just wonderingg if you're coming over tonight", "Wed", false, true),
            ReadMessage(R.drawable.amir, "Amir Khan", "Hey man, just wonderingg if you're coming over tonight", "Wed", false, true),
            UnreadMessage(R.drawable.amir, "Amir Khan", "Hey man, just wonderingg if you're coming over tonight", "Wed", false, true),
            IntroductionMessage(R.drawable.amir, "Amir Khan", "Hey man, just wonderingg if you're coming over tonight", "Wed", false, true),
            ReadMessage(R.drawable.amir, "Amir Khan", "Hey man, just wonderingg if you're coming over tonight", "Wed", false, true),
            UnreadMessage(R.drawable.amir, "Amir Khan", "Hey man, just wonderingg if you're coming over tonight", "Wed", false, true),
            IntroductionMessage(R.drawable.amir, "Amir Khan", "Hey man, just wonderingg if you're coming over tonight", "Wed", false, true),
            ReadMessage(R.drawable.amir, "Amir Khan", "Hey man, just wonderingg if you're coming over tonight", "Wed", false, true),
            UnreadMessage(R.drawable.amir, "Amir Khan", "Hey man, just wonderingg if you're coming over tonight", "Wed", false, true),
            IntroductionMessage(R.drawable.amir, "Amir Khan", "Hey man, just wonderingg if you're coming over tonight", "Wed", false, true),
            ReadMessage(R.drawable.amir, "Amir Khan", "Hey man, just wonderingg if you're coming over tonight", "Wed", false, true),
            UnreadMessage(R.drawable.amir, "Amir Khan", "Hey man, just wonderingg if you're coming over tonight", "Wed", false, true),
            IntroductionMessage(R.drawable.amir, "Amir Khan", "Hey man, just wonderingg if you're coming over tonight", "Wed", false, true),
            ReadMessage(R.drawable.amir, "Amir Khan", "Hey man, just wonderingg if you're coming over tonight", "Wed", false, true),
            UnreadMessage(R.drawable.amir, "Amir Khan", "Hey man, just wonderingg if you're coming over tonight", "Wed", false, true),
            IntroductionMessage(R.drawable.amir, "Amir Khan", "Hey man, just wonderingg if you're coming over tonight", "Wed", false, true)

        )

    }

    private fun addGroupCard(name: String, imageResource: Int) {
        val view = layoutInflater.inflate(R.layout.card_inbox_groups, null)

        val nameView = view.findViewById<TextView>(R.id.nameCardGroups)
        val imageView = view.findViewById<ImageView>(R.id.ivCardGroups)

        nameView.text = name
        imageView.setImageResource(imageResource)

        binding.cardContainerGroups.addView(view)
    }
}