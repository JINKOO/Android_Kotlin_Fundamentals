package com.kjk.guesstheword.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.lang.Math.random

class GameViewModel : ViewModel() {

    private lateinit var wordList: MutableList<String>

    // 현재 점수
    private val _currentScore = MutableLiveData<Int>()
    val currentScore: LiveData<Int>
        get() = _currentScore

    // 현재 단어
    private val _currentWord = MutableLiveData<String>()
    val currentWord: LiveData<String>
        get() = _currentWord

    // TODO challenge Code
    val wordHintString = Transformations.map(currentWord) { word ->
        val randomPosition = (1..word.length).random()
        "Current Word has " +
                word.length.toString() +
                " letters" +
                "\nThe letter at position " +
                randomPosition.toString() +
                " is " +
                word[randomPosition-1].uppercase()
    }

    // 모든 단어를 순회했을 때, Game 이 종료되었다는 것을 알리기 위한 LiveData
    private val _eventGameFinish = MutableLiveData<Boolean>()
    val eventGameFinish: LiveData<Boolean>
        get() = _eventGameFinish

    // 현재 timer
    private val _currentTimer = MutableLiveData<Long>()
    val currentTimer: LiveData<Long>
        get() = _currentTimer

    //
    val currentTimerString = Transformations.map(currentTimer) { time: Long ->
        Log.d(TAG, ":${time} ")
        DateUtils.formatElapsedTime(time)
    }

    private val timer: CountDownTimer

    init {
        Log.i(TAG, "init{}: GameViewModel Created() ${eventGameFinish.value}")
        _currentWord.value = ""
        _currentScore.value = 0

        resetList()
        nextWord()

        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {
            override fun onTick(millisUntilFinished: Long) {
                _currentTimer.value = millisUntilFinished / ONE_SECOND
                Log.i(TAG, "onTick: ${_currentTimer.value}")
            }

            override fun onFinish() {
                _currentTimer.value = DONE
                onGameFinish()
            }
        }
        timer.start()
    }

    /** random하게 set한다. */
    private fun resetList() {
        wordList = mutableListOf(
            "queen",
            "hospital",
            "basketball",
            "cat",
            "change",
            "snail",
            "soup",
            "calendar",
            "sad",
            "desk",
            "guitar",
            "home",
            "railway",
            "zebra",
            "jelly",
            "car",
            "crow",
            "trade",
            "bag",
            "roll",
            "bubble"
        )
        wordList.shuffle()
    }

    private fun nextWord() {
        if (wordList.isEmpty()) {
            //onGameFinish()
            resetList()
        } else {
            _currentWord.value = wordList.removeAt(0)
        }
    }

    fun onCorrect() {
        _currentScore.value = currentScore.value?.plus(1)
        nextWord()
    }

    fun onSkip() {
        _currentScore.value = currentScore.value?.minus(1)
        nextWord()
    }

    fun onGameFinish() {
        _eventGameFinish.value = true
    }

    fun onGameFinishComplete() {
        _eventGameFinish.value = false
    }

    override fun onCleared() {
        super.onCleared()
        Log.i(TAG, "onCleared(): ")
        timer.cancel()
    }

    companion object {
        private const val TAG = "GameViewModel"

        /** timer */
        // timer종료
        private const val DONE = 0L
        // 타이머 간격
        private const val ONE_SECOND = 1000L
        // 총 타임
        private const val COUNTDOWN_TIME = 60000L
    }
}