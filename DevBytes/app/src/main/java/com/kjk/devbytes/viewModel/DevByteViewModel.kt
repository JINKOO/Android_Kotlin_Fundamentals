package com.kjk.devbytes.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kjk.devbytes.database.VideoDatabase
import com.kjk.devbytes.domain.DevByteVideo
import com.kjk.devbytes.network.NetworkVideoContainer
import com.kjk.devbytes.network.VideoApi
import com.kjk.devbytes.network.asDomainModel
import com.kjk.devbytes.repository.VideoRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class DevByteViewModel(
    application: Application
) : AndroidViewModel(application) {

    /**
     * Repository object정의
     * 이 때, database를 생성자 parameter로 넘겨준다.
     */
    private val videoRepository =
        VideoRepository(VideoDatabase.getInstance(application))


    /*
    /**
     * Network로 부터 direct로 fetch해, domain object로 변환한 videos data
     */
    private val _videos = MutableLiveData<List<DevByteVideo>>()
    val videos: LiveData<List<DevByteVideo>>
        get() = _videos
     */

    /**
     * repository로 부터 fetch한 videos.
     * Network로 부터 direct하게 가져오지 않는다.
     */
    val videos: LiveData<List<DevByteVideo>> =
        videoRepository.videos


    /**
     * 로그 메세지에 임시 network의 Success, Failure를 보여주기 위한
     * LiveData.
     */
    private val _response = MutableLiveData<String>()
    val response: LiveData<String>
        get() = _response


    /**
     * API call 상태
     * enum class Type의 LiveData
     * progress bar를 보여주기 위함
     */
    private val _apiStatus = MutableLiveData<VideoApiStatus>()
    val apiStatus: LiveData<VideoApiStatus>
        get() = _apiStatus


    /**
     * Network오류에 따른
     * Toast message를 출력하기 위한 event Trigger LiveData
     * api status로 따른 처리를 하기 때문에, 사용하지 않음
     */
//    private val _showNetworkErrorMessage = MutableLiveData<Boolean>()
//    val showNetworkErrorMessage: LiveData<Boolean>
//        get() = _showNetworkErrorMessage


    init {
        //refreshDataFromNetWork()
        refreshDataFromRepository()
    }

    /**
     * 기존의 network으로부터에서만 data를 fetch하는 코드.
     * offline cache를 구현하지 않고, direct로 network로 부터 videos data를 fetch한다.
     */
//    private fun refreshDataFromNetWork() {
//        viewModelScope.launch {
//            _apiStatus.value = VideoApiStatus.LOADING
//            try {
//                _videos.value = VideoApi.retrofitService.getVideos().asDomainModel()
//                _response.value = "SUCCESS:: ${_videos.value!!.size}!!"
//                _apiStatus.value = VideoApiStatus.DONE
//                _showNetworkErrorMessage.value = false
//            } catch (e: Exception) {
//                _response.value = "Failure:: ${e.message}"
//                _apiStatus.value = VideoApiStatus.ERROR
//                _showNetworkErrorMessage.value = true
//            }
//        }
//    }

    /**
     * refreshDataFromRepository
     * 이 viewModel이 repository에게 data를 요청했다.
     * Repository patteren에서 Repository integration을 통한 database refresh 전략이 필요한데, 여기서는
     * 'Repository에 data를 요청한 놈이, 최신의 data를 즉, network의 data와 local의 data의 sync를 맞추는 책임을 갖도록 한다'
     * 라는 전략을 사용한다.
     * 위에서 videos라는 livedata를 UI controller에서 사용하므로,
     * refresh한다.
     * */
    private fun refreshDataFromRepository() {
        viewModelScope.launch {
            _apiStatus.value = VideoApiStatus.LOADING
            try {
                // database refresh
                videoRepository.refreshVideos()
                _apiStatus.value = VideoApiStatus.DONE
            } catch (e: Exception) {
                _response.value = "Failure :: ${e.message}"
                if (videos.value.isNullOrEmpty()) {
                    _apiStatus.value = VideoApiStatus.ERROR
                }
            }
        }
    }


    companion object {
        private const val TAG = "DevByteViewModel"
    }
}

enum class VideoApiStatus {
    LOADING,
    ERROR,
    DONE
}