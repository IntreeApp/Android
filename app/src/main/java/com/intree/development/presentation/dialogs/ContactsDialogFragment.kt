package com.intree.development.presentation.dialogs

import android.Manifest
import android.app.Dialog
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
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.intree.development.R
import com.intree.development.data.model.ReferContactData
import com.intree.development.databinding.DialogFragmentContactsBinding
import com.intree.development.presentation.adapter.ContactsAdapter
import com.intree.development.presentation.home.invite.InviteFragment
import com.intree.development.presentation.home.invite.InviteViewModel
import com.intree.development.presentation.interfaces.IContacts

class ContactsDialogFragment : DialogFragment(), IContacts {

    private lateinit var binding: DialogFragmentContactsBinding
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
            binding = DialogFragmentContactsBinding.inflate(inflater)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == requestContactCode) {
            if (allPermissionsGranted()) {
                binding.progressBar.visibility = View.VISIBLE
                inviteVm.getContacts(requireContext())
                initAdapter()
            } else {
                dialog?.cancel()
            }
        }
    }

    private fun allPermissionsGranted() = permissionArray.all {
        ContextCompat.checkSelfPermission(
            requireContext(),
            it
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun initAdapter() {
        inviteVm.contactsLive.observe(viewLifecycleOwner) { contacts ->
            if (!contacts.isNullOrEmpty()) {
                binding.rvContacts.adapter = contactsAdapter
                binding.rvContacts.setHasFixedSize(true)
                val layoutManager = LinearLayoutManager(requireContext())
                binding.rvContacts.layoutManager = layoutManager
                contactsAdapter.setData(contacts)
                initSearching()
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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : Dialog(requireContext(), theme) {
            override fun onBackPressed() {
                dialog?.cancel()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)
            dialog.window?.setBackgroundDrawableResource(
                R.color.white
            )
            dialog.setCancelable(true)
        }
    }

    override fun onItemClicked(model: ReferContactData) {

    }

    override fun onItemReady() {
        binding.progressBar.visibility = View.GONE
    }
}