package com.example.assignment.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.assignment.databinding.FragmentRegistrationBinding
import com.example.assignment.nnn.AuthViewModel
import com.example.assignment.nnn.Injection
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegistrationFragment : Fragment() {
    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        authViewModel = ViewModelProvider(
            this,
            Injection.provideViewModelFactory(requireContext())
        ).get(AuthViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userNameEditText: EditText = binding.editTextUsernameRegistration
        val passwordEditText: EditText = binding.editTextPasswordRegistration
        val passwordRepeatEditText: EditText = binding.editTextPasswordRepeatRegistration

        val registrationButton: Button = binding.buttonRegister

        registrationButton.setOnClickListener {
            val username = userNameEditText.text.toString()
            val password = passwordEditText.text.toString()
            val passwordRepeat = passwordRepeatEditText.text.toString()

            val passwordsMatch = validatePasswords(password, passwordRepeat)

            if (!passwordsMatch) {
                Toast.makeText(context, "Heslá sa nezhodujú alebo sú prázdne", Toast.LENGTH_SHORT).show()
            } else {
                CoroutineScope(Dispatchers.Main).launch {
                    try {
                        authViewModel.signup(username, password)

                        findNavController().navigate(
                            RegistrationFragmentDirections.actionRegistrationFragmentToBarsListFragment()
                        )
                    } catch (e: Exception) {
                        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun validatePasswords(password: String, passwordRepeat: String): Boolean {
        if (password.isEmpty() || passwordRepeat.isEmpty()) {
            return false
        } else if (password != passwordRepeat) {
            return false
        }
        return true
    }
}