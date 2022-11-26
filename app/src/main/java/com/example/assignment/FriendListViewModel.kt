package com.example.assignment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignment.nnn.DataRepository
import com.example.assignment.user.Friend
import kotlinx.coroutines.launch

class FriendListViewModel(
    private val repository: DataRepository
): ViewModel() {
    private var _friends = listOf<Friend>()

    val friends
        get() = _friends

    suspend fun fetchFriends() {
        _friends = repository.fetchFriends()
    }
}