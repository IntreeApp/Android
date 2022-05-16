package com.intree.development.presentation.home.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.intree.development.R
import com.intree.development.data.uiModels.ExploreImages
import com.intree.development.data.uiModels.ExploreUIModel
import com.intree.development.databinding.FragmentExploreBinding
import com.intree.development.presentation.adapter.ExploreAdapter

class ExploreFragment : Fragment(R.layout.fragment_explore), IExplore {

    private lateinit var binding: FragmentExploreBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!::binding.isInitialized) {
            binding = FragmentExploreBinding.inflate(inflater)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
    }

    private fun initAdapter() {
        val adapter = ExploreAdapter(this, requireActivity().supportFragmentManager)
        binding.rvContent.adapter = adapter
        binding.rvContent.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvContent.layoutManager = layoutManager
        adapter.setData(setMockData())
    }

    private fun setMockData(): ArrayList<ExploreUIModel> {
        val list = ArrayList<ExploreUIModel>()
        val images = ArrayList<ExploreImages>()

        images.add(ExploreImages(0, R.drawable.frame_137))
        images.add(ExploreImages(1, R.drawable.frame_137))

        list.add(
            ExploreUIModel(
                0,
                "Michael Andersson",
                R.drawable.julian,
                "Aspect",
                "This work i did from i client is probably the best i’ve done. What do you think?",
                images,
                "5"
            )
        )

        list.add(
            ExploreUIModel(
                0,
                "Michael Andersson",
                R.drawable.julian,
                "Aspect",
                "This work i did from i client is probably the best i’ve done. What do you think?",
                arrayListOf(),
                "15"
            )
        )

        list.add(
            ExploreUIModel(
                0,
                "Michael Andersson",
                R.drawable.julian,
                "Aspect",
                "This work i did from i client is probably the best i’ve done. What do you think?",
                images,
                "5"
            )
        )
        return list
    }
}