package com.kjk.guesstheword.score

import android.util.Log
import androidx.lifecycle.ViewModel

class ScoreViewModel(
    finalScore: Int
) : ViewModel() {

    val finalScore = finalScore

    init {
        Log.i(TAG, "init{}: ${finalScore}")
    }

    companion object {
        private const val TAG = "ScoreViewModel"
    }
}