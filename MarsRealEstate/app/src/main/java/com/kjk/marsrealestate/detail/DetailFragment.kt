package com.kjk.marsrealestate.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.kjk.marsrealestate.R
import com.kjk.marsrealestate.databinding.FragmentDetailBinding

/**
 * 상세 화면을 보여주는 fragment
 * OverviewFragment로부터 상세 화면에 보여줄 MarsProperty를 가져온다.
 */
class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding

    private lateinit var viewModelFactory: DetailViewModelFactory
    private lateinit var viewModel: DetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_detail,
            container,
            false
        )

        // arguments
        val arguments = DetailFragmentArgs.fromBundle(requireArguments())
        Log.d(TAG, "onCreateView: ${arguments.marsProperty.id}")

        // viewModelFactory
        val application = requireNotNull(activity).application
        viewModelFactory = DetailViewModelFactory(arguments.marsProperty, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(DetailViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    companion object {
        private const val TAG = "DetailFragment"
    }
}