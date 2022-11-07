package com.example.assignment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.assignment.databinding.FragmentRegistrationBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegistrationFragment : Fragment() {
    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        val loginButton: Button =  binding.buttonLoginRegistration

        loginButton.setOnClickListener {
            findNavController().navigate(
                RegistrationFragmentDirections.actionRegistrationFragmentToLoginFragment()
            )
        }

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
                        AuthServer.register(username, password)
                    } catch (e: Exception) {
                        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

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