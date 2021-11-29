package com.example.mycalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import java.lang.RuntimeException
import java.util.*
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {
    // Represent whether the lastly pressed key is numeric or not
    var lastNumeric: Boolean = false

    // If true, do not allow to add another DOT
    var lastDot: Boolean = false

    var parenStack = ArrayDeque<CharSequence>()

    private var tvInput: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvInput = findViewById(R.id.tvInput)
    }

    fun onDigit(view: View) {
        tvInput?.append((view as Button).text)
        lastNumeric = true
    }

    fun onClear(view: View) {
        tvInput?.text = ""
        lastNumeric = false
        lastDot = false
    }

    /**
     * Append . to the TextView
     */
    fun onDecimalPoint(view: View) {

        // If the last appended value is numeric then append(".") or don't.
        if (lastNumeric && !lastDot) {
            tvInput?.append(".")
            lastNumeric = false // Update the flag
            lastDot = true // Update the flag
        }
    }

    /**
     * Append +,-,*,/ operators to the TextView as per the Button.Text
     */
    fun onOperator(view: View) {
        tvInput?.append((view as Button).text)
    }

    /**
     * Append ( operator to the TextView as per the Button.Text
     */
    fun onLeftParen(view: View) {
        tvInput?.append((view as Button).text)
        lastNumeric = true
    }

    /**
     * Append ( operator to the TextView as per the Button.Text
     */
    fun onRightParen(view: View) {
        tvInput?.append((view as Button).text)
        lastNumeric = true
    }

    fun onSquared(view: View) {
        tvInput?.append((view as Button).text)
        lastNumeric = true
    }

    fun onCubed(view: View) {
        tvInput?.append((view as Button).text)
        lastNumeric = true
    }

    fun onExponent(view: View) {
        tvInput?.append((view as Button).text)
        lastNumeric = true
    }

    fun onSqrt(view: View) {
        tvInput?.append((view as Button).text)
        lastNumeric = true
    }
    fun onSin(view: View) {
        tvInput?.append((view as Button).text)
        lastNumeric = true
    }
    fun onCos(view: View) {
        tvInput?.append((view as Button).text)
        lastNumeric = true
    }
    fun onTan(view: View) {
        tvInput?.append((view as Button).text)
        lastNumeric = true
    }

    /**
     * Calculate the output
     */
    fun onEqual(view: View) {
        // If the last input is a number only, solution can be found.
        if (lastNumeric) {
            // Read the textView value
            var tvValue = tvInput?.text.toString()
            var tvCalc: Double = eval(tvValue)
            tvInput?.text =
                removeZeroAfterDot(tvCalc.toString())
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

    /**
     * It is used to check whether any of the operator is used or not.
     */
    private fun isOperatorAdded(value: String): Boolean {

        /**
         * Here first we will check that if the value starts with "-" then will ignore it.
         * As it is the result value and perform further calculation.
         */

        return if (value.startsWith("-")) {
            false
        } else {
            (value.contains("/")
                    || value.contains("*")
                    || value.contains("-")
                    || value.contains("+"))
        }
    }

    fun eval(str: String): Double {
        return object : Any() {
            var pos = -1
            var ch = 0
            fun nextChar() {
                ch = if (++pos < str.length) str[pos].toInt() else -1
            }

            fun eat(charToEat: Int): Boolean {
                while (ch == ' '.toInt()) nextChar()
                if (ch == charToEat) {
                    nextChar()
                    return true
                }
                return false
            }

            fun parse(): Double {
                nextChar()
                val x = parseExpression()
                if (pos < str.length) throw RuntimeException("Unexpected: " + ch.toChar())
                return x
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            //        | number | functionName factor | factor `^` factor
            fun parseExpression(): Double {
                var x = parseTerm()
                while (true) {
                    if (eat('+'.toInt())) x += parseTerm() // addition
                    else if (eat('-'.toInt())) x -= parseTerm() // subtraction
                    else return x
                }
            }

            fun parseTerm(): Double {
                var x = parseFactor()
                while (true) {
                    if (eat('*'.toInt())) x *= parseFactor() // multiplication
                    else if (eat('/'.toInt())) x /= parseFactor() // division
                    else return x
                }
            }

            fun parseFactor(): Double {
                if (eat('+'.toInt())) return parseFactor() // unary plus
                if (eat('-'.toInt())) return -parseFactor() // unary minus
                var x: Double
                val startPos = pos
                if (eat('('.toInt())) { // parentheses
                    x = parseExpression()
                    eat(')'.toInt())
                } else if (ch >= '0'.toInt() && ch <= '9'.toInt() || ch == '.'.toInt()) { // numbers
                    while (ch >= '0'.toInt() && ch <= '9'.toInt() || ch == '.'.toInt()) nextChar()
                    x = str.substring(startPos, pos).toDouble()
                } else if (ch >= 'a'.toInt() && ch <= 'z'.toInt()) { // functions
                    while (ch >= 'a'.toInt() && ch <= 'z'.toInt()) nextChar()
                    val func = str.substring(startPos, pos)
                    x = parseFactor()
                    x =
                        if (func == "sqrt") Math.sqrt(x) else if (func == "sin") Math.sin(
                            Math.toRadians(
                                x
                            )
                        ) else if (func == "cos") Math.cos(
                            Math.toRadians(x)
                        ) else if (func == "tan") Math.tan(Math.toRadians(x)) else throw RuntimeException(
                            "Unknown function: $func"
                        )
                } else {
                    throw RuntimeException("Unexpected: " + ch.toChar())
                }
                if (eat('^'.toInt())) x = Math.pow(x, parseFactor()) // exponentiation
                return x
            }
        }.parse()
    }
}
