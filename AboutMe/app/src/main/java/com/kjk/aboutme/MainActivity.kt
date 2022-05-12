package com.kjk.aboutme

import android.content.Context
import android.inputmethodservice.InputMethodService
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.kjk.aboutme.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // dataBinding 사용
    private lateinit var binding: ActivityMainBinding
    // test DataBinding
    private val myName: MyName = MyName("jinkweon_KO")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // dataBinding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.myName = myName

        // Done Button Click Event
        binding.doneButton.setOnClickListener {
            addNickName()
        }

        binding.nicknameText.setOnClickListener {
            updateNickname()
        }
    }

    private fun addNickName() {
        binding.apply {
            myName?.nickName = nicknameEdit.text.toString()
            // nickName이 변경 될 때 마다, UI는 새로운 data를 가진다. 따라서 기존의 binding expression들을 invalidate시켜야 한다.
            // UI data 갱신에 따른 binding object를 update한다.
            invalidateAll()

            nicknameText.visibility = View.VISIBLE
            nicknameEdit.visibility = View.GONE
            doneButton.visibility = View.GONE

            // keyboard hide
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(doneButton.windowToken, 0)
        }
    }

    private fun updateNickname() {
        binding.apply {
            doneButton.visibility = View.GONE
            nicknameEdit.visibility = View.VISIBLE
            doneButton.visibility = View.VISIBLE
            nicknameEdit.requestFocus()
            // set keyboard
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(nicknameEdit, 0)
        }
    }
}