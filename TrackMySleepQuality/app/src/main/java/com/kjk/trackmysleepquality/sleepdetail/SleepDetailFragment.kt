package com.kjk.trackmysleepquality.sleepdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.kjk.trackmysleepquality.R
import com.kjk.trackmysleepquality.database.SleepDatabase
import com.kjk.trackmysleepquality.databinding.FragmentSleepDetailBinding

class SleepDetailFragment : Fragment() {

    private lateinit var binding: FragmentSleepDetailBinding

    private lateinit var viewModelFactory: SleepDetailViewModelFactory
    private lateinit var viewModel: SleepDetailViewModel

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

        // argument
        val arguments = SleepDetailFragmentArgs.fromBundle(requireArguments())

        // database
        val application = requireNotNull(this.activity).application
        val database =  SleepDatabase.getInstance(application).sleepDatabaseDao

        // viewModelFactory 그리고 viewModel
        viewModelFactory = SleepDetailViewModelFactory(database, arguments.sleepNightKey)
        viewModel = ViewModelProvider(this, viewModelFactory).get(SleepDetailViewModel::class.java)

        // data binding
        binding.sleepDetailViewModel = viewModel
        binding.lifecycleOwner = this

        // observe
        observe()

        return binding.root
    }

    private fun observe() {
        viewModel.onNavigateToSleepTracker.observe(viewLifecycleOwner, Observer { toMove ->
            toMove?.let {
                // moveToSleepTrackerFragment
                moveToSleepTrackerFragment()
                viewModel.onNavigateToSleepTrackerDone()
            }
        })
    }

    private fun moveToSleepTrackerFragment() {
        this.findNavController()
            .navigate(SleepDetailFragmentDirections.actionSleepDetailFragmentToSleepTrackerFragment())
    }
}