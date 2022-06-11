package com.kjk.marsrealestate.overview

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kjk.marsrealestate.network.MarsApi
import com.kjk.marsrealestate.network.MarsProperty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OverviewViewModel : ViewModel() {

    /** api로 부터 얻어온 properties*/
    private val _properties = MutableLiveData<List<MarsProperty>>()
    val properties: LiveData<List<MarsProperty>>
        get() = _properties

    /** */
    private val _response = MutableLiveData<String>()
    val response: LiveData<String>
        get() = _response

    init {
        getMarsPropertyFromService()
    }

    private fun getMarsPropertyFromService() {
        MarsApi.retrofitService.getProperties().enqueue(
            object: Callback<List<MarsProperty>> {
                override fun onResponse(
                    call: Call<List<MarsProperty>>,
                    response: Response<List<MarsProperty>>
                ) {
                    if (!response.isSuccessful) {
                        return
                    }
                    
                    response.body()?.let { 
                        _properties.value = it
                        _response.value = "Success :: ${it.size}"
                    }
                }

                override fun onFailure(call: Call<List<MarsProperty>>, t: Throwable) {
                    _response.value = "Failure :: ${t.message}"
                }
            }
        )
    }
}