package com.kjk.trackmysleepquality.sleepdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kjk.trackmysleepquality.database.SleepDatabaseDao
import java.lang.IllegalArgumentException

class SleepDetailViewModelFactory(
    private val database: SleepDatabaseDao,
    private val sleepNightId: Long
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SleepDetailViewModel::class.java)) {
            return SleepDetailViewModel(database, sleepNightId) as T
        }
        throw IllegalArgumentException("Unknown viewModel class")
    }
}