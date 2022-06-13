package com.kjk.devbytes.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kjk.devbytes.domain.DevByteVideo
import com.kjk.devbytes.network.VideoApi
import com.kjk.devbytes.network.asDomainModel
import kotlinx.coroutines.launch
import java.lang.Exception

class DevByteViewModel(
    application: Application
) : AndroidViewModel(application) {

    /** */
    private val _videos = MutableLiveData<List<DevByteVideo>>()
    val videos: LiveData<List<DevByteVideo>>
        get() = _videos

    /** 임시 응답*/
    private val _response = MutableLiveData<String>()
    val response: LiveData<String>
        get() = _response

    init {
        refreshDataFromNetWork()
    }

    /** networking 작업 */
    private fun refreshDataFromNetWork() {
        viewModelScope.launch {
            try {
                _videos.value = VideoApi.retrofitService.getVideos().asDomainModel()
                _response.value = "SUCCESS!!"
            } catch (e: Exception) {
                _response.value = "Failure:: ${e.message}"
            }
        }
    }

    companion object {
        private const val TAG = "DevByteViewModel"
    }
}