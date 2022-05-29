package com.kjk.guesstheword.game

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.kjk.guesstheword.R
import com.kjk.guesstheword.databinding.FragmentGameBinding
import kotlin.properties.Delegates

class GameFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentGameBinding

    // viewModel
    // viewModel의 instance를 생성할 때에는 ViewModel을 사용하지 않고,
    // ViewModelProvider를 사용한다.
    private lateinit var viewModel: GameViewModel

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

        // init viewModel
        Log.i(TAG, "onCreateView: called viewModelProvider.get()")
        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        // view layout파일에서 direct로 viewModel의 UI data를 사용하기 위해서
        binding.gameViewModel = viewModel

        /** dataBinding으로 listener binding를 사용해서 필요없음 */
        //setListener()

        binding.lifecycleOwner = viewLifecycleOwner

        /**
        viewModel.currentScore.observe(viewLifecycleOwner, Observer { newScore ->
            Log.i(TAG, "onCreateView: newScore:  ${newScore}")
            updateScore(newScore)
        })

        viewModel.currentWord.observe(viewLifecycleOwner, Observer { newWord ->
            Log.i(TAG, "onCreateView: newWord: ${newWord}")
            updateWord(newWord)
        })
        */

        viewModel.eventGameFinish.observe(viewLifecycleOwner, Observer { hasFinished ->
            Log.i(TAG, "onCreateView: hasFinished: ${hasFinished}")
            // viewModel의 hasFinish값이 true인 경우에만 종료한다.
            if (hasFinished) {
                onEndGame()
            }
        })

        return binding.root
    }

    private fun setListener() {
        binding.apply {
            skipButton.setOnClickListener(this@GameFragment)
            correctButton.setOnClickListener(this@GameFragment)
            endGameButton.setOnClickListener(this@GameFragment)
        }
    }

    /** GameFragment에서 버튼을 클릭했을 때 이벤트 처리 */
    private fun onCorrect() {
        viewModel.onCorrect()
    }

    private fun onSkip() {
        viewModel.onSkip()
    }

    /** ui data update with viewModel's data */
    private fun updateScore(score: Int) {
        binding.scoreText.text = score.toString()
    }

    private fun updateWord(word: String) {
        binding.wordTextview.text = word
    }


    private fun onEndGame() {
        gameFinished()
    }

    /** 사용자가 end game을 tab하면, score fragment로 이동 */
    private fun gameFinished() {
        Toast.makeText(context, "Game Finished", Toast.LENGTH_SHORT).show()
        val action = GameFragmentDirections.actionGameFragmentToScoreFragment(viewModel.currentScore.value ?: 0)
        NavHostFragment.findNavController(this).navigate(action)
        viewModel.onGameFinishComplete()
    }

    override fun onClick(v: View?) {
        binding.run {
            when (v) {
                skipButton -> { onSkip() }
                correctButton -> { onCorrect() }
                endGameButton -> { onEndGame() }
            }
        }
    }

    companion object {
        private const val TAG = "GameFragment"
    }
}