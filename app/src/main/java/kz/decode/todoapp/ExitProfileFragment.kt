package kz.decode.todoapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import kz.decode.todoapp.databinding.FragmentExitProfileBinding

class ExitProfileFragment : BottomSheetDialogFragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentExitProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExitProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnCancelSignOut.setOnClickListener {
            dismiss()
        }

        binding.btnSignOut.setOnClickListener {
            dismiss()
            auth.signOut()
            requireActivity().finishAffinity()
        }
    }
}