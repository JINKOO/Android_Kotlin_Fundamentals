package com.kjk.trackmysleepquality.sleepquality

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
import com.kjk.trackmysleepquality.databinding.FragmentSleepQualityBinding

class SleepQualityFragment : Fragment() {

    private lateinit var binding: FragmentSleepQualityBinding

    private lateinit var viewModelFactory: SleepQualityViewModelFactory

    private lateinit var viewModel: SleepQualityViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_sleep_quality,
            container,
            false
        )

        // safe args
        val arguments = SleepQualityFragmentArgs.fromBundle(requireArguments())

        // application
        val application = requireNotNull(this.activity).application

        // database
        val dataSource = SleepDatabase.getInstance(application).sleepDatabaseDao

        // viewModelFactory
        viewModelFactory = SleepQualityViewModelFactory(dataSource, arguments.sleepNightKey)

        // viewModel
        viewModel = ViewModelProvider(this, viewModelFactory).get(SleepQualityViewModel::class.java)

        // data binding
        binding.sleepQualityViewModel = viewModel
        binding.lifecycleOwner = this

        // observing
        observe()

        return binding.root
    }

    private fun observe() {
        viewModel.onNavigateToSleepTracker.observe(viewLifecycleOwner, Observer { toMove ->
            if (toMove) {
                navigateToSleepTracker()
                viewModel.onNavigateDone()
            }
        })
    }

    /** navigate To SleepTrackerFragment */
    private fun navigateToSleepTracker() {
        this.findNavController()
            .navigate(SleepQualityFragmentDirections.actionSleepQualityFragmentToSleepTrackerFragment())
    }
}