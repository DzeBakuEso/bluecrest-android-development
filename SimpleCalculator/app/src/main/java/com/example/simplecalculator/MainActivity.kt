package com.example.simplecalculator

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

// Simple Calculator for a small business owner:
// two numbers, four operations (+ - x /), divide-by-zero check and a Clear button.
class MainActivity : AppCompatActivity() {

    private lateinit var etFirst: EditText
    private lateinit var etSecond: EditText
    private lateinit var tvResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Connect screen elements to the code
        etFirst = findViewById(R.id.etFirst)
        etSecond = findViewById(R.id.etSecond)
        tvResult = findViewById(R.id.tvResult)

        // Each operation button calls the same function with its own symbol
        findViewById<Button>(R.id.btnAdd).setOnClickListener { calculate("+") }
        findViewById<Button>(R.id.btnSubtract).setOnClickListener { calculate("-") }
        findViewById<Button>(R.id.btnMultiply).setOnClickListener { calculate("×") }
        findViewById<Button>(R.id.btnDivide).setOnClickListener { calculate("÷") }

        // Clear button: reset both inputs and the result
        findViewById<Button>(R.id.btnClear).setOnClickListener {
            etFirst.text.clear()
            etSecond.text.clear()
            tvResult.text = ""
            etFirst.requestFocus()
        }
    }

    private fun calculate(op: String) {
        val a = etFirst.text.toString().toDoubleOrNull()
        val b = etSecond.text.toString().toDoubleOrNull()

        // Both numbers must be entered
        if (a == null) {
            etFirst.error = "Enter the first number"
            return
        }
        if (b == null) {
            etSecond.error = "Enter the second number"
            return
        }

        // Divide by zero is not allowed
        if (op == "÷" && b == 0.0) {
            tvResult.setTextColor(Color.RED)
            tvResult.text = "Error: cannot divide by zero"
            Toast.makeText(this, "Cannot divide by zero", Toast.LENGTH_SHORT).show()
            return
        }

        // Perform the selected operation
        val result = when (op) {
            "+" -> a + b
            "-" -> a - b
            "×" -> a * b
            else -> a / b
        }

        // Display the result
        tvResult.setTextColor(Color.parseColor("#1B5E20"))
        tvResult.text = "Result: ${fmt(a)} $op ${fmt(b)} = ${fmt(result)}"
    }

    // Show 15 instead of 15.0, but keep decimals like 4.5 (up to 4 places)
    private fun fmt(x: Double): String {
        return if (x == Math.floor(x) && !x.isInfinite()) {
            x.toLong().toString()
        } else {
            "%.4f".format(x).trimEnd('0').trimEnd('.')
        }
    }
}
