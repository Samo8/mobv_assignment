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
import com.example.assignment.R
import com.example.assignment.common.Injection
import com.example.assignment.common.PasswordHashService
import com.example.assignment.common.PreferenceData
import com.example.assignment.databinding.FragmentLoginBinding
import com.example.assignment.ui.viewmodels.AuthViewModel

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
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
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val x = PreferenceData.getInstance().getUserItem(requireContext())
        println(x)
        if ((x?.uid ?: "").isNotBlank()) {
            if (x!!.uid != "-1") {
                findNavController().navigate(
                    LoginFragmentDirections.actionLoginFragmentToBarsListFragment()
                )
            }
            return
        }

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            model = authViewModel
        }

        val userNameEditText: EditText = binding.editTextUsernameLogin
        val passwordEditText: EditText = binding.editTextPasswordLogin
        val loginButton: Button = binding.buttonLogin
        val buttonGoToRegistration: Button = binding.buttonGoToRegistration

        buttonGoToRegistration.setOnClickListener {
            findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToRegistrationFragment()
            )
        }

        loginButton.setOnClickListener {
            val username = userNameEditText.text.toString()
            val password = passwordEditText.text.toString()

            val isInputValid = validateInputData(username, password)

            if (!isInputValid) {
                Toast.makeText(
                    context,
                    getString(R.string.user_name_or_password_empty),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val hashedPassword = passwordHashService.getSHA512(password)
                authViewModel.login(username, hashedPassword)
            }
        }

        authViewModel.message.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }

        authViewModel.user.observe(viewLifecycleOwner){
            it?.let {
                PreferenceData.getInstance().putUserItem(requireContext(), it)
                findNavController().navigate(
                    LoginFragmentDirections.actionLoginFragmentToBarsListFragment()
                )
            }
        }
    }

    private fun validateInputData(username: String, password: String): Boolean {
        if (username.isEmpty() || password.isEmpty()) {
            return false
        }
        return true
    }
}