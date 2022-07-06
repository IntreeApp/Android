package com.intree.development.presentation.home.invite

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidmads.library.qrgenearator.QRGContents
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.card.MaterialCardView
import com.intree.development.R
import com.intree.development.data.model.ReferContactData
import com.intree.development.databinding.FragmentInviteBinding
import com.intree.development.presentation.adapter.ContactsAdapter
import com.intree.development.presentation.home.profile.vm.ProfileViewModel
import com.intree.development.presentation.interfaces.IContacts
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import androidmads.library.qrgenearator.QRGEncoder

/**
 *  Press the i-icon to the right of "Choose Aspects" to show sharing options
 *  */


class InviteFragment : Fragment(R.layout.fragment_invite), IContacts {

    private lateinit var binding: FragmentInviteBinding
    private val profileVm by viewModels<ProfileViewModel>()
    private val inviteVm by viewModels<InviteViewModel>()
    private val contactsAdapter = ContactsAdapter(this)
    lateinit var qrEncoder: QRGEncoder

    private val TAG = "InviteFragment"

    companion object {
        private const val requestContactCode = 1
        private val permissionArray = arrayOf(
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!::binding.isInitialized) {
            binding = FragmentInviteBinding.inflate(inflater)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermissions(requireActivity(), permissionArray, requestContactCode)

        initOnFocusChangeListener()


        if (allPermissionsGranted()) {
            inviteVm.getContacts(requireContext())
            initAspects()
            initSearching()
            initOnClickListeners()
            initRadioButton()

        } else {
            view.findNavController().navigateUp()
        }
    }

    private fun initOnFocusChangeListener() {

        //Doesn't work. I have no idea why. It works when i set the listener on etPhoneNumber
        binding.etSearchingContacts.setOnFocusChangeListener  { _, b ->
            Log.d(TAG, "setOnFocusChangeListener ran")
            if (b) {
                Log.d(TAG, "phone editText is in focus")

                binding.sharingOptionsContainer.startAnimation(
                    AnimationUtils.loadAnimation(
                    view?.context, R.anim.invite_move_up)
                )
            } else {
                Log.d(TAG, "phone editText is NOT in focus anymore")
            }
        }
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

    private fun initRadioButton() {
        val typefaceBold = resources.getFont(R.font.montserrat_bold)
        val typefaceNormal = resources.getFont(R.font.montserrat)

        binding.radioGroup.setOnCheckedChangeListener { radioGroup, checkedId ->

            if (binding.radioQr.isChecked) {
                binding.radioQr.typeface = typefaceBold
                binding.radioDm.typeface = typefaceNormal
                binding.radioContacts.typeface = typefaceNormal
                binding.radioLink.typeface = typefaceNormal
            }
            if (binding.radioDm.isChecked) {
                binding.radioDm.typeface = typefaceBold
                binding.radioQr.typeface = typefaceNormal
                binding.radioContacts.typeface = typefaceNormal
                binding.radioLink.typeface = typefaceNormal
            }
            if (binding.radioContacts.isChecked) {
                binding.radioContacts.typeface = typefaceBold
                binding.radioQr.typeface = typefaceNormal
                binding.radioDm.typeface = typefaceNormal
                binding.radioLink.typeface = typefaceNormal
            }
            if (binding.radioLink.isChecked) {
                binding.radioLink.typeface = typefaceBold
                binding.radioQr.typeface = typefaceNormal
                binding.radioDm.typeface = typefaceNormal
                binding.radioContacts.typeface = typefaceNormal
            }

            when (checkedId) {
                R.id.radioQr -> {
                    binding.layoutQR.visibility = View.VISIBLE
                    binding.layoutLink.visibility = View.GONE
                    binding.layoutDM.visibility = View.GONE
                    binding.layoutContacts.visibility = View.GONE
                    initQrCode()
                }
                R.id.radioDm -> {
                    binding.layoutDM.visibility = View.VISIBLE
                    binding.layoutQR.visibility = View.GONE
                    binding.layoutLink.visibility = View.GONE
                    binding.layoutContacts.visibility = View.GONE
                }
                R.id.radioContacts -> {
                    binding.layoutContacts.visibility = View.VISIBLE
                    binding.layoutQR.visibility = View.GONE
                    binding.layoutLink.visibility = View.GONE
                    binding.layoutDM.visibility = View.GONE
                    initAdapter()
                }
                R.id.radioLink -> {
                    binding.layoutLink.visibility = View.VISIBLE
                    binding.layoutQR.visibility = View.GONE
                    binding.layoutDM.visibility = View.GONE
                    binding.layoutContacts.visibility = View.GONE
                }
            }
        }
    }

    private fun initQrCode() {
        val link = "https://www.erdetfredag.dk/" // Random site for testing purpose
        qrEncoder = QRGEncoder(link, null, QRGContents.Type.TEXT, 200)

        try {

            val bitmap = qrEncoder.encodeAsBitmap()
            binding.ivQrView.setImageBitmap(bitmap)

        } catch (e: Exception) {
            // set bitmap for error
            binding.ivQrView.setImageResource(R.drawable.ic_delete_or_error)

            e.printStackTrace()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == requestContactCode) {
            if (allPermissionsGranted()) {
                inviteVm.getContacts(requireContext())
                initAspects()
                initSearching()
                initAdapter()
            } else {
                view?.findNavController()?.navigateUp()
            }
        }
    }


    private val barcodeLauncher = registerForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult ->
        if (result.contents == null) {
            // Toast.makeText(requireContext(), "Cancelled", Toast.LENGTH_LONG).show()
        } else {
            result.contents

            Toast.makeText(
                requireContext(),
                "Scanned: " + result.contents,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun allPermissionsGranted() = permissionArray.all {
        ContextCompat.checkSelfPermission(
            requireContext(),
            it
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun initAspects() {

        // Add New Group button as groups-card
        addCheckableGroupCard("BJJ", R.drawable.bjj)
        addCheckableGroupCard("Crypto", R.drawable.crypto)
        addCheckableGroupCard("Fashion", R.drawable.fashion)

        /*
        profileVm.getOwnRooms()
        val viewpager: ViewPager2 = binding.vpRooms
        viewpager.adapter = AspectVPAdapter(childFragmentManager, lifecycle)

        viewpager.clipToPadding = false
        viewpager.offscreenPageLimit = 2

        binding.vpRoomsIndicator.setViewPager(viewpager)
        profileVm.ownAspects.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                it.forEach { roomForPreview ->
                    (viewpager.adapter as AspectVPAdapter).addExistingRoomItem(aspectEntityForPreview = roomForPreview)
                }
            }
            (viewpager.adapter as AspectVPAdapter).addCreateRoomItem(it.isEmpty())

            binding.vpRoomsIndicator.setViewPager(viewpager)
        }*/
    }

    private fun initSearching() {
        binding.etSearchingContacts.hint = getString(R.string.search_contacts)
        binding.etSearchingContacts.setOnFocusChangeListener { _, b ->
            if (b) {
                binding.etSearchingContacts.hint = ""
            } else {
                binding.etSearchingContacts.hint = getString(R.string.search_contacts)
            }
        }
        binding.etSearchingContacts.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                binding.etSearchingContacts.clearFocus()
                val imm: InputMethodManager =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.etSearchingContacts.windowToken, 0)
                return@OnKeyListener true
            }
            false
        })

        binding.etSearchingContacts.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                inviteVm.contactsLive.observe(viewLifecycleOwner) { contacts ->
                    if (!contacts.isNullOrEmpty()) {
                        val searchingList = ArrayList<ReferContactData>()
                        searchingList.clear()
                        val regex = "[0-9]+".toRegex()
                        for (item in contacts) {
                            if (s.toString().matches(regex)) {
                                if (item.phone.contains(s.toString(), true)) {
                                    searchingList.add(item)
                                }
                            } else {
                                if (item.name.contains(s.toString(), true)) {
                                    searchingList.add(item)
                                }
                            }
                        }
                        contactsAdapter.setData(searchingList)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            resources.getString(R.string.you_did_not_have_contacts),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        })
    }

    private fun initOnClickListeners() {
        binding.btnBack.setOnClickListener {
            activity?.onBackPressed()
        }

        //for testing purposes
        binding.infoIconChooseAspects.setOnClickListener {
            binding.sharingOptionsContainer.visibility = View.VISIBLE
            binding.layoutLink.visibility = View.VISIBLE
        }
/*
        binding.btnScanning.setOnClickListener {
            val options = ScanOptions()
            options.captureActivity = ScanCodeActivity::class.java
            options.setOrientationLocked(true)
            options.setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES)
            options.setPrompt("Scan a QR code")
            options.setCameraId(0)
            options.setBeepEnabled(true)
            options.setBarcodeImageEnabled(true)
            barcodeLauncher.launch(options)
        }*/
    }



    private fun initAdapter() {
        inviteVm.contactsLive.observe(viewLifecycleOwner) { contacts ->
            if (!contacts.isNullOrEmpty()) {
                binding.rvContacts.adapter = contactsAdapter
                binding.rvContacts.setHasFixedSize(true)
                val layoutManager = LinearLayoutManager(requireContext())
                binding.rvContacts.layoutManager = layoutManager
                contactsAdapter.setData(contacts)
            } else {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.you_did_not_have_contacts),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onItemClicked(model: ReferContactData) {

    }

    override fun onItemReady() {
        binding.progressBar.visibility = View.GONE
    }

}