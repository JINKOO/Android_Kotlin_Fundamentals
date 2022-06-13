package com.kjk.devbytes.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kjk.devbytes.R
import com.kjk.devbytes.databinding.FragmentDevByteBinding
import com.kjk.devbytes.viewModel.DevByteViewModel
import com.kjk.devbytes.viewModel.DevByteViewModelFactory

class DevByteFragment : Fragment() {

    private lateinit var binding: FragmentDevByteBinding

    private lateinit var viewModelFactory: DevByteViewModelFactory
    private lateinit var viewModel: DevByteViewModel

    // adapter
    private val videoAdapter: VideoAdapter by lazy {
        VideoAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // binding
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_dev_byte,
            container,
            false
        )

        val application = requireNotNull(activity).application
        viewModelFactory = DevByteViewModelFactory(application)
        viewModel = ViewModelProvider(this).get(DevByteViewModel::class.java)

        initLayout()

        observe()

        return binding.root
    }

    private fun observe() {
        viewModel.response.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "observe: ${it}")
        })

        viewModel.videos.observe(viewLifecycleOwner, Observer { videos ->
            videos?.let {
                videoAdapter.submitList(it)
            }
        })
    }

    /** layout 초기화 하는 부분 */
    private fun initLayout() {
        binding.recyclerview.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = videoAdapter
        }
    }

    companion object {
        private const val TAG = "DevByteFragment"
    }
}