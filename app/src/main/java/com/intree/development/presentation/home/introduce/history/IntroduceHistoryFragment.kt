package com.intree.development.presentation.home.introduce.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.intree.development.R
import com.intree.development.data.model.IntroduceHistoryUIModel
import com.intree.development.databinding.FragmentIntroduceHistoryBinding
import com.intree.development.presentation.adapter.IntroduceHistoryAdapter
import com.intree.development.presentation.interfaces.IintroduceHistory

class IntroduceHistoryFragment : Fragment(R.layout.fragment_introduce_history), IintroduceHistory {

    private lateinit var binding: FragmentIntroduceHistoryBinding
    private val adapter = IntroduceHistoryAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!::binding.isInitialized) {
            binding = FragmentIntroduceHistoryBinding.inflate(inflater)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initOnClickListeners()
        initAdapter()
    }

    private fun initOnClickListeners() {
        binding.tvBack.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun initAdapter() {
        binding.rvHistory.adapter = adapter
        binding.rvHistory.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvHistory.layoutManager = layoutManager
        adapter.setData(mockData())
    }

    private fun mockData(): ArrayList<IntroduceHistoryUIModel> {
        val list = ArrayList<IntroduceHistoryUIModel>()
        list.add(
            IntroduceHistoryUIModel(
                1,
                R.drawable.ic_ellipse_175,
                R.drawable.ic_ellipse_85,
                "Mikkel",
                "Rasmus"
            )
        )
        return list
    }

    override fun onItemClicked(model: IntroduceHistoryUIModel) {

    }
}