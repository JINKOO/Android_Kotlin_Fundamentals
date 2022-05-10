package com.kjk.diceroller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var rollButton: Button
    private lateinit var countUpButton: Button
    private lateinit var resultText: TextView
    private lateinit var resetButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rollButton = findViewById(R.id.roll_button)
        countUpButton = findViewById(R.id.count_up_button)
        resultText = findViewById(R.id.result_text)
        resetButton = findViewById(R.id.reset_button)

        rollButton.setOnClickListener {
            rollDice()
        }

        countUpButton.setOnClickListener {
            countUp()
        }

        resetButton.setOnClickListener {
            reset()
        }
    }

    // button을 선택하면, 1..6의 랜덤한 숫자 보여주기
    private fun rollDice() {
        // AppCompatActivity는 Context의 subClass이다.
        // Context는 Android OS의 현재 상태에 대한 정보를 얻을 수 있고, 사용자와 상호 작용 할 수 있도록 하는 객체이다.
//        Toast.makeText(this, "button clicked", Toast.LENGTH_LONG).show()

        val randomValue = (1..6).random()
        resultText.text = randomValue.toString()
    }

    private fun countUp() {
        if (resultText.text.toString() == getString(R.string.result_text)) {
            resultText.text = DICE_MIN_VALUE
            return
        }
        if (resultText.text.toString() == DICE_MAX_VALUE) {
            return
        }
        var intValue = resultText.text.toString().toInt()
        resultText.text = (++intValue).toString()
    }

    private fun reset() {
        resultText.text = DICE_RESET_VALUE
    }

    companion object {
        private const val DICE_RESET_VALUE = "0"
        private const val DICE_MIN_VALUE = "1"
        private const val DICE_MAX_VALUE = "6"
    }
}