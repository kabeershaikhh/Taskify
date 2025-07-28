package com.example.taskify.dataBase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    var title: String,
    var description: String,

    var isCompleted: Boolean = false
)
