package kz.decode.todoapp.data.entity

data class Task(
    val taskId:String,
    val date: String,
    val description: String,
    val name: String,
    val status: TaskStatus
)