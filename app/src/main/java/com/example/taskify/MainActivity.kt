package com.example.taskify

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskify.adapter.TaskAdapter
import com.example.taskify.dataBase.Task
import com.example.taskify.viewModel.TaskViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TaskAdapter
    private val taskViewModel: TaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.recyclerViewTasks)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = TaskAdapter(
            taskList = emptyList(),
            onEditClick = { task -> showTaskBottomSheet(task) },
            onCompleteClick = { task ->
                // Optional: You could mark it completed instead of deleting
                taskViewModel.deleteTask(task)
            }
        )

        recyclerView.adapter = adapter

        val noTasksText = findViewById<TextView>(R.id.emptyTextView)
        // Observe LiveData from ViewModel
        taskViewModel.allTasks.observe(this, Observer { tasks ->
            val activeTasks = tasks.filter { !it.isCompleted }
            adapter.updateList(activeTasks)

            if (activeTasks.isEmpty()) {
                noTasksText.visibility = View.VISIBLE
            } else {
                noTasksText.visibility = View.GONE
            }
        })

        findViewById<FloatingActionButton>(R.id.fab_add_task).setOnClickListener {
            showTaskBottomSheet(null)
        }
    }

    private fun showTaskBottomSheet(taskToEdit: Task?) {
        val dialog = BottomSheetDialog(this)
        val view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_add_edit_task, null)
        dialog.setContentView(view)

        val titleInput = view.findViewById<EditText>(R.id.editTextTitle)
        val descInput = view.findViewById<EditText>(R.id.editTextDescription)
        val saveButton = view.findViewById<Button>(R.id.btnSave)

        if (taskToEdit != null) {
            titleInput.setText(taskToEdit.title)
            descInput.setText(taskToEdit.description)
            saveButton.text = "Update Task"
        }

        saveButton.setOnClickListener {
            val title = titleInput.text.toString().trim()
            val desc = descInput.text.toString().trim()

            if (title.isEmpty() || desc.isEmpty()) {
                Toast.makeText(this, "Both fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (taskToEdit != null) {
                val updatedTask = taskToEdit.copy(title = title, description = desc)
                taskViewModel.updateTask(updatedTask)
            } else {
                val newTask = Task(title = title, description = desc)
                taskViewModel.addTask(newTask)
            }

            dialog.dismiss()
        }

        dialog.show()
    }
}
