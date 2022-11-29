package com.example.assignment.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.assignment.databinding.FragmentAddFriendBinding
import com.example.assignment.ui.viewmodels.AddFriendViewModel
import com.example.assignment.common.Injection

class AddFriendFragment : Fragment() {
    private var _binding: FragmentAddFriendBinding? = null
    private val binding get() = _binding!!

    private lateinit var addFriendViewModel: AddFriendViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addFriendViewModel = ViewModelProvider(
            this,
            Injection.provideViewModelFactory(requireContext())
        )[AddFriendViewModel::class.java]
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

        addFriendViewModel.message.observe(this.viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }

        binding.buttonAddFriend.setOnClickListener {
            val friendName = binding.editTextFriendName.text.toString()
            if (friendName.isEmpty()) {
                Toast.makeText(context, "Meno je prazdne", Toast.LENGTH_SHORT).show()
            } else {
                addFriendViewModel.addFriend(friendName)
            }
        }
    }
}