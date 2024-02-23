package kz.decode.todoapp.main.screens.profile

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.FirebaseFirestore
import kz.decode.todoapp.R


class EditProfileFragment: Fragment() {
    private lateinit var firestore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firestore = FirebaseFirestore.getInstance()
    }

     override fun onCreateView(
         inflater: LayoutInflater, container: ViewGroup?,
         savedInstanceState: Bundle?
     ): View {
         return inflater.inflate(R.layout.fragment_edit_profile, container, false)
     }

     override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
         super.onViewCreated(view, savedInstanceState)
         val task = hashMapOf(
             "date" to  System.currentTimeMillis(),
             "iconUrl" to "",
             "name" to "Murat",
             "status" to "",
             )
//         firestore.collection("tasks")
//             .add(task)
//             .addOnSuccessListener { documentReference ->
//
//             }
//             .addOnFailureListener { e ->
//                 Log.w(TAG, "Error adding document", e)
//             }

         firestore.collection("tasks")
             .get()
             .addOnSuccessListener { result ->
                 for (document in result) {
                     Log.d("Murat one", "${document.id} => ${document.data}")
                 }
             }
             .addOnFailureListener { exception ->
                 Log.w(TAG, "Error getting documents.", exception)
             }

     }
 }


