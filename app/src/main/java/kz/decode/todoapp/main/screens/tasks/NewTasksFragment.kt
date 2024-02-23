package kz.decode.todoapp.main.screens.tasks

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.SuccessContinuation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kz.decode.todoapp.R
import kz.decode.todoapp.data.entity.TaskStatus
import kz.decode.todoapp.data.entity.User
import kz.decode.todoapp.databinding.FragmentNewTasksBinding

class NewTasksFragment : Fragment() {
    private lateinit var binding: FragmentNewTasksBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewTasksBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnAddTask.setOnClickListener {
            val taskName = binding.etName.text.toString()
            val taskDescription = binding.etDescription.text.toString()
            val taskDate = binding.etDate.text.toString()
            val newTask = hashMapOf(
                "name" to taskName,
                "description" to taskDescription,
                "date" to taskDate,
                "status" to TaskStatus.TO_DO.name
            )

            firestore.collection("tasks").add(newTask).addOnSuccessListener { document ->
                val myUser =
                    firestore.collection("users").document(auth.currentUser?.email.toString())
                myUser.get().addOnSuccessListener { documentSnapshot ->
                    val tasks = (documentSnapshot.data?.get("tasks") as List<String>).orEmpty()

                    myUser.update("tasks", tasks + listOf(document.id)).addOnSuccessListener {
                        findNavController().navigate(NewTasksFragmentDirections.actionNewTasksFragmentToTasksFragment())
                    }

                }
            }


        }
    }
}