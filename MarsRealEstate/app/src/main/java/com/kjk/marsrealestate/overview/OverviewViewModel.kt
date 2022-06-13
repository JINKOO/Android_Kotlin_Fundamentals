package com.kjk.marsrealestate.overview

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kjk.marsrealestate.network.MarsApi
import com.kjk.marsrealestate.network.MarsProperty
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OverviewViewModel : ViewModel() {

    /** api로 부터 얻어온 properties*/
    private val _properties = MutableLiveData<List<MarsProperty>>()
    val properties: LiveData<List<MarsProperty>>
        get() = _properties

    /** */
    private val _status = MutableLiveData<MarsApiStatus>()
    val status: LiveData<MarsApiStatus>
        get() = _status

    /** DetailFragment로 navigate 하는 event trigger */
    private val _onNavigateToDetail = MutableLiveData<MarsProperty?>()
    val onNavigateToDetail: LiveData<MarsProperty?>
        get() = _onNavigateToDetail

    init {
        getMarsPropertyFromService(MarsApiFilter.SHOW_ALL)
    }

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

        /** corountine 사용*/
        viewModelScope.launch {
            _status.value = MarsApiStatus.LOADING
            try {
                _properties.value = MarsApi.retrofitService.getProperties(filter.value)
                _status.value = MarsApiStatus.DONE
            } catch (e: Exception) {
                _status.value = MarsApiStatus.ERROR
            }
            Log.d(TAG, "getMarsPropertyFromService: ${_status.value.toString()}")
        }
    }

    fun moveToDetail(marsProperty: MarsProperty) {
        _onNavigateToDetail.value = marsProperty
    }

    fun doneNavigateToDetail() {
        _onNavigateToDetail.value = null
    }

    fun updateFilter(filter: MarsApiFilter) {
        getMarsPropertyFromService(filter)
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