package com.intree.development.presentation.home.network

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.intree.development.databinding.FragmentNetworkBinding
import com.intree.development.R
import com.intree.development.domain.UserDataEntity


class NetworkFragment : Fragment(R.layout.fragment_network)  {


    private lateinit var binding: FragmentNetworkBinding


    // Mock DB for filling out Recents-scrollview
    val usernamesRecents = arrayOf("Amir Khan", "Art Peterson", "Janko Pascal", "Julian Joyce", "Imansyah XX", "Jessica Hansson", "Jon Buckley")
    val userIconsMapRecents = mapOf("Amir Khan" to R.drawable.amir,"Art Peterson" to R.drawable.art, "Imansyah XX" to R.drawable.imansyah,"Janko Pascal" to R.drawable.janko, "Jessica Hansson" to R.drawable.jessica, "Jon Buckley" to R.drawable.jonathan, "Julian Joyce" to R.drawable.julian)

    // Mock DB for filling out bottom-scrollview
    val usernamesBottom = arrayOf("Cesar Rincon", "Clem Onojeghuo", "Hannah Busing", "Madison Lavern", "Mika laPetri", "Mike Clark", "Seth Doyle")
    val userIconsMapBottom = mapOf("Cesar Rincon" to R.drawable.cesar_rincon, "Clem Onojeghuo" to R.drawable.clem_onojeghuo, "Hannah Busing" to R.drawable.hannah_busing, "Madison Lavern" to R.drawable.madison_lavern, "Mika laPetri" to R.drawable.mika_lapetri, "Mike Clark" to R.drawable.mike_clark, "Seth Doyle" to R.drawable.seth_doyle)
    val connectionsMapBottom = mapOf("Cesar Rincon" to "By invite", "Clem Onojeghuo" to "Copenhagen, Denmark", "Hannah Busing" to "Kyiv, Ukraine", "Madison Lavern" to "Copenhagen, Denmark", "Mika laPetri" to "Madrid, Spain", "Mike Clark" to "Boston, Usa", "Seth Doyle" to "London, England")


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentNetworkBinding.bind(view)

        // Fills out the Recents-list with small cards
        for (username in usernamesRecents) {
            val imageDrawable = userIconsMapRecents.get(username)

            if (imageDrawable != null) {
                addSmallCard(username, imageDrawable)
            }
        }

        // Fills out the bottom contactlist with wide cards
        for (username in usernamesBottom) {
            val imageDrawable = userIconsMapBottom.get(username)
            val connection = connectionsMapBottom.get(username)

            if (imageDrawable != null && connection != null) {
                addWideCard(username, imageDrawable, connection)
            }
        }

        // Adds special wide card at the bottom to compensate for the view overflow
        addWideCardBottomSpacer()


        /**
            Setup of search bar
         */
        var searchview = binding.searchView

        searchview.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
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
                transparentBar.background = ResourcesCompat.getDrawable(resources, R.drawable.transparent_spacer, context?.theme)
                cardContainerVG.addView(transparentBar)

                //Filter List based on search criteria
                var filteredUsernamesList = ArrayList<String>()
                if (newText!= null) {
                    filteredUsernamesList = usernamesBottom.filter { it.contains(newText, ignoreCase = true) } as ArrayList<String>

                }

                //Fills cardContainer with cards based on search criteria using UserDataEntity
                /*
                if (filteredUsernamesList != null) {
                    for (i in 0 until filteredUsernamesList.size) {
                        var user = UserDataEntity()
                        user.fullName = filteredUsernamesList.get(i)
                        val imageResource = userIconsMapBottom.get(name)
                        val connection = connectionsMapBottom.get(name)

                        //addWideCard(name, imageResource, connection)
                    }
                }
                */

                //Fill cardContainer with cards based on search criteria using mock DB
                if (filteredUsernamesList != null) {
                    for (name in filteredUsernamesList) {


                        val imageResource = userIconsMapBottom.get(name)
                        val connection = connectionsMapBottom.get(name)

                        addWideCard(name, imageResource!!, connection!!)

                    }
                }
                return false
            }

        })

        // Transition to alternative fragment when qrIcon is pushed
        /*
        _binding.qrIcon.setOnClickListener{
            val fragment = NetworkAltFragment()
            val manager = parentFragmentManager
            val transaction = manager.beginTransaction()
            transaction.replace(
                R.id.main_host_fragment, fragment
            )
            transaction.commit()

        }
        */
    }

    private fun addSmallCard(name: String, imageResource: Int) {
        val view = layoutInflater.inflate(R.layout.network_card_small, null)

        val nameView = view.findViewById<TextView>(R.id.tv_name_smallcard)
        val imageView = view.findViewById<ImageView>(R.id.iv_smallcard)

        nameView.text = name
        imageView.setImageResource(imageResource)

        binding.cardContainerRecents.addView(view)
    }


    private fun addWideCard(name: String, imageResource: Int, connection: String) {
        val view = layoutInflater.inflate(R.layout.network_card_wide, null)

        val nameView = view.findViewById<TextView>(R.id.tvNameWideCard)
        val imageView = view.findViewById<ImageView>(R.id.ivWidecard)
        val connectionView = view.findViewById<TextView>(R.id.tvConnectedContentWideCard)

        nameView.text = name
        imageView.setImageResource(imageResource)
        connectionView.text = connection

        binding.cardContainerBottom.addView(view)
    }

    private fun addWideCardBottomSpacer() {
        val view = layoutInflater.inflate(R.layout.network_card_wide_bottom_spacer, null)

        val nameView = view.findViewById<TextView>(R.id.tvNameWideCard)
        val imageView = view.findViewById<ImageView>(R.id.ivWidecard)

        nameView.text = "Intree"
        imageView.setImageResource(R.drawable.intree_logo)


        binding.cardContainerBottom.addView(view)
    }

    // For possible future use
    /*
    private fun addWideCardWithUserDataEntity(user: UserDataEntity) {
        val view = layoutInflater.inflate(R.layout.network_card_wide, null)

        val nameView = view.findViewById<TextView>(R.id.tvNameWideCard)
        val imageView = view.findViewById<ImageView>(R.id.ivWidecard)
        val connectionView = view.findViewById<TextView>(R.id.tvConnectedContentWideCard)

        nameView.text = user.fullName
        imageView.setImageBitmap(user.photo.photo)
        connectionView.text = user.metIn.toString()

        _binding.cardContainerBottom.addView(view)

    }
    */
}