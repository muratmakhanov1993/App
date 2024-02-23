package kz.decode.todoapp.main.screens.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.decode.todoapp.data.entity.TaskStatus
import kz.decode.todoapp.databinding.FilterItemBinding

data class TaskFilter(
    val id: Long,
    val filterId: String,
    val filterName: String,
    val isSelected: Boolean,
    val status: TaskStatus,
)

class TaskFilterAdapter(private var filters : List<TaskFilter>, private val onFilterClicked: (TaskFilter) -> Unit) : RecyclerView.Adapter<TaskFilterAdapter.FilterViewHolder>() {
    fun setItems(filters : List<TaskFilter>) {
        this.filters = filters
        notifyDataSetChanged()
    }

    inner class FilterViewHolder(private val view: FilterItemBinding) : RecyclerView.ViewHolder(view.root) {
        fun bindFilter(filter: TaskFilter) {
            view.filter.text = filter.filterName
            view.filter.isSelected = filter.isSelected
            view.filter.setOnClickListener { onFilterClicked(filter)
            }


        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        val binding = FilterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilterViewHolder(binding)
    }

    override fun getItemCount(): Int = filters.size

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        holder.bindFilter(filter = filters[position])
    }

}