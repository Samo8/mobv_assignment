package com.example.assignment.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.assignment.server.MpageServer
import com.example.assignment.R
import com.example.assignment.data.models.AuthData
import com.example.assignment.databinding.FragmentLoginBinding
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        val preferences: SharedPreferences? =
            activity?.getSharedPreferences("BAR_APP", Context.MODE_PRIVATE)
        val authData = preferences?.getString("auth_data", null)

        if (authData != null) {
            val res = Gson().fromJson(authData, AuthData::class.java)
            Log.i("auth data", res.access)
            findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToBarsListFragment()
            )
        }

        val userNameEditText: EditText = binding.editTextUsernameLogin
        val passwordEditText: EditText = binding.editTextPasswordLogin
        val loginButton: Button = binding.buttonLogin

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
                CoroutineScope(Dispatchers.Main).launch {
                    try {
                        val loginData = MpageServer.login(username, password)

                        val serializedData = Gson().toJson(loginData)
                        preferences?.edit()?.putString("auth_data", serializedData)?.apply()

                        findNavController().navigate(
                            LoginFragmentDirections.actionLoginFragmentToBarsListFragment()
                        )
                    } catch (e: Exception) {
                        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    }
                }
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