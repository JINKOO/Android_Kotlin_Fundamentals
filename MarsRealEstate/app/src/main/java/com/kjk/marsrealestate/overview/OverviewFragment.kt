package com.kjk.marsrealestate.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.kjk.marsrealestate.R
import com.kjk.marsrealestate.databinding.FragmentOverviewBinding

class OverviewFragment : Fragment() {

    private lateinit var binding: FragmentOverviewBinding

    private val viewModel: OverviewViewModel by lazy {
        ViewModelProvider(this).get(OverviewViewModel::class.java)
    }

    private val marsAdapter: MarsRealEstateAdapter by lazy {
        MarsRealEstateAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // dataBinding
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_overview,
            container,
            false
        )
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // initLayout
        initLayout()

        // observe
        observe()

        return binding.root
    }

    private fun initLayout() {
        binding.marsRecyclerview.apply {
            layoutManager = GridLayoutManager(activity, MAX_SPAN_COUNT)
            adapter = marsAdapter
        }
    }

    private fun observe() {
        viewModel.properties.observe(viewLifecycleOwner, Observer { properties ->
            properties?.let {
                marsAdapter.updateProperties(properties)
            }
        })
    }

    companion object {
        private const val MAX_SPAN_COUNT = 2
    }
}