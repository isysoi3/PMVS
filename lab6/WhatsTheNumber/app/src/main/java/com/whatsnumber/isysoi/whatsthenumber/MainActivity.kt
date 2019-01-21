package com.whatsnumber.isysoi.whatsthenumber

import android.content.res.Configuration
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import android.text.method.ScrollingMovementMethod



class MainActivity : AppCompatActivity() {

    private enum class SavedInstanceMainActivityStateEnum(val value: String) {
        infoTextView("CURRENT_TEXT_INFO_MAIN_ACTIVITY"),
        guessNumber("CURRENT_GUESS_NUMBER_MAIN_ACTIVITY"),
        isNumberGuessed("CURRENT_NUMBER_IS_GUESSED_MAIN_ACTIVITY"),
        buttonText("CURRENT_BUTTON_TEXT_MAIN_ACTIVITY"),
        countGuessing("CURRENT_COUNT_GUESSING_MAIN_ACTIVITY"),
        historyTextViewInfo("CURRENT_HISTORY_TEXT_VIEW_INFO_MAIN_ACTIVITY")
    }

    private var guessNumber = 0
    private var isNumberGuessed = false
    private var countGuessing = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        guessNumber = randomInt(1, 100)
        if (savedInstanceState != null) {
            guessNumber = savedInstanceState.getInt(SavedInstanceMainActivityStateEnum.guessNumber.value)
            infoTextView.text = savedInstanceState.getString(SavedInstanceMainActivityStateEnum.infoTextView.value)
            isNumberGuessed = savedInstanceState.getBoolean(SavedInstanceMainActivityStateEnum.isNumberGuessed.value)
            button.text = savedInstanceState.getString(SavedInstanceMainActivityStateEnum.buttonText.value)
            countGuessing = savedInstanceState.getInt(SavedInstanceMainActivityStateEnum.countGuessing.value)
            historyTextViewInfo.text = savedInstanceState.getString(SavedInstanceMainActivityStateEnum.historyTextViewInfo.value)
        }

        button.setOnClickListener {
            if (!isNumberGuessed) {
                val userInput = numberEditText.text.toString()s
                if (userInput == null) {
                    infoTextView.text = resources.getString(R.string.no_input)
                    return@setOnClickListener
                }
                numberEditText.setText("")
                countGuessing++
                when {
                    userInput < 1 -> infoTextView.text = resources.getString(R.string.error_less_than_1)
                    userInput > 100 -> infoTextView.text = resources.getString(R.string.error_more_than_100)
                    userInput == guessNumber -> {
                        infoTextView.text = resources.getString(R.string.win)
                        isNumberGuessed = true
                        button.text = resources.getString(R.string.button_win_text)
                    }
                    userInput > guessNumber -> infoTextView.text = resources.getString(R.string.error_more)
                    userInput < guessNumber -> infoTextView.text = resources.getString(R.string.error_less)
                }
                historyTextViewInfo.text =  "${historyTextViewInfo.text}$countGuessing: $userInput \n"

            } else {
                guessNumber = randomInt(1, 100)
                infoTextView.text = resources.getString(R.string.info_text_view_init)
                button.text = resources.getString(R.string.button_play_text)
                historyTextViewInfo.text = ""
                isNumberGuessed = false
                countGuessing = 0
            }


        }
        historyTextViewInfo.movementMethod = ScrollingMovementMethod()
        updateLayout(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE);
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        if (newConfig != null) {
            updateLayout(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
        }
    }

    private fun updateLayout(isLandscape: Boolean) {
        if (isLandscape) {
            historyTextViewInfo.visibility = View.INVISIBLE
            historyTextView.visibility = View.INVISIBLE
        } else {
            historyTextViewInfo.visibility = View.VISIBLE
            historyTextView.visibility = View.VISIBLE
        }
    }

    private fun randomInt(start: Int, end: Int): Int {
        return Random().nextInt(end) + start
    }


    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putInt(SavedInstanceMainActivityStateEnum.guessNumber.value, guessNumber)
        outState?.putInt(SavedInstanceMainActivityStateEnum.countGuessing.value, countGuessing)
        outState?.putBoolean(SavedInstanceMainActivityStateEnum.isNumberGuessed.value, isNumberGuessed)
        outState?.putString(SavedInstanceMainActivityStateEnum.buttonText.value, button.text.toString())
        outState?.putString(SavedInstanceMainActivityStateEnum.historyTextViewInfo.value, historyTextViewInfo.text.toString())
        outState?.putString(SavedInstanceMainActivityStateEnum.infoTextView.value, infoTextView.text.toString())

        super.onSaveInstanceState(outState)
    }

}
