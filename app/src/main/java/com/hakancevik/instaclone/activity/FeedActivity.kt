package com.hakancevik.instaclone.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.hakancevik.instaclone.databinding.ActivityFeedBinding
import com.hakancevik.instaclone.bottomsheet.ActionBottomDialogFragment
import com.hakancevik.instaclone.bottomsheet.ProgressBarBottomDialogFragment

class FeedActivity : AppCompatActivity(), ActionBottomDialogFragment.ItemClickListener, ProgressBarBottomDialogFragment.ItemClickListener {

    private lateinit var binding: ActivityFeedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


    }

    override fun onItemClick(item: String) {

    }

}