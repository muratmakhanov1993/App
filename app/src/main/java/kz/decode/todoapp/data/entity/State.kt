package kz.decode.todoapp.data.entity

import kz.decode.todoapp.main.screens.tasks.TaskFilter

data class State(
    val filters: List<TaskFilter>,
    val tasks: List<Task>?
)
