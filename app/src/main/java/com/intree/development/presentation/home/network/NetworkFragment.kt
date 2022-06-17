package com.intree.development.presentation.home.network

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.intree.development.databinding.FragmentNetworkBinding
import com.intree.development.R
import com.intree.development.data.uiModels.NetworkRecentsUIModel
import com.intree.development.data.uiModels.NetworkWideCardUIModel
import com.intree.development.databinding.ProfilePreviewModeFragmentBinding
import com.intree.development.presentation.adapter.NetworkHorizontalAdapter
import com.intree.development.presentation.adapter.NetworkVerticalAdapter


class NetworkFragment : Fragment(R.layout.fragment_network) {

    private lateinit var binding: FragmentNetworkBinding
    private val horizontalAdapter = NetworkHorizontalAdapter()
    private val verticalAdapter = NetworkVerticalAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!::binding.isInitialized) {
            binding = FragmentNetworkBinding.inflate(inflater)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initContent()
    }



    private fun initContent() {

        initOnClickListeners()
        initHorizontalAdapter()
        //initVerticalAdapter()
    }

    private fun initOnClickListeners() {

        binding.inviteBtn.setOnClickListener {
            findNavController().navigate(R.id.fragmentInvite)
        }

        binding.qrIcon.setOnClickListener{
            // Todo:

        }
    }
/*
    private fun initVerticalAdapter() {
        binding.verticalRecyclerView.adapter = verticalAdapter
        binding.verticalRecyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.verticalRecyclerView.layoutManager = layoutManager

        verticalAdapter.setData(verticalMockData())
    }
    */

    private fun initHorizontalAdapter() {
        binding.horizontalRecyclerView.adapter = horizontalAdapter
        binding.horizontalRecyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        binding.horizontalRecyclerView.layoutManager = layoutManager

        horizontalAdapter.setData(horizontalMockData())
    }

    private fun horizontalMockData(): ArrayList<NetworkRecentsUIModel> {
        return mutableListOf(
            NetworkRecentsUIModel(
                "Paul Hammer",
                R.drawable.amir
            ),
            NetworkRecentsUIModel(
                "Paul Hammer",
                R.drawable.art
            ),
            NetworkRecentsUIModel(
                "Paul Hammer",
                R.drawable.art
            ),
            NetworkRecentsUIModel(
                "Paul Hammer",
                R.drawable.art
            ),
            NetworkRecentsUIModel(
                "Paul Hammer",
                R.drawable.art
            ),
            NetworkRecentsUIModel(
                "Paul Hammer",
                R.drawable.art
            ),
            NetworkRecentsUIModel(
                "Paul Hammer",
                R.drawable.art
            )
        ) as ArrayList<NetworkRecentsUIModel>
    }

    private fun verticalMockData(): ArrayList<NetworkWideCardUIModel> {
        return mutableListOf(
            NetworkWideCardUIModel(
                "Klaus Hessler",
                R.drawable.amir,
                "Frankfurt"
            ),
            NetworkWideCardUIModel(
                "Klaus Hessler",
                R.drawable.amir,
                "Frankfurt"
            ),
            NetworkWideCardUIModel(
                "Klaus Hessler",
                R.drawable.amir,
                "Frankfurt"
            ),
            NetworkWideCardUIModel(
                "Klaus Hessler",
                R.drawable.amir,
                "Frankfurt"
            ),
            NetworkWideCardUIModel(
                "Klaus Hessler",
                R.drawable.amir,
                "Frankfurt"
            ),
            NetworkWideCardUIModel(
                "Klaus Hessler",
                R.drawable.amir,
                "Frankfurt"
            ),
            NetworkWideCardUIModel(
                "Klaus Hessler",
                R.drawable.amir,
                "Frankfurt"
            ),
            NetworkWideCardUIModel(
                "Klaus Hessler",
                R.drawable.amir,
                "Frankfurt"
            )
        ) as ArrayList<NetworkWideCardUIModel>
    }

    /*

        /**
        Setup of search bar
         */

        var searchview = binding.searchView

        searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(newText: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Cast cardContainer as ViewGroup to access removeAllViews()
                val cardContainerVG = binding.cardContainerBottom as ViewGroup

                // Clear the content of bottom list
                cardContainerVG.removeAllViews()

                // Add transparent bar
                val transparentBar = TextView(requireContext())

                transparentBar.width = ViewGroup.LayoutParams.MATCH_PARENT
                // Scale pixels to dp
                val scale = context!!.resources.displayMetrics.density
                val dp = 30
                val pixels = (dp * scale + 0.5f)
                transparentBar.height = pixels.toInt()
                transparentBar.background = ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.transparent_spacer,
                    context?.theme
                )
                cardContainerVG.addView(transparentBar)

                //Filter List based on search criteria
                var filteredUsernamesList = ArrayList<String>()
                if (newText != null) {
                    filteredUsernamesList = usernamesBottom.filter {
                        it.contains(
                            newText,
                            ignoreCase = true
                        )
                    } as ArrayList<String>

                }

                //Fills cardContainer with cards based on search criteria using UserDataEntity

                if (filteredUsernamesList != null) {
                    for (i in 0 until filteredUsernamesList.size) {
                        var user = UserDataEntity()
                        user.fullName = filteredUsernamesList.get(i)
                        val imageResource = userIconsMapBottom.get(name)
                        val connection = connectionsMapBottom.get(name)

                        //addWideCard(name, imageResource, connection)
                    }
                }
                //Fill cardContainer with cards based on search criteria using mock DB
                if (filteredUsernamesList != null) {
                    for (name in filteredUsernamesList) {
                        val imageResource = userIconsMapBottom.get(name)
                        val connection = connectionsMapBottom.get(name)

                        //addWideCard(name, imageResource!!, connection!!)

                    }
                }
                return false
            }
        })
     */
}