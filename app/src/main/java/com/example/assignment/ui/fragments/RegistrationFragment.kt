package com.example.assignment.ui.fragments

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
import com.example.assignment.ui.viewmodels.AuthViewModel
import com.example.assignment.common.Injection
import com.example.assignment.common.PasswordHashService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.BigInteger
import java.security.MessageDigest

class RegistrationFragment : Fragment() {
    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    private lateinit var authViewModel: AuthViewModel

    private val passwordHashService = PasswordHashService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        authViewModel = ViewModelProvider(
            this,
            Injection.provideViewModelFactory(requireContext())
        )[AuthViewModel::class.java]
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
                        val hashedPassword = passwordHashService.getSHA512(password)
                        authViewModel.signup(username, hashedPassword)

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