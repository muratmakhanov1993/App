package kz.decode.todoapp.main.screens.tasks.bottom_sheet

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kz.decode.todoapp.data.entity.State
import kz.decode.todoapp.data.entity.Task
import kz.decode.todoapp.data.entity.TaskStatus
import kz.decode.todoapp.main.screens.tasks.TaskFilter

class TasksViewModel : ViewModel() {
    private var taskFilters = mutableListOf<TaskFilter>()
    private var tasksFromFirestore = mutableListOf<Task>()
    private val _isLoading = MutableLiveData<Boolean>()

    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _state = MutableLiveData<State>()
    val state: LiveData<State>
        get() = _state

    fun initPage() {
        viewModelScope.launch {
            tasksFromFirestore.clear()
            _state.value = State(taskFilters, null)
            _isLoading.value = true
            if (taskFilters.isEmpty()) {
                firestore.collection(FILTERS_COLLECTION)
                    .get().addOnSuccessListener { snapshot ->
                        snapshot.documents.forEach() { documentSnapshot ->
                            val status =
                                TaskStatus.valueOf((documentSnapshot.get("status") as String))
                            val taskFilter = TaskFilter(
                                id = documentSnapshot.get("id") as Long,
                                filterName = documentSnapshot.get("filterName") as String,
                                isSelected = status == TaskStatus.ALL,
                                filterId = documentSnapshot.id,
                                status = status
                            )

                            if (taskFilters.isEmpty()) {
                                taskFilters.add(
                                    taskFilter
                                )
                            } else {
                                if (taskFilters.find { it.filterId == documentSnapshot.id } == null)
                                    taskFilters.add(
                                        taskFilter
                                    )
                            }
                        }
                    }.addOnFailureListener { exception ->
                        _isLoading.value = false
                        Log.d("FILTERS_EXCEPTION", exception.message.toString())
                    }
            }
        }

        val myUser =
            firestore.collection(USER_COLLECTION).document(auth.currentUser?.email.toString())
        myUser.get().addOnSuccessListener { documentSnapshot ->
            val tasks = (documentSnapshot.data?.get("tasks") as List<String>)

            if (tasks.isEmpty()) {
                _state.value = State(
                    filters = taskFilters.sortedBy { it.id },
                    tasks = emptyList()
                )
            }

            tasks.forEach { taskId ->
                val documentSelection = firestore.collection(TASK_COLLECTION).document(taskId)
                    .get().addOnSuccessListener { doc ->
                        val task = Task(
                            taskId = taskId,
                            name = doc.get("name").toString(),
                            date = doc.get("date").toString(),
                            description = doc.get("description").toString(),
                            status = TaskStatus.valueOf(doc.get("status").toString())
                        )
                        tasksFromFirestore.add(task)
                    }
                documentSelection.addOnSuccessListener {
                    _state.value = State(
                        filters = taskFilters.sortedBy { it.id },
                        tasks = tasksFromFirestore.ifEmpty { emptyList() }.filter {
                            if (taskFilters.find { it.isSelected }?.status == TaskStatus.ALL) {
                                true
                            } else {
                                it.status == taskFilters.find { it.isSelected }?.status
                            }
                        }
                    )
                }

                    .addOnFailureListener { exception ->
                        _isLoading.value = false
                        Log.d("TASKS_EXCEPTION", exception.message.toString())
                    }
                _isLoading.value = false
            }
        }
    }

    fun changeTaskStatus(taskId: String, taskStatus: TaskStatus) {
        viewModelScope.launch {
            _isLoading.value = true
            val taskById = firestore.collection(TASK_COLLECTION).document(taskId)
            val status =
                if (taskStatus == TaskStatus.DONE) TaskStatus.DONE.status else TaskStatus.TO_DO.status
            taskById.update("status", status)
                .addOnSuccessListener {


                    _isLoading.value = false
                }
                .addOnFailureListener { exception ->

                    _isLoading.value = false
                }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun deleteTask(taskId: String) {
        viewModelScope.launch {
            _isLoading.value = true

            val myUser =
                firestore.collection(USER_COLLECTION)
                    .document(auth.currentUser?.email.toString())
            myUser.get().addOnSuccessListener { documentSnapshot ->
                val tasks = (documentSnapshot.data?.get("tasks") as MutableList<String>)

                if (tasks.contains(taskId)) {
                    tasks.remove(taskId)
                    myUser.update("tasks", tasks)
                        .addOnSuccessListener {
                            val tasksCollection = firestore.collection(TASK_COLLECTION)

                            tasksCollection.document(taskId)
                                .delete()
                                .addOnSuccessListener {
                                    _isLoading.value = false
                                    tasksFromFirestore.removeIf {
                                        it.taskId == taskId
                                    }
//                                    _state.value = State(
//                                       filters = emptyList(),
//                                       tasks = emptyList()
//                                    )

                                    initPage()
                                }
                                .addOnFailureListener { e ->
                                    Log.d("Dias", e.message.toString())
                                    _isLoading.value = false
                                }
                        }
                }
            }


        }
    }

    fun filterTasks(text: String) {
        _state.value = State(
            filters = taskFilters,
            tasks = tasksFromFirestore.filter {
                it.name.contains(text)
            }
        )
    }

    fun changeFilter(filter: TaskFilter) {
        taskFilters = taskFilters.map { taskFilter ->
            taskFilter.copy(isSelected = taskFilter.id == filter.id)
        }.toMutableList()

        _state.value = State(
            filters = taskFilters.sortedBy { it.id },
            tasks = tasksFromFirestore.filter { task ->
                if (filter.status == TaskStatus.ALL) true
                else task.status == filter.status
            }
        )
    }

    companion object {
        const val FILTERS_COLLECTION = "filters"
        const val TASK_COLLECTION = "tasks"
        const val USER_COLLECTION = "users"
    }
}
