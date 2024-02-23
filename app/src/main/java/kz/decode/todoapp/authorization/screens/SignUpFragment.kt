package kz.decode.todoapp.authorization.screens

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kz.decode.todoapp.databinding.FragmentSignUpBinding
import kz.decode.todoapp.main.MainActivity

class SignUpFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var binding: FragmentSignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnRegis.setOnClickListener {
            val email = binding.edEmail.text.toString()
            val password = binding.edPass.text.toString()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val user = hashMapOf(
                            "email" to email,
                            "password" to password,
                            "tasks" to emptyList<String>()
                        )
                        firestore.collection("users").document(email)
                            .set(user)
                            .addOnCompleteListener {
                                findNavController().navigate(
                                    SignUpFragmentDirections.actionSignUpFragmentToSignInFragment()
                                )
                            }
                            .addOnFailureListener {
                                findNavController().navigate(
                                    SignUpFragmentDirections.actionSignUpFragmentToSignInFragment()
                                )
                            }

                    } else {
                        Log.d("TAG", "createUserWithEmail:failure", it.exception)
                        Toast.makeText(
                            requireContext(),
                            "Authentication failed",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
        }
        binding.tvSignIn.setOnClickListener {
            findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToSignUpFragment())
        }
    }

    companion object {
        const val USER_NAME = "USER_NAME"
        const val USER_EMAIL = "USER_EMAIL"
        const val USER_PASSWORD = "USER_PASSWORD"

    }
}



