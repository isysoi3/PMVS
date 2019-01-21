package com.whatsnumber.isysoi.whatsthenumber

import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation
import android.view.animation.TranslateAnimation
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


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
    private val totalAttemps = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        relativeLayout.background = if (this.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
            getDrawable(R.drawable.wallpapers)
        else
            getDrawable(R.drawable.wallpapers_land)

        guessNumber = randomInt(1, 20)
        if (savedInstanceState != null) {
            guessNumber = savedInstanceState.getInt(SavedInstanceMainActivityStateEnum.guessNumber.value)
            infoTextView.text = savedInstanceState.getString(SavedInstanceMainActivityStateEnum.infoTextView.value)
            isNumberGuessed = savedInstanceState.getBoolean(SavedInstanceMainActivityStateEnum.isNumberGuessed.value)
            tryButton.text = savedInstanceState.getString(SavedInstanceMainActivityStateEnum.buttonText.value)
            countGuessing = savedInstanceState.getInt(SavedInstanceMainActivityStateEnum.countGuessing.value)
            progressBar.progress = totalAttemps - countGuessing
        }

        tryButton.setOnClickListener {
            startAnimations()
            when {
                !isNumberGuessed -> numberGuessing()
                else -> reloadAll()
            }


        }
    }

    private fun numberGuessing() {
        val userInput = numberEditText.text.toString().toIntOrNull()
        if (userInput == null) {
            infoTextView.text = resources.getString(R.string.no_input)
            return
        }
        numberEditText.setText("")
        countGuessing++
        if (countGuessing >= totalAttemps) {
            showDialog(getString(R.string.lose))
            return
        }
        progressBar.progress = totalAttemps - countGuessing
        when {
            userInput < 1 -> infoTextView.text = resources.getString(R.string.error_less_than_1)
            userInput > 20 -> infoTextView.text = resources.getString(R.string.error_more_than_20)
            userInput == guessNumber -> winAction()
            userInput > guessNumber -> infoTextView.text = resources.getString(R.string.error_more)
            userInput < guessNumber -> infoTextView.text = resources.getString(R.string.error_less)
        }

    }

    private fun winAction() {
        showDialog(resources.getString(R.string.win))
        infoTextView.text = resources.getString(R.string.win)
        isNumberGuessed = true
    }

    private fun reloadAll() {
        guessNumber = randomInt(1, 20)
        infoTextView.text = resources.getString(R.string.info_text_view_init)
        tryButton.text = resources.getString(R.string.button_play_text)
        isNumberGuessed = false
        countGuessing = 0
        progressBar.progress = totalAttemps
    }

    private fun showDialog(title: String) {
        val builder = AlertDialog.Builder(this)

        builder.setTitle(title)
        builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
            reloadAll()
        }
        builder.setNegativeButton(getString(R.string.no)) { _, _ ->
            Toast.makeText(this, getString(R.string.end_game), Toast.LENGTH_SHORT).show()
            this.finishAffinity()
        }

        builder.create().show()
    }

    private fun startAnimations() {
        tryButton.startAnimation(AlphaAnimation(0.3f, 1.0f).apply { duration = 300; fillAfter = true })

        val animSet = AnimationSet(true)
        animSet.isFillEnabled = true
        animSet.addAnimation(TranslateAnimation(0f, -20f, 0f, 0f).apply { duration = 200; startOffset = 300 })
        animSet.addAnimation(TranslateAnimation(0f, 20f, 0f, 0f).apply { duration = 200 })
        animSet.addAnimation(ScaleAnimation(1.5f, 0.5f, 1f, 1f).apply { duration = 200; startOffset = 300 })
        animSet.addAnimation(ScaleAnimation(0.5f, 1.5f, 1f, 1f).apply { duration = 200 })

        infoTextView.startAnimation(animSet)
    }

    private fun randomInt(start: Int, end: Int): Int {
        return Random().nextInt(end) + start
    }


    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putInt(SavedInstanceMainActivityStateEnum.guessNumber.value, guessNumber)
        outState?.putInt(SavedInstanceMainActivityStateEnum.countGuessing.value, countGuessing)
        outState?.putBoolean(SavedInstanceMainActivityStateEnum.isNumberGuessed.value, isNumberGuessed)
        outState?.putString(SavedInstanceMainActivityStateEnum.buttonText.value, tryButton.text.toString())
        outState?.putString(SavedInstanceMainActivityStateEnum.infoTextView.value, infoTextView.text.toString())

    }

}
