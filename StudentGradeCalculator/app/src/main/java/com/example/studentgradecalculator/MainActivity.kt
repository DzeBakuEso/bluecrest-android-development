package com.example.studentgradecalculator

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

// Student Grade Calculator
// Reads a student's name, CA marks and exam marks, then shows the total score and grade.
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 1. Connect the screen elements (from activity_main.xml) to the code
        val etName = findViewById<EditText>(R.id.etName)
        val etCa = findViewById<EditText>(R.id.etCa)
        val etExam = findViewById<EditText>(R.id.etExam)
        val btnCalculate = findViewById<Button>(R.id.btnCalculate)
        val tvResult = findViewById<TextView>(R.id.tvResult)

        // 2. What happens when the Calculate button is tapped
        btnCalculate.setOnClickListener {
            val name = etName.text.toString().trim()
            val ca = etCa.text.toString().toDoubleOrNull()      // null if empty or not a number
            val exam = etExam.text.toString().toDoubleOrNull()

            // 3. Check the inputs
            if (name.isEmpty()) {
                etName.error = "Please enter the student's name"
                return@setOnClickListener
            }
            if (ca == null) {
                etCa.error = "Enter continuous assessment marks"
                return@setOnClickListener
            }
            if (exam == null) {
                etExam.error = "Enter examination marks"
                return@setOnClickListener
            }
            if (ca < 0 || exam < 0) {
                tvResult.text = "Marks cannot be negative."
                return@setOnClickListener
            }

            // 4. Calculate the total score
            val total = ca + exam
            if (total > 100) {
                tvResult.text = "Total cannot be more than 100. Please check the marks."
                return@setOnClickListener
            }

            // 5. Work out the grade
            val grade = getGrade(total)

            // 6. Display name, total score and grade
            tvResult.text = "Name: $name\n" +
                    "Total Score: ${"%.1f".format(total)}\n" +
                    "Grade: $grade"
        }
    }

    // Grading system: 80-100 A, 70-79 B, 60-69 C, 50-59 D, below 50 F
    private fun getGrade(total: Double): String {
        return when {
            total >= 80 -> "A"
            total >= 70 -> "B"
            total >= 60 -> "C"
            total >= 50 -> "D"
            else -> "F"
        }
    }
}
