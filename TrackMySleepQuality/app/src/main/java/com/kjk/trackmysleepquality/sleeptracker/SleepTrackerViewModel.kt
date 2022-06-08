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

    // 현재 기록 할 sleepNight
    private var toNight = MutableLiveData<SleepNight?>()

    // room이 background에서 실행 한다. suspend로 실행하지 않아도 된다.
    var allNights = dataBase.getAllNights()

    // allnights transformations
    val toNightString = Transformations.map(allNights) { allNights ->
        formatNights(allNights, application.resources)
    }

    // SleepQualityFragment로 navigate trigger
    private val _navigateToSleepQuality = MutableLiveData<SleepNight?>()
    val navigateToSleepQuality: LiveData<SleepNight?>
        get() = _navigateToSleepQuality

    /** 현재 기록할 sleepNight가 null이어야 startButton을 enable한다. */
    val startButtonEnable = Transformations.map(toNight) {
        it == null
    }

    /** 현재 기록 중인 sleepNight가 있다면, stopButton은 enable*/
    val stopButtonEnable = Transformations.map(toNight) {
        it != null
    }

    /** clear는 data가 있는 경우에만, clearButtonEnable한다. */
    val clearButtonEnable = Transformations.map(allNights) {
        it?.isNotEmpty()
    }

    /** SnackBar Message 출력 event trigger */
    private val _onSnackBarEvent = MutableLiveData<Boolean>()
    val onSnackBarEvent: LiveData<Boolean>
        get() = _onSnackBarEvent

    /** SleepDetailFragment로 이동하는 event Trigger */
    private val _onNavigateToSleepDetail = MutableLiveData<Long?>()
    val onNavigateToSleepDetail: LiveData<Long?>
        get() = _onNavigateToSleepDetail

    init {
        initToNight()
    }

    private fun initToNight() {
        viewModelScope.launch {
            toNight.value = getToNightFromDatabase()
        }
    }

    /** start와 end가 동일하지 않다는 의미는 현재 기록 중인 SleepNight가 있다는 의미이다. */
    private suspend fun getToNightFromDatabase(): SleepNight? {
        val night = dataBase.getToNight()
        if (night?.endTimeMillis != night?.startTimeMillis) {
            Log.d(TAG, "getToNightFromDatabase: ${night?.nightId}")
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
            _navigateToSleepQuality.value = oldNight
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
            _onSnackBarEvent.value = true
        }
    }

    private suspend fun clear() {
        dataBase.clear()
    }

    fun onNavigateDone() {
        _navigateToSleepQuality.value = null
    }

    fun onSnackBarEventDone() {
        _onSnackBarEvent.value = false
    }

    fun onNavigateToSleepDetail(sleepNightId: Long) {
        _onNavigateToSleepDetail.value = sleepNightId
    }

    fun onNavigateDoneToSleepDetail() {
        _onNavigateToSleepDetail.value = null
    }

    companion object {
        private const val TAG = "SleepTrackerViewModel"
    }
}