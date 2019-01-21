package com.calculator3.isysoi.calculator3.length

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.calculator3.isysoi.calculator3.R
import com.calculator3.isysoi.calculator3.SPEED_TAG
import com.calculator3.isysoi.calculator3.speed.SpeedFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

class LengthFragment: Fragment(), View.OnClickListener, AdapterView.OnItemSelectedListener {

    private enum class SavedInstanceLengthFragmentStateEnum(val value: String) {
        isCurrentValuesIsLengths("IS_CURRENT_VALUES_LENGTHS_MAIN_ACTIVITY"),
        isDoubleMode("IS_DOUBLE_MODE_MAIN_ACTIVITY"),
        inputValueTextView("INPUT_VALUE_TEXT_VIEW_MAIN_ACTIVITY"),
        resultTextView("RESULT_TEXT_VIEW_MAIN_ACTIVITY"),
        currentFromValue("CURRENT_FROM_VALUE_MAIN_ACTIVITY"),
        currentToValue("CURRENT_TO_VALUE_MAIN_ACTIVITY")
    }


    private var defaulValue = "METER"

    private var currentFromValue: String? = "METER"
    private var currentToValue: String? = "METER"
    private var isDoubleMode = false
    private var currentDictToConvert = LengthData.lengths
    private lateinit var activityView: View


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        activityView = inflater.inflate(R.layout.activity_main, container, false)

        addValuesToSpiner()
        addListnerToCalcButtons()

        return activityView
    }

    private fun addValuesToSpiner() {
        val spinnerArrayAdapter = ArrayAdapter<String>(
                activity!!, android.R.layout.simple_spinner_item, currentDictToConvert.keys.toTypedArray())

        val indexFrom = currentDictToConvert.keys.toTypedArray().indexOf(if (currentFromValue == null)  defaulValue else currentFromValue)
        val indexTo = currentDictToConvert.keys.toTypedArray().indexOf(if (currentToValue == null)  defaulValue else currentToValue)
        activityView.fromSpinner.setSelection(indexFrom)
        activityView.toSpinner.setSelection(indexTo)

        activityView.fromSpinner.adapter = spinnerArrayAdapter
        activityView.toSpinner.adapter = spinnerArrayAdapter

        activityView.fromSpinner.onItemSelectedListener = this
        activityView.toSpinner.onItemSelectedListener = this
    }

    private fun addListnerToCalcButtons() {
        activityView.calc_button0.setOnClickListener(this)
        activityView.calc_button1.setOnClickListener(this)
        activityView.calc_button2.setOnClickListener(this)
        activityView.calc_button3.setOnClickListener(this)
        activityView.calc_button4.setOnClickListener(this)
        activityView.calc_button5.setOnClickListener(this)
        activityView.calc_button6.setOnClickListener(this)
        activityView.calc_button7.setOnClickListener(this)
        activityView.calc_button8.setOnClickListener(this)
        activityView.calc_button9.setOnClickListener(this)
        activityView.calc_button_double.setOnClickListener(this)
        activityView.calc_button_erase.setOnClickListener(this)
        activityView.swap_convert_values_button.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        var value = ""
        when (v.id) {
            activityView.calc_button0.id -> {
                value = "0"
            }
            activityView.calc_button1.id -> {
                value = "1"
            }
            activityView.calc_button2.id -> {
                value = "2"
            }
            activityView.calc_button3.id -> {
                value = "3"
            }
            activityView.calc_button4.id -> {
                value = "4"
            }
            activityView.calc_button5.id -> {
                value = "5"
            }
            activityView.calc_button6.id -> {
                value = "6"
            }
            activityView.calc_button7.id -> {
                value = "7"
            }
            activityView.calc_button8.id -> {
                value = "8"
            }
            activityView.calc_button9.id -> {
                value = "9"
            }
            activityView.calc_button_double.id -> {
                value = ""
                if (isDoubleMode) {
                    return
                }
                isDoubleMode = true
            }
            activityView.calc_button_erase.id -> {
                activityView.inputValueTextView.text = ""
                activityView.resultTextView.text = ""
                isDoubleMode = false
                return
            }
            activityView.swap_convert_values_button.id -> {
                showSwitchDialog()

                return
            }
        }
        var result = activityView.inputValueTextView.text.toString()
        if (value == "") {
            activityView.inputValueTextView.text = (result + resources.getString(R.string.numbers_double_separator))
        } else {
            activityView.inputValueTextView.text = (result + value)
        }
        recountValues()
    }

    private fun showSwitchDialog() {
        val builder = AlertDialog.Builder(activity!!)

        builder.setTitle(R.string.switch_converter)
        builder.setPositiveButton(R.string.ok) { _, _ ->
            fragmentManager!!.beginTransaction()
                    .setCustomAnimations(R.anim.slide_to_down, R.anim.slide_to_up, R.anim.slide_back_from_up, R.anim.slide_back_from_down)
                    .replace(R.id.calculator_container, SpeedFragment(), SPEED_TAG)
                    .addToBackStack(null)
                    .commit()
        }

        builder.create().show()

    }

    private fun recountValues() {
        val fromRate = currentDictToConvert[currentFromValue]
        val toRate = currentDictToConvert[currentToValue]
        val inputNumber = activityView.inputValueTextView.text.toString().toDoubleOrNull()
        if (inputNumber != null) {
            val output = (inputNumber / fromRate!!) * toRate!!
            activityView.resultTextView.text = String.format("%.3f", output)
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        currentFromValue = fromSpinner.selectedItem.toString()
        currentToValue = toSpinner.selectedItem.toString()
        recountValues()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {}

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            isDoubleMode = savedInstanceState.getBoolean(SavedInstanceLengthFragmentStateEnum.isDoubleMode.value)
            activityView.inputValueTextView.text = savedInstanceState.getString(SavedInstanceLengthFragmentStateEnum.inputValueTextView.value)
            activityView.resultTextView.text = savedInstanceState.getString(SavedInstanceLengthFragmentStateEnum.resultTextView.value)
            currentFromValue = savedInstanceState.getString(SavedInstanceLengthFragmentStateEnum.currentFromValue.value)
            currentToValue = savedInstanceState.getString(SavedInstanceLengthFragmentStateEnum.currentToValue.value)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(SavedInstanceLengthFragmentStateEnum.isDoubleMode.value, isDoubleMode)
        outState.putString(SavedInstanceLengthFragmentStateEnum.inputValueTextView.value, activityView.inputValueTextView.text.toString())
        outState.putString(SavedInstanceLengthFragmentStateEnum.resultTextView.value, activityView.resultTextView.text.toString())
        outState.putString(SavedInstanceLengthFragmentStateEnum.currentFromValue.value, currentFromValue)
        outState.putString(SavedInstanceLengthFragmentStateEnum.currentToValue.value, currentToValue)
    }

}