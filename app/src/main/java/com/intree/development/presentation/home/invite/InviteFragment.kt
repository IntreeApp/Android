package com.intree.development.presentation.home.invite

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.intree.development.R
import com.intree.development.data.model.ReferContactData
import com.intree.development.databinding.FragmentInviteBinding
import com.intree.development.presentation.adapter.ContactsAdapter
import com.intree.development.presentation.home.profile.view_pager.RoomVPAdapter
import com.intree.development.presentation.home.profile.vm.ProfileViewModel
import com.intree.development.presentation.interfaces.IContacts
import com.intree.development.presentation.scanCode.ScanCodeActivity
import com.journeyapps.barcodescanner.CaptureActivity
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions


class InviteFragment : Fragment(R.layout.fragment_invite), IContacts {

    private lateinit var binding: FragmentInviteBinding
    private val profileVm by viewModels<ProfileViewModel>()
    private val inviteVm by viewModels<InviteViewModel>()
    private val contactsAdapter = ContactsAdapter(this)

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
        binding.progressBar.visibility = View.VISIBLE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermissions(requireActivity(), permissionArray, requestContactCode)
        if (allPermissionsGranted()) {
            inviteVm.getContacts(requireContext())
            initRooms()
            initSearching()
            initAdapter()
            initOnClickListeners()
        } else {
            view.findNavController().navigateUp()
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
                initRooms()
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

    private fun initRooms() {
        profileVm.getOwnRooms()
        val viewpager: ViewPager2 = binding.vpRooms
        viewpager.adapter = RoomVPAdapter(childFragmentManager, lifecycle)

        viewpager.clipToPadding = false
        viewpager.offscreenPageLimit = 2

        binding.vpRoomsIndicator.setViewPager(viewpager)
        profileVm.ownRooms.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                it.forEach { roomForPreview ->
                    (viewpager.adapter as RoomVPAdapter).addExistingRoomItem(roomEntityForPreview = roomForPreview)
                }
            }
            (viewpager.adapter as RoomVPAdapter).addCreateRoomItem(it.isEmpty())

            binding.vpRoomsIndicator.setViewPager(viewpager)
        }
    }

    private fun initSearching() {
        binding.etSearching.hint = getString(R.string.search_from)
        binding.etSearching.setOnFocusChangeListener { _, b ->
            if (b) {
                binding.etSearching.hint = ""
            } else {
                binding.etSearching.hint = getString(R.string.search_from)
            }
        }
        binding.etSearching.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                binding.etSearching.clearFocus()
                val imm: InputMethodManager =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.etSearching.windowToken, 0)
                return@OnKeyListener true
            }
            false
        })

        binding.etSearching.addTextChangedListener(object : TextWatcher {
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
        }
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