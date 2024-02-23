package kz.decode.todoapp.main.screens.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.decode.todoapp.data.entity.Task
import kz.decode.todoapp.databinding.TaskItemBinding

class TaskAdapter(private var tasks: List<Task>, private val onTaskClicked: (Task) -> Unit) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    fun setItems(tasks : List<Task>) {
        this.tasks = tasks
        notifyDataSetChanged()
    }
    inner class TaskViewHolder(private val view: TaskItemBinding) :
        RecyclerView.ViewHolder(view.root) {
        fun bindTask(task: Task) {
            view.tvTaskTitle.text = task.name
            view.tvTaskDate.text = task.date
            view.root.setOnClickListener {
                onTaskClicked(task)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = TaskItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun getItemCount(): Int = tasks.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bindTask(task = tasks[position])
    }
}


