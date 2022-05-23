package com.kjk.guesstheword.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.kjk.guesstheword.R
import com.kjk.guesstheword.databinding.FragmentGameBinding
import kotlin.properties.Delegates

class GameFragment : Fragment() {

    private lateinit var binding: FragmentGameBinding

    // 단어들이 들어 있는 리스트
    private lateinit var wordList: MutableList<String>

    // 현재 단어
    private lateinit var currentWord: String

    // 현재 점수
    private var currentScore: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_game,
            container,
            false
        )

        setListener()
        resetList()
        nextWord()
        updateWord()
        updateScore()

        return binding.root
    }

    private fun setListener() {
        binding.apply {

        }
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
        if (wordList.isNotEmpty()) {
            currentWord = wordList.removeAt(0)
        }
        updateWord()
        updateScore()
    }

    private fun updateScore() {
        binding.scoreText.text = currentScore.toString()
    }

    private fun updateWord() {
        binding.wordTextview.text = currentWord
    }
}