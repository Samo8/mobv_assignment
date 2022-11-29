package com.example.assignment.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment.ui.viewmodels.FriendListViewModel
import com.example.assignment.ui.adapters.FriendsListAdapter
import com.example.assignment.databinding.FragmentFriendsListBinding
import com.example.assignment.common.Injection

class FriendsListFragment : Fragment() {
    private var _binding: FragmentFriendsListBinding? = null
    private val binding get() = _binding!!

    private lateinit var friendListViewModel: FriendListViewModel

    private lateinit var friendsListAdapter: FriendsListAdapter
    private lateinit var recyclerViewFriends: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        friendListViewModel = ViewModelProvider(
            this,
            Injection.provideViewModelFactory(requireContext())
        )[FriendListViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFriendsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewFriends = binding.recyclerViewFriendsList
        recyclerViewFriends.layoutManager = LinearLayoutManager(context)

        friendListViewModel.fetchFriends()

        friendListViewModel.friends.observe(this.viewLifecycleOwner) {
            friendsListAdapter = FriendsListAdapter(it, this.findNavController())
            recyclerViewFriends.adapter = friendsListAdapter
        }
    }
}