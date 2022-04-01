package com.intree.development.presentation.home.introduce

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.intree.development.databinding.FragmentIntroductionsBinding
import com.intree.development.R
import com.ncorti.slidetoact.SlideToActView



class IntroductionsFragment : Fragment() {

    private lateinit var _binding: FragmentIntroductionsBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_introductions, container, false)


        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentIntroductionsBinding.bind(view)

        val confirmSlider = _binding.confirmSlider
        confirmSlider.onSlideCompleteListener {}

        // Move editTextTextMultiLine up, on focus
        _binding.editTextTextMultiLine.setOnFocusChangeListener { view, hasFocus ->

            if (hasFocus) {
                _binding.editTextTextMultiLine.isCursorVisible = true

                _binding.editTextContainer.startAnimation(
                    AnimationUtils.loadAnimation(
                        view.context, R.anim.move_up
                    )
                )
            }
            else {
                _binding.editTextTextMultiLine.isCursorVisible = false

                _binding.editTextContainer.startAnimation(
                    AnimationUtils.loadAnimation(
                        view.context, R.anim.move_down
                    )
                )
            }
        }
    }

}

private fun SlideToActView.onSlideCompleteListener(value: () -> Unit) {
    println("you have slided")

}
