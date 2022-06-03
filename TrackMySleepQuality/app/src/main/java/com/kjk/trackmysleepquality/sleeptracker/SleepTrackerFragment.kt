package com.kjk.trackmysleepquality.sleeptracker

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.kjk.trackmysleepquality.R
import com.kjk.trackmysleepquality.database.SleepDatabase
import com.kjk.trackmysleepquality.database.SleepNight
import com.kjk.trackmysleepquality.databinding.FragmentSleepTrackerBinding
import com.kjk.trackmysleepquality.sleepquality.SleepQualityFragment

class SleepTrackerFragment : Fragment() {

    private lateinit var binding: FragmentSleepTrackerBinding

    private lateinit var viewModel: SleepTrackerViewModel

    private lateinit var viewModelFactory: SleepTrackerViewModelFactory

    //
    private val sleepNightAdapter by lazy {
        SleepNightAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_sleep_tracker,
            container,
            false
        )

        //
        val application = requireNotNull(this.activity).application
        val dataSource = SleepDatabase.getInstance(application).sleepDatabaseDao

        viewModelFactory = SleepTrackerViewModelFactory(dataSource, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(SleepTrackerViewModel::class.java)

        binding.lifecycleOwner = this
        binding.sleepTrackerViewModel = viewModel

        // recyclerView
        binding.recyclerView.apply {
            //layoutManager = LinearLayoutManager(activity)
            layoutManager = GridLayoutManager(
                activity,
                3,
                GridLayoutManager.VERTICAL,
                false
            )
            adapter = sleepNightAdapter
        }

        // observe
        observe()

        return binding.root
    }

    private fun observe() {
        viewModel.navigateToSleepQuality.observe(viewLifecycleOwner, Observer { night ->
            night?.let {
                moveToSleepQuality(night)
                viewModel.onNavigateDone()
            }
        })

        viewModel.onSnackBarEvent.observe(viewLifecycleOwner, Observer { toShowMessage ->
            if (toShowMessage) {
                showSnackBarMessage()
                viewModel.onSnackBarEventDone()
            }
        })

        viewModel.allNights.observe(viewLifecycleOwner, Observer { nights ->
            Log.d(TAG, "observe allNights: ")
            nights?.let {
                //sleepNightAdapter.data = nights
                sleepNightAdapter.submitList(nights)
            }
        })
    }

    private fun moveToSleepQuality(night: SleepNight) {
        this.findNavController()
            .navigate(SleepTrackerFragmentDirections.actionSleepTrackerFragmentToSleepQualityFragment(night.nightId))
    }

    private fun showSnackBarMessage() {
        Snackbar.make(
            requireActivity().findViewById(android.R.id.content),
            getString(R.string.cleared_message),
            Snackbar.LENGTH_SHORT
        ).show()
    }

    companion object {
        private const val TAG = "SleepTrackerFragment"
    }
}