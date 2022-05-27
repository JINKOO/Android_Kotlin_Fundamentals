package com.kjk.guesstheword.game

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    private lateinit var wordList: MutableList<String>

    // 현재 점수
    private var _currentScore = MutableLiveData<Int>()
    val currentScore: LiveData<Int>
        get() = _currentScore

    // 현재 단어
    private var _currentWord = MutableLiveData<String>()
    val currentWord: LiveData<String>
        get() = _currentWord

    // 모든 단어를 순회했을 때, Game 이 종료되었다는 것을 알리기 위한 LiveData
    private var _eventGameFinish = MutableLiveData<Boolean>()
    val eventGameFinish: LiveData<Boolean>
        get() = _eventGameFinish

    init {
        Log.i(TAG, "init{}: GameViewModel Created() ${eventGameFinish.value}")
        _currentWord.value = ""
        _currentScore.value = 0
        resetList()
        nextWord()
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
            onGameFinish()
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
    }

    companion object {
        private const val TAG = "GameViewModel"
    }
}