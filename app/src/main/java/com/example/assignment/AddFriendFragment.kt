package com.example.assignment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.assignment.auth.AuthData
import com.example.assignment.databinding.FragmentAddFriendBinding
import com.example.assignment.server.MpageServer
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddFriendFragment : Fragment() {
    private var _binding: FragmentAddFriendBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManager = SessionManager(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddFriendBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editTextFriendName: EditText = binding.editTextFriendName
        val buttonAddFriend: Button = binding.buttonAddFriend

        buttonAddFriend.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    val friendName = editTextFriendName.text.toString()
                    if (friendName.isEmpty()) {
                        Toast.makeText(context, "Meno je prazdne", Toast.LENGTH_SHORT).show()
                    } else {
                        val preferences: SharedPreferences? =
                            activity?.getSharedPreferences("BAR_APP", Context.MODE_PRIVATE)
                        val authDataString = preferences?.getString("auth_data", null)

                        if (authDataString != null) {
                            val authData = Gson().fromJson(authDataString, AuthData::class.java)
                            MpageServer.addFriend(authData, friendName, sessionManager)
                            Toast.makeText(context, "Kamarat uspesne pridany", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}