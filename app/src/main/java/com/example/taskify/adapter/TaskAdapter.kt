package com.example.taskify.adapter

import com.example.taskify.dataBase.Task
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.taskify.R

class TaskAdapter(
    private var taskList: List<Task>,
    private val onEditClick: (Task) -> Unit,
    private val onCompleteClick: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleText: TextView = itemView.findViewById(R.id.taskTitle)
        val descriptionText: TextView = itemView.findViewById(R.id.taskDescription)
        val btnEdit: ImageButton = itemView.findViewById(R.id.btnEdit)
        val btnComplete: ImageButton = itemView.findViewById(R.id.btnComplete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]
        holder.titleText.text = task.title
        holder.descriptionText.text = task.description

        holder.btnEdit.setOnClickListener { onEditClick(task) }
        holder.btnComplete.setOnClickListener { onCompleteClick(task) }
    }

    override fun getItemCount(): Int = taskList.size

    // To update list later
    fun updateList(newList: List<Task>) {
        taskList = newList
        notifyDataSetChanged()
    }

    fun getCurrentList(): List<Task> {
        return taskList
    }
}
