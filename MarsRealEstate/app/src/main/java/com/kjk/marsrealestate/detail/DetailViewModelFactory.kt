package com.kjk.marsrealestate.detail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kjk.marsrealestate.domain.MarsProperty
import java.lang.IllegalArgumentException

class DetailViewModelFactory(
    private val marsProperty: MarsProperty,
    private val app: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(marsProperty, app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}