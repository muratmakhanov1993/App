package kz.decode.todoapp.main.screens.tasks.bottom_sheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import kz.decode.todoapp.databinding.FragmentTasksBinding
import kz.decode.todoapp.main.screens.tasks.TaskAdapter
import kz.decode.todoapp.main.screens.tasks.TaskFilterAdapter

class TasksFragment : Fragment() {

    private val tasksViewModel: TasksViewModel by activityViewModels()
    private lateinit var taskFilterAdapter: TaskFilterAdapter
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var binding: FragmentTasksBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        tasksViewModel.initPage()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTasksBinding.inflate(layoutInflater)
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        initObservers()
    }

    private fun setupViews() {
        binding.search.doOnTextChanged { text, start, before, count ->
            Log.d("dddd",text.toString())
            tasksViewModel.filterTasks(text.toString())
        }

        binding.addTask.setOnClickListener{
            findNavController().navigate(TasksFragmentDirections.actionTasksFragmentToNewTasksFragment())
        }

        taskFilterAdapter = TaskFilterAdapter(emptyList()) { taskFilter ->
            tasksViewModel.changeFilter(taskFilter)
        }
        binding.filters.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.filters.adapter = taskFilterAdapter


        taskAdapter = TaskAdapter(emptyList()) { task ->
            //tasksViewModel.deleteTask(task)
            findNavController().navigate(TasksFragmentDirections.actionTasksFragmentToChangeStatusFragment(task.taskId))

        }
        binding.tasks.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.tasks.adapter = taskAdapter

    }

    private fun initObservers() {
        tasksViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.loaderContent.visibility = View.VISIBLE
            } else {
                binding.loaderContent.visibility = View.GONE
            }
        }

        tasksViewModel.state.observe(viewLifecycleOwner) { state ->
            taskAdapter.setItems(state.tasks.orEmpty())
            taskFilterAdapter.setItems(state.filters.orEmpty())
        }
    }

}