package com.kjk.guesstheword.score

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.kjk.guesstheword.R
import com.kjk.guesstheword.databinding.FragmentScoreBinding

class ScoreFragment :
    Fragment(),
    View.OnClickListener {

    private lateinit var binding: FragmentScoreBinding

    private lateinit var viewModel: ScoreViewModel
    private lateinit var viewModelFactory: ScoreViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_score,
            container,
            false
        )

        viewModelFactory = ScoreViewModelFactory(ScoreFragmentArgs.fromBundle(requireArguments()).score)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ScoreViewModel::class.java)

        // dataBinding layout에서 viewModel의 UI data를 사용 할 수 있도록
        binding.scoreViewModel = viewModel

        // initListener
        /** Listener binding을 사용해서 필요없음 */
        //initListener()

        /** observe live data */
        /**
        viewModel.finalScore.observe(viewLifecycleOwner, Observer { finalScore ->
            // update final Score TextView
            updateFinalScore(finalScore)
        })
        */

        viewModel.eventPlayAgain.observe(viewLifecycleOwner, Observer { isPlayAgain ->
            if (isPlayAgain) {
                moveToGameFragment()
            }
        })

        return binding.root
    }

    private fun initListener() {
        binding.apply {
            playButton.setOnClickListener(this@ScoreFragment)
        }
    }

    private fun updateFinalScore(score: Int) {
        Log.i(TAG, "updateFinalScore: ${score}")
        binding.resultScoreTextview.run {
            text = score.toString()
        }
    }

    private fun moveToGameFragment() {
        findNavController().navigate(ScoreFragmentDirections.actionScoreFragmentToGameFragment())
        viewModel.onPlayAgainComplete()
    }

    private fun playAgainEvent() {
        // set playAgainEvent true
        viewModel.onPlayAgain()
    }

    override fun onClick(v: View?) {
        binding.run {
            when(v) {
                playButton -> { playAgainEvent() }
                else -> {}
            }
        }
    }

    companion object {
        private const val TAG = "ScoreFragment"
    }
}