package com.kjk.trackmysleepquality.sleepdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.kjk.trackmysleepquality.R
import com.kjk.trackmysleepquality.databinding.FragmentSleepDetailBinding

class SleepDetailFragment : Fragment() {

    private lateinit var binding: FragmentSleepDetailBinding

    /** */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // data binding
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_sleep_detail,
            container,
            false
        )

        return binding.root
    }
}