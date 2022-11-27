package com.example.assignment.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignment.auth.AuthData
import com.example.assignment.common.Evento
import com.example.assignment.data.DataRepository
import kotlinx.coroutines.launch

class AddFriendViewModel(
    private val repository: DataRepository
): ViewModel() {
    private val _message = MutableLiveData<Evento<String>>()

    val message: LiveData<Evento<String>>
        get() = _message

    val user= MutableLiveData<AuthData>(null)

    fun addFriend(friendName: String) {
        viewModelScope.launch {
            repository.addFriend(friendName)
        }
    }
}