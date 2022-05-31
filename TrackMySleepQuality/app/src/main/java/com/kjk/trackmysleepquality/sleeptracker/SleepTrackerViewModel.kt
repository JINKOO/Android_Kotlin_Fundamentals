package com.kjk.trackmysleepquality.sleeptracker

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.kjk.trackmysleepquality.database.SleepDatabaseDao
import com.kjk.trackmysleepquality.database.SleepNight
import formatNights
import kotlinx.coroutines.launch

class SleepTrackerViewModel(
    private val dataBase: SleepDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    // 가장 최근의 night
    private var toNight = MutableLiveData<SleepNight?>()

    // room이 background에서 실행 한다. suspend로 실행하지 않아도 된다.
    private var allNights = dataBase.getAllNights()

    // allnights transformations
    val toNightString = Transformations.map(allNights) { allNights ->
        formatNights(allNights, application.resources)
    }

    // SleepQualityFragment로 이동 유무
    private val _navigateToSleepQuality = MutableLiveData<SleepNight>()
    val navigateToSleepQuality: LiveData<SleepNight>
        get() = _navigateToSleepQuality

    init {
        initToNight()
    }

    private fun initToNight() {
        viewModelScope.launch {
            toNight.value = getToNightFromDatabase()
        }
    }

    private suspend fun getToNightFromDatabase(): SleepNight? {
        val night = dataBase.getToNight()
        if (night?.endTimeMillis != night?.startTimeMillis) {
            return null
        }
        Log.d(TAG, "getToNightFromDatabase: ${night?.startTimeMillis}")
        return night
    }

    /** start 버튼 클릭 시, 실행 lisenter binding으로 수행한다. */
    fun onStartTracker() {
        viewModelScope.launch {
            // 새로 수면 기록을 할 instance를 생성.
            val newNight = SleepNight()
            insert(newNight)
            toNight.value = getToNightFromDatabase()
        }
    }

    private suspend fun insert(newNight: SleepNight) {
        dataBase.insert(newNight)
    }

    /** stop버튼 클릭 시 수행. listener binding으로 수행한다. */
    fun onStopTracker() {
        viewModelScope.launch {
            Log.d(TAG, "onStopTracker: ${toNight.value?.startTimeMillis} ")
            val oldNight = toNight.value ?: return@launch
            Log.d(TAG, "onStopTracker: ${toNight.value?.startTimeMillis} ")
            oldNight.endTimeMillis = System.currentTimeMillis()
            update(oldNight)
        }
    }

    private suspend fun update(night: SleepNight) {
        dataBase.update(night)
    }

    /** clear 버튼 클릭시, listener binding으로 실행 */
    fun onClearTracker() {
        viewModelScope.launch {
            clear()
            toNight.value = null
        }
    }

    private suspend fun clear() {
        dataBase.clear()
    }

    companion object {
        private const val TAG = "SleepTrackerViewModel"
    }
}