package com.example.assignment

import androidx.lifecycle.ViewModel
import com.example.assignment.user.Friend

class FriendListViewModel: ViewModel() {
    private var _friends: List<Friend> = mutableListOf()

    val friends: List<Friend>
        get() = _friends

    fun updateFriends(friends: List<Friend>) {
        _friends = friends
    }
}