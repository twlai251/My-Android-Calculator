package com.example.mycalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.lang.ArithmeticException

class MainActivity : AppCompatActivity() {


    private var tvInput: TextView? = null
    private var btnOne: Button? = null

    // If true, do not allow to add another DOT
    var lastDot: Boolean = false

    // Represent whether the lastly pressed key is numeric or not
    var lastNumeric: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // set display as default
        tvInput = findViewById(R.id.tvInput)


    }


    //   Button Reader
    fun onDigit(view: View) {
        tvInput?.append((view as Button).text)
        lastNumeric = true

    }

    //    Clear button
    fun onClear(view: View){
        tvInput?.text = ""
    }

    //    Decimal Point
    fun onDec(view: View) {

        // If the last appended value is numeric then append(".") or don't.
        if (lastNumeric && !lastDot) {
            tvInput?.append(".")
            lastNumeric = false // Update the flag
            lastDot = true // Update the flag
        }

    }
    //     Adding Operators
    fun onOperator(view: View) {
        tvInput?.text?.let {
            if (lastNumeric && !isOperatorAdded(it.toString())) {
                tvInput?.append((view as Button).text)
                lastNumeric = false     // Update the flag
                lastDot = false         //Reset the DOT flag
            }
        }
    }

    private fun isOperatorAdded(value: String): Boolean {
        return if (value.startsWith("-")) { //-36
            false
        } else {
            value.contains("/")
                    || value.contains("*")
                    || value.contains("+")
                    || value.contains("-")
        }

    }

    /**
     * Remove the zero after decimal point
     */
    private fun removeZeroAfterDot(result: String): String {

        var value = result

        if (result.contains(".0")) {
            value = result.substring(0, result.length - 2)
        }

        return value
    }

    //    Equals
    fun onEqual(view: View){
        if (lastNumeric){
            var tvValue = tvInput?.text.toString()
            var prefix = ""

            try {

                if(tvValue.startsWith("-")){
                    prefix = "-"
                    tvValue = tvValue.substring(1)
                    val splitValue = tvValue.split("-")     //[99, 1]
                    var one = splitValue[0]
                    var two = splitValue[1]

                    if(prefix.isNotEmpty()){
                        one = prefix + one
                    }

                    tvInput?.text = (one.toDouble() - two.toDouble()).toString()
                }
                else if(tvValue.contains("/")){
                    val splitValue = tvValue.split("/")

                    var one = splitValue[0] // Value One
                    val two = splitValue[1] // Value Two
                    tvInput?.text = removeZeroAfterDot((one.toDouble() / two.toDouble()).toString())
                }
                else if(tvValue.contains("*")){
                    val splitValue = tvValue.split("*")

                    var one = splitValue[0] // Value One
                    val two = splitValue[1] // Value Two

                    tvInput?.text = removeZeroAfterDot((one.toDouble() * two.toDouble()).toString())

                }
                else if(tvValue.contains("+")){
                    val splitValue = tvValue.split("+")

                    var one = splitValue[0] // Value One
                    val two = splitValue[1] // Value Two

                    tvInput?.text = removeZeroAfterDot((one.toDouble() + two.toDouble()).toString())

                }
                else {
                    val splitValue = tvValue.split("-")

                    var one = splitValue[0] // Value One
                    val two = splitValue[1] // Value Two


                    tvInput?.text = (one.toDouble() - two.toDouble()).toString()

                }


            }
            catch (e:ArithmeticException){
                e.printStackTrace()
            }


        }


    }


}