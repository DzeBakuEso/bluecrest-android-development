package com.example.studentattendance

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

// One attendance record
data class Student(val name: String, val index: String, var present: Boolean)

// Student Attendance app: replaces the paper register.
class MainActivity : AppCompatActivity() {

    private val students = mutableListOf<Student>()      // all records
    private val displayLines = mutableListOf<String>()   // text shown in the list
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var tvSummary: TextView

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
        val etName = findViewById<EditText>(R.id.etName)
        val etIndex = findViewById<EditText>(R.id.etIndex)
        val rbPresent = findViewById<RadioButton>(R.id.rbPresent)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        val lvStudents = findViewById<ListView>(R.id.lvStudents)
        tvSummary = findViewById(R.id.tvSummary)

        // The adapter turns our list of text lines into rows in the ListView
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, displayLines)
        lvStudents.adapter = adapter

        // 4. Submit: record attendance
        btnSubmit.setOnClickListener {
            val name = etName.text.toString().trim()
            val index = etIndex.text.toString().trim()
            val present = rbPresent.isChecked

            // Check the inputs
            if (name.isEmpty()) {
                etName.error = "Enter the student's name"
                return@setOnClickListener
            }
            if (index.isEmpty()) {
                etIndex.error = "Enter the index number"
                return@setOnClickListener
            }

            // Same index number already recorded? Update it instead of adding a duplicate
            val existing = students.find { it.index.equals(index, ignoreCase = true) }
            if (existing != null) {
                existing.present = present
                Toast.makeText(this, "Updated $index", Toast.LENGTH_SHORT).show()
            } else {
                students.add(Student(name, index, present))
                Toast.makeText(this, "Recorded $name", Toast.LENGTH_SHORT).show()
            }

            refreshList()

            // Clear the form for the next student
            etName.text.clear()
            etIndex.text.clear()
            rbPresent.isChecked = true
            etName.requestFocus()
        }
    }

    // 5 and 6: rebuild the list and the "present" total
    private fun refreshList() {
        displayLines.clear()
        students.forEachIndexed { i, s ->
            val status = if (s.present) "PRESENT" else "ABSENT"
            displayLines.add("${i + 1}. ${s.name} (${s.index}) – $status")
        }
        adapter.notifyDataSetChanged()

        val presentCount = students.count { it.present }
        tvSummary.text = "Present: $presentCount of ${students.size} students"
    }
}
