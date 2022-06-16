package com.kjk.marsrealestate.overview

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.kjk.marsrealestate.R
import com.kjk.marsrealestate.databinding.FragmentOverviewBinding
import com.kjk.marsrealestate.domain.MarsProperty

class OverviewFragment : Fragment() {

    private lateinit var binding: FragmentOverviewBinding

    private val viewModel: OverviewViewModel by lazy {
        val activity = requireNotNull(activity).application
        ViewModelProvider(this, OverviewViewModelFactory(activity)).get(OverviewViewModel::class.java)
    }

    private val marsAdapter: MarsRealEstateAdapter by lazy {
        MarsRealEstateAdapter(OnClickListener {
            viewModel.moveToDetail((it))
        })
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
        binding.lifecycleOwner = viewLifecycleOwner

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
        setHasOptionsMenu(true)
    }

    private fun observe() {
        viewModel.onNavigateToDetail.observe(viewLifecycleOwner, Observer { marsProperty ->
            marsProperty?.let {
                moveToDetailFragment(marsProperty)
                viewModel.doneNavigateToDetail()
            }
        })
    }

    private fun moveToDetailFragment(marsProperty: MarsProperty) {
        this.findNavController()
            .navigate(OverviewFragmentDirections.actionOverViewFragmentToDetailFragment(marsProperty))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.item_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.updateFilter(
            when (item.itemId) {
                R.id.show_buy_menu -> MarsApiFilter.BUY
                R.id.show_rent_menu -> MarsApiFilter.RENT
                else -> MarsApiFilter.SHOW_ALL
            }
        )
        return true
    }

    companion object {
        private const val MAX_SPAN_COUNT = 3
    }
}