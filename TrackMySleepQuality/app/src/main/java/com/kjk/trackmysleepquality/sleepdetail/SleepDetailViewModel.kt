package com.kjk.trackmysleepquality.sleepdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kjk.trackmysleepquality.database.SleepDatabaseDao
import com.kjk.trackmysleepquality.database.SleepNight

/**
 * SleepDetail화면에 보여줄 UI data를 관리하는 class이다.
 */
class SleepDetailViewModel(
    private val database: SleepDatabaseDao,
    private val sleepNightId: Long = 0L
) : ViewModel() {

    private val sleepNight: LiveData<SleepNight> = database.getNightWithId(sleepNightId)

    fun getSleepNight() = sleepNight

    /** SleepTrackerFragment로 이동하는 event trigger */
    private val _onNavigateToSleepTracker = MutableLiveData<Boolean?>()
    val onNavigateToSleepTracker: LiveData<Boolean?>
        get() = _onNavigateToSleepTracker


    /** 'CLOSE'버튼을 클릭 시 SleepTrakcerFragment이동 LiveData를 true로 변경 */
    fun onClose() {
        _onNavigateToSleepTracker.value = true
    }

    /** 이동 완료시, null로 set 한다.*/
    fun onNavigateToSleepTrackerDone() {
        _onNavigateToSleepTracker.value = null
    }
}













