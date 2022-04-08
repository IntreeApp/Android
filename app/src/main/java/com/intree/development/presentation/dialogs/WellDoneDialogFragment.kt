package com.intree.development.presentation.dialogs

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.intree.development.R
import com.intree.development.databinding.DialogFragmentWellDoneBinding

class WellDoneDialogFragment : DialogFragment() {

    private lateinit var binding: DialogFragmentWellDoneBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!::binding.isInitialized) {
            binding = DialogFragmentWellDoneBinding.inflate(inflater)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        closeDialog(1500)
    }

    private fun closeDialog(delayMillis: Long) {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            dialog?.cancel()
        }, delayMillis)
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
            dialog.setCancelable(false)
        }
    }
}