package com.kjk.diceroller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    // TODO findViewById()
    // rollDice()에서 findViewById()를 사용하면,
    // 클릭 핸들러 함수가 실행될 때 마다, findViewById()가 실행된다.
    // 안드로이드 시스템은 전체의 뷰 계층에서 해당 id값을 탐색하기 때문이다. 이는 비용이 비싸다.
    // 따라서, 다음과 같이 field에서 사용하도록 한다.
    // 즉, findViewById()는 한번만 호출되도록 한다.

    // TODO lateinit keyword
    // Kotlin compiler에게 해당 변수는 홀출되기 전에 반드시 초기화 될거야!
    // 라고 알려준다. 따라서, 사용할 때, non-null 변수로 취급된다.
    // private var diceImage?: ImageView = null
    // 윗줄과 같이 선언과 동시에 null로 초기화를 진행하지 않아도 된다. 이는 사용 할 때, null인지 계속 check해야해서 번거롭다.
    private lateinit var firstDiceImage: ImageView
    private lateinit var secondDiceImage: ImageView
    private lateinit var rollButton: Button
    private lateinit var resultText: TextView
    private lateinit var countUpButton: Button
    private lateinit var resetButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firstDiceImage = findViewById(R.id.first_dice_image)
        secondDiceImage = findViewById(R.id.second_dice_image)
        rollButton = findViewById(R.id.roll_button)
        resultText = findViewById(R.id.result_text)
        countUpButton = findViewById(R.id.count_up_button)
        resetButton = findViewById(R.id.reset_button)

        rollButton.setOnClickListener {
            rollDice()
        }

        resetButton.setOnClickListener {
            reset()
        }
    }

    // TODO 01.3.8 - Challenge
    // 주사위 2개로 보여주기
    private fun getRandomDiceImage(): Int {
        val randomValue = (1..6).random()
        return when (randomValue) {
            1 -> R.drawable.dice_1
            2 -> R.drawable.dice_2
            3 -> R.drawable.dice_3
            4 -> R.drawable.dice_4
            5 -> R.drawable.dice_5
            else -> R.drawable.dice_6
        }
    }

    // button을 선택하면, 1..6의 랜덤한 숫자 보여주기
    private fun rollDice() {
        // TODO Context 개념
        // AppCompatActivity는 Context의 subClass이다.
        // Context는 Android OS의 현재 상태에 대한 정보를 얻을 수 있고, 사용자와 상호 작용 할 수 있도록 하는 객체이다.
//        Toast.makeText(this, "button clicked", Toast.LENGTH_LONG).show()
        resultText.visibility = View.GONE
        firstDiceImage.setImageResource(getRandomDiceImage())
        secondDiceImage.setImageResource(getRandomDiceImage())
    }

    private fun reset() {
        resultText.visibility = View.VISIBLE
        firstDiceImage.setImageResource(R.drawable.empty_dice)
        secondDiceImage.setImageResource(R.drawable.empty_dice)
    }
}