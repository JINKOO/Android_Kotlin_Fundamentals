package com.kjk.marsrealestate.overview

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kjk.marsrealestate.database.MarsPropertyDatabase
import com.kjk.marsrealestate.network.MarsApi
import com.kjk.marsrealestate.domain.MarsProperty
import com.kjk.marsrealestate.repository.MarsRepository
import kotlinx.coroutines.launch

class OverviewViewModel(
    application: Application
) : ViewModel() {

    /**
     * repository instance
     */
    private val repository: MarsRepository =
        MarsRepository(MarsPropertyDatabase.getInstance(application))

    /**
     *  repository로 부터 가져온 data
     *  이 data는 network로 부터 가져온 것이 아니라, local database에서 가져온 것이다.
     *  단, UI Controller, ViewModel에서는 이 data가 remote에서 온 것인지,
     *  local에서 온것인지 모른다.
     */
    val properties = repository.marsProperties

    /*
    /** api로 부터 얻어온 properties*/
    private val _properties = MutableLiveData<List<MarsProperty>>()
    val properties: LiveData<List<MarsProperty>>
        get() = _properties
     */


    /**
     * api network 상태
     */
    private val _status = MutableLiveData<MarsApiStatus>()
    val status: LiveData<MarsApiStatus>
        get() = _status

    /**
     * DetailFragment로 navigate 하는 event trigger
     */
    private val _onNavigateToDetail = MutableLiveData<MarsProperty?>()
    val onNavigateToDetail: LiveData<MarsProperty?>
        get() = _onNavigateToDetail

    init {
        //getMarsPropertyFromService(MarsApiFilter.SHOW_ALL)
        getMarsPropertyFromRepository(MarsApiFilter.SHOW_ALL)
    }

    /**
     *  또한, 현재 이 ViewModel이 repository에 data를 요청했으므로, database refresh를 해야한다.
     *  Repository pattern에서는 repository integrate를 database refresh 전략이 필요하다.
     *  여기서는 'repository에 data를 요청한 놈이 network와 database의 data sync를 맞춰야 하는 최신화의 책임을 진다.' 라는 전략을 사용한다.
     */
    private fun getMarsPropertyFromRepository(filter: MarsApiFilter) {
        viewModelScope.launch {
            _status.value = MarsApiStatus.LOADING
            try {
                repository.refresh(filter)
                _status.value = MarsApiStatus.DONE
            } catch (e: Exception) {
                Log.d(TAG, "${e.message}")
                _status.value = MarsApiStatus.ERROR
            }
        }
    }

    /**
     * network로 부터 direct로 data를 fetch해 바로
     * UI Controller에 보여주는 방식
     */
    private fun getMarsPropertyFromService(filter: MarsApiFilter) {
        /** call Back 함수를 사용하는 방식 */
//        MarsApi.retrofitService.getProperties().enqueue(
//            object: Callback<List<MarsProperty>> {
//                override fun onResponse(
//                    call: Call<List<MarsProperty>>,
//                    response: Response<List<MarsProperty>>
//                ) {
//                    if (!response.isSuccessful) {
//                        return
//                    }
//
//                    response.body()?.let {
//                        _properties.value = it
//                        _response.value = "Success :: ${it.size}"
//                    }
//                }
//
//                override fun onFailure(call: Call<List<MarsProperty>>, t: Throwable) {
//                    _response.value = "Failure :: ${t.message}"
//                }
//            }
//        )

//        /** corountine 사용*/
//        viewModelScope.launch {
//            _status.value = MarsApiStatus.LOADING
//            try {
//                _properties.value = MarsApi.retrofitService.getProperties(filter.value)
//                _status.value = MarsApiStatus.DONE
//            } catch (e: Exception) {
//                _status.value = MarsApiStatus.ERROR
//            }
//            Log.d(TAG, "getMarsPropertyFromService: ${_status.value.toString()}")
//        }
    }

    fun moveToDetail(marsProperty: MarsProperty) {
        _onNavigateToDetail.value = marsProperty
    }

    fun doneNavigateToDetail() {
        _onNavigateToDetail.value = null
    }

    fun updateFilter(filter: MarsApiFilter) {
        //getMarsPropertyFromService(filter)
        getMarsPropertyFromRepository(filter)
    }

    companion object {
        private const val TAG = "OverviewViewModel"
    }
}

enum class MarsApiStatus {
    LOADING,
    DONE,
    ERROR
}

enum class MarsApiFilter(val value: String) {
    SHOW_ALL("all"),
    BUY("buy"),
    RENT("rent")
}