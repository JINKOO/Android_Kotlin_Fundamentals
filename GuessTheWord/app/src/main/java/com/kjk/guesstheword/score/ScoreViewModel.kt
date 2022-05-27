package com.kjk.guesstheword.score

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScoreViewModel(
    finalScore: Int
) : ViewModel() {

    // 최종 점수
    private val _finalScore = MutableLiveData<Int>()
    val finalScore: LiveData<Int>
        get() = _finalScore

    // play again 버튼 tab한 경우의 event발생 유무 flag
    private val _eventPlayAgain = MutableLiveData<Boolean>()
    val eventPlayAgain: LiveData<Boolean>
        get() = _eventPlayAgain

    init {
        Log.i(TAG, "init{}: ${finalScore}")
        _finalScore.value = finalScore
    }

    fun onPlayAgain() {
        _eventPlayAgain.value = true
    }

    /** */
    fun onPlayAgainComplete() {
        _eventPlayAgain.value = false
    }

    companion object {
        private const val TAG = "ScoreViewModel"
    }
}