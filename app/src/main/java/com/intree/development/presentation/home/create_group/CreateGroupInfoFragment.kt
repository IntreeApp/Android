package com.intree.development.presentation.home.create_group

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.intree.development.databinding.FragmentCreateGroupInfoBinding
import com.intree.development.R

class CreateGroupInfoFragment: Fragment(R.layout.fragment_create_group_info) {

    private lateinit var binding: FragmentCreateGroupInfoBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCreateGroupInfoBinding.bind(view)

        setOnClickLIsteners()
    }

    private fun setOnClickLIsteners() {

        binding.btnBack.setOnClickListener{
            activity?.onBackPressed()
        }

        var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                //binding.btnUploadCover.visibility = View.INVISIBLE
                binding.uploadGroupCoverImageView.setImageURI(data?.data)
            }
        }

        //binding.btnUploadCover.setOnClickListener{
        //    val intent = Intent(Intent.ACTION_PICK)
        //    intent.type = "image/*"
        //    resultLauncher.launch(intent)
        //}

        binding.uploadGroupCoverImageView.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            resultLauncher.launch(intent)
        }

        binding.btnContinue.setOnClickListener{
            val action = CreateGroupInfoFragmentDirections.actionCreateGroupInfoFragmentToCreateGroupFragment()
            findNavController().navigate(action)
        }
    }
}