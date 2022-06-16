package com.kjk.devbytes.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kjk.devbytes.R
import com.kjk.devbytes.databinding.FragmentDevByteBinding
import com.kjk.devbytes.viewModel.DevByteViewModel
import com.kjk.devbytes.viewModel.DevByteViewModelFactory
import com.kjk.devbytes.viewModel.VideoApiStatus

class DevByteFragment : Fragment() {

    /**
     * Data Binding object
     */
    private lateinit var binding: FragmentDevByteBinding

    /**
     *  ViewModelFactory,
     *  ViewModel
     */
    private lateinit var viewModelFactory: DevByteViewModelFactory
    private lateinit var viewModel: DevByteViewModel

    /**
     * adapter
     */
    private val videoAdapter: VideoAdapter by lazy {
        VideoAdapter(VideoClickListener { video ->
            // move to youtube, if youtube App 이 존재하는 경우
            var intent = Intent(Intent.ACTION_VIEW, video.launchUri)
            try {
                Log.d(TAG, "callback: ${video.launchUri}")
                startActivity(intent)
            } catch (e : ActivityNotFoundException) {
                Log.d(TAG, "callback: ${e.message}")
                // 만약 해당 intent를 실행할 수 있는 Activity가 없는 경우, 즉 youTube App이 존재하지 않는 경우,
                intent = Intent(Intent.ACTION_VIEW, Uri.parse(video.url))
                startActivity(intent)
            }
        })
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

    /**
     * ViewModel의 LiveData(observable)를
     * Observing
     */
    private fun observe() {
        viewModel.response.observe(viewLifecycleOwner) {
            Log.d(TAG, "observe: ${it}")
        }

        viewModel.videos.observe(viewLifecycleOwner) { videos ->
            videos?.let {
                videoAdapter.submitList(it)
            }
        }

        viewModel.apiStatus.observe(viewLifecycleOwner, Observer { status ->
            status?.let {
                if (status == VideoApiStatus.ERROR) {
                    showNetworkErrorMessage()
                }
            }
        })
    }


    /**
     * layout 초기화 하는 부분
     */
    private fun initLayout() {
        binding.recyclerview.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = videoAdapter
        }
    }


    /**
     * NetWork오류 메세지 출력 toast
     */
    private fun showNetworkErrorMessage() {
        Toast.makeText(
            context,
            getString(R.string.network_error),
            Toast.LENGTH_SHORT
        ).show()
    }

    companion object {
        private const val TAG = "DevByteFragment"
    }
}