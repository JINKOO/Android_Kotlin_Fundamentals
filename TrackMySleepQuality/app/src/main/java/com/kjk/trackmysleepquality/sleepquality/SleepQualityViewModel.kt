package com.kjk.trackmysleepquality.sleepquality

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kjk.trackmysleepquality.database.SleepDatabaseDao
import com.kjk.trackmysleepquality.database.SleepNight
import kotlinx.coroutines.launch

/**
 * 현재 Update해야하는 sleepNight
 */
class SleepQualityViewModel(
    private val database: SleepDatabaseDao,
    private val nightSleepKey: Long = 0L
) : ViewModel() {

    /** sleep quality set 한후, 다시 sleepTracker로 돌아가는 event trigger */
    private val _onNavigateToSleepTracker = MutableLiveData<Boolean>()
    val onNavigateToSleepTracker: LiveData<Boolean>
        get() = _onNavigateToSleepTracker

    /**
     * 현재 기록 중인 sleepNight의 sleepquality를 set한다.
     * listener binding을 사용한다.
     */
    fun onSetSleepQuality(quality: Int) {
        viewModelScope.launch {
            // 우선 key값으로, nightSleep가져오기
            val currentSleepNight = get(nightSleepKey) ?: return@launch
            Log.d(TAG, "onSetSleepQuality: ${quality}")

            // sleepQuality set한다.
            currentSleepNight.sleepQuality = quality

            // 해당 SleepNight를 update한다.
            update(currentSleepNight)

            // event trigger를 true로 set한다. 그래야 선택 하고, SleepTracker로 이동한다.
            _onNavigateToSleepTracker.value = true
        }
    }

    private suspend fun get(key: Long): SleepNight? {
        return database.get(key)
    }

    private suspend fun update(sleepNight: SleepNight) {
        database.update(sleepNight)
    }

    /** Navigate완료  */
    fun onNavigateDone() {
        _onNavigateToSleepTracker.value = false
    }

    companion object {
        private const val TAG = "SleepQualityViewModel"
    }
}