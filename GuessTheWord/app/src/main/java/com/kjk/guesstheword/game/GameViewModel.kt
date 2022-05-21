package com.kjk.guesstheword.game

import android.util.Log
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    init {
        Log.i(TAG, ": GameViewModel Created()")
    }

    override fun onCleared() {
        super.onCleared()
        Log.i(TAG, "onCleared(): ")
    }

    companion object {
        private const val TAG = "GameViewModel"
    }
}