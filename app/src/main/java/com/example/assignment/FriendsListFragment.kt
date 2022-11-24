package com.example.assignment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment.databinding.FragmentBarsListBinding
import com.example.assignment.databinding.FragmentFriendsListBinding
import com.example.assignment.server.MpageServer
import com.example.assignment.ui.BarsListAdapter
import com.example.assignment.ui.viewmodels.PubDataViewModel
import com.example.assignment.user.Friend
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FriendsListFragment : Fragment() {
    private var _binding: FragmentFriendsListBinding? = null
    private val binding get() = _binding!!

    private lateinit var sessionManager: SessionManager
    private val friendListViewModel: FriendListViewModel by activityViewModels()

    private lateinit var friendsListAdapter: FriendsListAdapter
    private lateinit var recyclerViewFriends: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sessionManager = SessionManager(context)
        friendsListAdapter = FriendsListAdapter(friendListViewModel)
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
        recyclerViewFriends.adapter = friendsListAdapter

        CoroutineScope(Dispatchers.Main).launch {
            val friends = MpageServer.fetchFriends(
                authData = sessionManager.fetchAuthData(),
                sessionManager = sessionManager,
            )

            friendListViewModel.updateFriends(friends)

            friendsListAdapter = FriendsListAdapter(friendListViewModel)
            recyclerViewFriends.adapter = friendsListAdapter
        }
    }
}