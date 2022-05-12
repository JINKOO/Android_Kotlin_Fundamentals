package com.kjk.colormyviews

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.kjk.colormyviews.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setListeners()
    }

    private fun setListeners() {
        binding.apply {
            val clickableView: List<View> =
                listOf(
                    boxOneText,
                    boxTwoText,
                    boxThreeText,
                    boxFourText,
                    boxFiveText,
                    constraintLayout,
                    redButton,
                    yellowButton,
                    greenButton
                )
            for (item in clickableView) {
                item.setOnClickListener { makeColored(item) }
            }
        }
    }

    private fun makeColored(view: View) {
        Log.d("1111", "makeColored: ${view.id}")
        binding.run {
            when (view.id) {
                R.id.box_one_text -> view.setBackgroundColor(Color.DKGRAY)
                R.id.box_two_text -> view.setBackgroundResource(R.drawable.ic_baseline_account_circle_24)
                R.id.box_three_text -> view.setBackgroundColor(Color.BLUE)
                R.id.box_four_text -> view.setBackgroundColor(Color.MAGENTA)
                R.id.box_five_text -> view.setBackgroundColor(Color.BLUE)
                R.id.red_button -> boxThreeText.setBackgroundResource(R.color.my_red)
                R.id.yellow_button -> boxFourText.setBackgroundResource(R.color.my_yellow)
                R.id.green_button -> boxFiveText.setBackgroundResource(R.color.my_green)
                else -> view.setBackgroundColor(Color.LTGRAY)
            }
        }
    }
}