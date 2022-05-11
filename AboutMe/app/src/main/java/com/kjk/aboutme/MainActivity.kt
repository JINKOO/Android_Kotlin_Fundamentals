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

class MainActivity : AppCompatActivity() {

    private lateinit var editText: EditText
    private lateinit var nicknameTextView: TextView
    private lateinit var doneButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editText = findViewById(R.id.nickname_edit)
        nicknameTextView = findViewById(R.id.nickname_text)
        doneButton = findViewById(R.id.done_button)

        // Done Button Click Event
        doneButton.setOnClickListener {
            addNickName(it)
        }

        nicknameTextView.setOnClickListener {
            updateNickname(it)
        }
    }

    private fun addNickName(view: View) {
        nicknameTextView.text = editText.text
        nicknameTextView.visibility = View.VISIBLE
        editText.visibility = View.GONE
        view.visibility = View.GONE

        // keyboard hide
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun updateNickname(view: View) {
        view.visibility = View.GONE
        editText.visibility = View.VISIBLE
        doneButton.visibility = View.VISIBLE

        editText.requestFocus()
        // set keyboard
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(editText, 0)
    }

}