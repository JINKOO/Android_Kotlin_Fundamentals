package com.kjk.devbytes.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kjk.devbytes.database.VideoDatabase
import com.kjk.devbytes.domain.DevByteVideo
import com.kjk.devbytes.network.VideoApi
import com.kjk.devbytes.network.asDomainModel
import com.kjk.devbytes.repository.VideoRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class DevByteViewModel(
    application: Application
) : AndroidViewModel(application) {

    /** Repository */
    private val videoRepository = VideoRepository(VideoDatabase.getInstance(application))

    /** repository로 부터 받아오는 video*/
    val videos: LiveData<List<DevByteVideo>> = videoRepository.videos

    /** 임시 응답*/
    private val _response = MutableLiveData<String>()
    val response: LiveData<String>
        get() = _response

    /** API call 상태 */
    private val _apiStatus = MutableLiveData<VideoApiStatus>()
    val apiStatus: LiveData<VideoApiStatus>
        get() = _apiStatus

    init {
        //refreshDataFromNetWork()
        refreshDataFromRepository()
    }

    /** networking 작업 */
//    private fun refreshDataFromNetWork() {
//        viewModelScope.launch {
//            _apiStatus.value = VideoApiStatus.LOADING
//            try {
//                _videos.value = VideoApi.retrofitService.getVideos().asDomainModel()
//                _response.value = "SUCCESS!!"
//                _apiStatus.value = VideoApiStatus.DONE
//            } catch (e: Exception) {
//                _response.value = "Failure:: ${e.message}"
//                _apiStatus.value = VideoApiStatus.ERROR
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
            Log.d(TAG, "refreshDataFromRepository: ")
            // database refresh
            videoRepository.refreshVideos()
            _apiStatus.value = VideoApiStatus.LOADING
            try {
                _apiStatus.value = VideoApiStatus.DONE
            } catch (e: Exception) {
                Log.d(TAG, "refreshDataFromRepository: ${e.message}")
                _apiStatus.value = VideoApiStatus.ERROR
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