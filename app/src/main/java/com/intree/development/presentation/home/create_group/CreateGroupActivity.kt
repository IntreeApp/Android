package com.intree.development.presentation.home.create_group

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.intree.development.databinding.ActivityCreateGroupBinding
import com.intree.development.R

class CreateGroupActivity: AppCompatActivity() {

    private lateinit var binding: ActivityCreateGroupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_group)
        binding = ActivityCreateGroupBinding.inflate(layoutInflater)
    }
}