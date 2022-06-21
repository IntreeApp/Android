package com.intree.development.presentation.home.introduce

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.intree.development.R
import com.intree.development.databinding.FragmentIntroduceBinding
import com.intree.development.presentation.dialogs.ContactsDialogFragment
import com.intree.development.presentation.dialogs.WellDoneDialogFragment
import com.ncorti.slidetoact.SlideToActView

class IntroduceFragment : Fragment(R.layout.fragment_introduce) {

    private lateinit var binding: FragmentIntroduceBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!::binding.isInitialized) {
            binding = FragmentIntroduceBinding.inflate(inflater)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initContent()
        initOnClickListeners()
    }

    private fun initContent() {
        Glide.with(requireContext())
            .load(R.drawable.ic_introduce_placeholder)
            .override(200, 200)
            .into(binding.avatarLeft)

        Glide.with(requireContext())
            .load(R.drawable.ic_introduce_placeholder)
            .override(200, 200)
            .into(binding.avatarRight)


    }

    private fun initOnClickListeners() {

        binding.apply {
            edtMessage.hint = "Write your introduction message here"
            edtMessage.setOnFocusChangeListener { _, b ->
                if (b) {
                    binding.apply {
                        edtMessage.hint = ""
                        cv.startAnimation(
                        AnimationUtils.loadAnimation(
                            view?.context, R.anim.move_up
                        )
                    )

                    }
                } else {
                    binding.apply{
                        edtMessage.hint = "Write your introduction message here"
                        cv.startAnimation(
                        AnimationUtils.loadAnimation(
                            view?.context, R.anim.move_down
                        )
                    )

                    }
                }
            }

            avatarLeft.setOnClickListener {
                ContactsDialogFragment().show(
                    requireActivity().supportFragmentManager,
                    this.javaClass.simpleName
                )
            }

            avatarRight.setOnClickListener {
                ContactsDialogFragment().show(
                    requireActivity().supportFragmentManager,
                    this.javaClass.simpleName
                )
            }

            /*
            edtMessage.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                    binding.edtMessage.clearFocus()
                    val imm: InputMethodManager =
                        requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(binding.edtMessage.windowToken, 0)
                    return@OnKeyListener true
                }
                false
            })
            */

            btnHistory.setOnClickListener {
                findNavController().navigate(R.id.introduceHistoryFragment)
            }

            sw.onSlideCompleteListener = object : SlideToActView.OnSlideCompleteListener {

                override fun onSlideComplete(view: SlideToActView) {
                    //todo logic
                    WellDoneDialogFragment().show(
                        requireActivity().supportFragmentManager,
                        this.javaClass.simpleName
                    )
                }
            }

        }
    }
}