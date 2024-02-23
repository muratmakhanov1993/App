package kz.decode.todoapp

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kz.decode.todoapp.data.entity.TaskStatus
import kz.decode.todoapp.databinding.FragmentChangeStatusBinding
import kz.decode.todoapp.main.screens.tasks.bottom_sheet.TasksViewModel

class ChangeStatusFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentChangeStatusBinding
    private lateinit var taskId: String
    private val tasksViewModel: TasksViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        taskId = arguments?.getString("taskId", "").toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangeStatusBinding.inflate(layoutInflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()

    }
    @RequiresApi(Build.VERSION_CODES.N)
    private fun setupViews() {
        binding.btnDeleteTask.setOnClickListener {
            dismiss()
            tasksViewModel.deleteTask(taskId)
        }
        binding.btnDone.setOnClickListener {
            tasksViewModel.changeTaskStatus(taskId, TaskStatus.DONE)
        }
        binding.btnToDo.setOnClickListener {
            tasksViewModel.changeTaskStatus(taskId, TaskStatus.TO_DO)
        }
    }

}




