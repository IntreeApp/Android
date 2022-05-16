package com.intree.development.presentation.home.create_group

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavDeepLinkBuilder
import androidx.navigation.fragment.findNavController
import com.intree.development.presentation.home.MainActivity
import com.intree.development.R

class CreateGroupDoneFragment: Fragment(R.layout.fragment_create_group_done) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.putExtra("groupCreated", true)
            startActivity(intent)

        }, 3000)
    }
}